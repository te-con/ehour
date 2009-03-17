package net.rrm.ehour.ui.report.aggregate;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.AbstractSpringInjectorTester;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * CustomerAggregateReport Tester.
 *
 * @author <Authors name>
 * @since <pre>09/11/2007</pre>
 * @version 1.0
 */
public class CustomerAggregateReportTest extends AbstractSpringInjectorTester
{
	private AggregateReportService aggregateReportService;

	@Before
	public void setup() throws Exception
	{
		super.springLocatorSetup();
		
		aggregateReportService = createMock(AggregateReportService.class);
		getMockContext().putBean("aggregateReportService", aggregateReportService);

	}
	
	@Test
    public void testCreateReport() throws Exception
    {
		expect(aggregateReportService.getAggregateReportData(isA(ReportCriteria.class)))
			.andReturn(ReportTestUtil.getAssignmentReportData());
		replay(aggregateReportService);
        UserCriteria userCriteria = new UserCriteria();

        ReportCriteria rc = new ReportCriteria(userCriteria);
        
        CustomerAggregateReport aggReport = new CustomerAggregateReport(rc);

        assertEquals(6, aggReport.getReportData().getReportElements().size());
        
        verify(aggregateReportService);
    }
}
