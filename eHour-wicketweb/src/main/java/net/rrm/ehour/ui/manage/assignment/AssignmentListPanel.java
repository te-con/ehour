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
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.sort.ProjectAssignmentComparator;
import net.rrm.ehour.ui.common.border.GreyRoundedWideBorder;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

/**
 * List of existing assignments
 */

@SuppressWarnings("serial")
public class AssignmentListPanel extends AbstractBasePanel<Void> {
    private static final long serialVersionUID = -8798859357268916546L;

    @SpringBean
    private ProjectAssignmentService projectAssignmentService;

    private EhourConfig config;
    private ListView<ProjectAssignment> assignmentListView;
    private User user;
    private final Border greyBorder;

    public AssignmentListPanel(String id, User user) {
        super(id);

        this.user = user;

        config = EhourWebSession.getEhourConfig();

        setOutputMarkupId(true);

        greyBorder = new GreyRoundedWideBorder("border",
                new StringResourceModel("admin.assignment.assignmentsFor",
                        this, null, new Object[]{new Model<>(user.getFullName())})
        );
        greyBorder.setOutputMarkupId(true);
        add(greyBorder);

        List<ProjectAssignment> projectAssignments = getProjectAssignments(user);
        List<ProjectAssignment> filteredAssignments = filterAssignments(projectAssignments);

        greyBorder.add(getProjectAssignmentLists(filteredAssignments));
        greyBorder.add(createFooter("footer", filteredAssignments.size(), projectAssignments.size()));
        greyBorder.add(createActiveCheckbox());
    }

    public void updateList(AjaxRequestTarget target, User user) {
        List<ProjectAssignment> projectAssignments = getProjectAssignments(user);
        List<ProjectAssignment> filteredAssignments = filterAssignments(projectAssignments);

        assignmentListView.setList(filteredAssignments);
        greyBorder.addOrReplace(createFooter("footer", filteredAssignments.size(), projectAssignments.size()));
        target.add(greyBorder);
    }

    private List<ProjectAssignment> filterAssignments(List<ProjectAssignment> projectAssignments) {
        Boolean hideInactive = getEhourWebSession().getHideInactiveSelections();

        List<ProjectAssignment> filteredAssignments = Lists.newArrayList();

        if (hideInactive) {
            for (ProjectAssignment projectAssignment : projectAssignments) {
                if (projectAssignment.isBookable()) {
                    filteredAssignments.add(projectAssignment);
                }
            }

            return filteredAssignments;
        } else {
            return projectAssignments;
        }
    }

    private Label createFooter(String id, int shown, int total) {
        Label label = new Label(id, new MessageResourceModel("admin.assignment.footer", this, shown, total));
        label.setOutputMarkupId(true);
        return label;
    }

    private AjaxCheckBox createActiveCheckbox() {
        return new AjaxCheckBox("filterToggle", new Model<>(getEhourWebSession().getHideInactiveSelections())) {
            private static final long serialVersionUID = 2585047163449150793L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                getEhourWebSession().setHideInactiveSelections(Boolean.valueOf(getValue()));
                updateList(target, user);
            }
        };
    }

    private ListView<ProjectAssignment> getProjectAssignmentLists(final List<ProjectAssignment> projectAssignments) {
        assignmentListView = new ListView<ProjectAssignment>("assignments", projectAssignments) {
            @Override
            protected void populateItem(ListItem<ProjectAssignment> item) {
                final ProjectAssignment assignment = item.getModelObject();

                AjaxLink<Void> link = new AjaxLink<Void>("itemLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        EventPublisher.publishAjaxEvent(AssignmentListPanel.this, new PayloadAjaxEvent<>(AssignmentAjaxEventType.ASSIGNMENT_EDIT,
                                assignment));
                    }
                };

                AjaxLink<Void> imgLink = new AjaxLink<Void>("imgLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        EventPublisher.publishAjaxEvent(AssignmentListPanel.this, new PayloadAjaxEvent<>(AssignmentAjaxEventType.ASSIGNMENT_EDIT,
                                assignment));
                    }
                };

                item.add(imgLink);
                item.add(link);
                link.add(new Label("project", assignment.getProject().getFullName()));
                item.add(new Label("customer", assignment.getProject().getCustomer().getFullName()));

                Label dateStart = new Label("dateStart", new DateModel(assignment.getDateStart(), config, DateModel.DATESTYLE_FULL_SHORT));
                dateStart.setEscapeModelStrings(false);
                item.add(dateStart);

                Label dateEnd = new Label("dateEnd", new DateModel(assignment.getDateEnd(), config, DateModel.DATESTYLE_FULL_SHORT));
                dateEnd.setEscapeModelStrings(false);
                item.add(dateEnd);

                item.add(new Label("assignmentType",
                        new ResourceModel(WebUtils.getResourceKeyForProjectAssignmentType(assignment.getAssignmentType()))));

                item.add(new Label("role",
                        (StringUtils.isBlank(assignment.getRole()))
                                ? "--"
                                : assignment.getRole()));

                item.add(new Label("currency", Currency.getInstance(config.getCurrency()).getSymbol(config.getCurrency())));
                item.add(new Label("rate", new Model<>(assignment.getHourlyRate())));

            }
        };

        return assignmentListView;
    }

    private List<ProjectAssignment> getProjectAssignments(User user) {
        List<ProjectAssignment> assignments = user.getUserId() != null ? projectAssignmentService.getProjectAssignmentsForUser(user) : Lists.<ProjectAssignment>newArrayList();

        if (assignments != null) {
            Collections.sort(assignments, new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_CUSTDATEPRJ));
            setVisible(true);
        } else {
            assignments = new ArrayList<>();
        }

        return assignments;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new CssResourceReference(AssignmentListPanel.class, "assignment_admin.css")));
    }
}