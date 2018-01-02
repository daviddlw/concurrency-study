package com.david.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final ByteBuf time = ctx.alloc().buffer(4);
		time.writeInt((int) (System.currentTimeMillis() / 1000L + Constants.TIME_ZONE)); // 默认格林尼治时间需要+2208988800L变成东八区
		/*
		 * Netty write operation does not need flip, and its writeAndFlush is
		 * asynchronous. Therefore, we need to call the close() mether util
		 * after the ChannelFuture is complete
		 */
		final ChannelFuture f = ctx.writeAndFlush(time); // any
		f.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) throws Exception {
		ex.printStackTrace();
		ctx.close();
	}
}
