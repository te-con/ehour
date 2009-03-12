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


import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.report.AbstractCachableReportModel;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;

public abstract class TreeReport extends AbstractCachableReportModel
{
	private static final long serialVersionUID = -3717854276306653784L;

	private float totalHours;
	private float totalTurnover;
	
	private ReportConfig	reportConfig;
	
	/**
	 * Default constructor which doesn't initialize the report
	 */
	public TreeReport(ReportCriteria reportCriteria, ReportConfig reportConfig)
	{
		super(reportCriteria);

		this.reportConfig = reportConfig;
	}

	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.report.Report#getReportData()
	 */
	@Override
	protected ReportData getReportData(ReportCriteria reportCriteria)
	{
		ReportData reportData = getValidReportData(reportCriteria);
		
        ReportBuilder reportBuilder = new ReportBuilder();
        List<ReportNode> rootNodes = reportBuilder.createReport(reportData, getReportNodeFactory());
        
        List<TreeReportElement> matrix = createMatrix(rootNodes, reportConfig.getReportColumns().length);
        calcTotals(rootNodes);
        
        ReportData wrappedReportData = new ReportData(matrix, reportCriteria.getReportRange());
        
        return wrappedReportData;
        
    }
    
	private ReportData getValidReportData(ReportCriteria reportCriteria)
	{
		ReportData reportData = fetchReportData(reportCriteria);
		
		notNull(reportData);
		notNull(reportData.getReportElements());

		return reportData;
	}
	
    protected abstract ReportData fetchReportData(ReportCriteria reportCriteria);
    
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
    private List<TreeReportElement> createMatrix(List<ReportNode> rootNodes, int matrixWidth)
    {
    	List<TreeReportElement> reportMatrix = new ArrayList<TreeReportElement>();
    	
    	for (ReportNode reportNode : rootNodes)
		{
    		reportMatrix.addAll(reportNode.getNodeMatrix(matrixWidth));
		}
    	
    	return reportMatrix;
    }
    
    /**
     * Get node factory
     * @return
     */
    public abstract ReportNodeFactory getReportNodeFactory();

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
