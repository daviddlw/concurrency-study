package com.david.concurrency;

import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PCNewQueue<T> {

	private final ReentrantLock putLock = new ReentrantLock();
	private final Condition putCondition = putLock.newCondition();
	private final ReentrantLock takeLock = new ReentrantLock();
	private final Condition takeCondition = takeLock.newCondition();
	private PriorityQueue<T> queue;
	private final int capacity; //一定要使用capacity,否则标注容器容量
	private final AtomicInteger count = new AtomicInteger(0);

	public PCNewQueue() {
		this(Integer.MAX_VALUE);	
	}

	public PCNewQueue(int capacity) {
		this.capacity = capacity;
		queue = new PriorityQueue<>();
	}
	
	/**
     * Links node at end of queue.
     *
     * @param node the node
     */
    private void enqueue(T e) {
        queue.add(e);
    }

    /**
     * Removes a node from head of queue.
     *
     * @return the node
     */
    private T dequeue() {
    	return queue.poll();
    }

	public void put(T e) {
		if (e == null) {
			throw new NullPointerException();
		}
		int c = -1;
		try {
			final AtomicInteger rsCount  = count;
			final ReentrantLock putLock = this.putLock;
			// 添加add锁
			putLock.lockInterruptibly();
			while (rsCount.get() == capacity) {
				System.out.println("put-rsCount="+rsCount.get());
				putCondition.await();
			}
			enqueue(e);
			c = rsCount.incrementAndGet();
			if (c + 1 < capacity) {
				putCondition.signal();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			putLock.unlock();
		}

		if (c == 0) {
			try {
				final ReentrantLock takeLock = this.takeLock;
				takeLock.lock();
				takeCondition.signal();
			} finally {
				takeLock.unlock();
			}

		}
	}

	public T take() {
		int c = -1;
		final ReentrantLock takeLock = this.takeLock;
		final AtomicInteger rsCount = count;
		T obj;
		
		try {
			// 添加poll锁
			takeLock.lockInterruptibly();
			while (rsCount.get() == 0) {
//				System.out.println("take-rsCount="+rsCount.get());
//				takeCondition.await();
				takeCondition.await(1, TimeUnit.SECONDS);
			}
			obj = dequeue();
			c = rsCount.decrementAndGet();
			if (c > 1) {
				takeCondition.signal();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return null;
		} finally {
			takeLock.unlock();
		}

		if (c == capacity) {
			try {
				putLock.lock();
				putCondition.signal();
			} finally {
				putLock.unlock();
			}
		}

		return obj;
	}

}
