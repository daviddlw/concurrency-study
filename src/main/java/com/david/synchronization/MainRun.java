package com.david.synchronization;

public class MainRun {
	public static void main(String[] args) {
		// runSyncMethod();
		// runTicketCinema();
		runProducerConsumer();

		// runPriceInfo();
		// runLockDemo();
		// runLockDemo2();
		// runFileMock();
		// getRuntimeProcessors();
	}

	private static void runFileMock() {
		FileMock fileMock = new FileMock(100, 10);
		Buffer buffer = new Buffer(20);
		MockProducer mp = new MockProducer(fileMock, buffer);
		Thread tmp = new Thread(mp, "MockProducer");

		MockConsumer[] mcs = new MockConsumer[3];
		Thread[] threads = new Thread[3];

		for (int i = 0; i < 3; i++) {
			mcs[i] = new MockConsumer(buffer);
			threads[i] = new Thread(mcs[i], "MockConsumer_" + i);
		}

		tmp.start();
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
	}

	private static void runPriceInfo() {
		PricesInfo pricesInfo = new PricesInfo();

		Thread[] trs = new Thread[5];

		for (int i = 0; i < trs.length; i++) {
			trs[i] = new Thread(new Reader(pricesInfo));
		}

		Thread tw = new Thread(new Writer(pricesInfo));
		for (int i = 0; i < trs.length; i++) {
			trs[i].start();
		}

		tw.start();
	}

	private static void runLockDemo() {
		int count = 10;
		PrintQueue queue = new PrintQueue();
		Thread[] threads = new Thread[count];
		for (int i = 0; i < count; i++) {
			threads[i] = new Thread(new Job(queue), "Thread-" + i);
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
	}

	private static void runLockDemo2() {
		int count = 10;
		PrintQueue queue = new PrintQueue();
		Thread[] threads = new Thread[count];
		for (int i = 0; i < count; i++) {
			threads[i] = new Thread(new Job(queue), "Thread-" + i);
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void runProducerConsumer() {
		EventStorage storage = new EventStorage();
		Thread t1 = new Thread(new Producer(storage));
		Thread t2 = new Thread(new Consumer(storage));

		t1.start();
		t2.start();
	}

	private static void runTicketCinema() {
		Cinema cinema = new Cinema();
		TicketOffice1 office1 = new TicketOffice1(cinema);
		TicketOffice2 office2 = new TicketOffice2(cinema);

		Thread t1 = new Thread(office1);
		Thread t2 = new Thread(office2);

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("OfficeOne Vancanies: " + cinema.getVacanciesCinema1());
		System.out.println("OfficeTwo Vancanies: " + cinema.getVacanciesCinema2());
		System.out.println("Test is over...");

	}

	private static void runSyncMethod() {
		Account account = new Account(1000);
		Company company = new Company(account);
		Bank bank = new Bank(account);

		System.out.println(String.format("Account initial balance: %d", account.getBalance()));

		company.start();
		bank.start();

		try {
			company.join();
			bank.join();
			System.out.println("Account : Final Balance: " + account.getBalance());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getRuntimeProcessors() {
		System.err.println(Runtime.getRuntime().availableProcessors());
	}
}
