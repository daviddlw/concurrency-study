package com.david.forkjoin;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Product> products;
	private int first;
	private int last;
	private int incremment;

	public Task(List<Product> products, int first, int last, int incremment)
	{
		super();
		this.products = products;
		this.first = first;
		this.last = last;
		this.incremment = incremment;
	}

	@Override
	protected void compute()
	{
		if (last - first < 10)
		{
			updatePrices();
		} else
		{
			int middle = (first + last) / 2;
			System.out.println("Pending tasks..." + getQueuedTaskCount());
			Task leftTask = new Task(products, 0, middle + 1, incremment);
			Task rightTask = new Task(products, middle + 1, last, incremment);

			invokeAll(leftTask, rightTask);
		}
	}

	private void updatePrices()
	{
		for (int i = first; i < last; i++)
		{
			Product product = products.get(i);
			product.setPrice(product.getPrice() + incremment);
		}
	}
}
