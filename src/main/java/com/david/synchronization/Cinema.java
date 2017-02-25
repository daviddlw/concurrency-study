package com.david.synchronization;

public class Cinema
{
	private long vacanciesCinema1;
	private long vacanciesCinema2;
	private final Object controlCinema1;
	private final Object controlCinema2;

	public Cinema()
	{
		super();
		vacanciesCinema1 = 20;
		vacanciesCinema2 = 20;
		controlCinema1 = new Object();
		controlCinema2 = new Object();
	}

	public boolean sellTickets1(int number)
	{
		synchronized (controlCinema1)
		{
			if (number < vacanciesCinema1)
			{
				vacanciesCinema1 -= number;
				return true;
			} else
			{
				return false;
			}
		}
	}

	public boolean sellTickets2(int number)
	{
		synchronized (controlCinema2)
		{
			if (number < vacanciesCinema2)
			{
				vacanciesCinema2 -= number;
				return true;
			} else
			{
				return false;
			}
		}
	}

	public boolean returnTicket1(int number)
	{
		synchronized (controlCinema1)
		{
			vacanciesCinema1 += number;
			return true;
			
		}
	}

	public boolean returnTicket2(int number)
	{
		synchronized (controlCinema2)
		{
			vacanciesCinema2 += number;
			return true;
		}
	}

	public long getVacanciesCinema1()
	{
		return vacanciesCinema1;
	}

	public void setVacanciesCinema1(long vacanciesCinema1)
	{
		this.vacanciesCinema1 = vacanciesCinema1;
	}

	public long getVacanciesCinema2()
	{
		return vacanciesCinema2;
	}

	public void setVacanciesCinema2(long vacanciesCinema2)
	{
		this.vacanciesCinema2 = vacanciesCinema2;
	}

	@Override
	public String toString()
	{
		return "Cinema [vacanciesCinema1=" + vacanciesCinema1
				+ ", vacanciesCinema2=" + vacanciesCinema2 + "]";
	}

}
