/**
 * Created on Mar 10, 2008
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

package net.rrm.ehour.ui.panel.report.criteria.quick;

import java.util.List;

import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.ajax.AjaxEvent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Dropdown choice which generates an event when the dropdown was changed 
 **/

public class QuickDropDownChoice extends DropDownChoice
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public QuickDropDownChoice(final String id, final List data, final IChoiceRenderer renderer)
	{
		super(id, data, renderer);

		add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 507045565542332885L;
	
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				AjaxEvent event = new AjaxEvent(target, QuickDateAjaxEventType.ADMIN_QUICK_DATE_CHANGED);
				
				// ahum..
				((AjaxAwareContainer)getParent().getParent().getParent()).publishAjaxEvent(event);
			}
		});
	}
}
