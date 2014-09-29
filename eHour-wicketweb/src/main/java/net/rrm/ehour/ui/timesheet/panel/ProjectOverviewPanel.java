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

package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.*;

/**
 * Panel showing overview
 */

public class ProjectOverviewPanel extends AbstractBasePanel<Void> {
    private static final long serialVersionUID = -5935376941518756941L;
    private static final String ID_GREY_BORDER = "greyBorder";
    private static final String ID_TABLE_DATA = "projectStatus";
    private static final String ID_SUMMARY_ROW = "summaryRow";
    private static final String ID_FOLD_LINK = "foldLink";
    private static final String ID_FOLD_IMG = "foldImg";

    Label label;

    public ProjectOverviewPanel(String id, Calendar overviewFor, Collection<UserProjectStatus> projectStatusses) {
        super(id);

        this.setOutputMarkupId(true);

        // this should be easier..
        label = new Label("title", new MessageResourceModel("projectoverview.aggregatedPerMonth", this, new DateModel(overviewFor, getConfig(), DateModel.DATESTYLE_MONTHONLY)));
        CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder(ID_GREY_BORDER, label);

        addGrandTotals(greyBorder, projectStatusses, getConfig());
        addColumnLabels(greyBorder);

        addTableData(greyBorder, projectStatusses);

        add(greyBorder);
    }

    private void addGrandTotals(WebMarkupContainer container, Collection<UserProjectStatus> projectStatusSet, EhourConfig config) {
        float totalHours = 0;

        if (projectStatusSet != null) {
            for (UserProjectStatus status : projectStatusSet) {
                totalHours += (status.getHours() != null) ? status.getHours().floatValue() : 0;
            }
        }

        container.add(new Label("grandTotalHours", new Model<Float>(totalHours)));

        float totalAllottedHours = 0;

        if (projectStatusSet != null) {
            for (UserProjectStatus status : projectStatusSet) {
                Float activityAllottedHours = status.getActivity().getAllottedHours();
                totalAllottedHours += (activityAllottedHours != null) ? activityAllottedHours : 0;
            }
        }

        container.add(new Label("grandTotalAllottedHours", new Model<Float>(totalAllottedHours)));

        Label projectLabel = HtmlUtil.getNbspLabel("grandProject");
        Label customerLabel = HtmlUtil.getNbspLabel("grandCustomer");

        if (!config.isShowTurnover()) {
            customerLabel.add(AttributeModifier.replace("style", "width: 30%"));
            projectLabel.add(AttributeModifier.replace("style", "width: 35%"));
        }
        container.add(customerLabel);
        container.add(projectLabel);
    }

    private void addColumnLabels(WebMarkupContainer container) {
        Label projectLabel = new Label("projectLabel", new ResourceModel("overview.project"));
        setProjectLabelWidth(projectLabel);
        container.add(projectLabel);

        Label customerLabel = new Label("customerLabel", new ResourceModel("overview.customer"));
        setCustomerLabelWidth(customerLabel);
        container.add(customerLabel);

        container.add(new Label("bookedHoursLabel", new ResourceModel("overview.hours")));
    }

    @SuppressWarnings("serial")
    private void addTableData(WebMarkupContainer container, Collection<UserProjectStatus> projectStatusSet) {
        List<UserProjectStatus> statusses = projectStatusSet == null ? new ArrayList<UserProjectStatus>() : new ArrayList<UserProjectStatus>(projectStatusSet);

        // table data should reflect the path to the listView
        ListView<UserProjectStatus> view = new ListView<UserProjectStatus>(ID_TABLE_DATA, statusses) {
            public void populateItem(final ListItem<UserProjectStatus> item) {
                UserProjectStatus projectStatus = item.getModelObject();

                WebMarkupContainer projectNameContainer = new WebMarkupContainer("projectNameContainer");
                item.add(projectNameContainer);
                setProjectLabelWidth(projectNameContainer);

                Project project = projectStatus.getActivity().getProject();
                Label projectLabel = new Label("projectName", project.getName());
                projectLabel.setMarkupId(String.format("prjN%d", project.getProjectId()));
                projectLabel.setOutputMarkupId(true);
                projectNameContainer.add(projectLabel);

                String name = project.getCustomer().getName();
                Label customerLabel = new Label("customerName", name);
                createTitle(customerLabel, name);
                setCustomerLabelWidth(customerLabel);
                item.add(customerLabel);

                Number hours = projectStatus.getHours();

                Label bookedHoursLabel = new Label("monthHours", new Model<Float>(hours != null ? hours.floatValue() : 0f));
                item.add(bookedHoursLabel);

                Float activityAllottedHours = projectStatus.getActivity().getAllottedHours();
                float activityAllottedHoursValue = (activityAllottedHours != null) ? activityAllottedHours : 0;
                Label allottedHoursLabel = new Label("allottedHours", new Model<Float>(activityAllottedHoursValue));
                item.add(allottedHoursLabel);

                Label activityLabel = new Label("activityName", projectStatus.getActivity().getFullName());
                item.add(activityLabel);

                // SummaryRow
                Component projectSummaryRow = createProjectSummaryRow(ID_SUMMARY_ROW, projectStatus);
                item.add(projectSummaryRow);

                item.add(createFoldLink(projectSummaryRow));
            }
        };

        container.add(view);
    }

    private void createTitle(Label label, String title) {
        label.add(AttributeModifier.append("title", title));
    }

    /**
     * Create fold link (also contains the creation of the summary row)
     */
    @SuppressWarnings("serial")
    private Component createFoldLink(final Component original) {
        AjaxLink<String> foldLink = new AjaxLink<String>(ID_FOLD_LINK) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                boolean isVisible = original.isVisible();

                original.setVisible(!isVisible);
                target.add(original);

                WebMarkupContainer img = createFoldImage(!isVisible);
                this.get(ID_FOLD_IMG).replaceWith(img);
                target.add(img);
            }
        };

        final WebMarkupContainer originalImage = createFoldImage(false);
        foldLink.add(originalImage);
        return foldLink;
    }

    private WebMarkupContainer createFoldImage(boolean up) {
        WebMarkupContainer foldImg = new WebMarkupContainer("foldImg");
        foldImg.add(AttributeModifier.replace("class", up ? "fa fa-angle-up" : "fa fa-angle-down"));
        foldImg.setOutputMarkupId(true);

        return foldImg;
    }

    // Activity are always of Alloted Types
    private Component createProjectSummaryRow(String id, UserProjectStatus projectStatus) {
        // SummaryRow placeholder
        WebMarkupContainer container = new WebMarkupContainer(id);
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        container.setVisible(false);

        // valid from until label
        Date activityStartDate = projectStatus.getActivity().getDateStart();
        Date activityEndDate = projectStatus.getActivity().getDateEnd();
        Label validityLabel = new Label("overview.validity", new StringResourceModel("overview.validity", this, null, new Object[] {
                new DateModel(activityStartDate, getConfig()),
                new DateModel(activityEndDate, getConfig()) }));
        validityLabel.setEscapeModelStrings(false);
        container.add(validityLabel);

        if (activityStartDate != null && activityEndDate != null) {
            validityLabel.setVisible(true);
        } else {
            validityLabel.setVisible(false);
        }

        WebMarkupContainer cont = new WebMarkupContainer("remainingHoursLabel");
        // only shown for allotted types

        Number bookedHours = projectStatus.getTotalBookedHours();
        float totalBookedHours = (bookedHours != null) ? projectStatus.getTotalBookedHours().floatValue() : 0;

        cont.add(new Label("overview.totalbooked", new MessageResourceModel("overview.totalbooked", this, totalBookedHours)));

        Label remainingLabel = new Label("overview.remainingfixed", new MessageResourceModel("overview.remainingfixed", this, projectStatus.getFixedHoursRemaining()));
        cont.add(remainingLabel);

        container.add(cont);

        return container;
    }


    private void setCustomerLabelWidth(Label label) {
            label.add(AttributeModifier.replace("style", "width: 30%;"));
    }

    private void setProjectLabelWidth(Component label) {
            label.add(AttributeModifier.replace("style", "width: 35%;"));
    }

    public Label getLabel() {
        return label;
    }
}
