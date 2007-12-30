/**
 * Created on Oct 31, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.page.report;

import static org.easymock.EasyMock.createMock;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.criteria.AggregateAvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 * TODO 
 **/

public class BaseTestReport extends BaseUIWicketTester
{
	protected ReportCriteriaService reportCriteriaService;
	protected ReportService  reportService;
	protected ReportData data;
	protected ReportCriteria reportCriteria;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		
		reportCriteriaService = createMock(ReportCriteriaService.class);
		mockContext.putBean("reportCriteriaService", reportCriteriaService);

		reportService = createMock(ReportService.class);
		mockContext.putBean("reportService", reportService);
		
		reportCriteria = new ReportCriteria();
		AggregateAvailableCriteria availCriteria = new AggregateAvailableCriteria();

		List<Customer> customers = new ArrayList<Customer>();
		customers.add(new Customer(1));
		availCriteria.setCustomers(customers);

		List<Project> projects = new ArrayList<Project>();
		projects.add(new Project(2));
		availCriteria.setProjects(projects);

		List<UserDepartment> depts = new ArrayList<UserDepartment>();
		depts.add(new UserDepartment(2));
		availCriteria.setUserDepartments(depts);
		
		List<User> usrs = new ArrayList<User>();
		usrs.add(new User(2));
		availCriteria.setUsers(usrs);
		
		reportCriteria.setAvailableCriteria(availCriteria);
		
		data = new ReportData();
		data.setReportCriteria(reportCriteria);
		List<AssignmentAggregateReportElement> agg = new ArrayList<AssignmentAggregateReportElement>();
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		ProjectAssignment ass = new ProjectAssignment(1);
		User user = new User(1);
		ass.setUser(user);
		
		Customer cust = new Customer(1);
		Project prj = new Project(1);
		prj.setCustomer(cust);
		ass.setProject(prj);
		pag.setProjectAssignment(ass);
		
		agg.add(pag);
		data.setReportElements(agg);		
	}
}
