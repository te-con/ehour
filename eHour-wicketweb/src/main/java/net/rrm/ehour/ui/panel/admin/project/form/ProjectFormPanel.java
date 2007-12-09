/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.panel.admin.project.form;

import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.ajax.AjaxEvent;
import net.rrm.ehour.ui.ajax.AjaxEventType;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.KeepAliveTextArea;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.panel.admin.AbstractAjaxAwareAdminPanel;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;
import net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBean;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Project admin form
 **/

public class ProjectFormPanel extends AbstractAjaxAwareAdminPanel
{
	private static final long serialVersionUID = -8677950352090140144L;

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public ProjectFormPanel(String id, CompoundPropertyModel model, List<User> eligablePms, List<Customer> customers)
	{
		super(id, model);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("projectForm");
		
		// name
		RequiredTextField	nameField = new RequiredTextField("project.name");
		form.add(nameField);
		nameField.add(new StringValidator.MaximumLengthValidator(64));
		nameField.setLabel(new ResourceModel("admin.project.name"));
		form.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));

		// project code
		RequiredTextField	codeField = new RequiredTextField("project.projectCode");
		form.add(codeField);
		codeField.add(new StringValidator.MaximumLengthValidator(16));
		codeField.setLabel(new ResourceModel("admin.project.code"));
		form.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));

		// project manager
		DropDownChoice projectManager = new DropDownChoice("project.projectManager", eligablePms, new ChoiceRenderer("fullName"));
		projectManager.setLabel(new ResourceModel("admin.project.projectManager"));
		form.add(projectManager);
		
		// description
		TextArea	textArea = new KeepAliveTextArea("project.description");
		textArea.setLabel(new ResourceModel("admin.project.description"));;
		form.add(textArea);
		
		// customers
		DropDownChoice customerDropdown = new DropDownChoice("project.customer", customers, new ChoiceRenderer("fullName"));
		customerDropdown.setRequired(true);
		customerDropdown.setLabel(new ResourceModel("admin.project.customer"));
		form.add(customerDropdown);
		form.add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerDropdown));
		
		
		// add new customer link
		AjaxLink newCustomerLink = new AjaxLink("newCustomer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				((AjaxAwareContainer)getPage()).publishAjaxEvent(new AjaxEvent(target, AjaxEventType.ADMIN_CUSTOMER_NEW_PANEL_REQUEST));
			}
		};
		
		form.add(newCustomerLink);
		
		// contact
		TextField	contactField = new TextField("project.contact");
		form.add(contactField);
		
		// default project
		form.add(new CheckBox("project.defaultProject"));
		
		// active
		form.add(new CheckBox("project.active"));

		// data save label
		form.add(new ServerMessageLabel("serverMessage"));
			
		
		//
		FormUtil.setSubmitActions(form
									,((ProjectAdminBackingBean)model.getObject()).getProject().isDeletable()
									,this
									,((EhourWebSession)getSession()).getEhourConfig());
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onchange", Duration.ONE_SECOND);
		
		greyBorder.add(form);
	}
}
