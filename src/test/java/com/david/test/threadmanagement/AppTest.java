package com.david.test.threadmanagement;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class AppTest
{
	private Logger logger = Logger.getLogger(AppTest.class);
	
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void test()
	{
//		fail("Not yet implemented");
		System.out.println("hello world");
		logger.info("hello world...");
	}

}
