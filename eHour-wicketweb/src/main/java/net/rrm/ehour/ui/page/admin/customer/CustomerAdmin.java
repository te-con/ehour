/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.page.admin.customer;

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.sort.CustomerComparator;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Customer admin page
 **/

public class CustomerAdmin  extends BaseTabbedAdminPage
{
	private static final long serialVersionUID = 3190421612132110664L;
	
	@SpringBean
	private CustomerService		customerService;
	private	static final Logger	logger = Logger.getLogger(CustomerAdmin.class);
	private EntrySelectorFilter	currentFilter;
	
	/**
	 * 
	 * @param pageTitle
	 * @param addTabTitle
	 * @param editTabTitle
	 */
	public CustomerAdmin(ResourceModel pageTitle, ResourceModel addTabTitle, ResourceModel editTabTitle)
	{
		super(pageTitle, addTabTitle, editTabTitle);
		
		List<Customer> customers;
		customers = getCustomers();
	}

	@Override
	protected Panel getAddPanel(String panelId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Panel getEditPanel(String panelId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AdminBackingBean getNewAddBackingBean()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AdminBackingBean getNewEditBackingBean()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Panel getNoSelectionPanel(String panelId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get customers from the backend
	 * 
	 * @return
	 */
	private List<Customer> getCustomers()
	{
		List<Customer>	customers = customerService.getCustomers();
		
		Collections.sort(customers, new CustomerComparator());

		return customers;
	}
}
