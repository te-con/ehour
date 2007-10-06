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
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;
import net.rrm.ehour.ui.sort.ProjectAssignmentComparator;

/**
 * Print report for printing a timesheet
 **/

public class PrintReport extends TrendReport<ProjectAssignment>
{
	private static final long serialVersionUID = 6099016674849151669L;
	private SimpleDateFormat	dateParser;
	
	public PrintReport()
	{
		dateParser = new SimpleDateFormat("ddMMyyyy");
	}
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.web.report.reports.TimelineReport#getRowKey(net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate)
	 */
	@Override
	protected ProjectAssignment getRowKey(FlatProjectAssignmentAggregate aggregate)
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
	protected Date getAggregateDate(FlatProjectAssignmentAggregate aggregate) throws ParseException
	{
		return dateParser.parse(aggregate.getEntryDate());
	}

	/**
	 * Row key comparator
	 */
	@Override
	protected Comparator<ProjectAssignment> getRKComparator()
	{
		return new ProjectAssignmentComparator();
	}

}
