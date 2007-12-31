/**
 * User: Thies
 * Date: Sep 11, 2007
 * Time: 9:28:30 PM
 * Copyright (C) 2005-2007 TE-CON, All Rights Reserved.
 * <p/>
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is
 * available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for
 * commercial use or open source, is subject to obtaining the prior express authorization of TE-CON.
 * <p/>
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 */

package net.rrm.ehour.ui.report;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;

public abstract class TreeReport extends Report
{
	private List<ReportNode>    nodes;
	private	DateRange			reportRange;
	
	/**
	 * Default constructor which doesn't initialize the report
	 */
	public TreeReport()
	{
	}
	
    /**
     *
     * @param reportData
     */
    public TreeReport(ReportData reportData)
    {
    	initializeReport(reportData);
    }

    /**
     * Initialize report
     * @param reportData
     */
    protected void initializeReport(ReportData reportData)
    {
        ReportBuilder reportBuilder = new ReportBuilder();
        nodes = reportBuilder.createReport(reportData, getReportNodeFactory());
        
        reportRange = reportData.getReportCriteria().getUserCriteria().getReportRange();
    	
    }
	
    /**
     * Get report nodes
     * @return
     */
    public List<ReportNode> getNodes()
    {
    	return nodes;
    }
    
    /**
     * Get node factory
     * @return
     */
    public abstract ReportNodeFactory getReportNodeFactory();

	/**
	 * @return the reportForRange
	 */
	public DateRange getReportRange()
	{
		return reportRange;
	}
}
