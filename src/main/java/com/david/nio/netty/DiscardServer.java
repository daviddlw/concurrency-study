package com.david.nio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 可以通过telnet 端口测试
 * @author Administrator
 *
 */
public class DiscardServer {
	private int port;

	public DiscardServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DiscardServer(int port) {
		super();
		this.port = port;
	}

	/**
	 * bossEventLoopGroup=>accepts an incoming connection
	 * workerEventLoopGroup=>handles the traffic of the accepted connection once
	 * the boss accepts the connection and registers the accepted connection to
	 * the worker.
	 */
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
//					ch.pipeline().addLast(new DiscardSeverHandler());
					ch.pipeline().addLast(new DiscardSeverEchoHandler());
				}
			}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}
	
	public static void main(String[] args) {
		int port = 8081;
		new DiscardServer(port).run();
	}
}
