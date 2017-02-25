package com.david.nettydemo;

import io.netty.bootstrap.ServerBootstrap;
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
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import com.david.common.CommonConstants;

public class NettyNioServerSeparator {

	public static void main(String[] args) {
		NettyNioServerSeparator.startSeparatorServer();
	}

	public static void startSeparatorServer() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		//设置两个sever的nio工作组，一个负责服务端接受客户端，一个负责处理网络读写
		ServerBootstrap sbs = new ServerBootstrap();
		//设置管道与处理类
		sbs.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ByteBuf delimiterBuf = Unpooled.copiedBuffer(";".getBytes());
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(CommonConstants.B1024, delimiterBuf));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new DelimiterServerHandler());

					}

				});

		try {
			System.out.println("server channel start");
			ChannelFuture cf = sbs.bind(CommonConstants.PORT).sync();
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

class DelimiterServerHandler extends ChannelInboundHandlerAdapter {

	private int count;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("This is " + (++count) + " times received from client: [" + body + "]");
		body += ";";
		ByteBuf byteBuf = Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(byteBuf);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
