package com.david.nettydemo.protobuff;

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
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.david.common.CommonConstants;

public class NettyNioProtobufServer
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
						ch.pipeline().addLast(new ProtobufVarint32FrameDecoder()); //处理半包消息问题
						ch.pipeline().addLast(
								new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
						ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
						ch.pipeline().addLast(new ProtobufEncoder());
						ch.pipeline().addLast(new ProtobufSerializeServerHandler());
					}
				});

		try
		{
			ChannelFuture cf = p.bind(CommonConstants.PORT).sync();
			System.out.println("server protobuf start");
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

class ProtobufSerializeServerHandler extends ChannelInboundHandlerAdapter
{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
		if (req.getUserName().equalsIgnoreCase("daviddai"))
		{
			System.out.println(String.format("server received the req data: [%s]", req.toString()));
			ctx.writeAndFlush(resp(req.getSubReqID()));
		} else
		{
			System.out.println("BAD REQUEST");
		}
	}

	private SubscribeRespProto.SubscribeResp resp(int i)
	{
		SubscribeRespProto.SubscribeResp.Builder subscribeResp = SubscribeRespProto.SubscribeResp.newBuilder();
		subscribeResp.setSubReqID(i);
		subscribeResp.setRespCode(0);
		subscribeResp.setDesc(String.format("netty server has received the message, timestamp: %d",
				System.currentTimeMillis()));

		return subscribeResp.build();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
		ctx.close(); //发生异常关闭链路
	}

}
