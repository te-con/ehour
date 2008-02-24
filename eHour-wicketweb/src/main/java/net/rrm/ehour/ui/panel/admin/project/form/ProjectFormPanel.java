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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.KeepAliveTextArea;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.panel.admin.AbstractAjaxAwareAdminPanel;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;
import net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBean;
import net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBeanImpl;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.sort.CustomerComparator;
import net.rrm.ehour.ui.sort.UserComparator;
import net.rrm.ehour.ui.util.CommonWebUtil;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Project admin form
 **/

public class ProjectFormPanel extends AbstractAjaxAwareAdminPanel
{
	@SpringBean
	private ProjectService	projectService;
	@SpringBean
	private CustomerService	customerService;
	@SpringBean
	private	UserService		userService;

	private static final long serialVersionUID = -8677950352090140144L;
	private	static final Logger	logger = Logger.getLogger(ProjectFormPanel.class);
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public ProjectFormPanel(String id, CompoundPropertyModel model)
	{
		super(id, model);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("projectForm");
		addFormComponents(form);
		
		FormUtil.setSubmitActions(form
									,((ProjectAdminBackingBean)model.getObject()).getProject().isDeletable()
									,this
									,((EhourWebSession)getSession()).getEhourConfig());
		
		greyBorder.add(form);
	}

	/**
	 * Add form components
	 * @param form
	 */
	protected void addFormComponents(Form form)
	{
		addCustomer(form);
		addDescriptionAndContact(form);
		addGeneralInfo(form);
		addMisc(form);
		addProjectManager(form);
	}
	
	/**
	 * Add form components to form
	 * @param form
	 */
	protected void addGeneralInfo(Form form)
	{
		// name
		RequiredTextField	nameField = new RequiredTextField("project.name");
		form.add(nameField);
		nameField.add(new StringValidator.MaximumLengthValidator(64));
		nameField.setLabel(new ResourceModel("admin.project.name"));
		nameField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));

		// project code
		RequiredTextField	codeField = new RequiredTextField("project.projectCode");
		form.add(codeField);
		codeField.add(new StringValidator.MaximumLengthValidator(16));
		codeField.setLabel(new ResourceModel("admin.project.code"));
		codeField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));
	}
	
	protected void addCustomer(Form form)
	{
		// customers
		DropDownChoice customerDropdown = new DropDownChoice("project.customer", getCustomers(), new ChoiceRenderer("fullName"));
		customerDropdown.setRequired(true);
		customerDropdown.setLabel(new ResourceModel("admin.project.customer"));
		customerDropdown.add(new ValidatingFormComponentAjaxBehavior());
		form.add(customerDropdown);
		form.add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerDropdown));
	}
	
	protected void addProjectManager(Form form)
	{
		// project manager
		DropDownChoice projectManager = new DropDownChoice("project.projectManager", getEligablePms(), new ChoiceRenderer("fullName"));
		projectManager.setLabel(new ResourceModel("admin.project.projectManager"));
		form.add(projectManager);
	}
	
	protected void addDescriptionAndContact(Form form)
	{
		// description
		TextArea	textArea = new KeepAliveTextArea("project.description");
		textArea.setLabel(new ResourceModel("admin.project.description"));;
		form.add(textArea);

		// contact
		TextField	contactField = new TextField("project.contact");
		form.add(contactField);
	}

	protected void addMisc(Form form)
	{
		
		// default project
		form.add(new CheckBox("project.defaultProject"));
		
		// active
		form.add(new CheckBox("project.active"));

		// data save label
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		ProjectAdminBackingBeanImpl backingBean = (ProjectAdminBackingBeanImpl) ((((IWrapModel) params)).getWrappedModel()).getObject();
		
		try
		{
			if (type == CommonWebUtil.AJAX_FORM_SUBMIT)
			{
				persistProject(backingBean);
			}
			else if (type == CommonWebUtil.AJAX_DELETE)
			{
				deleteProject(backingBean);
			}
			
			((AjaxAwareContainer)getPage()).ajaxRequestReceived(target,  CommonWebUtil.AJAX_FORM_SUBMIT);
		}
		catch (Exception e)
		{
			logger.error("While persisting/deleting project", e);
			backingBean.setServerMessage(getLocalizer().getString("general.saveError", this));
			target.addComponent(this);
		}
	}
	
	
	/**
	 * Persist project
	 */
	protected void persistProject(ProjectAdminBackingBean backingBean) throws Exception
	{
		projectService.persistProject(backingBean.getProject());
	}
	
	/**
	 * Delete project
	 * @throws ParentChildConstraintException 
	 */
	protected void deleteProject(ProjectAdminBackingBean backingBean) throws ParentChildConstraintException
	{
		projectService.deleteProject(backingBean.getProject().getProjectId());
	} 
	
	/**
	 * Get customers for customer dropdown
	 */
	private List<Customer> getCustomers()
	{
		List<Customer> customers;
		
		customers = customerService.getCustomers(true);
		
		if (customers != null)
		{
			Collections.sort(customers, new CustomerComparator());
		}
		else
		{
			customers = new ArrayList<Customer>();
		}
		
		return customers;
	}
	
	/**
	 * Get users for PM selection
	 * @return
	 */
	private List<User> getEligablePms()
	{
		List<User> users;

		users = userService.getUsersWithEmailSet();
		
		if (users != null)
		{
			Collections.sort(users, new UserComparator(false));
		}
		else
		{
			users = new ArrayList<User>();
		}
		
		return users;
	}	
}
