/**
 * Created on 22-feb-2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.reports;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;

/**
 * Data holder for aggregate reports
 **/

public class ReportDataAggregate implements Serializable
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
