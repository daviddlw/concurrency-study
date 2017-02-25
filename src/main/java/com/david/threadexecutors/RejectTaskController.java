package com.david.threadexecutors;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectTaskController implements RejectedExecutionHandler
{

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
	{
		System.err.println("The task - " + r.toString() + " has been rejected...");
		System.err.println("Executor: "+executor);
		System.err.println("Terminating: "+executor.isTerminating());
		System.err.println("Terminated: "+executor.isTerminated());
	}

}
