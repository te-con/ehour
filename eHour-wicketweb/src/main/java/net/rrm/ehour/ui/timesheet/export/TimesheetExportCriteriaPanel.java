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

package net.rrm.ehour.ui.timesheet.export;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.project.util.ProjectUtil;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker;
import net.rrm.ehour.ui.common.report.excel.ExcelRequestHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * TimesheetExportCriteriaPanel holding the form for month based exports for consultants
 */

public class TimesheetExportCriteriaPanel extends AbstractBasePanel<ReportCriteria> {
    private static final long serialVersionUID = -3732529050866431376L;

    private Projects billableProjects;
    private Projects unbillableProjects;

    public TimesheetExportCriteriaPanel(String id, IModel<ReportCriteria> model) {
        super(id, model);
        setOutputMarkupId(true);

        add(createCriteriaPanel("criteriaForm", model));
    }

    /**
     * Create the criteria panel with the form, assignments and submit buttons
     */
    private Form<ReportCriteria> createCriteriaPanel(String id, IModel<ReportCriteria> model) {
        SelectionForm form = new SelectionForm(id, model);

        ReportCriteria criteria = (ReportCriteria) getDefaultModelObject();
        List<Project> allProjects = criteria.getAvailableCriteria().getProjects();
        billableProjects = new Projects();
        unbillableProjects = new Projects();

        form.add(createBillableProjectGroup("billableProjectGroup", allProjects));
        form.add(createUnbillableProjectGroup("unbillableProjectGroup", allProjects));

        form.add(createSignOffCheck("signOff"));

        form.add(createSubmitButton("store", form));

        form.add(new LocalizedDatePicker("startDate", new PropertyModel<Date>(model, "reportRange.dateStart")));
        form.add(new LocalizedDatePicker("endDate", new PropertyModel<Date>(model, "reportRange.dateEnd")));

        return form;
    }

    @SuppressWarnings("serial")
    private SubmitLink createSubmitButton(String id, SelectionForm form) {
        return new SubmitLink(id, form);
    }

    private CheckBox createSignOffCheck(String id) {
        CheckBox signOffCheck = new CheckBox(id, new PropertyModel<Boolean>(this.getDefaultModel(), "userSelectedCriteria.customParameters[INCL_SIGN_OFF]"));
        signOffCheck.setMarkupId("signoff");
        return signOffCheck;
    }

    private CheckGroup<Project> createUnbillableProjectGroup(String id, List<Project> allProjects) {
        CheckGroup<Project> unbillableGroup = new CheckGroup<>(id, new PropertyModel<Collection<Project>>(unbillableProjects, "projects"));
        unbillableGroup.add(new CheckGroupSelector("checkall"));

        List<Project> unbillableProjects = ProjectUtil.filterUnbillable(allProjects);
        unbillableGroup.setVisible(!unbillableProjects.isEmpty());

        ListView<Project> unbillableProjectsView = getAssignmentCheckboxesView("unbillableProjects", unbillableProjects);
        unbillableGroup.add(unbillableProjectsView);

        return unbillableGroup;
    }

    private CheckGroup<Project> createBillableProjectGroup(String id, List<Project> allProjects) {
        CheckGroup<Project> billableGroup = new CheckGroup<>(id, new PropertyModel<Collection<Project>>(billableProjects, "projects"));
        billableGroup.add(new CheckGroupSelector("checkall"));

        List<Project> billableProjects = ProjectUtil.filterBillable(allProjects);
        billableGroup.setVisible(!billableProjects.isEmpty());

        ListView<Project> billableProjectsView = getAssignmentCheckboxesView("billableProjects", billableProjects);
        billableGroup.add(billableProjectsView);

        return billableGroup;
    }

    @SuppressWarnings("serial")
    private ListView<Project> getAssignmentCheckboxesView(String id, List<Project> projects) {
        return new ListView<Project>(id, projects) {
            @Override
            protected void populateItem(ListItem<Project> item) {
                item.add(new Check<>("check", item.getModel()));
                item.add(new Label("project", new PropertyModel<String>(item.getModel(), "fullNameWithCustomer")));
            }
        };
    }

    /**
     * Created on Feb 18, 2009, 5:39:23 PM
     *
     * @author Thies Edeling (thies@te-con.nl)
     */
    private class SelectionForm extends Form<ReportCriteria> {
        private static final long serialVersionUID = -8232635495078008621L;

        public SelectionForm(String id, IModel<ReportCriteria> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit() {
            final ReportCriteria reportCriteria = mergeBillablesAndUnbillables();
            final TimesheetExcelExport timesheetExcelExport = new TimesheetExcelExport(reportCriteria);

            String filename = createFilename(reportCriteria);

            getRequestCycle().scheduleRequestHandlerAfterCurrent(new ExcelRequestHandler(filename, timesheetExcelExport));
        }

        private ReportCriteria mergeBillablesAndUnbillables() {
            List<Project> projects = new ArrayList<>(TimesheetExportCriteriaPanel.this.billableProjects.getProjects());
            projects.addAll(TimesheetExportCriteriaPanel.this.unbillableProjects.getProjects());

            final ReportCriteria reportCriteria = getModelObject();
            reportCriteria.getUserSelectedCriteria().setProjects(projects);
            return reportCriteria;
        }

        private String createFilename(ReportCriteria reportCriteria) {
            DateRange reportRange = reportCriteria.getUserSelectedCriteria().getReportRange();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String start = formatter.format(reportRange.getDateStart());
            String end = formatter.format(reportRange.getDateEnd());

            return String.format("eHour_export_%s-%s.xlsx", start, end);
        }
    }

    private static class Projects implements Serializable {
        private List<Project> projects = new ArrayList<>();

        private List<Project> getProjects() {
            return projects;
        }

        private void setProjects(List<Project> projects) {
            this.projects = projects;
        }
    }

}
