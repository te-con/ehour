/**
 * Created on Jan 31, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
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
