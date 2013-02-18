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

package net.rrm.ehour.ui.timesheet.export.criteria;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.project.util.ProjectUtil;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.report.excel.ExcelRequestHandler;
import net.rrm.ehour.ui.common.util.Function;
import net.rrm.ehour.ui.timesheet.export.ExportCriteriaParameter;
import net.rrm.ehour.ui.timesheet.export.excel.ExportReportExcel;
import net.rrm.ehour.ui.timesheet.export.print.PrintMonth;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Collection;
import java.util.List;

/**
 * ExportCriteriaPanel holding the form for month based exports for consultants
 */

public class ExportCriteriaPanel extends Panel {
    private static final long serialVersionUID = -3732529050866431376L;

    private enum ExportType {
        EXCEL,
        PRINT
    }

    public ExportCriteriaPanel(String id, IModel<ReportCriteria> model) {
        super(id, model);
        setOutputMarkupId(true);

        add(createCriteriaPanel("criteriaForm", model));
    }

    /**
     * Create the criteria panel with the form, assignments and submit buttons
     */
    private Form<ReportCriteria> createCriteriaPanel(String id, IModel<ReportCriteria> model) {
        SelectionForm form = new SelectionForm(id, model);

        form.add(createAssignmentCheckboxes("projectGroup"));

        form.add(createSignOffCheck("signOff"));

        form.add(createSubmitButton("printButton", form, ExportType.PRINT));
        form.add(createSubmitButton("excelButton", form, ExportType.EXCEL));

        return form;
    }

    @SuppressWarnings("serial")
    private SubmitLink createSubmitButton(String id, SelectionForm form, final ExportType type) {
        return new SubmitLink(id, form) {
            @Override
            public void onSubmit() {
                ReportCriteria criteria = (ReportCriteria) ExportCriteriaPanel.this.getDefaultModelObject();

                criteria.getUserCriteria().getCustomParameters().put(ExportCriteriaParameter.EXPORT_TYPE, type);
            }
        };
    }

    private CheckBox createSignOffCheck(String id) {
        return new CheckBox(id, new PropertyModel<Boolean>(this.getDefaultModel(), "userCriteria.customParameters[INCL_SIGN_OFF]"));
    }

    private CheckGroup<Project> createAssignmentCheckboxes(String id) {
        CheckGroup<Project> projectGroup = new CheckGroup<Project>(id, new PropertyModel<Collection<Project>>(getDefaultModel(), "userCriteria.projects"));

        ReportCriteria criteria = (ReportCriteria) getDefaultModelObject();

        List<Project> allProjects = criteria.getAvailableCriteria().getProjects();

        List<Project> billableProjects = ProjectUtil.getBillableProjects(allProjects);
        ListView<Project> billableProjectsView = getAssignmentCheckboxesView("billableProjects", billableProjects);
        billableProjectsView.setVisible(billableProjects.size() > 0);
        projectGroup.add(billableProjectsView);

        List<Project> unbillableProjects = ProjectUtil.getUnbillableProjects(allProjects);
        ListView<Project> unbillableProjectsView = getAssignmentCheckboxesView("unbillableProjects", unbillableProjects);
        unbillableProjectsView.setVisible(unbillableProjects.size() > 0);
        projectGroup.add(unbillableProjectsView);

        return projectGroup;
    }

    @SuppressWarnings("serial")
    private ListView<Project> getAssignmentCheckboxesView(String id, List<Project> projects) {
        return new ListView<Project>(id, projects) {
            @Override
            protected void populateItem(ListItem<Project> item) {
                item.add(new Check<Project>("check", item.getModel()));
                item.add(new Label("project", new PropertyModel<String>(item.getModel(), "fullNameWithCustomer")));
            }
        };
    }

    /**
     * Created on Feb 18, 2009, 5:39:23 PM
     *
     * @author Thies Edeling (thies@te-con.nl)
     */
    private static class SelectionForm extends Form<ReportCriteria> {
        private static final long serialVersionUID = -8232635495078008621L;

        public SelectionForm(String id, IModel<ReportCriteria> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit() {
            ReportCriteria criteria = getModelObject();

            ExportType type = (ExportType) criteria.getUserCriteria().getCustomParameters().get(ExportCriteriaParameter.EXPORT_TYPE);

            if (type == ExportType.EXCEL) {
                excelExport(criteria);
            } else if (type == ExportType.PRINT) {
                setResponsePage(new PrintMonth(criteria));
            }
        }

        private void excelExport(final ReportCriteria criteria) {
            final ExportReportExcel exportReportExcel = new ExportReportExcel();

            getRequestCycle().scheduleRequestHandlerAfterCurrent(new ExcelRequestHandler(exportReportExcel.getFilename(), new Function<byte[]>() {
                @Override
                public byte[] apply() {

                    return exportReportExcel.getExcelData(criteria);
                }
            }));
        }
    }
}
