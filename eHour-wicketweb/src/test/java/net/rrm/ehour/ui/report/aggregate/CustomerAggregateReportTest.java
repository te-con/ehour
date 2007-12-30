package net.rrm.ehour.ui.report.aggregate;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;

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
    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateReport() throws Exception
    {
        ReportData aggData = new ReportData();
        UserCriteria userCriteria = new UserCriteria();

        ReportCriteria rc = new ReportCriteria();
        rc.setUserCriteria(userCriteria);
        aggData.setReportCriteria(rc);
        aggData.setReportElements(AggregateTestUtil.getAssignmentAggregateReportElements());
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
