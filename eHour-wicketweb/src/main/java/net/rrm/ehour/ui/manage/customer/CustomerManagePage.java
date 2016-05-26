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
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel.EntrySelectorBuilder;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

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

    public CustomerManagePage() {
        super(new ResourceModel("admin.customer.title"),
                new ResourceModel("admin.customer.addCustomer"),
                new ResourceModel("admin.customer.editCustomer"),
                new ResourceModel("admin.customer.noEditEntrySelected"));

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.customer.title"));
        add(greyBorder);

        ClickHandler clickHandler = new ClickHandler() {

            @Override
            public void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException {
                final Integer customerId = (Integer) row.getId();

                getTabbedPanel().setEditBackingBean(createEditBean(customerId));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        entrySelectorPanel = EntrySelectorBuilder.startAs(CUSTOMER_SELECTOR_ID)
                .withData(createSelectorData(getCustomers()))
                .onClick(clickHandler)
                .withInactiveTooltip(new ResourceModel("admin.customer.hideInactive"))
                .build();

        greyBorder.add(entrySelectorPanel);
    }

    public CustomerManagePage(PageParameters params) {
        this();

        StringValue id = params.get("id");

        if (!id.isEmpty()) {
            try {
                CustomerAdminBackingBean editBean = createEditBean(id.toInt());

                AddEditTabbedPanel<CustomerAdminBackingBean> tabbedPanel = getTabbedPanel();
                tabbedPanel.forceLoadEditTab(editBean);
            } catch (Exception e) {

            }
        }
    }


    private CustomerAdminBackingBean createEditBean(Integer customerId) throws ObjectNotFoundException {
        return new CustomerAdminBackingBean(customerService.getCustomerAndCheckDeletability(customerId));
    }

    private EntrySelectorData createSelectorData(List<Customer> customers) {

        List<Header> headers = Lists.newArrayList(new Header("admin.customer.name"),
                                                  new Header("admin.customer.code"),
                                                  new Header("admin.customer.projects", ColumnType.NUMERIC)
        );

        List<EntrySelectorData.EntrySelectorRow> rows = Lists.newArrayList();

        for (Customer customer : customers) {
            boolean active = customer.isActive();

            List<String> cells = Lists.newArrayList(customer.getName(),
                    customer.getCode(),
                    Integer.toString(customer.getProjects() == null ? 0 : customer.getProjects().size()));
            rows.add(new EntrySelectorData.EntrySelectorRow(cells, customer.getCustomerId(),  active));
        }

        return new EntrySelectorData(headers, rows);
    }


    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new CustomerFormPanel(panelId, new CompoundPropertyModel<>(getTabbedPanel().getAddBackingBean()));
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        return new CustomerFormPanel(panelId, new CompoundPropertyModel<>(getTabbedPanel().getEditBackingBean()));
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
    protected void onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent, AjaxRequestTarget target) {
        entrySelectorPanel.updateData(createSelectorData(getCustomers()));
        entrySelectorPanel.reRender(target);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == CustomerAjaxEventType.CUSTOMER_UPDATED
                || type == CustomerAjaxEventType.CUSTOMER_DELETED) {
            entrySelectorPanel.updateData(createSelectorData(getCustomers()));
            entrySelectorPanel.reRender(ajaxEvent.getTarget());
            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
            return false;
        }

        return true;
    }

    private List<Customer> getCustomers() {
        List<Customer> customers = isHideInactive() ? customerService.getActiveCustomers() : customerService.getCustomers();
        Collections.sort(customers, new CustomerComparator());

        return customers;
    }
}

