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
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;

import com.david.common.CommonConstants;
import com.david.common.CommonUtils;

public class NettyNioServerDealPack {
	public static void main(String[] args) {
		NettyNioServerDealPack.bind(CommonConstants.PORT);
	}

	public static void bind(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap p = new ServerBootstrap();
			p.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, CommonConstants.B1024)
					.childHandler(new NettyTimeServerHandlerDealPack());

			ChannelFuture cf = p.bind(port).sync();
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}

/**
 * 把处理类添加到管道
 * 
 * @author dailiwei
 * 
 */
class NettyTimeServerHandlerDealPack extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 增加解码器
		ch.pipeline().addLast(new LineBasedFrameDecoder(CommonConstants.B1024));
		ch.pipeline().addLast(new StringDecoder());
		ch.pipeline().addLast(new TimeServerHandlerDealPack());

		System.out.println("netty server start [" + ch.localAddress() + "], begin to adapt client: ["
				+ ch.remoteAddress() + "]");
	}
}

/**
 * 管道接入的时候
 * 
 * @author dailiwei
 * 
 */
class TimeServerHandlerDealPack extends ChannelInboundHandlerAdapter {
	private int count;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String message = (String)msg;
		System.out.println("sever accept message from client, contents:[" + message + "]" + ", count: " + (++count));
		String writeMessage = message.equalsIgnoreCase("REQUEST TIME ORDER") ? CommonUtils.sdf.format(new Date())
				: "BAD REQUEST";
		writeMessage = writeMessage + CommonConstants.LINE_SEPARATOR;
		ByteBuf writeBuf = Unpooled.copiedBuffer(writeMessage.getBytes());
		ctx.writeAndFlush(writeBuf);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
