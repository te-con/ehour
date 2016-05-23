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

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.util.WebGeo;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Customer admin form panel
 */

public class CustomerFormPanel extends AbstractFormSubmittingPanel<CustomerAdminBackingBean> {
    private static final long serialVersionUID = 8536721437867359030L;

    @SpringBean
    private CustomerService customerService;

    public CustomerFormPanel(String id, CompoundPropertyModel<CustomerAdminBackingBean> model) {
        super(id, model);

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", WebGeo.AUTO);
        add(greyBorder);

        setOutputMarkupId(true);

        final Form<CustomerAdminBackingBean> form = new Form<>("customerForm", model);

        // name
        RequiredTextField<String> nameField = new RequiredTextField<>("customer.name");
        form.add(nameField);


        nameField.add(StringValidator.lengthBetween(0, 64));
        nameField.setLabel(new ResourceModel("admin.customer.name"));
        nameField.add(new ValidatingFormComponentAjaxBehavior());
        form.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));

        // code
        final RequiredTextField<String> codeField = new RequiredTextField<>("customer.code");
        form.add(codeField);
        codeField.add(StringValidator.lengthBetween(0, 16));
        codeField.setLabel(new ResourceModel("admin.customer.code"));
        codeField.add(new ValidatingFormComponentAjaxBehavior());
        form.add(new UniqueCustomerValidator(nameField, codeField));
        form.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));

        // description
        TextArea<String> textArea = new KeepAliveTextArea("customer.description");
        textArea.setLabel(new ResourceModel("admin.customer.description"));
        form.add(textArea);

        // active
        CheckBox activeCheckbox = new CheckBox("customer.active");
        activeCheckbox.setMarkupId("active");
        form.add(activeCheckbox);

        // data save label
        form.add(new ServerMessageLabel("serverMessage", "formValidationError"));

        //

        boolean deletable = model.getObject().isDeletable();
        FormConfig formConfig = FormConfig.forForm(form).withDelete(deletable).withSubmitTarget(this)
                .withDeleteEventType(CustomerAjaxEventType.CUSTOMER_DELETED)
                .withSubmitEventType(CustomerAjaxEventType.CUSTOMER_UPDATED);


        FormUtil.setSubmitActions(formConfig);

        greyBorder.add(form);
    }

    /*
     * (non-Javadoc)
     * @see net.rrm.ehour.persistence.persistence.ui.common.panel.noentry.AbstractAjaxAwareAdminPanel#processFormSubmit(net.rrm.ehour.persistence.persistence.ui.common.model.AdminBackingBean, int)
     */
    @Override
    protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        CustomerAdminBackingBean customerBackingBean = (CustomerAdminBackingBean) backingBean;

        if (type == CustomerAjaxEventType.CUSTOMER_UPDATED) {
            persistCustomer(customerBackingBean);
        } else if (type == CustomerAjaxEventType.CUSTOMER_DELETED) {
            deleteCustomer(customerBackingBean);
        }
        return true;
    }

    /**
     * Persist customer to db
     *
     * @param backingBean
     * @throws ObjectNotUniqueException
     */
    private void persistCustomer(CustomerAdminBackingBean backingBean) throws ObjectNotUniqueException {
        customerService.persistCustomer(backingBean.getCustomer());
    }

    /**
     * Delete customer
     *
     * @param backingBean
     * @throws ParentChildConstraintException
     */
    private void deleteCustomer(CustomerAdminBackingBean backingBean) throws ParentChildConstraintException {
        customerService.deleteCustomer(backingBean.getCustomer().getCustomerId());
    }

    /**
     * Unique customer code / name validator
     *
     * @author Thies
     */
    private class UniqueCustomerValidator extends AbstractFormValidator {
        private static final long serialVersionUID = 1181184585571474550L;
        private FormComponent<String>[] components;

        @SuppressWarnings("unchecked")
        public UniqueCustomerValidator(FormComponent<String> customerName, FormComponent<String> customerCode) {
            components = new FormComponent[]{customerName, customerCode};
        }

        /*
         * (non-Javadoc)
         * @see org.apache.wicket.markup.html.form.validation.IFormValidator#getDependentFormComponents()
         */
        public FormComponent<?>[] getDependentFormComponents() {
            return components;
        }

        /*
         * (non-Javadoc)
         * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
         */
        public void validate(Form<?> form) {
            if (!StringUtils.isBlank(components[0].getInput())
                    && !StringUtils.isBlank(components[1].getInput())) {
                String orgName = ((CustomerAdminBackingBean) getDefaultModelObject()).getOriginalCustomerName();
                String orgCode = ((CustomerAdminBackingBean) getDefaultModelObject()).getOriginalCustomerCode();

                if ((StringUtils.equals(orgName, components[0].getInput()))
                        && StringUtils.equals(orgCode, components[1].getInput())) {
                    return;
                }

                Customer customer = customerService.getCustomer(components[0].getInput(), components[1].getInput());

                if (customer != null) {
                    error(components[0], "admin.customer.errorNotUnique");
                }
            }
        }
    }
}
