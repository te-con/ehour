/**
 * Created on Aug 21, 2007
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

package net.rrm.ehour.ui.panel.admin.customer.form;

import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.KeepAliveTextArea;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;

import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Customer admin form panel
 **/

public class CustomerFormPanel extends Panel
{
	private static final long serialVersionUID = 8536721437867359030L;

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
		form.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));
			
		// code
		RequiredTextField	codeField = new RequiredTextField("customer.code");
		form.add(codeField);
		codeField.add(new StringValidator.MaximumLengthValidator(16));
		codeField.setLabel(new ResourceModel("admin.customer.code"));
		form.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));
		
		// description
		TextArea	textArea = new KeepAliveTextArea("customer.description");
		textArea.setLabel(new ResourceModel("admin.customer.description"));;
		form.add(textArea);
			
		// active
		form.add(new CheckBox("customer.active"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage"));
	
		//
		FormUtil.setSubmitActions(form);
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onchange", Duration.seconds(1));
		
		greyBorder.add(form);
	}
}
