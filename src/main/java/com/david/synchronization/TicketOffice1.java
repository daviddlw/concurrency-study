package com.david.synchronization;

public class TicketOffice1 implements Runnable
{
	private Cinema cinema;

	public TicketOffice1(Cinema cinema)
	{
		super();
		this.cinema = cinema;
	}

	@Override
	public void run()
	{
		cinema.sellTickets1(1);
		cinema.sellTickets2(2);
		cinema.sellTickets1(3);
		cinema.sellTickets2(4);
		cinema.returnTicket1(2);
		cinema.returnTicket2(3);
	}

}
