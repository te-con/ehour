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

package net.rrm.ehour.ui.report.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.common.DummyWebDataGenerator;

/**
 * Test data
 **/

@SuppressWarnings({"deprecation"})
public class ReportTestUtil
{
	public static List<FlatReportElement> getFlatReportElements()
	{
		List<FlatReportElement> els = new ArrayList<FlatReportElement>();
		
		{
			FlatReportElement fre = new FlatReportElement();
			fre.setAssignmentId(1);
			fre.setComment("ja hallo");
			fre.setCustomerCode("AA");
			fre.setCustomerId(1);
			fre.setCustomerName("A Company");
			fre.setDayDate(new Date(2007 - 1900, 12 - 1, 31));
			fre.setDisplayOrder(1);
			fre.setEntryDate("492007");
			fre.setHours(5);
			fre.setTotalHours(5);
			fre.setProjectId(1);
			fre.setProjectName("PRJ");
			fre.setUserId(5);
			els.add(fre);
		}

		{
			FlatReportElement fre = new FlatReportElement();
			fre.setAssignmentId(1);
			fre.setComment("ja hallo");
			fre.setCustomerCode("AA");
			fre.setCustomerId(1);
			fre.setCustomerName("A Company");
			fre.setDayDate(new Date(2007 - 1900, 12 - 1, 30));
			fre.setDisplayOrder(1);
			fre.setEntryDate("492007");
			fre.setHours(5);
			fre.setTotalHours(5);
			fre.setProjectId(1);
			fre.setProjectName("PRJ");
			fre.setUserId(5);
			els.add(fre);
		}
		
		{
			FlatReportElement fre = new FlatReportElement();
			fre.setAssignmentId(2);
			fre.setComment("ja hallo");
			fre.setCustomerCode("AA");
			fre.setCustomerId(1);
			fre.setCustomerName("A Company");
			fre.setDayDate(new Date(2007 - 1900, 12 - 1, 30));
			fre.setDisplayOrder(1);
			fre.setEntryDate("492007");
			fre.setHours(5);
			fre.setTotalHours(5);
			fre.setProjectId(1);
			fre.setProjectName("PRJ");
			fre.setUserId(6);
			els.add(fre);
		}
		
		{
			FlatReportElement fre = new FlatReportElement();
			fre.setAssignmentId(2);
			fre.setComment("ja hallo");
			fre.setCustomerCode("AA");
			fre.setCustomerId(1);
			fre.setCustomerName("A Company");
			fre.setDayDate(new Date(2007 - 1900, 12 - 1, 30));
			fre.setDisplayOrder(2);
			fre.setEntryDate("492007");
			fre.setHours(5);
			fre.setTotalHours(5);
			fre.setProjectId(1);
			fre.setProjectName("PRJ");
			fre.setUserId(6);
			els.add(fre);
		}	
		
		
		{
			FlatReportElement fre = new FlatReportElement();
			fre.setAssignmentId(2);
			fre.setComment("ja hallo");
			fre.setCustomerCode("AA");
			fre.setCustomerId(1);
			fre.setCustomerName("A Company");
			fre.setDayDate(new Date(2007 - 1900, 12 - 1, 30));
			fre.setDisplayOrder(1);
			fre.setEntryDate("492007");
			fre.setHours(5);
			fre.setTotalHours(5);
			fre.setProjectId(3);
			fre.setProjectName("PRJ");
			fre.setUserId(6);
			els.add(fre);
		}			

		{
			FlatReportElement fre = new FlatReportElement();
			fre.setAssignmentId(3);
			fre.setComment("ja hallo");
			fre.setCustomerCode("AA");
			fre.setCustomerId(2);
			fre.setCustomerName("A Company");
			fre.setDayDate(new Date(2007 - 1900, 12 - 1, 29));
			fre.setDisplayOrder(1);
			fre.setEntryDate("492007");
			fre.setHours(5);
			fre.setTotalHours(5);
			fre.setProjectId(2);
			fre.setProjectName("PRJ");
			fre.setUserId(5);
			els.add(fre);
		}		
		
		return els;
	}
	
	public static ReportData  getFlatReportData()
	{
		ReportData reportData = new ReportData(ReportTestUtil.getFlatReportElements(), new DateRange());

		return reportData;		
	}
	
	/**
	 * Get assignment report dummy data stuff
	 * @return
	 */
	public static List<AssignmentAggregateReportElement> getAssignmentAggregateReportElements()
	{
		AssignmentAggregateReportElement pagA, pagB, pagC, pagD, pagE, pagF;
	    List<AssignmentAggregateReportElement> aggs;
		
        pagA = DummyWebDataGenerator.getProjectAssignmentAggregate(1, 1, 1);
        pagA.getProjectAssignment().getProject().setProjectId(1);
        pagB = DummyWebDataGenerator.getProjectAssignmentAggregate(20, 1, 2);
        pagB.getProjectAssignment().getProject().setProjectId(2);
        pagF = DummyWebDataGenerator.getProjectAssignmentAggregate(30, 1, 3);
        pagF.getProjectAssignment().getProject().setProjectId(2);
        pagC = DummyWebDataGenerator.getProjectAssignmentAggregate(3, 2, 1);
        pagD = DummyWebDataGenerator.getProjectAssignmentAggregate(4, 2, 2);
        pagE = DummyWebDataGenerator.getProjectAssignmentAggregate(5, 3, 3);

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
	public static ReportData getAssignmentReportData()
	{
		ReportData reportData = new ReportData(ReportTestUtil.getAssignmentAggregateReportElements(), new DateRange());

		return reportData;
	}
	
	/**
	 * Get report criteria
	 * @return
	 */
	public static ReportCriteria getReportCriteria()
	{
		
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setReportRange(new DateRange(new Date(), new Date()));
		ReportCriteria criteria = new ReportCriteria(userCriteria);

		return criteria;
	}
}
