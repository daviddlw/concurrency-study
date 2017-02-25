package com.david.nettydemo.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import com.david.common.CommonConstants;

public class HttpFileServer {
	private static final String DEFAULT_URL = "/src/main/resources/folder1";

	public static void main(String[] args) {
		// System.out.println(System.getProperty("user.dir"));
		// System.out.println(HttpResponseStatus.BAD_REQUEST);
		startServer();
	}

	private static void startServer() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		ServerBootstrap p = new ServerBootstrap();
		p.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new HttpServerCodec()); //netty4中使用这个替换了HttpRequestDecoder与HttpRequestEncoder
						ch.pipeline().addLast(new HttpObjectAggregator(65536));
						ch.pipeline().addLast(new ChunkedWriteHandler());
						ch.pipeline().addLast(new HttpFileServerHandler(DEFAULT_URL));
					}
				});

		try {
			ChannelFuture cf = p.bind(CommonConstants.LOCALHOST_IP, CommonConstants.PORT).sync();
			System.out.println("http文件目录服务器启动成功，网址是：[http://" + CommonConstants.LOCALHOST_IP + ":"
					+ CommonConstants.PORT + DEFAULT_URL + "]");
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}
}

class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private String url;
	private static final String LOCATION = "Location";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
	private static final Pattern ALLOW_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
	private static final String CONNECTION = "Connection";

	public HttpFileServerHandler(String url) {
		super();
		this.url = url;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if (ctx.channel().isActive()) {
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		// 判断解码信息是否成功
		if (!req.getDecoderResult().isSuccess()) {
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}

		// 判断http method是否是get方法
		if (!req.getMethod().equals(HttpMethod.GET)) {
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
			return;
		}

		final String uri = req.getUri();
		final String path = sanitizeUrl(uri);

		// 路径不存在不能提供服务
		if (path == null) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}

		File file = new File(path);
		if (file.isHidden() || !file.exists()) {
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}

		if (file.isDirectory()) {
			if (uri.endsWith("/")) {
				sendListing(ctx, file);
			} else {
				sendRedirect(ctx, uri + "/");
			}
			return;
		}

		if (!file.isFile()) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}

		RandomAccessFile raf = new RandomAccessFile(file, "r");
		long fileLength = raf.length();
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		HttpHeaders.setContentLength(response, fileLength);
		setContentTypeHeader(response, file);

		if (HttpHeaders.isKeepAlive(req)) {
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		ctx.write(response);
		ChannelFuture sendFileFuture;
		sendFileFuture = ctx.write(new ChunkedFile(raf, 0, fileLength, 8192), ctx.newProgressivePromise());
		sendFileFuture.addListener(new ChannelProgressiveFutureListener() {

			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				System.out.println("transfer complete.");
			}

			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
					throws Exception {
				if (total < 0) {
					System.out.println(String.format("transfer progress is [%d]", progress));
				} else {
					System.out.println(String.format("transfer progress is [%d/%d]", progress, total));
				}
			}
		});

		ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if (!HttpHeaders.isKeepAlive(req)) {
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private void sendListing(ChannelHandlerContext ctx, File dir) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set(CONTENT_TYPE, "text/html;charset=UTF-8");
		StringBuilder sb = new StringBuilder();
		String dirPath = dir.getPath();
		sb.append("<!DOCTYPE html>\r\n");
		sb.append("<html><head><title>");
		sb.append(dirPath);
		sb.append(" 目录： ");
		sb.append("</head></title><body>\r\n");
		sb.append("<h3>");
		sb.append(dirPath).append(" 目录： ");
		sb.append("</h3>\r\n");
		sb.append("<ul>");
		sb.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
		for (File file : dir.listFiles()) {
			if (file.isHidden() || !file.canRead()) {
				continue;
			}

			if (!ALLOW_FILE_NAME.matcher(file.getName()).matches()) {
				continue;
			}

			sb.append("<li>链接：<a href=\"");
			sb.append(file.getName());
			sb.append("\">");
			sb.append(file.getName());
			sb.append("</a></li>\r\n");
			sb.append("</ul></body></html>\r\n");
		}
		
		ByteBuf byteBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
		System.out.println(byteBuf.readableBytes());
		System.out.println("before: " + response.content().readableBytes());
		response.content().writeBytes(byteBuf);
		System.out.println("after: " + response.content().readableBytes());
		byteBuf.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 发送重定向请求
	 * 
	 * @param ctx
	 * @param newUri
	 */
	private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		response.headers().set(LOCATION, newUri);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		// ctx.writeAndFlush(response);
	}

	/**
	 * 处理uri
	 * 
	 * @param uri
	 * @return
	 */
	private String sanitizeUrl(String uri) {
		try {
			uri = URLDecoder.decode(uri, CharsetUtil.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			try {
				uri = URLDecoder.decode(uri, CharsetUtil.ISO_8859_1.name());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		if (!uri.startsWith(url)) {
			return null;
		}

		if (!uri.startsWith("/")) {
			return null;
		}

		uri = uri.replace('/', File.separatorChar);
		if (uri.contains(File.separator + '.') || uri.contains("." + File.separator) || uri.startsWith(".")
				|| uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
			return null;
		}
		return System.getProperty("user.dir") + File.separator + uri;
	}

	/**
	 * 发送错误请求信息
	 * 
	 * @param ctx
	 * @param status
	 */
	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer(("Failure: " + status.toString() + "\r\n").getBytes(CharsetUtil.UTF_8)));
		response.headers().set(CONTENT_TYPE, "text/plain;charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		// ctx.writeAndFlush(response);
	}

	/**
	 * 设置Http请求中的网页内容
	 * 
	 * @param response
	 * @param file
	 */
	private static void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap mftm = new MimetypesFileTypeMap();
		response.headers().set(CONTENT_TYPE, mftm.getContentType(file.getPath()));
	}
}
