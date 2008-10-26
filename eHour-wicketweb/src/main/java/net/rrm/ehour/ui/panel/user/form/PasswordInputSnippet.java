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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

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
		add(passwordTextField);

		PasswordTextField	confirmPasswordTextField = new PasswordTextField("confirmPassword");
		confirmPasswordTextField.setRequired(false);	// passwordField defaults to required
		add(confirmPasswordTextField);
		add(new AjaxFormComponentFeedbackIndicator("confirmPasswordValidationError", confirmPasswordTextField));
		form.add(new EqualPasswordInputValidator(passwordTextField, confirmPasswordTextField)
		{
			private static final long serialVersionUID = -8287236970656810795L;

			protected String resourceKey()
			{
				return "user.errorConfirmPassNeeded";
			}
		});
	}
}
