package com.david.nettydemo;

import com.david.common.CommonConstants;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyNioClientSeparator {
	public static void main(String[] args) {
		startSeparatorClient();
	}

	private static void startSeparatorClient() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bs = new Bootstrap();
		bs.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ByteBuf delimiterBuf = Unpooled.copiedBuffer(";".getBytes());
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(CommonConstants.B1024, delimiterBuf));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new DelimiterClientHandler());
					}
				});

		try {
			ChannelFuture cf = bs.connect(CommonConstants.LOCALHOST, CommonConstants.PORT);
			cf.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}

class DelimiterClientHandler extends ChannelInboundHandlerAdapter {

	private int count;
	private String message = "hello, daviddai. Welcome to Netty;";

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println(String.format("This is %d times received from server", (++count), body));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
