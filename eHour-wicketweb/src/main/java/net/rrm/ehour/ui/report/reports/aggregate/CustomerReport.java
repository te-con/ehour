/**
 * Created on Jan 31, 2007
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
import net.rrm.ehour.ui.sort.ProjectComparator;

/**
 * Create a customer report based on the supplied report data
 * Structure: Customer -> Project -> Aggregate
 **/

public class CustomerReport extends AggregateReport<Customer, Project, Integer>
{
	private static final long serialVersionUID = 6365903846883586472L;

	/**
	 * 
	 */
	@Override
	public String getReportName()
	{
		// TODO i18n
		return "Customer report";
		
	}

	/**
	 * Get the project as the child key
	 */
	@Override
	protected Project getChildKey(ProjectAssignmentAggregate aggregate)
	{
		return aggregate.getProjectAssignment().getProject();
	}

	/**
	 * Get the customer as the root key
	 */
	@Override
	protected Customer getRootKey(ProjectAssignmentAggregate aggregate)
	{
		return aggregate.getProjectAssignment().getProject().getCustomer();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.reports.AggregateReport#getComparator()
	 */
//	@Override
//	public Comparator<Project> getComparator()
//	{
//		return new ProjectComparator();
//	}

	@Override
	protected ReportValueWrapperFactory getChildValueWrapperFactory()
	{
		return new ProjectValueWrapperFactory();
	}

	@Override
	protected ReportValueWrapperFactory getRootValueWrapperFactory()
	{
		return new CustomerValueWrapperFactory();
	}

	@Override
	protected Comparator<Project> getComparator()
	{
		return new ProjectComparator();
	}
}
