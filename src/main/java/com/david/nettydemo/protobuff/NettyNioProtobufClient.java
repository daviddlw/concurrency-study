package com.david.nettydemo.protobuff;

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
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.david.common.CommonConstants;

public class NettyNioProtobufClient
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
						// 禁止对类加载器进行缓存，因为它基于osgi的动态模块化编程中经常使用，由于osgi的bundle可以进行热部署和热升级，所以类似这种
						// 动态模块化编程过程中，很少对类加载器进行缓存
						ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
						ch.pipeline().addLast(
								new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));// 告知要解码的目标类是什么，否则从自己数组无法判断
						ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
						ch.pipeline().addLast(new ProtobufEncoder());
						ch.pipeline().addLast(new ProtobufClientHandler());
					}
				});

		try
		{
			ChannelFuture cf = p.connect(CommonConstants.LOCALHOST, CommonConstants.PORT).sync();
			System.out.println("client protobuf start");
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

class ProtobufClientHandler extends ChannelInboundHandlerAdapter
{

	private SubscribeReqProto.SubscribeReq subReq(int i)
	{
		SubscribeReqProto.SubscribeReq.Builder req = SubscribeReqProto.SubscribeReq.newBuilder();
		req.setSubReqID(i);
		req.setUserName("daviddai");
		req.setProjectName("上海恒大互联网-nettydemo");
		req.setPhoneNumber("123456789");
		req.setAddress("上海虹口区中山北一路" + (i * 100) + "号-nettydemo");
		return req.build();
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
