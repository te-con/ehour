package net.rrm.ehour.ui.report.aggregate;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.report.reports.dto.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.DummyDataGenerator;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;

import org.junit.Before;
import org.junit.Test;

/**
 * CustomerAggregateReport Tester.
 *
 * @author <Authors name>
 * @since <pre>09/11/2007</pre>
 * @version 1.0
 */
public class CustomerAggregateReportTest 
{
	private AssignmentAggregateReportElement pagA, pagB, pagC, pagD, pagE, pagF;
    private List<AssignmentAggregateReportElement> aggs;

    @Before
	public void setUp()
	{
        pagA = DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1);
        pagA.getProjectAssignment().getProject().setProjectId(1);
        pagB = DummyDataGenerator.getProjectAssignmentAggregate(20, 1, 2);
        pagB.getProjectAssignment().getProject().setProjectId(2);
        pagF = DummyDataGenerator.getProjectAssignmentAggregate(30, 1, 3);
        pagF.getProjectAssignment().getProject().setProjectId(2);
        pagC = DummyDataGenerator.getProjectAssignmentAggregate(3, 2, 1);
        pagD = DummyDataGenerator.getProjectAssignmentAggregate(4, 2, 2);
        pagE = DummyDataGenerator.getProjectAssignmentAggregate(5, 3, 3);

        aggs = new ArrayList<AssignmentAggregateReportElement>();
        aggs.add(pagE);
        aggs.add(pagD);
        aggs.add(pagB);
        aggs.add(pagC);
        aggs.add(pagA);
        aggs.add(pagF);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateReport() throws Exception
    {

        ReportDataAggregate aggData = new ReportDataAggregate();
        UserCriteria userCriteria = new UserCriteria();

        ReportCriteria rc = new ReportCriteria();
        rc.setUserCriteria(userCriteria);
        aggData.setReportCriteria(rc);
        aggData.setProjectAssignmentAggregates(aggs);
        CustomerAggregateReport aggReport = new CustomerAggregateReport(aggData);

        assertEquals(3, aggReport.getNodes().size());

        for (ReportNode node : aggReport.getNodes())
        {
            if (node.getId().equals(1))
            {
                assertEquals(51.0f, node.getHours().floatValue(), 0);
                assertEquals(2, node.getReportNodes().size());
                assertEquals(2, node.getReportNodes().get(0).getReportNodes().size());
                
             // test matrix creation
                int matrixWidth =	node.getColumnValues().length + 
                					node.getReportNodes().get(0).getColumnValues().length +
                					node.getReportNodes().get(0).getReportNodes().get(0).getColumnValues().length;
                
                Serializable[][] matrix = node.getNodeMatrix(matrixWidth);
                
                assertEquals("TestUser, Dummy", matrix[0][3]);
                assertEquals(15.0f, matrix[2][6]);
            }
        }
    }

//    @Test
//    public void testCreateReportForId() throws Exception
//    {
//
//        ReportDataAggregate aggData = new ReportDataAggregate();
//        ReportCriteria rc = new ReportCriteria();
//        UserCriteria userCriteria = new UserCriteria();
//        rc.setUserCriteria(userCriteria);
//        aggData.setReportCriteria(rc);
//        aggData.setProjectAssignmentAggregates(aggs);
//        CustomerAggregateReport aggReport = new CustomerAggregateReport(aggData, 1);
//
//        assertEquals(1, aggReport.getNodes().size());
//
//        for (ReportNode node : aggReport.getNodes())
//        {
//            if (node.getId().equals(1))
//            {
//                assertEquals(51.0f, node.getHours().floatValue(), 0);
//                assertEquals(2, node.getReportNodes().size());
//                assertEquals(2, node.getReportNodes().get(0).getReportNodes().size());
//            }
//        }
//    }
}
