package com.david.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PCQueue<T> {

	private final ReentrantLock putLock = new ReentrantLock();
	private final Condition putCondition = putLock.newCondition();
	private final ReentrantLock takeLock = new ReentrantLock();
	private final Condition takeCondition = takeLock.newCondition();
//	private PriorityQueue<T> queue;
	private final int capacity; //一定要使用capacity,否则标注容器容量
	private final AtomicInteger count = new AtomicInteger(0);
    private transient Node<T> head;
    private transient Node<T> last;

	static class Node<T> {
        T item;
        Node<T> next;
        Node(T x) { item = x; }
    }
	
	public PCQueue() {
		this(Integer.MAX_VALUE);	
	}

	public PCQueue(int capacity) {
		this.capacity = capacity;
		last = head = new Node<T>(null);
	}
	
	/**
     * Links node at end of queue.
     *
     * @param node the node
     */
    private void enqueue(Node<T> node) {
        last = last.next = node;
    }

    /**
     * Removes a node from head of queue.
     *
     * @return the node
     */
    private T dequeue() {
        Node<T> h = head;
        Node<T> first = h.next;
        h.next = h; // help GC
        head = first;
        T x = first.item;
        first.item = null;
        return x;
    }

	public void put(T e) {
		if (e == null) {
			throw new NullPointerException();
		}
		Node<T> node = new Node<T>(e);
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
			enqueue(node);
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
