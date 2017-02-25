package com.david.synchronization;

public class TicketOffice2 implements Runnable
{
	private Cinema cinema;

	public TicketOffice2(Cinema cinema)
	{
		super();
		this.cinema = cinema;
	}

	@Override
	public void run()
	{
		cinema.sellTickets1(5);
		cinema.sellTickets2(6);
	}

}
