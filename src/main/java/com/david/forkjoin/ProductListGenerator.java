package com.david.forkjoin;

import java.util.ArrayList;
import java.util.List;

public class ProductListGenerator
{
	public List<Product> generate(int size)
	{
		List<Product> ls = new ArrayList<>();
		for (int i = 0; i < size; i++)
		{
			ls.add(new Product("Product" + i, 10));
		}

		return ls;
	}
}
