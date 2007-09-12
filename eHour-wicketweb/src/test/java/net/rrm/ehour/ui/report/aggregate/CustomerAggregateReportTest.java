package net.rrm.ehour.ui.report.aggregate;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.common.DummyDataGenerator;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomerAggregateReport Tester.
 *
 * @author <Authors name>
 * @since <pre>09/11/2007</pre>
 * @version 1.0
 */
public class CustomerAggregateReportTest 
{
	private ProjectAssignmentAggregate pagA, pagB, pagC, pagD, pagE;
    private List<ProjectAssignmentAggregate> aggs;

    @Before
	public void setUp()
	{
        pagA = DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1);
        pagA.getProjectAssignment().getProject().setProjectId(1);
        pagB = DummyDataGenerator.getProjectAssignmentAggregate(20, 1, 2);
        pagB.getProjectAssignment().getProject().setProjectId(2);
        pagC = DummyDataGenerator.getProjectAssignmentAggregate(3, 2, 1);
        pagD = DummyDataGenerator.getProjectAssignmentAggregate(4, 2, 2);
        pagE = DummyDataGenerator.getProjectAssignmentAggregate(5, 3, 3);

        aggs = new ArrayList<ProjectAssignmentAggregate>();
        aggs.add(pagE);
        aggs.add(pagD);
        aggs.add(pagB);
        aggs.add(pagC);
        aggs.add(pagA);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateReport() throws Exception
    {

        ReportDataAggregate aggData = new ReportDataAggregate();
        aggData.setProjectAssignmentAggregates(aggs);
        CustomerAggregateReport aggReport = new CustomerAggregateReport(aggData);

        assertEquals(3, aggReport.getNodes().size());

        for (ReportNode node : aggReport.getNodes())
        {
            if (node.getId().equals(1))
            {
                assertEquals(21.0f, node.getHours().floatValue(), 0);
                assertEquals(2, node.getReportNodes().size());
                assertEquals(1, node.getReportNodes().get(0).getReportNodes().size());
            }
        }
    }

    @Test
    public void testCreateReportForId() throws Exception
    {

        ReportDataAggregate aggData = new ReportDataAggregate();
        aggData.setProjectAssignmentAggregates(aggs);
        CustomerAggregateReport aggReport = new CustomerAggregateReport(aggData, 1);

        assertEquals(1, aggReport.getNodes().size());

        for (ReportNode node : aggReport.getNodes())
        {
            if (node.getId().equals(1))
            {
                assertEquals(21.0f, node.getHours().floatValue(), 0);
                assertEquals(2, node.getReportNodes().size());
                assertEquals(1, node.getReportNodes().get(0).getReportNodes().size());
            }
        }
    }
}
