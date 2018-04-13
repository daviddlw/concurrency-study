package com.david.concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PCNewQueueRun {
	public static void main(String[] args) throws InterruptedException {
		List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
		PCNewQueue<Integer> pcQueue = new PCNewQueue<>();
		PCNewProducer<Integer> pcProducer = new PCNewProducer<Integer>(pcQueue, list);
		PCNewConsumer<Integer> pcConsumer = new PCNewConsumer<Integer>(pcQueue);
		System.err.println("producer-consumer start...");
		pcProducer.setName("pcProducer-thread");
		pcConsumer.setName("pcConsumer-thread");
		pcProducer.start();
		pcConsumer.start();
		pcProducer.join();
		System.err.println("producer-consumer end...");
	}
}

class PCNewConsumer<T> extends Thread {

	private PCNewQueue<T> pcQueue;

	public PCNewConsumer() {
		super();
		pcQueue = new PCNewQueue<>();
	}

	public PCNewConsumer(PCNewQueue<T> pcQueue) {
		super();
		this.pcQueue = pcQueue;
	}
	
	

	@Override
	public void run() {
		while (true) {
			T result = pcQueue.take();
			System.out.println(Thread.currentThread().getName()+"poll item-" + result);
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

class PCNewProducer<T> extends Thread {

	private PCNewQueue<T> pcQueue;
	private List<T> messageList;

	public PCNewProducer() {
		super();
		pcQueue = new PCNewQueue<T>();
	}

	public PCNewProducer(PCNewQueue<T> pcQueue, List<T> messageList) {
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
				e.printStackTrace();
			}
		}
	}

}
