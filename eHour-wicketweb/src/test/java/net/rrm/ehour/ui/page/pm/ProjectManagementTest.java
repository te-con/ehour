/**
 * 
 */
package net.rrm.ehour.ui.page.pm;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.panel.report.pm.PmReportPanel;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.util.tester.FormTester;
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
		List<Project> projects = new ArrayList<Project>();
		projects.add(new Project(1));
		
		expect(projectService.getProjectManagerProjects(isA(User.class)))
						.andReturn(projects);	
		tester.createRequestCycle();
		
		replay(projectService);
		replay(aggregateReportService);
		tester.setupRequestAndResponse(true);
		final ProjectManagement pmPage = (ProjectManagement)tester.startPage(ProjectManagement.class);
		tester.assertRenderedPage(ProjectManagement.class);
		tester.assertNoErrorMessage();
		verify(projectService);
		
		verify(aggregateReportService);
		
//		tester.getComponentFromLastRenderedPage("sidePanel:criteriaForm:submitButton");

		
//		tester.clickLink("sidePanel:criteriaForm:submitButton");
		
		FormTester form = tester.newFormTester("sidePanel:criteriaForm");
//		form.setValue("userCriteria.projects:0", "1");
		
		form.select("userCriteria.projects", 0);
		
//		tester.executeAjaxEvent("sidePanel:criteriaForm:submitButton", "onclick");
////		
////        // set the parameters for each component in the form
////        // notice that the name is relative to the form - so it's 'username', not 'form:username' as in assertComponent
////        form.setValue("username", "test");
////        // unset value is empty string (wicket binds this to null, so careful if your setter does not expect nulls)
////        form.setValue("password", "");
////        // slight pain in the butt, for RadioGroups the value string is a bit complicated
////        // I believe it's the pageversion followed by the complete component name (not the relative, now) then the id for the choice itself
////        // the easiest way is to render the page once and then copy & paste 
////        // pageversion didn't seem to have an effect, so I always replace it by 0
////        form.setValue("offSiteAccessEnabled", "0:form:offSiteAccessEnabled:Yes");
////        // another one to pay attention: listviews
////        // here I have a 3 column iteration through a listview with 10 rows iterating through another listview
////        // so it's the listview followed by the row id followed by the inner component in the listview
////        form.setValue("addressRow:0:addressColumn:0:mask", "");
////        // all set, submit
        form.submit("sidePanel:criteriaForm:submitBuftton");
//        // check if the page is correct: in this case, I'm expecting an error to take me back to the same page
//        // check if the error message is the one expected (you should use wicket's internationalization for this)
//        // if you're not expecting an error (testing for submit successful) use assertNoErrorMessage() instead
        tester.assertNoErrorMessage();
        
//        tester.assertComponent("reportPanel", PmReportPanel.class);
//        tester.assertComponentOnAjaxResponse("reportPanel");
        
//        tester.assertErrorMessages(new String[] { "A Login and Password are required to enable offsite access." });		
	}
}
