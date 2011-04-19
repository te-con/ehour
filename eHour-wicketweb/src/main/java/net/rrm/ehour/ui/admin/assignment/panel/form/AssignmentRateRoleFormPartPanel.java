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

package net.rrm.ehour.ui.admin.assignment.panel.form;

import java.util.Currency;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MinimumValidator;

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
	public AssignmentRateRoleFormPartPanel(String id, IModel<AssignmentAdminBackingBean> model)
	{
		super(id, model);

		EhourConfig config = ((EhourWebSession)getSession()).getEhourConfig();
		
		// add role
		TextField<String> role = new TextField<String>("projectAssignment.role");
		add(role);
		
		// add hourly rate
		TextField<Float> hourlyRate = new TextField<Float>("projectAssignment.hourlyRate",
											new PropertyModel<Float>(model, "projectAssignment.hourlyRate"));
		hourlyRate.setType(Float.class);
		hourlyRate.add(new ValidatingFormComponentAjaxBehavior());
		hourlyRate.add(new MinimumValidator<Float>(0f));
		add(hourlyRate);
		add(new AjaxFormComponentFeedbackIndicator("rateValidationError", hourlyRate));

		// and currency
		add(new Label("currency",  Currency.getInstance(config.getCurrency()).getSymbol(config.getCurrency())));
	}
}
