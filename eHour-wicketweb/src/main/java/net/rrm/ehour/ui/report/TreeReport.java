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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.RangedReport;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;

public abstract class TreeReport<EL extends ReportElement> extends RangedReport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3717854276306653784L;
	private List<Serializable[]> 	reportMatrix;
	private float					totalHours;
	private float					totalTurnover;
	
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
    public TreeReport(ReportData<EL> reportData, ReportConfig reportConfig)
    {
    	initializeReport(reportData, reportConfig);
    }

    /**
     * Initialize report
     * @param reportData
     */
    protected void initializeReport(ReportData<EL> reportData, ReportConfig reportConfig)
    {
        ReportBuilder<EL> reportBuilder = new ReportBuilder<EL>();
        List<ReportNode> rootNodes = reportBuilder.createReport(reportData, getReportNodeFactory());
        
        createMatrix(rootNodes, reportConfig.getReportColumns().length);
        calcTotals(rootNodes);
        
        setReportRange(reportData.getReportCriteria().getUserCriteria().getReportRange());
    }
    
    /**
     * Calculate total turnover & hours booked
     * @param rootNodes
     */
    private void calcTotals(List<ReportNode> rootNodes)
    {
    	for (ReportNode reportNode : rootNodes)
		{
    		totalTurnover += reportNode.getTurnover().floatValue();
			totalHours += reportNode.getHours().floatValue();
		}
    }
    /**
     * 
     * @param rootNodes
     * @param matrixWidth
     */
    private void createMatrix(List<ReportNode> rootNodes, int matrixWidth)
    {
    	reportMatrix = new ArrayList<Serializable[]>();
    	
    	for (ReportNode reportNode : rootNodes)
		{
    		reportMatrix.addAll(reportNode.getNodeMatrix(matrixWidth));
		}
    }
    
    /**
     * Get node factory
     * @return
     */
    public abstract ReportNodeFactory getReportNodeFactory();

	/**
	 * @return the reportMatrix
	 */
	public List<Serializable[]> getReportMatrix()
	{
		return reportMatrix;
	}

	/**
	 * @return the totalHours
	 */
	public float getTotalHours()
	{
		return totalHours;
	}

	/**
	 * @return the totalTurnover
	 */
	public float getTotalTurnover()
	{
		return totalTurnover;
	}
}
