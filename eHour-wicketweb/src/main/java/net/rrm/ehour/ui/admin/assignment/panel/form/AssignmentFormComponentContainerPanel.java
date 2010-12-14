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


import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import java.util.Arrays;
import java.util.List;

/**
 * Assignment form
 **/

public class AssignmentFormComponentContainerPanel extends AbstractAjaxPanel<AssignmentAdminBackingBean>
{
	public enum DisplayOption
	{
		HIDE_PROJECT_SELECTION,
		SHOW_PROJECT_SELECTION,
		HIDE_SAVE_BUTTON,
		SHOW_SAVE_BUTTON,
		HIDE_DELETE_BUTTON,
		SHOW_DELETE_BUTTON
	}

	private static final long serialVersionUID = -85486044225123470L;

	private AssignmentTypeFormPartPanel typeFormPartPanel;

	public AssignmentFormComponentContainerPanel(String id, Form<AssignmentAdminBackingBean> form, final IModel<AssignmentAdminBackingBean> model, DisplayOption... displayOptions)
	{
		super(id, model);

		setUpPanel(form, model, Arrays.asList(displayOptions));
	}

	/**
	 * Setup panel
	 */
	private void setUpPanel(Form<AssignmentAdminBackingBean> form, final IModel<AssignmentAdminBackingBean> model, List<DisplayOption> displayOptions)
	{
		// setup the customer & project dropdowns
		add(createProjectSelection("projectSelection", model, displayOptions));

		// Add rate & role
		add(new AssignmentRateRoleFormPartPanel("rateRole", model));

		// Project duration form components
		add(new AssignmentTypeFormPartPanel("assignmentType", model, form));

		// active
		add(new CheckBox("projectAssignment.active"));
	}

	private WebMarkupContainer createProjectSelection(String id, IModel<AssignmentAdminBackingBean> model, List<DisplayOption> displayOptions)
	{
		if (displayOptions.contains(DisplayOption.SHOW_PROJECT_SELECTION))
		{
			return new AssignmentProjectSelectionPanel(id, model);
		}
		else
		{
			return new PlaceholderPanel(id);
		}
	}

	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == AssignmentProjectSelectionPanel.EntrySelectorAjaxEventType.PROJECT_CHANGE)
		{
			updateNotifiableComponents(ajaxEvent.getTarget());

			return false;
		}

		return super.ajaxEventReceived(ajaxEvent);
	}

	private void updateNotifiableComponents(AjaxRequestTarget target)
	{
		Component[] components = typeFormPartPanel.getNotifiableComponents();

		for (Component component : components)
		{
			target.addComponent(component);
		}
	}
}
