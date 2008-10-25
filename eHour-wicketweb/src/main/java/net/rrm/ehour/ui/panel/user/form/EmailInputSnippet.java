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

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

/**
 * E-mail input
 **/

public class EmailInputSnippet extends Panel
{
	private static final long serialVersionUID = 644114040564619959L;
	
	/**
	 * 
	 * @param id
	 */
	public EmailInputSnippet(String id)
	{
		super(id);
		
		// email
		TextField	emailField = new TextField("user.email");
		emailField.add(EmailAddressValidator.getInstance());
		emailField.add(new ValidatingFormComponentAjaxBehavior());
		add(emailField);
		add(new AjaxFormComponentFeedbackIndicator("emailValidationError", emailField));		
	}
}
