package com.david.nettydemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import com.david.common.CommonConstants;

public class NettyNioJdkSerializeServer
{
	public static void main(String[] args)
	{
		startServer();
	}

	private static void startServer()
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap p = new ServerBootstrap();
		p.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.SO_BACKLOG, CommonConstants.B1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception
					{
						ch.pipeline().addLast(
								new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(this.getClass()
										.getClassLoader())));
						ch.pipeline().addLast(new ObjectEncoder());
						ch.pipeline().addLast(new JdkSerializeServerHandler());
					}
				});

		try
		{
			ChannelFuture cf = p.bind(CommonConstants.PORT).sync();
			System.out.println("server start");
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}
}

class JdkSerializeServerHandler extends ChannelInboundHandlerAdapter
{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		SubscribeReq req = (SubscribeReq) msg;
		if (req.getUserName().equalsIgnoreCase("daviddai"))
		{
			System.out.println(String.format("server received the req data: [%s]", req.toString()));
			ctx.writeAndFlush(resp(req.getSubReqID()));
		} else
		{
			System.out.println("BAD REQUEST");
		}
	}

	private SubscribeResp resp(int i)
	{
		SubscribeResp subscribeResp = new SubscribeResp();
		subscribeResp.setSubReqID(i);
		subscribeResp.setRespCode(0);
		subscribeResp.setDesc(String.format("netty server has received the message, timestamp: %d",
				System.currentTimeMillis()));

		return subscribeResp;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
		ctx.close(); //发生异常关闭链路
	}

}
