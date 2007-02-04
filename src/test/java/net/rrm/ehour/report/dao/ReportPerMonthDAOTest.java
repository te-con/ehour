/**
 * Created on Feb 4, 2007
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

package net.rrm.ehour.report.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.project.WeeklyProjectAssignmentAggregate;

/**
 * TODO 
 **/

public class ReportPerMonthDAOTest extends BaseDAOTest
{
	private	ReportPerMonthDAO	dao;
	
	public void testGetHoursPerMonthPerAssignmentForUsersIntegerArrayDateRange()
	{
		List userIds = new ArrayList();
		userIds.add(2);
		
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 12 - 1, 1), // deprecated? hmm ;) 
			    new Date(2007 - 1900, 10, 30));

		
		List<WeeklyProjectAssignmentAggregate> results = dao.getHoursPerMonthPerAssignmentForUsers(userIds, dateRange);
		
		assertEquals(1, results.size());
	}

	/**
	 * 
	 *
	 */
	public void testGetHoursPerMonthPerAssignmentForUsersIntegerArrayIntegerArrayDateRange()
	{
		List userIds = new ArrayList();
		userIds.add(1);
		
		List projectIds = new ArrayList();
		projectIds.add(2);
		
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;) 
			    new Date(2006 - 1900, 10, 3));

		
		List<WeeklyProjectAssignmentAggregate> results = dao.getHoursPerMonthPerAssignmentForUsers(userIds, projectIds, dateRange);
		
		assertEquals(1, results.size());
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(ReportPerMonthDAO dao)
	{
		this.dao = dao;
	}

}
