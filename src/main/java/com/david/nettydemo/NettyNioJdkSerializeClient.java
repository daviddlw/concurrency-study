package com.david.nettydemo;

import com.david.common.CommonConstants;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyNioJdkSerializeClient
{
	public static void main(String[] args)
	{
		startClient();
	}

	public static void startClient()
	{
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap p = new Bootstrap();
		p.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception
					{
						//禁止对类加载器进行缓存，因为它基于osgi的动态模块化编程中经常使用，由于osgi的bundle可以进行热部署和热升级，所以类似这种
						//动态模块化编程过程中，很少对类加载器进行缓存
						ch.pipeline().addLast(
								new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
						ch.pipeline().addLast(new ObjectEncoder());
						ch.pipeline().addLast(new JdkSerializeClientHandler());
					}
				});

		try
		{
			ChannelFuture cf = p.connect(CommonConstants.LOCALHOST, CommonConstants.PORT).sync();
			System.out.println("client start");
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			workerGroup.shutdownGracefully();
		}
	}
}

class JdkSerializeClientHandler extends ChannelInboundHandlerAdapter
{

	private SubscribeReq subReq(int i)
	{
		SubscribeReq req = new SubscribeReq();
		req.setSubReqID(i);
		req.setUserName("daviddai");
		req.setProjectName("上海恒大互联网");
		req.setPhoneNumber("123456789");
		req.setAddress("上海虹口区中山北一路" + (i * 100) + "号");
		return req;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		for (int i = 1; i <= 10; i++)
		{
			ctx.write(subReq(i));
		}
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		System.out.println(String.format("client received message from server: [%s]", msg));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
	{
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
		ctx.close();
	}

}
