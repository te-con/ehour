/**
 * Created on Aug 21, 2007
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

package net.rrm.ehour.ui.common.panel.customer.form;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.common.FormUtil;
import net.rrm.ehour.ui.common.panel.customer.form.dto.CustomerAdminBackingBean;
import net.rrm.ehour.ui.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Customer admin form panel
 **/

public class CustomerFormPanel extends AbstractFormSubmittingPanel
{
	private static final long serialVersionUID = 8536721437867359030L;

	@SpringBean
	private CustomerService		customerService;
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public CustomerFormPanel(String id, CompoundPropertyModel model)
	{
		super(id, model);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("customerForm");
		
		// name
		RequiredTextField	nameField = new RequiredTextField("customer.name");
		form.add(nameField);
		nameField.add(new StringValidator.MaximumLengthValidator(64));
		nameField.setLabel(new ResourceModel("admin.customer.name"));
		nameField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));
			
		// code
		final RequiredTextField	codeField = new RequiredTextField("customer.code");
		form.add(codeField);
		codeField.add(new StringValidator.MaximumLengthValidator(16));
		codeField.setLabel(new ResourceModel("admin.customer.code"));
		codeField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new UniqueCustomerValidator(nameField, codeField));
		form.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));
		
		// description
		TextArea	textArea = new KeepAliveTextArea("customer.description");
		textArea.setLabel(new ResourceModel("admin.customer.description"));;
		form.add(textArea);
			
		// active
		form.add(new CheckBox("customer.active"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
	
		//
		FormUtil.setSubmitActions(form 
									,((CustomerAdminBackingBean)model.getObject()).getCustomer().isDeletable()
									,this
									,CustomerAjaxEventType.CUSTOMER_UPDATED
									,CustomerAjaxEventType.CUSTOMER_DELETED
									,((EhourWebSession)getSession()).getEhourConfig());
		
		greyBorder.add(form);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.panel.AbstractAjaxAwareAdminPanel#processFormSubmit(net.rrm.ehour.ui.common.model.AdminBackingBean, int)
	 */
	@Override
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		CustomerAdminBackingBean customerBackingBean = (CustomerAdminBackingBean) backingBean;
		
		if (type == CustomerAjaxEventType.CUSTOMER_UPDATED)
		{
			persistCustomer(customerBackingBean);
		}
		else if (type == CustomerAjaxEventType.CUSTOMER_DELETED)
		{
			deleteCustomer(customerBackingBean);
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
	 * Delete customer
	 * @param backingBean
	 * @throws ParentChildConstraintException
	 */
	private void deleteCustomer(CustomerAdminBackingBean backingBean) throws ParentChildConstraintException
	{
		customerService.deleteCustomer(backingBean.getCustomer().getCustomerId());
	}
	
	/**
	 * Unique customer code / name validator
	 * @author Thies
	 *
	 */
	private class UniqueCustomerValidator extends AbstractFormValidator
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1181184585571474550L;
		private FormComponent[] components;
		
		/**
		 * 
		 * @param passwordField
		 * @param confirmField
		 */
		public UniqueCustomerValidator(FormComponent customerName, FormComponent customerCode)
		{
			components = new FormComponent[]{customerName, customerCode};
		}

		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.html.form.validation.IFormValidator#getDependentFormComponents()
		 */
		public FormComponent[] getDependentFormComponents()
		{
			return components;
		}

		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
		 */
		public void validate(Form form)
		{
			if (!StringUtils.isBlank(components[0].getInput())
					&& !StringUtils.isBlank(components[1].getInput()))
			{
				String orgName = ((CustomerAdminBackingBean)((CompoundPropertyModel)getModel()).getObject()).getOriginalCustomerName();
				String orgCode = ((CustomerAdminBackingBean)((CompoundPropertyModel)getModel()).getObject()).getOriginalCustomerCode();
				
				if ((StringUtils.equals(orgName, components[0].getInput()))
						&& StringUtils.equals(orgCode, components[1].getInput()))
				{
					return;
				}
				
				Customer customer = customerService.getCustomer(components[0].getInput(), components[1].getInput());
				
				if (customer != null)
				{
					error(components[0], "admin.customer.errorNotUnique");	
				}
			}
		}
	}	
}
