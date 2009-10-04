/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.admin.customer.page;

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.admin.customer.common.CustomerAjaxEventType;
import net.rrm.ehour.ui.admin.customer.dto.CustomerAdminBackingBean;
import net.rrm.ehour.ui.admin.customer.panel.CustomerFormPanel;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorAjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.sort.CustomerComparator;
import net.rrm.ehour.ui.common.util.WebGeo;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Customer admin page
 **/

public class CustomerAdmin extends AbstractTabbedAdminPage<CustomerAdminBackingBean>
{
	private final String	CUSTOMER_SELECTOR_ID = "customerSelector";
	
	private static final long serialVersionUID = 3190421612132110664L;
	
	@SpringBean
	private CustomerService		customerService;
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
				new ResourceModel("admin.customer.editCustomer"),
				new ResourceModel("admin.customer.noEditEntrySelected"),
				"admin.customer.help.header",
				"admin.customer.help.body");
		
		// setup the entry selector
		List<Customer> customers;
		customers = getCustomers();
		
		Fragment customerListHolder = getCustomerListHolder(customers);
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", 
						new ResourceModel("admin.customer.title"), 
						WebGeo.W_ENTRY_SELECTOR);
		add(greyBorder);			
		
		greyBorder.add(new EntrySelectorPanel(CUSTOMER_SELECTOR_ID,
												customerListHolder,
												null,
												new ResourceModel("admin.customer.hideInactive")
												));
		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseAddPanel(String panelId)
	{
		return new CustomerFormPanel(panelId, new CompoundPropertyModel(getTabbedPanel().getAddBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseEditPanel(String panelId)
	{
		return new CustomerFormPanel(panelId, new CompoundPropertyModel(getTabbedPanel().getEditBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getNewAddBackingBean()
	 */
	@Override
	protected CustomerAdminBackingBean getNewAddBaseBackingBean()
	{
		return CustomerAdminBackingBean.createCustomerAdminBackingBean();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected CustomerAdminBackingBean getNewEditBaseBackingBean()
	{
		return CustomerAdminBackingBean.createCustomerAdminBackingBean();
	}

	/**
	 * Handle Ajax request
	 * @param target
	 * @param type of ajax req
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		AjaxEventType type = ajaxEvent.getEventType();

		if (type == EntrySelectorAjaxEventType.FILTER_CHANGE)
		{
			currentFilter = ((PayloadAjaxEvent<EntrySelectorFilter>)ajaxEvent).getPayload();
			
			List<Customer> customers = getCustomers();
			customerListView.setList(customers);
			
			return false;
		}
		else if (type == CustomerAjaxEventType.CUSTOMER_UPDATED
					|| type == CustomerAjaxEventType.CUSTOMER_DELETED)
		{
			List<Customer> customers = getCustomers();
			customerListView.setList(customers);
			
			((EntrySelectorPanel)
					((MarkupContainer)get("entrySelectorFrame"))
						.get(CUSTOMER_SELECTOR_ID)).refreshList(ajaxEvent.getTarget());					
			
			getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Get a the customerListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
	@SuppressWarnings("serial")
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
						try
						{
							getTabbedPanel().setEditBackingBean(new CustomerAdminBackingBean(customerService.getCustomerAndCheckDeletability(customerId)));
							getTabbedPanel().switchTabOnAjaxTarget(target, 1);
						} catch (ObjectNotFoundException e)
						{
							// TODO
						}
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
