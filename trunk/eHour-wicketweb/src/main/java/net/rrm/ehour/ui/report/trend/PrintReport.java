/**
 * Created on Mar 3, 2007
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

package net.rrm.ehour.ui.report.trend;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.sort.ProjectAssignmentComparator;
import net.rrm.ehour.ui.common.util.CommonWebUtil;

import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Print report for printing a timesheet
 **/

public class PrintReport extends TrendReport<ProjectAssignment>
{
	@SpringBean
	private DetailedReportService detailedReportService;
	
	public PrintReport(ReportCriteria criteria)
	{
		super(criteria);
	}

	private static final long serialVersionUID = 6099016674849151669L;
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.web.report.reports.TimelineReport#getRowKey(net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate)
	 */
	@Override
	protected ProjectAssignment getRowKey(FlatReportElement aggregate)
	{
		ProjectAssignment pa = new ProjectAssignment();
		
		pa.setAssignmentId(aggregate.getAssignmentId());
		pa.setRole(aggregate.getAssignmentDesc());
		
		Project prj = new Project();
		prj.setName(aggregate.getProjectName());
		prj.setProjectId(aggregate.getProjectId());
		
		pa.setProject(prj);
		
		return pa;
	}

	/**
	 * Format is ddmmyyyy 
	 * @throws ParseException 
	 */
	@Override
	protected Date getAggregateDate(FlatReportElement aggregate) throws ParseException
	{
		return aggregate.getDayDate();
	}

	/**
	 * Row key comparator
	 */
	@Override
	protected Comparator<ProjectAssignment> getRKComparator()
	{
		return new ProjectAssignmentComparator();
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.trend.TrendReport#fetchReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	@Override
	protected ReportData fetchReportData(ReportCriteria reportCriteria)
	{
		ReportData detailedReportData = getDetailedReportService().getDetailedReportData(reportCriteria);
		
		return detailedReportData;
	}
	
	private DetailedReportService getDetailedReportService()
	{
		if (detailedReportService == null)
		{
			CommonWebUtil.springInjection(this);
		}
		
		return detailedReportService;
	}
}
