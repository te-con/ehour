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

package net.rrm.ehour.ui.pm.page;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.component.LoadAwareButton;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.panel.datepicker.DatePickerPanel;
import net.rrm.ehour.ui.common.panel.sidepanel.SidePanel;
import net.rrm.ehour.ui.common.renderers.DomainObjectChoiceRenderer;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;

/**
 * Date range and project selection
 */

public class ProjectManagementReportCriteriaPanel extends SidePanel {
    private static final long serialVersionUID = -6129389795390181179L;


    public ProjectManagementReportCriteriaPanel(String id, IModel<ReportCriteria> model) {
        super(id);

        Form<Void> form = new Form<Void>("criteriaForm");

        ListChoice<Project> projectDropDown;
        projectDropDown = new ListChoice<Project>("userCriteria.projects",
                new PropertyModel<Project>(model, "userCriteria.project"),
                new PropertyModel<List<Project>>(model, "availableCriteria.projects"),
                new DomainObjectChoiceRenderer<Project>());
        projectDropDown.setNullValid(false);
        projectDropDown.setMaxRows(1);
        projectDropDown.setRequired(true);
        form.add(projectDropDown);

        addDatePickers(form, model);

        addSubmits(form);

        this.add(form);
    }

    protected void addSubmits(Form<?> form) {
        @SuppressWarnings("serial")
        AjaxFallbackButton submitButton = new LoadAwareButton("submitButton", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                EventPublisher.publishAjaxEvent(this, new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_UPDATED));
            }
        };

        form.add(submitButton);
    }

    private void addDatePickers(WebMarkupContainer parent, IModel<ReportCriteria> model) {

        parent.add(new DatePickerPanel("dateStart", new PropertyModel<Date>(model, "userCriteria.reportRange.dateStart"), new PropertyModel<Boolean>(model, "userCriteria.infiniteStartDate")));
        parent.add(new DatePickerPanel("dateEnd", new PropertyModel<Date>(model, "userCriteria.reportRange.dateEnd"), new PropertyModel<Boolean>(model, "userCriteria.infiniteEndDate")));
    }
}
