/**
 * Created on 22-feb-2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.reports;

import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;

/**
 * Data holder for aggregate reports
 **/

public class ReportDataAggregate implements ReportData
{
	private static final long serialVersionUID = -6344570520998830487L;
	
	private List<ProjectAssignmentAggregate>		projectAssignmentAggregates;
	private List<FlatProjectAssignmentAggregate>	flatProjectAssignmentAggregates;
	private	ReportCriteria							reportCriteria;
	
	/**
	 * Default constructor
	 */
	public ReportDataAggregate()
	{
	}
	
	/**
	 * Full constructor
	 * @param pag projectAssignmentAggregates
	 * @param wpag
	 * @param criteria
	 */
	public ReportDataAggregate(List<ProjectAssignmentAggregate> pag, List<FlatProjectAssignmentAggregate> wpag, ReportCriteria criteria)
	{
		projectAssignmentAggregates = pag;
		flatProjectAssignmentAggregates = wpag;
		reportCriteria = criteria;
	}
	
	/**
	 * @return the projectAssignmentAggregates
	 */
	public List<ProjectAssignmentAggregate> getProjectAssignmentAggregates()
	{
		return projectAssignmentAggregates;
	}
	/**
	 * @param projectAssignmentAggregates the projectAssignmentAggregates to set
	 */
	public void setProjectAssignmentAggregates(
			List<ProjectAssignmentAggregate> projectAssignmentAggregates)
	{
		this.projectAssignmentAggregates = projectAssignmentAggregates;
	}
	/**
	 * @return the reportCriteria
	 */
	public ReportCriteria getReportCriteria()
	{
		return reportCriteria;
	}
	/**
	 * @param reportCriteria the reportCriteria to set
	 */
	public void setReportCriteria(ReportCriteria reportCriteria)
	{
		this.reportCriteria = reportCriteria;
	}
	/**
	 * @return the flatProjectAssignmentAggregates
	 */
	public List<FlatProjectAssignmentAggregate> getFlatProjectAssignmentAggregates()
	{
		return flatProjectAssignmentAggregates;
	}
	/**
	 * @param flatProjectAssignmentAggregates the flatProjectAssignmentAggregates to set
	 */
	public void setFlatProjectAssignmentAggregates(
			List<FlatProjectAssignmentAggregate> flatProjectAssignmentAggregates)
	{
		this.flatProjectAssignmentAggregates = flatProjectAssignmentAggregates;
	}
}
