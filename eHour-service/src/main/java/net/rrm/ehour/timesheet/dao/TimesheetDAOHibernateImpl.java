/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
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
