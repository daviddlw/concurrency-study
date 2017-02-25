package com.david.customizingconcurrency;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyAbstrackQueuedSynchronizer extends AbstractQueuedSynchronizer
{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected boolean tryAcquire(int arg)
	{
		// TODO Auto-generated method stub
		return super.tryAcquire(arg);
	}
	
	@Override
	protected boolean tryRelease(int arg)
	{
		// TODO Auto-generated method stub
		return super.tryRelease(arg);
	}
}
