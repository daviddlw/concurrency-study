package com.david.concurrency;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		int processors = Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(processors, processors * 2, 200, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(processors * 2));
		for (int i = 0; i < 15; i++) {
			MyTask myTask = new MyTask(i);
			executor.execute(myTask);

			System.err.println("线程池当中数目：" + executor.getPoolSize() + ", 队列中等待线程数目：" + executor.getQueue().size() + ", 已执行完成线程数目："
					+ executor.getCompletedTaskCount());
		}
		executor.shutdown();
		
	}
}

class MyTask implements Runnable {

	private int taskNum;

	public MyTask(int taskNum) {
		super();
		this.taskNum = taskNum;
	}

	@Override
	public void run() {
		System.err.println(String.format("正在执行线程-%d", taskNum));
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println(String.format("线程-%d已经完成...", taskNum));
	}

}
