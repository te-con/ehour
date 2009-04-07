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

package net.rrm.ehour.report.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.element.FlatReportElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * 
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings({"unchecked", "deprecation"})
public class DetailedReportDAOTest extends BaseDAOTest
{
	@Autowired
	private	DetailedReportDAO	detailedReportDAO;
	
	
	@Test
	public void testGetHoursPerDayForAssignment()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;) 
			    new Date(2008 - 1900, 1, 3));
		List projectIds = new ArrayList();
		projectIds.add(1);
		List<FlatReportElement> results = detailedReportDAO.getHoursPerDayForAssignment(projectIds, dateRange);
		
		assertEquals(6, results.size());
		
		assertNotNull(results.get(0).getProjectId());
	}	
	
	@Test
	public void testGetHoursPerDayForUsers()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;) 
			    new Date(2008 - 1900, 1, 3));
		List userIds = new ArrayList();
		userIds.add(1);
		List<FlatReportElement> results = detailedReportDAO.getHoursPerDayForUsers(userIds, dateRange);
		
		assertEquals(10, results.size());
		
		assertNotNull(results.get(0).getProjectId());
	}	
	
	@Test
	public void testGetHoursPerDayForProjects()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;) 
			    new Date(2008 - 1900, 1, 3));
		List projectIds = new ArrayList();
		projectIds.add(2);
		List<FlatReportElement> results = detailedReportDAO.getHoursPerDayForProjects(projectIds, dateRange);
		
		assertEquals(4, results.size());
		
		assertEquals(2, results.get(0).getProjectId().intValue());
	}	
	
	
	@Test
	public void testGetHoursPerDayForProjectsAndUsers()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;) 
			    new Date(2008 - 1900, 1, 3));
		List projectIds = new ArrayList();
		projectIds.add(2);
		List userIds = new ArrayList();
		userIds.add(1);		
		
		List<FlatReportElement> results = detailedReportDAO.getHoursPerDayForProjectsAndUsers(projectIds, userIds, dateRange);
		
		assertEquals(2, results.size());
		
		assertEquals(2, results.get(0).getProjectId().intValue());
	}
	
	@Test
	public void testGetHoursPerDay()
	{
		DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;) 
			    new Date(2008 - 1900, 1, 3));
		
		List<FlatReportElement> results = detailedReportDAO.getHoursPerDay(dateRange);
		
		for (FlatReportElement flatReportElement : results)
		{
			System.out.println(flatReportElement.getTotalHours());
		}
		
		assertEquals(12, results.size());
	}	
}
