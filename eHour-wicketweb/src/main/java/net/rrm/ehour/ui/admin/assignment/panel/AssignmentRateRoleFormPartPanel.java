/**
 * Created on Feb 24, 2008
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

package net.rrm.ehour.ui.admin.assignment.panel;

import java.util.Currency;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.NumberValidator;

/**
 * Rate & role 
 **/

public class AssignmentRateRoleFormPartPanel extends Panel
{
	private static final long serialVersionUID = -3250880303705076821L;

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AssignmentRateRoleFormPartPanel(String id, IModel model)
	{
		super(id, model);

		EhourConfig config = ((EhourWebSession)getSession()).getEhourConfig();
		
		// add role
		TextField role = new TextField("projectAssignment.role");
		add(role);
		
		// add hourly rate
		TextField	hourlyRate = new TextField("projectAssignment.hourlyRate",
											new FloatModel(new PropertyModel(model, "projectAssignment.hourlyRate"), config, null));
		hourlyRate.setType(Float.class);
		hourlyRate.add(new ValidatingFormComponentAjaxBehavior());
		hourlyRate.add(NumberValidator.minimum(0));
		add(hourlyRate);
		add(new AjaxFormComponentFeedbackIndicator("rateValidationError", hourlyRate));

		// and currency
		add(new Label("currency",  Currency.getInstance(config.getCurrency()).getSymbol(config.getCurrency())));
	}
}
