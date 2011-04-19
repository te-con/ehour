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

package net.rrm.ehour.report.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.report.dao.DetailedReportDao;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.element.FlatReportElement;

import org.junit.Before;
import org.junit.Test;

/**
 * DetailedReportServiceImplTest 
 **/
@SuppressWarnings({"unchecked"})
public class DetailedReportServiceImplTest
{
	private	DetailedReportDao		detailedReportDAO;
	private	DetailedReportService	detailedReportService;
	private	ReportCriteria			rc;
	private UserCriteria 			uc;
	private DateRange 				dr;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		detailedReportService = new DetailedReportServiceImpl();

		detailedReportDAO = createMock(DetailedReportDao.class);
		((DetailedReportServiceImpl)detailedReportService).setDetailedReportDAO(detailedReportDAO);

		dr = new DateRange();
		uc = new UserCriteria();
		uc.setReportRange(dr);
		rc = new ReportCriteria(uc);
	}

	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.report.service.DetailedReportServiceImpl#getDetailedReportData(net.rrm.ehour.persistence.persistence.report.criteria.ReportCriteria)}.
	 */
	@Test
	public void testGetDetailedReportAll()
	{
		expect(detailedReportDAO.getHoursPerDay(dr))
				.andReturn(new ArrayList<FlatReportElement>());
		replay(detailedReportDAO);
		detailedReportService.getDetailedReportData(rc);
		verify(detailedReportDAO);
	}

	@Test
	public void testGetDetailedReportDataUsersOnly()
	{
		List<User> l = new ArrayList<User>();
		l.add(new User(1));
		uc.setUsers(l);
		
		expect(detailedReportDAO.getHoursPerDayForUsers(isA(List.class), isA(DateRange.class)))
			.andReturn(new ArrayList<FlatReportElement>());
		replay(detailedReportDAO);
		detailedReportService.getDetailedReportData(rc);
		verify(detailedReportDAO);
	}
	
	@Test
	public void testGetDetailedReportDataProjectsOnly()
	{
		List<Project> l = new ArrayList<Project>();
		l.add(new Project(1));
		uc.setProjects(l);
		
		expect(detailedReportDAO.getHoursPerDayForProjects(isA(List.class), isA(DateRange.class)))
			.andReturn(new ArrayList<FlatReportElement>());
		replay(detailedReportDAO);
		detailedReportService.getDetailedReportData(rc);
		verify(detailedReportDAO);
	}	
	
	@Test
	public void testGetDetailedReportDataProjectsAndUsers()
	{
		List<Project> l = new ArrayList<Project>();
		l.add(new Project(1));
		uc.setProjects(l);
		
		List<User> u = new ArrayList<User>();
		u.add(new User(1));
		uc.setUsers(u);		
		
		expect(detailedReportDAO.getHoursPerDayForProjectsAndUsers(isA(List.class), isA(List.class), isA(DateRange.class)))
			.andReturn(new ArrayList<FlatReportElement>());
		replay(detailedReportDAO);
		detailedReportService.getDetailedReportData(rc);
		verify(detailedReportDAO);
	}	
}
