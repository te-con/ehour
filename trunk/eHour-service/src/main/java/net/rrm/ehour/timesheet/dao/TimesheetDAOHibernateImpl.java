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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
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
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.dao.TimesheetDAO#getTimesheetEntriesInRange(net.rrm.ehour.domain.ProjectAssignment, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<TimesheetEntry> getTimesheetEntriesInRange(ProjectAssignment assignment, DateRange dateRange)
	{
		List		results;
		String[]	keys = new String[3];
		Object[]	params = new Object[3];
		String		hql= "Timesheet.getEntriesBetweenDateForAssignment";
		
		keys[0] = "dateStart";
		keys[1] = "dateEnd";
		keys[2] = "assignment";
		
		params[0] = dateRange.getDateStart();
		params[1] = dateRange.getDateEnd();
		params[2] = assignment;
		
		results = getHibernateTemplate().findByNamedQueryAndNamedParam(hql, keys, params);
		
		return results;			
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
	 * 
	 * @param userId
	 * @param range
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
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
		return (TimesheetEntry) getHibernateTemplate().executeWithNativeSession(
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
			});		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.dao.TimesheetDAO#deleteTimesheetEntries(java.util.List)
	 */
	public int deleteTimesheetEntries(List<Serializable> assignmentIds)
	{
		Session session = getSession();
		Query	query = session.getNamedQuery("Timesheet.deleteOnAssignmentIds");
		query.setParameterList("assignmentIds", assignmentIds);
		
		int rowCount = query.executeUpdate();
		
		return rowCount;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.dao.TimesheetDAO#getTimesheetEntriesAfter(java.lang.Integer, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<TimesheetEntry> getTimesheetEntriesAfter(ProjectAssignment assignment, Date date)
	{
		String[]	keys = new String[2];
		Object[]	params = new Object[2];
		
		keys[0] = "date";
		keys[1] = "assignment";
		
		params[0] = date;
		params[1] = assignment;
		
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Timesheet.getEntriesAfterDateForAssignment", keys, params);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.dao.TimesheetDAO#getTimesheetEntriesBefore(java.lang.Integer, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<TimesheetEntry> getTimesheetEntriesBefore(ProjectAssignment assignment, Date date)
	{
		String[]	keys = new String[2];
		Object[]	params = new Object[2];
		
		keys[0] = "date";
		keys[1] = "assignment";
		
		params[0] = date;
		params[1] = assignment;
		
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Timesheet.getEntriesAfterDateForAssignment", keys, params);
	}
}
