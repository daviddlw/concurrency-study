package com.david.nio.netty;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientV2Handler extends ChannelInboundHandlerAdapter {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ByteBuf buf;

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		buf = ctx.alloc().buffer(4);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		buf.release();
		buf = null;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf m = (ByteBuf) msg;
		buf.writeBytes(m);
		m.release();

		if (buf.readableBytes() >= 4) {
			long currentTimeMillis = (buf.readUnsignedInt() - Constants.TIME_ZONE) * 1000L;
			System.out.println(sdf.format(new Date(currentTimeMillis)));
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) throws Exception {
		ex.printStackTrace();
		ctx.close();
	}
}
