package com.david.threadmanagement;

import java.io.File;

/**
 * 演示InterruptedException的效果
 * @author dailiwei
 *
 */
public class FileSearch implements Runnable
{
	private String initPath;
	private String fileName;

	public String getInitPath()
	{
		return initPath;
	}

	public void setInitPath(String initPath)
	{
		this.initPath = initPath;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public FileSearch(String initPath, String fileName)
	{
		super();
		this.initPath = initPath;
		this.fileName = fileName;
	}

	@Override
	public void run()
	{
		File file = new File(initPath);
		if(file.isDirectory()){
			try
			{
				directoryProcess(file);
				
			} catch (InterruptedException e)
			{
				System.err.println(String.format("%s: The search has been interrupted", Thread.currentThread().getName()));
			}
		}
	}

	private void directoryProcess(File file) throws InterruptedException
	{
		File[] files = file.listFiles();
		for (File item : files)
		{
			if (item.isDirectory())
			{
				directoryProcess(item);
			} else
			{
				fileProcess(item);
			}
		}

		if (Thread.interrupted())
		{
			throw new InterruptedException();
		}
	}

	public void fileProcess(File file) throws InterruptedException
	{
		if (file.getName().equalsIgnoreCase(fileName))
		{
			System.out.println(String.format("%s :, %s", Thread.currentThread().getName(), file.getAbsolutePath()));
		}

		if (Thread.interrupted())
		{
			throw new InterruptedException();
		}
	}

}
