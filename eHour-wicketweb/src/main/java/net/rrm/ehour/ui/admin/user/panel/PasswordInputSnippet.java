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

package net.rrm.ehour.ui.admin.user.panel;

import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;

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
