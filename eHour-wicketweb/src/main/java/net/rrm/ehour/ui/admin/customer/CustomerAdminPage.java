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

package net.rrm.ehour.ui.admin.customer;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.sort.CustomerComparator;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorListView;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

/**
 * Customer admin page
 */

public class CustomerAdminPage extends AbstractTabbedAdminPage<CustomerAdminBackingBean> {
    private static final String CUSTOMER_SELECTOR_ID = "customerSelector";

    private static final long serialVersionUID = 3190421612132110664L;
    private final EntrySelectorPanel entrySelectorPanel;

    @SpringBean
    private CustomerService customerService;
    private ListView<Customer> customerListView;
    private EntrySelectorFilter currentFilter;

    public CustomerAdminPage() {
        super(new ResourceModel("admin.customer.title"),
                new ResourceModel("admin.customer.addCustomer"),
                new ResourceModel("admin.customer.editCustomer"),
                new ResourceModel("admin.customer.noEditEntrySelected"));

        // setup the entry selector

        Fragment customerListHolder = getCustomerListHolder(getCustomers());

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.customer.title"));
        add(greyBorder);

        entrySelectorPanel = new EntrySelectorPanel(CUSTOMER_SELECTOR_ID,
                customerListHolder,
                new ResourceModel("admin.customer.hideInactive")
        );

        greyBorder.add(entrySelectorPanel);

    }

    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new CustomerFormPanel(panelId, new CompoundPropertyModel<CustomerAdminBackingBean>(getTabbedPanel().getAddBackingBean()));
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        return new CustomerFormPanel(panelId, new CompoundPropertyModel<CustomerAdminBackingBean>(getTabbedPanel().getEditBackingBean()));
    }

    @Override
    protected CustomerAdminBackingBean getNewAddBaseBackingBean() {
        return CustomerAdminBackingBean.createCustomerAdminBackingBean();
    }

    @Override
    protected CustomerAdminBackingBean getNewEditBaseBackingBean() {
        return CustomerAdminBackingBean.createCustomerAdminBackingBean();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == CustomerAjaxEventType.CUSTOMER_UPDATED
                || type == CustomerAjaxEventType.CUSTOMER_DELETED) {
            List<Customer> customers = getCustomers();
            customerListView.setList(customers);

            entrySelectorPanel.refreshList(ajaxEvent.getTarget());

            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
            return false;
        }

        return true;
    }

    @Override
    protected Component onFilterChanged(EntrySelectorPanel.FilterChangedEvent filterChangedEvent) {
        currentFilter = filterChangedEvent.getFilter();

        List<Customer> customers = getCustomers();
        customerListView.setList(customers);

        return customerListView.getParent();
    }

    /**
     * Get a the customerListHolder fragment containing the listView
     *
     * @param users
     * @return
     */
    @SuppressWarnings("serial")
    private Fragment getCustomerListHolder(List<Customer> customers) {
        Fragment fragment = new Fragment("itemListHolder", "itemListHolder", CustomerAdminPage.this);

        customerListView = new EntrySelectorListView<Customer>("itemList", customers) {
            @Override
            protected void onPopulate(ListItem<Customer> item, IModel<Customer> itemModel) {
                Customer customer = item.getModelObject();

                if (!customer.isActive()) {
                    item.add(AttributeModifier.append("class", "inactive"));
                }

                item.add(new Label("name", customer.getName()));
                item.add(new Label("code", customer.getCode()));
                item.add(new Label("projects", customer.getProjects() == null ? 0 : customer.getProjects().size()));
            }

            @Override
            protected void onClick(ListItem<Customer> item, AjaxRequestTarget target) throws ObjectNotFoundException {
                final Integer customerId = item.getModelObject().getCustomerId();

                getTabbedPanel().setEditBackingBean(new CustomerAdminBackingBean(customerService.getCustomerAndCheckDeletability(customerId)));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        fragment.add(customerListView);
        fragment.setOutputMarkupId(true);

        return fragment;
    }

    private List<Customer> getCustomers() {
        List<Customer> customers = currentFilter != null && !currentFilter.isFilterToggle() ? customerService.getCustomers() : customerService.getCustomers(true);
        Collections.sort(customers, new CustomerComparator());

        return customers;
    }
}
