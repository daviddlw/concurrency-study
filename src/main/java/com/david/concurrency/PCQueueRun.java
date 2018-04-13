package com.david.concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PCQueueRun {
	public static void main(String[] args) throws InterruptedException {
		List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
		PCQueue<Integer> pcQueue = new PCQueue<>(2);
		PCProducer<Integer> pcProducer = new PCProducer<Integer>(pcQueue, list);
		PCConsumer<Integer> pcConsumer = new PCConsumer<Integer>(pcQueue);
		PCConsumer<Integer> pcConsumer2 = new PCConsumer<Integer>(pcQueue);
		System.err.println("producer-consumer start...");
		pcProducer.setName("pcProducer-thread");
		pcConsumer.setName("pcConsumer-thread");
		pcConsumer2.setName("pcConsumer2-thread");
		pcProducer.start();
		pcConsumer.start();
		pcConsumer2.start();
		pcProducer.join();
//		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//		exec.execute(pcProducer);
//		exec.execute(pcConsumer);
		System.err.println("producer-consumer end...");
	}
}

class PCConsumer<T> extends Thread {

	private PCQueue<T> pcQueue;

	public PCConsumer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PCConsumer(PCQueue<T> pcQueue) {
		super();
		this.pcQueue = pcQueue;
	}
	
	

	@Override
	public void run() {
		while (true) {
			T result = pcQueue.take();
			System.out.println(Thread.currentThread().getName()+"poll item-" + result);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

class PCProducer<T> extends Thread {

	private PCQueue<T> pcQueue;
	private List<T> messageList;

	public PCProducer() {
		super();
		pcQueue = new PCQueue<T>();
	}

	public PCProducer(PCQueue<T> pcQueue, List<T> messageList) {
		super();
		this.pcQueue = pcQueue;
		this.messageList = messageList;
	}

	@Override
	public void run() {
		for (T item : messageList) {
			pcQueue.put(item);
			System.out.println("add item-" + item);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
