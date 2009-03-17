/**
 * 
 */
package net.rrm.ehour.ui.pm.page;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Thies
 *
 */
public class ProjectManagementTest extends AbstractSpringWebAppTester
{
	ProjectService	projectService;
	
	AggregateReportService aggregateReportService;
		
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		projectService = createMock(ProjectService.class);
		mockContext.putBean("projectService", projectService);		
		
		aggregateReportService = createMock(AggregateReportService.class);
		mockContext.putBean("aggregatedReportService", aggregateReportService);
	}


	/**
	 * Test method for {@link net.rrm.ehour.ui.pm.page.ProjectManagement#ProjectManagement()}.
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	@Test
	public void testProjectManagement() throws SecurityException, NoSuchMethodException
	{
		List<Project> projects = new ArrayList<Project>();
		projects.add(new Project(1));
		
		ProjectManagerReport report = new ProjectManagerReport();
		report.setProject(new Project(1));
		report.setReportRange(new DateRange(new Date(), new Date()));
		expect(aggregateReportService.getProjectManagerDetailedReport(isA(DateRange.class), isA(Integer.class)))
			.andReturn(report)
			.anyTimes();
		
		expect(projectService.getProjectManagerProjects(isA(User.class)))
						.andReturn(projects);	
		getTester().createRequestCycle();
		
		replay(projectService);
		replay(aggregateReportService);
		getTester().setupRequestAndResponse(true);
		getTester().startPage(ProjectManagement.class);
		getTester().assertRenderedPage(ProjectManagement.class);
		getTester().assertNoErrorMessage();
		
		
		FormTester form = getTester().newFormTester("sidePanel:criteriaForm");
		form.select("userCriteria.projects", 0);
		
        form.submit("submitButton");

        getTester().executeAjaxEvent("sidePanel:criteriaForm:submitButton", "onclick");
        
        getTester().assertNoErrorMessage();
		verify(projectService);
		
		verify(aggregateReportService);
        
//        getTester().assertComponent("reportPanel", PmReportPanel.class);
//        getTester().assertComponentOnAjaxResponse("reportPanel");
        
//        getTester().assertErrorMessages(new String[] { "A Login and Password are required to enable offsite access." });		
	}
}
