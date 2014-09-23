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

package net.rrm.ehour.ui.manage.assignment;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.panel.noentry.NoEntrySelectedPanel;
import net.rrm.ehour.ui.manage.AbstractManagePage;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;


/**
 * Project assignments page
 */

@SuppressWarnings("serial")
public class AssignmentManagePage extends AbstractManagePage<Void> {
    private static final long serialVersionUID = 566527529422873370L;
    private static final String USER_SELECTOR_ID = "userSelector";
    public static final String ASSIGNMENT_PANEL_ID = "assignmentPanel";

    @SpringBean
    private UserService userService;

    private Panel assignmentPanel;

    public AssignmentManagePage(User user) {
        this();

        assignmentPanel = new AssignmentPanel(ASSIGNMENT_PANEL_ID, user);

        addOrReplace(assignmentPanel);
    }

    public AssignmentManagePage() {
        super(new ResourceModel("admin.assignment.title"));

        GreyRoundedBorder grey = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.assignment.title"));
        add(grey);

        EntrySelectorPanel.ClickHandler clickHandler = new EntrySelectorPanel.ClickHandler() {
            @Override
            public void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException {
                Integer userId = (Integer) row.getId();
                User user = userService.getUser(userId);
                replaceAssignmentPanel(target, user);
            }
        };

        EntrySelectorPanel entrySelectorPanel = new EntrySelectorPanel(USER_SELECTOR_ID,
                createSelectorData(getUsers()),
                clickHandler);


        grey.add(entrySelectorPanel);

        assignmentPanel = new NoEntrySelectedPanel(ASSIGNMENT_PANEL_ID, true, new ResourceModel("admin.assignment.noEditEntrySelected"));

        add(assignmentPanel);
    }

    private EntrySelectorData createSelectorData(List<User> users) {
        List<EntrySelectorData.Header> headers = Lists.newArrayList(new EntrySelectorData.Header("admin.user.lastName"),
                new EntrySelectorData.Header("admin.user.firstName"),
                new EntrySelectorData.Header("admin.assignment.assignments", EntrySelectorData.ColumnType.NUMERIC));

        List<EntrySelectorData.EntrySelectorRow> rows = Lists.newArrayList();

        for (User user : users) {
            Set<ProjectAssignment> assignments = user.getProjectAssignments();

            List<String> cells = Lists.newArrayList(user.getLastName(), user.getFirstName(), Integer.toString((assignments != null) ? assignments.size() : 0));
            rows.add(new EntrySelectorData.EntrySelectorRow(cells, user.getUserId()));
        }

        return new EntrySelectorData(headers, rows);
    }

    private void replaceAssignmentPanel(AjaxRequestTarget target, User user) {
        AssignmentPanel newAssignmentPanel = new AssignmentPanel(ASSIGNMENT_PANEL_ID, user);

        assignmentPanel.replaceWith(newAssignmentPanel);
        target.add(newAssignmentPanel);

        assignmentPanel = newAssignmentPanel;
    }

    private List<User> getUsers() {
        return userService.getUsers(UserRole.USER);
    }
}
