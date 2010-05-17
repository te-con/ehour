/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.timesheet.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.AbstractGenericDaoHibernateImpl;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.timesheet.dto.BookedDay;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository("timesheetDAO")
public class TimesheetDAOHibernateImpl 
		extends AbstractGenericDaoHibernateImpl<TimesheetEntry, TimesheetEntryId>
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
