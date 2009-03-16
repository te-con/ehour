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
import net.rrm.ehour.ui.common.BaseUIWicketTester;

import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author thies
 *
 */
public class AuditReportPageTest extends BaseUIWicketTester
{
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		expect(auditService.getAuditCount(isA(AuditReportRequest.class)))
			.andReturn(5)
			.anyTimes();

		expect(auditService.getAudit(isA(AuditReportRequest.class)))
			.andReturn(new ArrayList<Audit>())
			.anyTimes();
	}
	
	@After
	public void tearDown()
	{
		verify(auditService);
	}
	
	private void startPage()
	{
		tester.startPage(AuditReportPage.class);
		tester.assertRenderedPage(AuditReportPage.class);
	}
	
	@Test
	public void shouldSubmit()
	{
		replay(auditService);
		startPage();

		String formPath = AuditConstants.PATH_FRAME + ":" + 
						AuditConstants.PATH_CRITERIA + ":" + 
						AuditConstants.PATH_FORM_BORDER + ":" +
						AuditConstants.ID_FORM;
		
		tester.executeAjaxEvent(formPath + ":" +  
								AuditConstants.PATH_FORM_SUBMIT,
								"onclick");
		
//		FormTester formTester = tester.newFormTester(formPath);
//		formTester.submit();
		
		tester.assertRenderedPage(AuditReportPage.class);
		
		tester.assertNoErrorMessage();
	}
	
	
}
