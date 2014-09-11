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

package net.rrm.ehour.ui.manage.customer;

import com.google.common.collect.Lists;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.sort.CustomerComparator;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.*;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
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

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.ColumnType;
import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.Header;
import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel.ClickHandler;

/**
 * Customer admin page
 */

public class CustomerManagePage extends AbstractTabbedManagePage<CustomerAdminBackingBean> {
    private static final String CUSTOMER_SELECTOR_ID = "customerSelector";

    private static final long serialVersionUID = 3190421612132110664L;
    private final EntrySelectorPanel entrySelectorPanel;

    @SpringBean
    private CustomerService customerService;
    private ListView<Customer> customerListView;
    private HideInactiveFilter currentFilter;

    public CustomerManagePage() {
        super(new ResourceModel("admin.customer.title"),
                new ResourceModel("admin.customer.addCustomer"),
                new ResourceModel("admin.customer.editCustomer"),
                new ResourceModel("admin.customer.noEditEntrySelected"));

        // setup the entry selector

        Fragment customerListHolder = getCustomerListHolder(getCustomers());

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.customer.title"));
        add(greyBorder);

        ClickHandler clickHandler = new ClickHandler() {

            @Override
            public void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException {
                final Integer customerId = (Integer) row.getId();

                getTabbedPanel().setEditBackingBean(new CustomerAdminBackingBean(customerService.getCustomerAndCheckDeletability(customerId)));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };


        entrySelectorPanel = new EntrySelectorPanel(CUSTOMER_SELECTOR_ID,
                customerListHolder,
                clickHandler,
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
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == CustomerAjaxEventType.CUSTOMER_UPDATED
                || type == CustomerAjaxEventType.CUSTOMER_DELETED) {
            List<Customer> customers = getCustomers();
            customerListView.setList(customers);

            entrySelectorPanel.reRender(ajaxEvent.getTarget());

            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
            return false;
        }

        return true;
    }

    @Override
    protected Component onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent) {
        currentFilter = inactiveFilterChangedEvent.hideInactiveFilter();

        List<Customer> customers = getCustomers();
        customerListView.setList(customers);

        return customerListView.getParent();
    }

    private EntrySelectorData createSelectorData(List<Customer> customers) {

        List<Header> headers = Lists.newArrayList(new Header("admin.customer.code"),
                                                  new Header("admin.customer.name"),
                                                  new Header(ColumnType.NUMERIC, "admin.customer.projects")
        );

        List<EntrySelectorData.EntrySelectorRow> rows = Lists.newArrayList();

        for (Customer customer : customers) {
            boolean active = customer.isActive();

            rows.add(new EntrySelectorData.EntrySelectorRow(Lists.newArrayList(customer.getName(), customer.getCode(), customer.getProjects() == null ? 0 : customer.getProjects().size()), active));
        }

        return new EntrySelectorData(headers, rows);
    }

    /**
     * Get a the customerListHolder fragment containing the listView
     */
    @SuppressWarnings("serial")
    private Fragment getCustomerListHolder(List<Customer> customers) {
        Fragment fragment = new Fragment("itemListHolder", "itemListHolder", CustomerManagePage.this);

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
        List<Customer> customers = currentFilter != null && !currentFilter.isHideInactive() ? customerService.getCustomers() : customerService.getActiveCustomers();
        Collections.sort(customers, new CustomerComparator());

        return customers;
    }
}

