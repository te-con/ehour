/**
 * 
 */
package net.rrm.ehour.ui.audit.page;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
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
		replay(auditService);
		
		tester.startPage(AuditReportPage.class);
		tester.assertRenderedPage(AuditReportPage.class);
		tester.assertNoErrorMessage();
		
		verify(auditService);
	}		
}
