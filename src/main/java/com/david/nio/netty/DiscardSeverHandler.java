package com.david.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handler a server-side channel
 * 
 * @author Administrator
 *
 */
public class DiscardSeverHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		try {
			while (byteBuf.isReadable()) {
				System.out.println((char) byteBuf.readByte());
				System.out.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) throws Exception {
		ex.printStackTrace();
		ctx.close();
	}
}
