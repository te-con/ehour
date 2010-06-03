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

package net.rrm.ehour.ui.report.page;

import static org.easymock.EasyMock.createMock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.service.report.reports.ReportData;
import net.rrm.ehour.service.report.service.AggregateReportService;
import net.rrm.ehour.service.report.service.DetailedReportService;
import net.rrm.ehour.service.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.junit.Before;

@SuppressWarnings("serial")
public abstract class BaseTestReport extends AbstractSpringWebAppTester implements Serializable
{
	protected ReportCriteriaService reportCriteriaService;
	protected AggregateReportService aggregateReportService;
	protected DetailedReportService  detailedReportService;
	protected ReportData data;
	protected ReportCriteria reportCriteria;
	
	@Before
	public final void before() throws Exception
	{
		reportCriteriaService = createMock(ReportCriteriaService.class);
		getMockContext().putBean("reportCriteriaService", reportCriteriaService);

		aggregateReportService = createMock(AggregateReportService.class);
		getMockContext().putBean("aggregatedReportService", aggregateReportService);

		detailedReportService = createMock(DetailedReportService.class);
		getMockContext().putBean("detailedReportService", detailedReportService);

		AvailableCriteria availCriteria = new AvailableCriteria();

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

		reportCriteria = new ReportCriteria(availCriteria);
		
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

		data = new ReportData(agg, reportCriteria.getReportRange());
	}
}
