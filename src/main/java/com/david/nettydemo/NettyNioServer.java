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

import java.nio.charset.Charset;
import java.util.Date;

import com.david.common.CommonConstants;
import com.david.common.CommonUtils;

public class NettyNioServer
{
	public static void main(String[] args)
	{
		NettyNioServer.bind(CommonConstants.PORT);
	}

	public static void bind(int port)
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try
		{
			ServerBootstrap p = new ServerBootstrap();
			p.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, CommonConstants.B1024).childHandler(new NettyTimeServerHandler());

			ChannelFuture cf = p.bind(port).sync();
			cf.channel().closeFuture().sync();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
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
class NettyTimeServerHandler extends ChannelInitializer<SocketChannel>
{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ch.pipeline().addLast(new TimeServerHandler());
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
class TimeServerHandler extends ChannelInboundHandlerAdapter
{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		ByteBuf byteBuf = (ByteBuf) msg;
		byte[] dataBytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(dataBytes);
		String message = new String(dataBytes, Charset.forName(CommonConstants.UTF_8));
		System.out.println("sever accept message from client, contents:[" + message + "]");
		String writeMessage = message.equalsIgnoreCase("REQUEST TIME ORDER") ? CommonUtils.sdf.format(new Date())
				: "BAD REQUEST";
		ByteBuf writeBuf = Unpooled.copiedBuffer(writeMessage.getBytes());
		ctx.write(writeBuf);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
	{
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		ctx.close();
	}

}
