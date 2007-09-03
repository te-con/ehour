/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.timesheet.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.domain.TimesheetEntryId;
import net.rrm.ehour.timesheet.dto.BookedDay;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class TimesheetDAOHibernateImpl 
		extends GenericDAOHibernateImpl<TimesheetEntry, TimesheetEntryId>
		implements TimesheetDAO
{
	/**
	 * @todo fix this a bit better
	 */
	public TimesheetDAOHibernateImpl()
	{
		super(TimesheetEntry.class);
	}	
	
	/**
	 * Get timesheet entries within date range for a user
	 * @param userId
	 * @param dateStart
	 * @param dateEnd
	 * @return List with TimesheetEntry domain objects
	 */
	@SuppressWarnings("unchecked")
	public List<TimesheetEntry> getTimesheetEntriesInRange(Integer userId, DateRange dateRange)
	{
		return getListOnUserIdAndRange(userId, dateRange, "Timesheet.getEntriesBetweenDateForUserId");
	}

	
	/**
	 * Get  hours per day for a date range
	 * @param userId
	 * @param dateRange
	 * @return List with key values -> key = date, value = hours booked
	 */	
	@SuppressWarnings("unchecked")
	public List<BookedDay> getBookedHoursperDayInRange(Integer userId, DateRange dateRange)
	{
		return getListOnUserIdAndRange(userId, dateRange, "Timesheet.getBookedDaysInRangeForUserId");
	}


	/**
	 * Get timesheet entry count for an assignment
	 */
	@SuppressWarnings("unchecked")
	public int getTimesheetEntryCountForAssignment(Integer assignmentId)
	{
		List	results;
		results = getHibernateTemplate().findByNamedQueryAndNamedParam("Timesheet.getEntryCountForAssignmentId",
																		"assignmentId",
																		assignmentId);
		
		return ((Long)results.get(0)).intValue();
		
	}

	/**
	 * Get list of project assignments booked on in a daterange
	 * @param userId
	 * @param dateRange
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> getBookedProjectAssignmentsInRange(Integer userId, DateRange dateRange)
	{
		return getListOnUserIdAndRange(userId, dateRange, "Timesheet.getBookedProjectsInRangeForUserId");
	}
	 	
	/**
	 * 
	 * @param userId
	 * @param range
	 * @param hql
	 * @return
	 */
	private List getListOnUserIdAndRange(Integer userId, DateRange dateRange, String hql)
	{
		List		results;
		String[]	keys = new String[3];
		Object[]	params = new Object[3];
		
		keys[0] = "dateStart";
		keys[1] = "dateEnd";
		keys[2] = "userId";
		
		params[0] = dateRange.getDateStart();
		params[1] = dateRange.getDateEnd();
		params[2] = userId;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam(hql, keys, params);
		
		return results;			
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.dao.TimesheetDAO#getLatestTimesheetEntryForAssignment(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public TimesheetEntry getLatestTimesheetEntryForAssignment(final Integer assignmentId)
	{
		return (TimesheetEntry) getHibernateTemplate().execute(
				new HibernateCallback()
				{
					public Object doInHibernate(Session session) throws HibernateException
					{
						List<TimesheetEntry> results;
						
						Query queryObject = session.getNamedQuery("Timesheet.getLatestEntryForAssignmentId");
						queryObject.setInteger("assignmentId", assignmentId);
						queryObject.setMaxResults(1);
						results = (List<TimesheetEntry>)queryObject.list();
						return ((results != null && results.size() > 0) ? results.get(0) : null);
					}
			}, true);		
	}
}
