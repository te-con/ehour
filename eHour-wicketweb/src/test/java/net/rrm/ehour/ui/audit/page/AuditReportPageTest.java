/**
 * 
 */
package net.rrm.ehour.ui.audit.page;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.ui.audit.AuditConstants;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author thies
 *
 */
public class AuditReportPageTest extends AbstractSpringWebAppTester
{
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		expect(getAuditService().getAuditCount(isA(AuditReportRequest.class)))
			.andReturn(5)
			.anyTimes();

		expect(getAuditService().getAudit(isA(AuditReportRequest.class)))
			.andReturn(new ArrayList<Audit>())
			.anyTimes();
	}
	
	@After
	public void tearDown()
	{
		verify(getAuditService());
	}
	
	private void startPage()
	{
		getTester().startPage(AuditReportPage.class);
		getTester().assertRenderedPage(AuditReportPage.class);
	}
	
	@Test
	public void shouldSubmit()
	{
		replay(getAuditService());
		startPage();

		String formPath = AuditConstants.PATH_FRAME + ":" + 
						AuditConstants.PATH_CRITERIA + ":" + 
						AuditConstants.PATH_FORM_BORDER + ":" +
						AuditConstants.ID_FORM;
		
		getTester().executeAjaxEvent(formPath + ":" +  
								AuditConstants.PATH_FORM_SUBMIT,
								"onclick");
		
//		FormTester formTester = getTester().newFormTester(formPath);
//		formTester.submit();
		
		getTester().assertRenderedPage(AuditReportPage.class);
		
		getTester().assertNoErrorMessage();
	}
	
	
}
