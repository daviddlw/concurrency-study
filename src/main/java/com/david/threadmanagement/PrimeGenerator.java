package com.david.threadmanagement;

/**
 * Interrupted Demo
 * @author dailiwei
 *
 */
public class PrimeGenerator extends Thread
{
	private int number = 1;

	@Override
	public void run()
	{
		while (true)
		{
			if (isPrime(number))
			{
				System.out.println(String.format("number is prime: %d", number));
			}

			if (isInterrupted())
			{
				System.out.println("The prime generator is interrupted...");
				return;
			}

			number++;
		}
	}

	private boolean isPrime(int number)
	{
		if (number <= 2)
		{
			return true;
		}

		if (number % 2 == 0)
		{
			return false;
		}

		return true;
	}
}
