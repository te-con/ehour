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
import net.rrm.ehour.ui.common.component.CurrencyLabel;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

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

    public ProjectOverviewPanel(String id, Calendar overviewFor, Collection<UserProjectStatus> projectStatusses) {
        super(id);

        this.setOutputMarkupId(true);

        // this should be easier..
        Label label = new Label("title", new MessageResourceModel("projectoverview.aggregatedPerMonth", this, new DateModel(overviewFor, getConfig(), DateModel.DATESTYLE_MONTHONLY)));
        CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder(ID_GREY_BORDER, label);

        addGrandTotals(greyBorder, projectStatusses, getConfig());
        addColumnLabels(greyBorder);

        addTableData(greyBorder, projectStatusses);

        add(greyBorder);
    }

    private void addGrandTotals(WebMarkupContainer container, Collection<UserProjectStatus> projectStatusSet, EhourConfig config) {
        float totalHours = 0;
        float totalTurnover = 0;
        Label turnOverLabel;

        if (projectStatusSet != null) {
            for (UserProjectStatus status : projectStatusSet) {
                totalHours += (status.getHours() != null) ? status.getHours().floatValue() : 0;
                totalTurnover += (status.getTurnOver() != null) ? status.getTurnOver().floatValue() : 0;
            }
        }

        container.add(new Label("grandTotalHours", new Model<>(totalHours)));

        turnOverLabel = new CurrencyLabel("grandTotalTurnover", new Model<>(totalTurnover));

        turnOverLabel.setVisible(config.isShowTurnover());
        turnOverLabel.setEscapeModelStrings(false);
        container.add(turnOverLabel);

        turnOverLabel = HtmlUtil.getNbspLabel("grandRate");
        turnOverLabel.setVisible(config.isShowTurnover());
        turnOverLabel.setEscapeModelStrings(false);
        container.add(turnOverLabel);

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

        Label rateLabel = new Label("rateLabel", new ResourceModel("overview.rate"));
        setRateWidthOrHide(rateLabel);
        container.add(rateLabel);

        Label turnOverLabel = new Label("turnoverLabel", new ResourceModel("overview.turnover"));
        setTurnoverWidthOrHide(turnOverLabel);
        container.add(turnOverLabel);
    }

    @SuppressWarnings("serial")
    private void addTableData(WebMarkupContainer container, Collection<UserProjectStatus> projectStatusSet) {
        List<UserProjectStatus> statusses = projectStatusSet == null ? new ArrayList<UserProjectStatus>() : new ArrayList<>(projectStatusSet);

        // table data should reflect the path to the listView
        ListView<UserProjectStatus> view = new ListView<UserProjectStatus>(ID_TABLE_DATA, statusses) {
            public void populateItem(final ListItem<UserProjectStatus> item) {
                UserProjectStatus projectStatus = item.getModelObject();

                WebMarkupContainer projectNameContainer = new WebMarkupContainer("projectNameContainer");
                item.add(projectNameContainer);
                setProjectLabelWidth(projectNameContainer);

                Project project = projectStatus.getProjectAssignment().getProject();
                Label projectLabel = new Label("projectName", project.getName());
                projectLabel.setMarkupId(String.format("prjN%d", project.getProjectId()));
                projectLabel.setOutputMarkupId(true);
                projectNameContainer.add(projectLabel);

                String name = project.getCustomer().getName();
                Label customerLabel = new Label("customerName", name);
                createTitle(customerLabel, name);
                setCustomerLabelWidth(customerLabel);
                item.add(customerLabel);

                String projectCode = project.getProjectCode();
                Label projectCodeLabel = new Label("projectCode", projectCode);
                createTitle(customerLabel, projectCode);
                projectCodeLabel.setMarkupId(String.format("prjC%d", project.getProjectId()));
                projectCodeLabel.setOutputMarkupId(true);
                item.add(projectCodeLabel);

                Float hourlyRate = projectStatus.getProjectAssignment().getHourlyRate();
                Label rateLabel = hourlyRate != null ? new CurrencyLabel("rate", hourlyRate) : new Label("rate", "--");
                rateLabel.setEscapeModelStrings(false);
                setRateWidthOrHide(rateLabel);
                item.add(rateLabel);

                Number hours = projectStatus.getHours();

                item.add(new Label("monthHours", new Model<>(hours != null ? hours.floatValue() : 0f)));

                boolean billable = project.isBillable();
                Label turnOverLabel = billable ? new CurrencyLabel("turnover", projectStatus.getTurnOver().floatValue()) : new Label("turnover", "--");
                setTurnoverWidthOrHide(turnOverLabel);

                item.add(turnOverLabel);

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

    private Component createProjectSummaryRow(String id, UserProjectStatus projectStatus) {
        // SummaryRow placeholder
        WebMarkupContainer container = new WebMarkupContainer(id);
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        container.setVisible(false);

        // valid from until label
        DateModel startDate = new DateModel(projectStatus.getProjectAssignment().getDateStart(), getConfig());
        DateModel endDate = new DateModel(projectStatus.getProjectAssignment().getDateEnd(), getConfig());
        Label validityLabel = new Label("overview.validity", new MessageResourceModel("overview.validity", this, startDate, endDate));
        validityLabel.setEscapeModelStrings(false);
        container.add(validityLabel);

        WebMarkupContainer cont = new WebMarkupContainer("remainingHoursLabel");
        // only shown for allotted types
        cont.setVisible(projectStatus.getProjectAssignment().getAssignmentType().isAllottedType());

        Number bookedHours = projectStatus.getTotalBookedHours();
        float totalBookedHours = (bookedHours != null) ? projectStatus.getTotalBookedHours().floatValue() : 0;

        cont.add(new Label("overview.totalbooked", new MessageResourceModel("overview.totalbooked", this, totalBookedHours)));

        Label remainingLabel = new Label("overview.remainingfixed", new MessageResourceModel("overview.remainingfixed", this, projectStatus.getFixedHoursRemaining()));
        remainingLabel.setVisible(projectStatus.getProjectAssignment().getAssignmentType().isAllottedType());
        cont.add(remainingLabel);

        Label remainingFlexLabel = new Label("overview.remainingflex", new MessageResourceModel("overview.remainingflex", this, projectStatus.getFlexHoursRemaining()));
        // only shown for flex allotted types
        remainingFlexLabel.setVisible(projectStatus.getProjectAssignment().getAssignmentType().isFlexAllottedType());
        cont.add(remainingFlexLabel);

        container.add(cont);

        return container;
    }


    private void setCustomerLabelWidth(Label label) {
        if (!isTurnOverVisible()) {
            label.add(AttributeModifier.replace("style", "width: 30%;"));
        }
    }

    private void setProjectLabelWidth(Component label) {
        if (!isTurnOverVisible()) {
            label.add(AttributeModifier.replace("style", "width: 35%;"));
        }
    }

    private void setRateWidthOrHide(Label label) {
        label.setVisible(isTurnOverVisible());
    }

    private void setTurnoverWidthOrHide(Label label) {
        label.setVisible(isTurnOverVisible());
    }

    private boolean isTurnOverVisible() {
        return getConfig().isShowTurnover();
    }
}
