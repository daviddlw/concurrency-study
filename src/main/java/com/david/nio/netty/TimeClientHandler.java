package com.david.nio.netty;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		try {
			long currentTimeMillis = (byteBuf.readUnsignedInt() - Constants.TIME_ZONE) * 1000L;
			Date time = new Date(currentTimeMillis);
			String dateStr = sdf.format(time);
			System.out.println(dateStr);
			ctx.close();
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
