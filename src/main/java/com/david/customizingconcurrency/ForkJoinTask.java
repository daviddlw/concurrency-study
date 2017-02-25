package com.david.customizingconcurrency;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class ForkJoinTask extends RecursiveAction
{
	private static final long serialVersionUID = 1L;

	private List<Product> products;
	private int first;
	private int last;
	private double increment;

	public ForkJoinTask()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ForkJoinTask(List<Product> products, int first, int last, double increment)
	{
		super();
		this.products = products;
		this.first = first;
		this.last = last;
		this.increment = increment;
	}

	@Override
	protected void compute()
	{
		if (last - first < 10)
		{
			updatePrice();
		} else
		{
			int middle = (first + last) / 2;
			ForkJoinTask firstTask = new ForkJoinTask(products, first, middle + 1, increment);
			ForkJoinTask lastTask = new ForkJoinTask(products, middle + 1, last, increment);
			invokeAll(firstTask, lastTask);
		}
	}

	private void updatePrice()
	{
		Product product;
		for (int i = first; i < last; i++)
		{
			product = products.get(i);
			if (product != null)
			{
				product.setPrice(10 * (1 + increment));
			}
		}
	}
}
