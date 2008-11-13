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
import net.rrm.ehour.ui.common.BaseUIWicketTester;

import org.junit.Test;


/**
 * @author thies
 *
 */
public class AuditReportPageTest extends BaseUIWicketTester
{

	@Test
	public void testRender()
	{
		expect(auditService.getAuditCount(isA(AuditReportRequest.class)))
			.andReturn(5);

		expect(auditService.getAudit(isA(AuditReportRequest.class)))
			.andReturn(new ArrayList<Audit>());
		
		replay(auditService);
		
		tester.startPage(AuditReportPage.class);
		tester.assertRenderedPage(AuditReportPage.class);
		tester.assertNoErrorMessage();
		
		verify(auditService);
	}		
}
