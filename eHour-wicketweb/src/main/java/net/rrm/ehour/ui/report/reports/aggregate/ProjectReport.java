/**
 * Created on Feb 24, 2007
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

package net.rrm.ehour.ui.report.reports.aggregate;

import java.util.Comparator;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.ui.report.value.CustomerValueWrapperFactory;
import net.rrm.ehour.ui.report.value.ProjectValueWrapperFactory;
import net.rrm.ehour.ui.report.value.ReportValueWrapperFactory;
import net.rrm.ehour.ui.sort.CustomerComparator;

/**
 * Project -> user report 
 **/

public class ProjectReport extends AggregateReport<Project, Customer, Integer>
{
	private static final long serialVersionUID = -6219017191889371362L;

	@Override
	protected Customer getChildKey(ProjectAssignmentAggregate aggregate)
	{
		return aggregate.getProjectAssignment().getProject().getCustomer();
	}

	@Override
	public String getReportName()
	{
		return "Project report";
	}

	@Override
	protected Project getRootKey(ProjectAssignmentAggregate aggregate)
	{
		return aggregate.getProjectAssignment().getProject();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.reports.AggregateReport#getComparator()
	 */
	@Override
	public Comparator<Customer> getComparator()
	{
		return new CustomerComparator();
	}

	@Override
	public ReportValueWrapperFactory getChildValueWrapperFactory()
	{
		return new CustomerValueWrapperFactory();
	}

	@Override
	public ReportValueWrapperFactory getRootValueWrapperFactory()
	{
		return new ProjectValueWrapperFactory();
	}

}
