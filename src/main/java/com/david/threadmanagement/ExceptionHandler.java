package com.david.threadmanagement;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

/**
 * 对线程内的Runtime异常进行捕获，进行下一步处理
 * @author dailiwei
 *
 */
public class ExceptionHandler implements UncaughtExceptionHandler
{
	private Logger logger = Logger.getLogger(ExceptionHandler.class);

	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		logger.error("An exception has occured...");
		logger.error(String.format("Thread - %d", t.getId()));
		logger.error(e.getMessage(), e);
	}

}
