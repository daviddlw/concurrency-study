package com.david.threadpool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class MainRun {

	private static final List<AtomicInteger> list1 = Arrays
			.asList(new AtomicInteger[]{new AtomicInteger(3), new AtomicInteger(6), new AtomicInteger(9)});
	private static final List<AtomicInteger> list2 = Arrays
			.asList(new AtomicInteger[]{new AtomicInteger(1), new AtomicInteger(2), new AtomicInteger(4), new AtomicInteger(7)});
	private static final List<AtomicInteger> list3 = Arrays.asList(new AtomicInteger[]{new AtomicInteger(5), new AtomicInteger(8)});
	private static ConcurrentHashMap<Integer, List<AtomicInteger>> resultMap = new ConcurrentHashMap<Integer, List<AtomicInteger>>();

	static {
		resultMap.put(0, list1);
		resultMap.put(1, list2);
		resultMap.put(2, list3);
	}

	public static void main(String[] args) {
		System.out.println("Start!");
		ExecutorService exec = Executors.newFixedThreadPool(3);
		List<Future<Integer>> futureList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			final List<AtomicInteger> list = resultMap.containsKey(i) ? resultMap.get(i) : new ArrayList<AtomicInteger>();
			Callable<Integer> callableTask = new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					int result = 0;
					for (AtomicInteger item : list) {
						result += item.incrementAndGet();
					}
					System.out.println(Thread.currentThread().getName() + "-result=" + result);
					return result;
				}
			};

			futureList.add(exec.submit(callableTask));
		}

		int total = 0;
		boolean isCompleted = true;
		List<Boolean> flagList = new ArrayList<>();
		while (true) {
			for (Future<Integer> item : futureList) {
				if (item.isDone()) {
					try {
						total += item.get(2, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					flagList.add(true);
				}
			}

			if (flagList.size() == 3) {
				for (Boolean flag : flagList) {
					isCompleted = flag & isCompleted;
				}
				if (isCompleted) {
					break;
				}
			}
		}

		System.out.println("Total=" + total);
	}
}
