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
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.panel.admin.customer.form.CustomerFormPanel;
import net.rrm.ehour.ui.panel.admin.customer.form.dto.CustomerAdminBackingBean;
import net.rrm.ehour.ui.panel.admin.user.form.UserFormPanel;
import net.rrm.ehour.ui.panel.admin.user.form.dto.UserBackingBean;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.sort.CustomerComparator;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.User;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Customer admin page
 **/

public class CustomerAdmin  extends BaseTabbedAdminPage
{
	private final String	CUSTOMER_SELECTOR_ID = "customerSelector";
	
	private static final long serialVersionUID = 3190421612132110664L;
	
	@SpringBean
	private CustomerService		customerService;
	private	static final Logger	logger = Logger.getLogger(CustomerAdmin.class);
	private	ListView			customerListView;
	private EntrySelectorFilter	currentFilter;
	
	/**
	 * 
	 * @param pageTitle
	 * @param addTabTitle
	 * @param editTabTitle
	 */
	public CustomerAdmin()
	{
		super(new ResourceModel("admin.customer.title"),
				new ResourceModel("admin.customer.addCustomer"),
				new ResourceModel("admin.customer.editCustomer"));
		
		// setup the entry selector
		List<Customer> customers;
		customers = getCustomers();
		
		Fragment customerListHolder = getCustomerListHolder(customers);
		
		add(new EntrySelectorPanel(CUSTOMER_SELECTOR_ID,
				new ResourceModel("admin.customer.title"),
				customerListHolder,
				null,
				getLocalizer().getString("admin.customer.hideInactive", this)
				));
		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getAddPanel(String panelId)
	{
		return new CustomerFormPanel(panelId, new CompoundPropertyModel(getAddBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getEditPanel(String panelId)
	{
		return new CustomerFormPanel(panelId, new CompoundPropertyModel(getEditBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewAddBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewAddBackingBean()
	{
		Customer	cust = new Customer();
		cust.setActive(true);
		
		return new CustomerAdminBackingBean(cust);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewEditBackingBean()
	{
		return new CustomerAdminBackingBean(new Customer());
	}

	/**
	 * Handle Ajax request
	 * @param target
	 * @param type of ajax req
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object param)
	{
		switch (type)
		{
			case CommonStaticData.AJAX_ENTRYSELECTOR_FILTER_CHANGE:
			{
				currentFilter = (EntrySelectorFilter)param;
	
				List<Customer> customers = getCustomers();
				customerListView.setList(customers);
				break;
			}
			case CommonStaticData.AJAX_FORM_SUBMIT:
			{
				CustomerAdminBackingBean backingBean = (CustomerAdminBackingBean) ((((IWrapModel) param)).getWrappedModel()).getObject();
				try
				{
					persistCustomer(backingBean);

					// update customer list
					List<Customer> customers = getCustomers();
					customerListView.setList(customers);
					
					((EntrySelectorPanel)get(CUSTOMER_SELECTOR_ID)).refreshList(target);
					
					succesfulSave(target);
				} catch (Exception e)
				{
					logger.error("While persisting user", e);
					failedSave(backingBean, target);
				}
				
				break;
			}
		}
	}	
	
	/**
	 * Persist customer to db
	 * @param backingBean
	 * @throws ObjectNotUniqueException 
	 */
	private void persistCustomer(CustomerAdminBackingBean backingBean) throws ObjectNotUniqueException
	{
		customerService.persistCustomer(backingBean.getCustomer());
	}
	
	/**
	 * Get a the customerListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
	private Fragment getCustomerListHolder(List<Customer> customers)
	{
		Fragment fragment = new Fragment("itemListHolder", "itemListHolder", CustomerAdmin.this);
		
		customerListView = new ListView("itemList", customers)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				Customer		customer = (Customer)item.getModelObject();
				final Integer	customerId = customer.getCustomerId();
				
				AjaxLink	link = new AjaxLink("itemLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						setEditBackingBean(new CustomerAdminBackingBean(customerService.getCustomer(customerId)));
						switchTabOnAjaxTarget(target, 1);
					}
				};
				
				item.add(link);
				// TODO add project count
				link.add(new Label("linkLabel", customer.getCode() + " - " + customer.getName() + (customer.isActive() ? "" : "*")));				
			}
		};
		
		fragment.add(customerListView);
		
		return fragment;
	}	
	
	/**
	 * Get customers from the backend
	 * 
	 * @return
	 */
	private List<Customer> getCustomers()
	{
		List<Customer>	customers;
		
		if (currentFilter != null && !currentFilter.isActivateToggle())
		{
			customers = customerService.getCustomers();
		}
		else
		{
			customers = customerService.getCustomers(true);
		}
		
		Collections.sort(customers, new CustomerComparator());

		return customers;
	}
}
