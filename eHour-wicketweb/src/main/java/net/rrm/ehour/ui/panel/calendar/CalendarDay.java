package net.rrm.ehour.ui.panel.calendar;

import java.io.Serializable;

public class CalendarDay implements Serializable
{
	private int monthDay;
	private boolean booked;
	private boolean weekendDay;

	public CalendarDay()
	{

	}

	public CalendarDay(int monthDay, boolean booked, boolean weekendDay)
	{
		this.monthDay = monthDay;
		this.booked = booked;
		this.weekendDay = weekendDay;
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

	/**
	 * @return the weekendDay
	 */
	public boolean isWeekendDay()
	{
		return weekendDay;
	}

	/**
	 * @param weekendDay the weekendDay to set
	 */
	public void setWeekendDay(boolean weekendDay)
	{
		this.weekendDay = weekendDay;
	}
}
