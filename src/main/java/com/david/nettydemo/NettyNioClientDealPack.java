package com.david.nettydemo;

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
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import com.david.common.CommonConstants;

public class NettyNioClientDealPack
{
	public static void main(String[] args)
	{
		NettyNioClientDealPack.connect(CommonConstants.LOCALHOST, CommonConstants.PORT);
	}

	public static void connect(String host, int port)
	{
		EventLoopGroup group = new NioEventLoopGroup();
		try
		{
			Bootstrap p = new Bootstrap();
			p.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new NettyTimeClientHandlerDealPack());

			ChannelFuture cf = p.connect(CommonConstants.LOCALHOST, CommonConstants.PORT).sync();
			System.out.println("netty client start [" + cf.channel().localAddress() + "], connect to server: ["
					+ cf.channel().remoteAddress() + "]");
			cf.channel().closeFuture().sync();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			group.shutdownGracefully();
		}
	}
}

class NettyTimeClientHandlerDealPack extends ChannelInitializer<SocketChannel>
{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		//增加解码器
		ch.pipeline().addLast(new LineBasedFrameDecoder(CommonConstants.B1024));
		ch.pipeline().addLast(new StringDecoder());
		ch.pipeline().addLast(new TimeClientHandlerDealPack());

	}
}

class TimeClientHandlerDealPack extends ChannelInboundHandlerAdapter
{
	private int count;
	byte[] req = null;

	public TimeClientHandlerDealPack()
	{
		super();
		req = ("REQUEST TIME ORDER" + CommonConstants.LINE_SEPARATOR).getBytes();

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		ByteBuf firstMessage = null;
		for (int i = 0; i < 100; i++)
		{
			firstMessage = Unpooled.buffer(req.length);
			firstMessage.writeBytes(req);
			ctx.writeAndFlush(firstMessage);
		}

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		String message = (String) msg;
		System.out.println("received data from server: " + message + ", now count is " + (++count));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

}
