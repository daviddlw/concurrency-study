package com.david.syncutils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class FileSearcher implements Runnable
{
	private String initPath;
	private String end;
	private List<String> results;
	private Phaser phaser;

	public FileSearcher(String initPath, String end, Phaser phaser)
	{
		super();
		this.initPath = initPath;
		this.end = end;
		this.results = new ArrayList<String>();
		this.phaser = phaser;
	}

	private void directoryProcess(File file)
	{
		File[] list = file.listFiles();
		if (list != null)
		{
			for (File item : list)
			{
				if (item.isDirectory())
				{
					directoryProcess(item);
				} else
				{
					fileProcess(item);
				}
			}
		}
	}

	private void fileProcess(File file)
	{
		if (file.getName().endsWith(end))
		{
			results.add(file.getAbsolutePath());
		}
	}

	private void filterResult()
	{
		List<String> newResults = new ArrayList<String>();
		long nowDate = new Date().getTime();

		for (String path : results)
		{
			File file = new File(path);
			long fileDate = file.lastModified();

			if (nowDate - fileDate < TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
			{
				newResults.add(path);
			}
		}

		results = newResults;
	}

	private boolean checkResults()
	{
		if (results.isEmpty())
		{
			System.out.println(String.format("%s: Pharse - 0 results...", Thread.currentThread().getName()));
			System.out.println(String.format("%s: Pharse End", Thread.currentThread().getName()));
			phaser.arriveAndDeregister();
			return false;
		} else
		{
			System.out.println(String.format("%s, Pharse %d: %d results...", Thread.currentThread().getName(),
					phaser.getPhase(), results.size()));
			phaser.arriveAndAwaitAdvance();
			return true;
		}
	}

	private void showInfo()
	{
		for (String path : results)
		{
			File file = new File(path);
			System.out.println(String.format("%s: %s", Thread.currentThread().getName(), file.getAbsolutePath()));
		}
		phaser.arriveAndAwaitAdvance();
	}

	@Override
	public void run()
	{
		phaser.arriveAndAwaitAdvance();
		System.out.println(String.format("%s: Starting...", Thread.currentThread().getName()));

		File file = new File(initPath);
		if (file.isDirectory())
		{
			directoryProcess(file);
		}

		if (!checkResults())
		{
			return;
		}

		filterResult();

		if (!checkResults())
		{
			return;
		}

		showInfo();

		phaser.arriveAndAwaitAdvance();
		System.out.println(String.format("%s: Work completed...", Thread.currentThread().getName()));
	}

}
