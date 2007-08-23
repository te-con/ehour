/**
 * Created on Aug 23, 2007
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

package net.rrm.ehour.ui.panel.admin.assignment;

import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;
import net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBean;

import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * Assignment form
 **/

public class AssignmentFormPanel extends Panel
{
	private static final long serialVersionUID = -85486044225123470L;

	/**
	 * 
	 * @param id
	 * @param model
	 * @param customers
	 * @param assignmenTypes
	 */
	public AssignmentFormPanel(String id,
								CompoundPropertyModel model,
								List<Customer> customers,
								List<ProjectAssignmentType> assignmenTypes)
	{
		super(id);
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("customerForm");
		
		// customer
		DropDownChoice customerChoice = new DropDownChoice("projectAssignment.customer", customers, new ChoiceRenderer("fullName"));
		customerChoice.setRequired(true);
		customerChoice.setLabel(new ResourceModel("admin.project.projectManager"));
		form.add(customerChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerChoice));

		// project
//		DropDownChoice projectChoice = new DropDownChoice("projects", , new ChoiceRenderer("fullName"));
//		customerChoice.setRequired(true);
//		customerChoice.setLabel(new ResourceModel("admin.project.projectManager"));
//		form.add(customerChoice);
//		form.add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerChoice));
		
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage"));
		
		//
		FormUtil.setSubmitActions(form, ((ProjectAdminBackingBean)model.getObject()).getProject().isDeletable());
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onchange", Duration.ONE_SECOND);
		
		greyBorder.add(form);

	}
}
