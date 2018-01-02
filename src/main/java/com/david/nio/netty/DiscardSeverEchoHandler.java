package com.david.nio.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler a server-side channel
 * 
 * @author Administrator
 *
 */
public class DiscardSeverEchoHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.write(msg);
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) throws Exception {
		ex.printStackTrace();
		ctx.close();
	}
}
