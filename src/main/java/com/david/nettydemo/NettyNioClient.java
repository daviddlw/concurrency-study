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

import java.nio.charset.Charset;

import com.david.common.CommonConstants;

public class NettyNioClient
{
	public static void main(String[] args)
	{
		NettyNioClient.connect(CommonConstants.LOCALHOST, CommonConstants.PORT);
	}

	public static void connect(String host, int port)
	{
		EventLoopGroup group = new NioEventLoopGroup();
		try
		{
			Bootstrap p = new Bootstrap();
			p.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new NettyTimeClientHandler());

			ChannelFuture cf = p.connect(CommonConstants.LOCALHOST, CommonConstants.PORT).sync();
			System.out.println("netty client start [" + cf.channel().localAddress() + "], connect to server: [" + cf.channel().remoteAddress()
					+ "]");
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

class NettyTimeClientHandler extends ChannelInitializer<SocketChannel>
{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ch.pipeline().addLast(new TimeClientHandler());		
	}
}

class TimeClientHandler extends ChannelInboundHandlerAdapter
{
	private final ByteBuf firstMessage;

	public TimeClientHandler()
	{
		super();
		String message = "REQUEST TIME ORDER";
		byte[] req = message.getBytes();
		firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		ctx.writeAndFlush(firstMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		ByteBuf readBuf = (ByteBuf) msg;
		byte[] req = new byte[readBuf.readableBytes()];
		readBuf.readBytes(req);
		String resp = new String(req, Charset.forName(CommonConstants.UTF_8));
		System.out.println("received data from server: " + resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

}
