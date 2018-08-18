package com.david.interview;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueDemo {
	public static void main(String[] args) {
		SynchronousQueue<String> queue = new SynchronousQueue<>();
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
		exec.scheduleAtFixedRate(new SynchronousConsumer(queue), 0, 2, TimeUnit.SECONDS);
		exec.scheduleAtFixedRate(new SynchronousProducer(queue), 0, 1, TimeUnit.SECONDS);
	}
}

class SynchronousConsumer implements Runnable {

	private SynchronousQueue<String> queue;

	public SynchronousConsumer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SynchronousConsumer(SynchronousQueue<String> queue) {
		super();
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			System.out.println("取出数据：" + queue.take());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class SynchronousProducer implements Runnable {

	private SynchronousQueue<String> queue;

	public SynchronousProducer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SynchronousProducer(SynchronousQueue<String> queue) {
		super();
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long timestamp = System.currentTimeMillis();
		String result = "添加elena_" + timestamp;
		queue.offer(result);
		System.out.println(result);
	}
}
