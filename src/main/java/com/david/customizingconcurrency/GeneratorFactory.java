package com.david.customizingconcurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorFactory
{
	public static List<Product> generate(int count)
	{
		List<Product> products = new ArrayList<Product>();
		for (int i = 0; i < count; i++)
		{
			products.add(new Product("Product-" + i, 10));
		}

		return products;
	}

	public static List<Integer> generateIntList(int count)
	{
		Random r = new Random();
		List<Integer> list = new ArrayList<>();
		int num = 0;
		for (int i = 0; i < count; i++)
		{
			num = r.nextInt(count);
			if (!list.contains(num))
			{
				list.add(num);
			} else
			{
				if (!list.contains(89))
				{
					list.add(89);
				}
			}
		}

		return list;
	}
}
