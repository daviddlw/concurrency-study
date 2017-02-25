package com.david.syncutils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Student implements Runnable
{
	private Phaser phaser;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Student(Phaser phaser)
	{
		super();
		this.phaser = phaser;
	}

	@Override
	public void run()
	{
		System.out.println(String.format("%s: Has arrived to do the exam. %s", Thread.currentThread().getName(), sdf.format(new Date())));
		phaser.arriveAndAwaitAdvance();

		System.out
				.println(String.format("%s, Is going to do the first exam. %s", Thread.currentThread().getName(), sdf.format(new Date())));
		doExam1();
		System.out.println(String.format("%s, finished first exam. %s", Thread.currentThread().getName(), sdf.format(new Date())));
		phaser.arriveAndAwaitAdvance();

		System.out
				.println(String.format("%s, Is going to do the second exam. %s", Thread.currentThread().getName(), sdf.format(new Date())));
		doExam2();
		System.out.println(String.format("%s, finished second exam. %s", Thread.currentThread().getName(), sdf.format(new Date())));
		phaser.arriveAndAwaitAdvance();

		System.out
				.println(String.format("%s, Is going to do the final exam. %s", Thread.currentThread().getName(), sdf.format(new Date())));
		doExam3();
		System.out.println(String.format("%s, finished final exam. %s", Thread.currentThread().getName(), sdf.format(new Date())));
		phaser.arriveAndAwaitAdvance();
	}

	private void doExam1()
	{
		long duration = (long) (Math.random() * 10);
		try
		{
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doExam2()
	{
		long duration = (long) (Math.random() * 10);
		try
		{
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doExam3()
	{
		long duration = (long) (Math.random() * 10);
		try
		{
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
