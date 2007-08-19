/**
 * Created on Aug 11, 2007
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

package net.rrm.ehour.ui.panel.user.form;

import java.util.List;

import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.user.form.dto.UserBackingBean;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * User Form Panel for admin
 **/

public class UserFormPanel extends Panel
{
	private static final long serialVersionUID = -7427807216389657732L;

	@SpringBean
	private UserService	userService;
	
	/**
	 * 
	 * @param id
	 * @param userModel
	 * @param roles
	 * @param departments
	 */
	public UserFormPanel(String id,
							CompoundPropertyModel userModel,
							List<UserRole> roles,
							List<UserDepartment> departments)
	{
		super(id, userModel);
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("userForm");

		// username
		RequiredTextField	usernameField = new RequiredTextField("user.username");
		form.add(usernameField);
		usernameField.add(new StringValidator.MaximumLengthValidator(32));
		usernameField.add(new DuplicateUsernameValidator());
		usernameField.setLabel(new ResourceModel("admin.user.username"));
		form.add(new AjaxFormComponentFeedbackIndicator("userValidationError", usernameField));
		
		// password & confirm
		PasswordTextField	passwordTextField = new PasswordTextField("user.password");
		passwordTextField.setLabel(new ResourceModel("admin.user.password"));
		passwordTextField.setRequired(false);	// passwordField defaults to required
		form.add(new AjaxFormComponentFeedbackIndicator("passwordValidationError", passwordTextField));
		form.add(passwordTextField);

		PasswordTextField	confirmPasswordTextField = new PasswordTextField("confirmPassword");
		confirmPasswordTextField.setRequired(false);	// passwordField defaults to required
		form.add(confirmPasswordTextField);
		form.add(new AjaxFormComponentFeedbackIndicator("confirmPasswordValidationError", confirmPasswordTextField));
		
		form.add(new ConfirmPasswordValidator(passwordTextField, confirmPasswordTextField));
		
		// first & last name
		TextField	firstNameField = new RequiredTextField("user.firstName");
		form.add(firstNameField);

		TextField	lastNameField = new RequiredTextField("user.lastName");
		form.add(lastNameField);
		lastNameField.setLabel(new ResourceModel("admin.user.lastName"));
		form.add(new AjaxFormComponentFeedbackIndicator("lastNameValidationError", lastNameField));
		
		// email
		TextField	emailField = new TextField("user.email");
		emailField.add(EmailAddressValidator.getInstance());
		form.add(emailField);
		form.add(new AjaxFormComponentFeedbackIndicator("emailValidationError", emailField));
		
		// department
		DropDownChoice userDepartment = new DropDownChoice("user.userDepartment", departments, new ChoiceRenderer("name"));
		userDepartment.setRequired(true);
		userDepartment.setLabel(new ResourceModel("admin.user.department"));
		form.add(userDepartment);
		form.add(new AjaxFormComponentFeedbackIndicator("departmentValidationError", userDepartment));
		
		// user roles
		ListMultipleChoice	userRoles = new ListMultipleChoice("user.userRoles", roles, new ChoiceRenderer("roleName"));
		userRoles.setMaxRows(4);
		userRoles.setLabel(new ResourceModel("admin.user.roles"));
		userRoles.setRequired(true);
		form.add(userRoles);
		form.add(new AjaxFormComponentFeedbackIndicator("rolesValidationError", userRoles));

		// active
		form.add(new CheckBox("user.active"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage"));
	
		//
		setSubmitActions(form);
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onchange", Duration.seconds(1));
		
		greyBorder.add(form);
	}
	
	/**
	 * Set submit actions for form
	 * @param form
	 * @param timesheet
	 */
	private void setSubmitActions(Form form)
	{
		// default submit
		form.add(new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				((BasePage)getPage()).ajaxRequestReceived(target, CommonStaticData.AJAX_FORM_SUBMIT, form.getModel());
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}			
        });
	}	
	
	/**
	 * Duplicate username validator
	 * @author Thies
	 *
	 */
	private class DuplicateUsernameValidator extends AbstractValidator
	{
		private static final long serialVersionUID = 542950054849279025L;

		@Override
		protected void onValidate(IValidatable validatable)
		{
			String username = (String)validatable.getValue();
			String orgUsername = ((UserBackingBean)((CompoundPropertyModel)getModel()).getObject()).getOriginalUsername();

			if (orgUsername != null && orgUsername.length() > 0 && username.equalsIgnoreCase(orgUsername))
			{
				return;
			}
			else if (userService.getUser(username) != null)
			{
				error(validatable, "admin.user.errorUsernameExists");
			}
		}
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class ConfirmPasswordValidator extends AbstractFormValidator
	{
		private static final long serialVersionUID = -7176398632862551019L;
		private FormComponent[] components;
		
		/**
		 * 
		 * @param passwordField
		 * @param confirmField
		 */
		public ConfirmPasswordValidator(FormComponent passwordField, FormComponent confirmField)
		{
			components = new FormComponent[]{passwordField, confirmField};
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
			String orgPassword = ((UserBackingBean)((CompoundPropertyModel)getModel()).getObject()).getOriginalPassword();
			
			if ("".equals(Objects.stringValue(components[0].getInput(), false))
					&& (orgPassword == null || orgPassword.equals("")))
			{
				error(components[0], "Required");
			}
			else if (!Objects.stringValue(components[0].getInput(), false)
					.equals(Objects.stringValue(components[1].getInput(), false)))
			{
				error(components[1], "admin.user.errorConfirmPassNeeded");
			}
		}
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class ServerMessageLabel extends Label
	{
		private boolean overrideVisibility = false;
		
		public ServerMessageLabel(String id)
		{
			super(id);

			add(new SimpleAttributeModifier("class", "formValidationError"));
			setOutputMarkupId(true);

			add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5))
				{
					@Override
					protected void onPostProcessTarget(AjaxRequestTarget target)
					{
						target.addComponent(ServerMessageLabel.this);
						overrideVisibility = true;
					}
				});		
		}

		public boolean isVisible()
		{
			return overrideVisibility ? false : getModel().getObject()!= null;
		}		
	}
}
