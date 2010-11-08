/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
	 * @see net.rrm.ehour.persistence.persistence.ui.common.report.Report#getReportData()
	 */
	@Override
	protected ReportData getReportData(ReportCriteria reportCriteria)
	{
		ReportData reportData = getValidReportData(reportCriteria);
		
		// flatten the original reportData into a matrix representing the whole report
        ReportBuilder reportBuilder = new ReportBuilder();
        List<ReportNode> rootNodes = reportBuilder.createReport(reportData, getReportNodeFactory());
        
        List<TreeReportElement> matrix = createMatrix(rootNodes, reportConfig.getReportColumns().length);
        calcTotals(rootNodes);
        
        ReportData wrappedReportData = new TreeReportData(matrix, reportCriteria.getReportRange(), reportData);
        
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
    protected abstract ReportNodeFactory getReportNodeFactory();

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
