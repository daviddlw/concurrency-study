package com.david.forkjoin;

import java.util.Random;

public class Document
{
	private String[] words = new String[] { "the", "hello", "goodbye", "packt", "java", "thread", "pool", "random", "class", "main" };

	public String[][] generateDocument(int numLines, int numWords, String word)
	{
		String[][] wordMatrix = new String[numLines][numWords];
		Random r = new Random();
		
		int counter = 0;

		for (int i = 0; i < numLines; i++)
		{		
			for (int j = 0; j < numWords; j++)
			{
				int index = r.nextInt(word.length());
				wordMatrix[i][j] = words[index];

				if (wordMatrix[i][j].equalsIgnoreCase(word))
				{
					counter++;
				}
			}
		}

		System.out.println(String.format("Document: The word appears %d times in the document.", counter));
		return wordMatrix;
	}
}
