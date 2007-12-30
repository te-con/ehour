/**
 * Created on Dec 30, 2007
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.report.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.DummyDataGenerator;

/**
 * Test data
 **/

public class AggregateTestUtil
{
	public static List<AssignmentAggregateReportElement> getAssignmentAggregateReportElements()
	{
		AssignmentAggregateReportElement pagA, pagB, pagC, pagD, pagE, pagF;
	    List<AssignmentAggregateReportElement> aggs;
		
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
        
        return aggs;
	}
	
	/**
	 * Get some dummy report data
	 * @return
	 */
	public static ReportData getReportData()
	{
		ReportData reportData = new ReportData();
		reportData.setReportElements(AggregateTestUtil.getAssignmentAggregateReportElements());
		
		ReportCriteria criteria = new ReportCriteria();
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setReportRange(new DateRange(new Date(), new Date()));
		criteria.setUserCriteria(userCriteria);
		reportData.setReportCriteria(criteria);

		return reportData;
	}
}
