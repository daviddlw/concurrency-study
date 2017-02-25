package com.david.syncutils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Participant implements Runnable
{
	private String name;
	private VideoConference conference;

	public Participant(String name, VideoConference conference)
	{
		super();
		this.name = name;
		this.conference = conference;
	}

	@Override
	public void run()
	{
		Random rand = new Random();
		int duration = rand.nextInt(8);

		try
		{
			System.out.println(String.format("%s sleep for %d seconds...", name, duration));
			TimeUnit.SECONDS.sleep(duration);
			
			conference.arrive(name);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
