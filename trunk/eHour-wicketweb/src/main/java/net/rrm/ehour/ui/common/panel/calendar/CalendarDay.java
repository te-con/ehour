package net.rrm.ehour.ui.common.panel.calendar;

import java.io.Serializable;

public class CalendarDay implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int monthDay;
	private boolean booked;

	public CalendarDay()
	{

	}

	public CalendarDay(int monthDay, boolean booked, boolean weekendDay)
	{
		this.monthDay = monthDay;
		this.booked = booked;
	}

	/**
	 * @return the monthDay
	 */
	public int getMonthDay()
	{
		return monthDay;
	}
	/**
	 * @param monthDay the monthDay to set
	 */
	public void setMonthDay(int monthDay)
	{
		this.monthDay = monthDay;
	}
	/**
	 * @return the booked
	 */
	public boolean isBooked()
	{
		return booked;
	}
	/**
	 * @param booked the booked to set
	 */
	public void setBooked(boolean booked)
	{
		this.booked = booked;
	}
}
