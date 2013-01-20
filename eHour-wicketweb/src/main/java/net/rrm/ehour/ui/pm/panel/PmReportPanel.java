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

package net.rrm.ehour.ui.pm.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.border.BlueTabRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.ArrayList;

/**
 * PM Report panel
 */

public class PmReportPanel extends AbstractBasePanel<Void> implements IHeaderContributor {
    private static final long serialVersionUID = -1735419536027937563L;

    public PmReportPanel(String id, ProjectManagerReport report) {
        super(id);

        setOutputMarkupId(true);

        final EhourConfig config = EhourWebSession.getSession().getEhourConfig();

        Border blueBorder = new BlueTabRoundedBorder("reportFrame");
        add(blueBorder);

        blueBorder.add(getReportHeaderLabel("reportHeader", report.getReportRange(), config));

        blueBorder.add(new ListView<AssignmentAggregateReportElement>("report", new ArrayList<AssignmentAggregateReportElement>(report.getAggregates())) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<AssignmentAggregateReportElement> item) {
                AssignmentAggregateReportElement aggregate = item.getModelObject();

                int column = 0;

                Label user = new Label("user", aggregate.getProjectAssignment().getUser().getFullName());
                applyCss(item, column++, user);
                item.add(user);

                Label role = new Label("role", aggregate.getProjectAssignment().getRole());
                applyCss(item, column++, role);
                item.add(role);
                Label type = new Label("type", new ResourceModel(WebUtils.getResourceKeyForProjectAssignmentType(aggregate.getProjectAssignment().getAssignmentType())));
                applyCss(item, column++, type);
                item.add(type);
                Label booked = new Label("booked", new Model<Float>(aggregate.getHours().floatValue()));
                applyCss(item, column++, booked);
                item.add(booked);
                Label allotted = new Label("allotted", new Model<Float>(aggregate.getProjectAssignment().getAllottedHours()));
                applyCss(item, column++, allotted);
                item.add(allotted);
                Label overrun = new Label("overrun", new Model<Float>(aggregate.getProjectAssignment().getAllowedOverrun()));
                applyCss(item, column++, overrun);
                item.add(overrun);
                Label available = new Label("available", new Model<Float>(aggregate.getAvailableHours()));
                applyCss(item, column++, available);
                item.add(available);
                Label percentageUsed = new Label("percentageUsed", new Model<Float>(aggregate.getProgressPercentage()));
                applyCss(item, column, percentageUsed);
                item.add(percentageUsed);

            }
        });
    }

    private void applyCss(ListItem<AssignmentAggregateReportElement> item, int column, Label cellLabel) {
        StringBuilder cssClassBuilder = new StringBuilder();
        cssClassBuilder.append(item.getIndex() == 0 ? "firstRow" : "");
        cssClassBuilder.append(column == 0 ? " firstColumn" : "");

        String cssClass = cssClassBuilder.toString();

        if (StringUtils.isNotEmpty(cssClass)) {
            cellLabel.add(AttributeModifier.replace("class", cssClass));
        }
    }


    private Label getReportHeaderLabel(String id, DateRange reportRange, EhourConfig config) {
        Label reportHeaderLabel = new Label(id, new StringResourceModel("report.header",
                this, null,
                new Object[]{new DateModel(reportRange.getDateStart(), config),
                        new DateModel(reportRange.getDateEnd(), config)}));
        reportHeaderLabel.setEscapeModelStrings(false);

        return reportHeaderLabel;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new CssResourceReference(TreeReportDataPanel.class, "style/reportStyle.css")));
    }
}
