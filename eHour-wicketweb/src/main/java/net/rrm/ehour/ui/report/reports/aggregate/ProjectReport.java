/**
 * Created on Feb 24, 2007
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
	 * @see net.rrm.ehour.web.report.reports.ReportBuilder#getComparator()
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
