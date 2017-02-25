package com.david.syncutils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class MainRun {
	private static final CountDownLatch latch = new CountDownLatch(3);

	public static void main(String[] args) {
		// runSemaphore();
		// runCountDownLatch();
		// runCyclicBarrier();
		// runBillTask();
		// runFileSearcher();
		// runWorkerAndBoss();
		// runCustomPhaser();
		// runProducerAndConsumer();

		// executeMergetDbDataTask();
		executeMergetDbDataTaskWithThreadPool();

	}

	/**
	 * 使用futureTask(线程合并数据源)
	 */
	private static void executeMergetDbDataTaskWithThreadPool() {
		List<User> mergeList = new ArrayList<User>();
		DbOneTask dbOneTask = new DbOneTask(latch);
		DbTwoTask dbTwoTask = new DbTwoTask(latch);
		DbThreeTask dbThreeTask = new DbThreeTask(latch);

		System.out.println("start get two db data...");

		ExecutorService exec = Executors.newFixedThreadPool(3);
		Future<List<User>> futureTask1 = exec.submit(dbOneTask);
		Future<List<User>> futureTask2 = exec.submit(dbTwoTask);
		Future<List<User>> futureTask3 = exec.submit(dbThreeTask);

		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			for (User user : futureTask1.get()) {
				mergeList.add(user);
			}

			for (User user : futureTask2.get()) {
				mergeList.add(user);
			}

			for (User user : futureTask3.get()) {
				mergeList.add(user);
			}
		} catch (ExecutionException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println("显示结果集..." + mergeList.size());
		for (User user : mergeList) {
			System.err.println(user);
		}

		System.out.println("merge data success...");
	}

	/**
	 * 使用futureTask(线程合并数据源)
	 */
	private static void executeMergetDbDataTask() {
		List<User> mergeList = new ArrayList<User>();
		DbOneTask dbOneTask = new DbOneTask(latch);
		DbTwoTask dbTwoTask = new DbTwoTask(latch);

		System.out.println("start get two db data...");

		FutureTask<List<User>> futureTask1 = new FutureTask<List<User>>(dbOneTask);
		FutureTask<List<User>> futureTask2 = new FutureTask<List<User>>(dbTwoTask);

		Thread t1 = new Thread(futureTask1);
		Thread t2 = new Thread(futureTask2);

		t1.start();
		t2.start();

		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			for (User user : futureTask1.get()) {
				mergeList.add(user);
			}

			for (User user : futureTask2.get()) {
				mergeList.add(user);
			}
		} catch (ExecutionException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println("显示结果集...");
		for (User user : mergeList) {
			System.err.println(user);
		}

		System.out.println("merge data success...");
	}

	private static void runProducerAndConsumer() {
		List<String> pBuffer = new ArrayList<String>();
		List<String> cBuffer = new ArrayList<String>();

		Exchanger<List<String>> exchanger = new Exchanger<List<String>>();

		Producer producer = new Producer(pBuffer, exchanger);
		Consumer consumer = new Consumer(cBuffer, exchanger);

		Thread tp = new Thread(producer);
		Thread cp = new Thread(consumer);

		tp.start();
		cp.start();
	}

	private static void runCustomPhaser() {
		MyPhaser myPhaser = new MyPhaser();
		Student[] students = new Student[5];

		for (int i = 0; i < students.length; i++) {
			students[i] = new Student(myPhaser);
			myPhaser.register();
		}

		Thread[] threads = new Thread[students.length];
		for (int i = 0; i < students.length; i++) {
			threads[i] = new Thread(students[i], "Student-" + i);
			threads[i].start();
		}

		for (int i = 0; i < students.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Main: The pharse has finished...");
	}

	private static void runWorkerAndBoss() {
		CountDownLatch latch = new CountDownLatch(3);
		CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {

			@Override
			public void run() {
				System.out.println("开始庆功宴...");
			}
		});
		boolean isUseCountDownLatch = false;
		Boss boss = new Boss(latch, isUseCountDownLatch);
		Thread bossThread = new Thread(boss);
		bossThread.start();

		Worker[] workers = new Worker[3];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Worker("Worker" + i, latch, barrier, isUseCountDownLatch);
			Thread t = new Thread(workers[i]);
			t.start();
		}

		// System.err.println("All tasks finished...");
	}

	private static void runFileSearcher() {
		Phaser phaser = new Phaser(3);
		FileSearcher system = new FileSearcher("C:\\Windows", "log", phaser);
		FileSearcher apps = new FileSearcher("C:\\Program Files", "log", phaser);
		FileSearcher document = new FileSearcher("C:\\Documents And Settings", "log", phaser);

		Thread systemThread = new Thread(system, "system");
		systemThread.start();

		Thread appsThread = new Thread(apps, "apps");
		appsThread.start();

		Thread documentThread = new Thread(document, "document");
		documentThread.start();

		try {
			systemThread.join();
			appsThread.join();
			documentThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Terminted: " + phaser.isTerminated());
	}

	private static void runSemaphore() {
		PrintQueue printQueue = new PrintQueue();
		Thread[] threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Job(printQueue));
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
	}

	private static void runCountDownLatch() {
		int count = 3;
		VideoConference conference = new VideoConference(count);
		Thread conferenceThread = new Thread(conference);

		conferenceThread.start();

		Thread[] participantThreads = new Thread[count];
		for (int i = 0; i < participantThreads.length; i++) {
			participantThreads[i] = new Thread(new Participant(String.format("Participant-%d", i), conference));
		}

		for (int i = 0; i < participantThreads.length; i++) {
			participantThreads[i].start();
		}

	}

	private static void runCyclicBarrier() {
		int rows = 10000;
		int number = 1000;
		int search = 5;
		int participants = 5;
		int lines_participant = 2000;

		MatrixMock mock = new MatrixMock(rows, number, search);
		Result result = new Result(rows);
		Grouper grouper = new Grouper(result);
		CyclicBarrier cyclicBarrier = new CyclicBarrier(participants, grouper);

		Searcher[] searchers = new Searcher[participants];
		for (int i = 0; i < participants; i++) {
			searchers[i] = new Searcher(i * lines_participant, i * lines_participant + lines_participant, mock, result, search,
					cyclicBarrier);
			Thread t = new Thread(searchers[i]);
			t.start();

		}

		System.err.println("All tasks has done...");
	}

	private static void runBillTask() {
		System.err.println("runBillTask start...");
		CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("start end task...");
				System.out.println("start to sleep for 2 seconds...");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("end end task...");
			}
		});

		ExecutorService exec = Executors.newFixedThreadPool(3);
		exec.execute(new BillTaskOne(barrier));
		exec.execute(new BillTaskTwo(barrier));
		exec.execute(new BillTaskThree(barrier));

		System.err.println("runBillTask end...");

	}
}
