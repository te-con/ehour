/**
 * 
 */
package net.rrm.ehour.ui.page.pm;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.panel.report.ReportTestUtil;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Thies
 *
 */
public class ProjectManagementTest extends BaseUIWicketTester
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
	 * Test method for {@link net.rrm.ehour.ui.page.pm.ProjectManagement#ProjectManagement()}.
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	@Test
	public void testProjectManagement() throws SecurityException, NoSuchMethodException
	{
		expect(projectService.getProjectManagerProjects(isA(User.class)))
						.andReturn(new ArrayList<Project>());	
		
		replay(projectService);
		replay(aggregateReportService);
		
		final ProjectManagement pmPage = (ProjectManagement)tester.startPage(ProjectManagement.class);
		tester.assertRenderedPage(ProjectManagement.class);
		tester.assertNoErrorMessage();
		verify(projectService);
		verify(aggregateReportService);
//		reset(projectService);
//		reset(aggregateReportService);	
//		ProjectManagerReport report = new ProjectManagerReport();
//		report.setProject(new Project(1));
//		report.setReportRange(new DateRange(new Date(), new Date()));
//		
//		SortedSet<AssignmentAggregateReportElement> agg = new TreeSet<AssignmentAggregateReportElement>();
//		agg.addAll((ReportTestUtil.getAssignmentAggregateReportElements()));
//		
//		report.setAggregates(agg);
//		expect(aggregateReportService.getProjectManagerReport(isA(DateRange.class), isA(Integer.class)))
//					.andReturn(report);	
//
//
//		pmPage.setReportPanel(new WebMarkupContainer("reportPanel")
//		{
//			@Override
//			public void replaceWith(Component replacement)
//			{
//				
//			}
//		}) ;
//		
//		AjaxRequestTarget target = createMock(AjaxRequestTarget.class, AjaxRequestTarget.class.getMethod("addComponent", Component.class));
//		
//		ReportCriteria reportCriteria = (ReportCriteria)pmPage.getModelObject();
//		UserCriteria userCriteria = new UserCriteria();
//		Project prj = new Project(1);
//		userCriteria.setProject(prj);
//		userCriteria.setReportRange(new DateRange());
//		reportCriteria.setUserCriteria(userCriteria);
//
//		
//		replay(aggregateReportService);
//		pmPage.ajaxRequestReceived(target, 0, null);
//		verify(aggregateReportService);
	}

}
