/**
 * Created on Oct 25, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.panel.user.form;

import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.panel.user.form.admin.dto.UserBackingBean;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IChainingModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.lang.Objects;

/**
 * Password input 
 **/

public class PasswordInputSnippet extends Panel
{
	private static final long serialVersionUID = -213282880713440676L;

	/**
	 * 
	 * @param id
	 */
	public PasswordInputSnippet(String id, Form form)
	{
		super(id);
		
		// password & confirm
		PasswordTextField	passwordTextField = new PasswordTextField("user.updatedPassword");
		passwordTextField.setLabel(new ResourceModel("user.password"));
		passwordTextField.setRequired(false);	// passwordField defaults to required
		passwordTextField.add(new ValidatingFormComponentAjaxBehavior());
		add(new AjaxFormComponentFeedbackIndicator("passwordValidationError", passwordTextField));
		add(passwordTextField);

		PasswordTextField	confirmPasswordTextField = new PasswordTextField("confirmPassword");
		confirmPasswordTextField.setRequired(false);	// passwordField defaults to required
		confirmPasswordTextField.add(new ValidatingFormComponentAjaxBehavior());
		add(confirmPasswordTextField);
		add(new AjaxFormComponentFeedbackIndicator("confirmPasswordValidationError", confirmPasswordTextField));
		form.add(new ConfirmPasswordValidator(passwordTextField, confirmPasswordTextField));		
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
			String orgPassword = ((UserBackingBean)((IChainingModel)getModel()).getChainedModel().getObject()).getOriginalPassword();
			
			if ("".equals(Objects.stringValue(components[0].getInput(), false))
					&& (orgPassword == null || orgPassword.equals("")))
			{
				error(components[0], "Required");
			}
			else if (!Objects.stringValue(components[0].getInput(), false)
					.equals(Objects.stringValue(components[1].getInput(), false)))
			{
				error(components[1], "user.errorConfirmPassNeeded");
			}
		}
	}	
}
