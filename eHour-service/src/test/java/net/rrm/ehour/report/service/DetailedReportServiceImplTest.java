/**
 * Created on Jan 20, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.dao.DetailedReportDAO;
import net.rrm.ehour.report.reports.element.FlatReportElement;

import org.junit.Before;
import org.junit.Test;

/**
 * DetailedReportServiceImplTest 
 **/

public class DetailedReportServiceImplTest
{
	private	DetailedReportDAO		detailedReportDAO;
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

		detailedReportDAO = createMock(DetailedReportDAO.class);
		((DetailedReportServiceImpl)detailedReportService).setDetailedReportDAO(detailedReportDAO);

		rc = new ReportCriteria();
		dr = new DateRange();
		uc = new UserCriteria();
		uc.setReportRange(dr);
		rc.setUserCriteria(uc);		
	}

	/**
	 * Test method for {@link net.rrm.ehour.report.service.DetailedReportServiceImpl#getDetailedReportData(net.rrm.ehour.report.criteria.ReportCriteria)}.
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
	
	
	/**
	 * Test method for {@link net.rrm.ehour.report.service.DetailedReportServiceImpl#getDetailedReportData(java.util.List, net.rrm.ehour.data.DateRange)}.
	 */
	@Test
	public void testGetDetailedReportDataListOfSerializableDateRange()
	{
		expect(detailedReportDAO.getHoursPerDayForAssignment(isA(List.class), isA(DateRange.class)))
		.andReturn(new ArrayList<FlatReportElement>());
		replay(detailedReportDAO);
		detailedReportService.getDetailedReportData(new ArrayList<Serializable>(), dr);
		verify(detailedReportDAO);
	}
}
