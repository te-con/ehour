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

package net.rrm.ehour.ui.manage.assignment.form;


import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.manage.assignment.AssignmentAdminBackingBean;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import java.util.List;

public class AssignmentFormComponentContainerPanel extends AbstractAjaxPanel<AssignmentAdminBackingBean> {

    public enum DisplayOption {
        SHOW_PROJECT_SELECTION,
        SHOW_CANCEL_BUTTON,
        NO_BORDER
    }

    private static final long serialVersionUID = -85486044225123470L;

    public AssignmentFormComponentContainerPanel(String id, Form<AssignmentAdminBackingBean> form, final IModel<AssignmentAdminBackingBean> model, List<DisplayOption> displayOptions) {
        super(id, model);

        setUpPanel(form, model, displayOptions);
    }

    private void setUpPanel(Form<AssignmentAdminBackingBean> form, final IModel<AssignmentAdminBackingBean> model, List<DisplayOption> displayOptions) {
        // setup the customer & project dropdowns
        add(createProjectSelection("projectSelection", model, displayOptions));

        // Add rate & role
        add(new AssignmentRateRoleFormPartPanel("rateRole", model));

        // Project duration form components
        add(new AssignmentTypeFormPartPanel("assignmentType", model, form));

        // active
        CheckBox active = new CheckBox("projectAssignment.active");
        active.setMarkupId("activeAssignment");
        add(active);
    }

    private WebMarkupContainer createProjectSelection(String id, IModel<AssignmentAdminBackingBean> model, List<DisplayOption> displayOptions) {
        if (displayOptions.contains(DisplayOption.SHOW_PROJECT_SELECTION)) {
            return new AssignmentProjectSelectionPanel(id, model);
        } else {
            return new PlaceholderPanel(id);
        }
    }

    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        if (ajaxEvent.getEventType() == AssignmentProjectSelectionPanel.EntrySelectorAjaxEventType.PROJECT_CHANGE) {
            EventPublisher.publishAjaxEventToParentChildren(this, ajaxEvent);

            return false;
        }

        return true;
    }
}
