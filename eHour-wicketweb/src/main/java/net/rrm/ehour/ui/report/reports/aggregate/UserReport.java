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

package net.rrm.ehour.ui.report.reports.aggregate;

import java.util.Comparator;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.ui.report.value.CustomerValueWrapperFactory;
import net.rrm.ehour.ui.report.value.ReportValueWrapperFactory;
import net.rrm.ehour.ui.report.value.UserValueWrapperFactory;
import net.rrm.ehour.ui.sort.CustomerComparator;
import net.rrm.ehour.user.domain.User;

/**
 * UserReport (User -> Customer -> Project details)
 **/

public class UserReport extends AggregateReport<User, Customer, Integer>
{
	private static final long serialVersionUID = -1983002053752895296L;

	/**
	 * 
	 */
	@Override
	public String getReportName()
	{
		return "User report";
	}

	/**
	 * Get the customer as the child key
	 */
	@Override
	protected Customer getChildKey(ProjectAssignmentAggregate aggregate)
	{
		return aggregate.getProjectAssignment().getProject().getCustomer();
	}

	/**
	 * Get the user as the root key
	 */
	@Override
	protected User getRootKey(ProjectAssignmentAggregate aggregate)
	{
		return aggregate.getProjectAssignment().getUser();
	}

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
		return new UserValueWrapperFactory();
	}
}
