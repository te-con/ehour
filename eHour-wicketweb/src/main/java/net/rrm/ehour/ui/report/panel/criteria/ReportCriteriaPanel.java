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

package net.rrm.ehour.ui.report.panel.criteria;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.Sort;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.panel.datepicker.LocalizedDatePicker;
import net.rrm.ehour.ui.common.renderers.DomainObjectChoiceRenderer;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.report.panel.criteria.quick.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

/**
 * Base report criteria panel which adds the quick date selections
 */

public class ReportCriteriaPanel extends AbstractAjaxPanel<ReportCriteriaBackingBean> {
    private static final long serialVersionUID = 161160822264046559L;

    private static final int MAX_CRITERIA_ROW = 6;
    private static final JavaScriptResourceReference CRITERIA_JS = new JavaScriptResourceReference(ReportCriteriaPanel.class, "report_criteria.js");
    private static final CssResourceReference CRITERIA_CSS = new CssResourceReference(ReportCriteriaPanel.class, "report_criteria.css");

    private static final String ID_USERDEPT_PLACEHOLDER = "userDepartmentPlaceholder";

    public static final int AMOUNT_OF_QUICKWEEKS = 8;
    public static final int AMOUNT_OF_QUICKMONTHS = 6;
    public static final int AMOUNT_OF_QUICKQUARTERS = 3;

    @SpringBean
    private ReportCriteriaService reportCriteriaService;

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ListMultipleChoice<Project> projects;
    private ListMultipleChoice<Customer> customers;
    private ListMultipleChoice<User> users;
    private List<WebMarkupContainer> quickSelections;
    private ListMultipleChoice<UserDepartment> departments;

    public ReportCriteriaPanel(String id, IModel<ReportCriteriaBackingBean> model) {
        super(id, model);

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", WebGeo.W_FULL);
        add(greyBorder);

        setOutputMarkupId(true);

        Form<ReportCriteriaBackingBean> form = new Form<ReportCriteriaBackingBean>("criteriaForm");
        form.setOutputMarkupId(true);
        greyBorder.add(form);

        addDates(form, model);

        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("customerProjectsBorder");
        form.add(blueBorder);

        addCustomerSelection(model.getObject(), blueBorder);
        addProjectSelection(blueBorder);

        boolean showDepartmentAndOtherUsers = getEhourWebSession().isWithReportRole() || getEhourWebSession().isWithPmRole();
        form.add(showDepartmentAndOtherUsers ? addDepartmentsAndUsers(ID_USERDEPT_PLACEHOLDER) : new PlaceholderPanel(ID_USERDEPT_PLACEHOLDER));

        addSubmitButtons(form);
    }

    private void addCustomerSelection(ReportCriteriaBackingBean bean, WebMarkupContainer parent) {
        customers = new ListMultipleChoice<Customer>("reportCriteria.userSelectedCriteria.customers",
                new PropertyModel<List<Customer>>(bean, "reportCriteria.availableCriteria.customers"),
                new DomainObjectChoiceRenderer<Customer>());
        customers.setMarkupId("customerSelect");
        customers.setMaxRows(MAX_CRITERIA_ROW);

        customers.setOutputMarkupId(true);
        parent.add(customers);

        parent.add(createOnlyActiveCustomersAndProjectsCheckbox("reportCriteria.userSelectedCriteria.onlyActiveCustomers"));

        parent.add(createCustomerSort());
    }

    private DropDownChoice<Sort> createCustomerSort() {
        DropDownChoice<Sort> customerSort = new DropDownChoice<Sort>("customerSort", new PropertyModel<Sort>(getPanelModelObject(), "reportCriteria.userSelectedCriteria.customerSort"), Arrays.asList(Sort.values()), new SortRenderer());
        customerSort.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                ReportCriteria reportCriteria = ReportCriteriaPanel.this.getBackingBeanFromModel().getReportCriteria();
                reportCriteria.updateCustomerSort();

                target.add(customers);
            }
        });

        return customerSort;
    }

    private void addProjectSelection(WebMarkupContainer parent) {
        projects = new ListMultipleChoice<Project>("reportCriteria.userSelectedCriteria.projects",
                new PropertyModel<List<Project>>(getDefaultModel(), "reportCriteria.availableCriteria.projects"),
                new DomainObjectChoiceRenderer<Project>());
        projects.setMaxRows(MAX_CRITERIA_ROW);
        projects.setOutputMarkupId(true);
        projects.setMarkupId("projectSelect");
        parent.add(projects);

        parent.add(createOnlyActiveCustomersAndProjectsCheckbox("reportCriteria.userSelectedCriteria.onlyActiveProjects"));

        AjaxCheckBox onlyBillableProjectsCheckbox = createOnlyBillableCheckbox("reportCriteria.userSelectedCriteria.onlyBillableProjects");
        parent.add(onlyBillableProjectsCheckbox);

        parent.add(createProjectSort());
    }

    private DropDownChoice<Sort> createProjectSort() {
        DropDownChoice<Sort> projectSort = new DropDownChoice<Sort>("projectSort", new PropertyModel<Sort>(getPanelModelObject(), "reportCriteria.userSelectedCriteria.projectSort"), Arrays.asList(Sort.values()), new SortRenderer());
        projectSort.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                ReportCriteria reportCriteria = ReportCriteriaPanel.this.getBackingBeanFromModel().getReportCriteria();
                reportCriteria.updateProjectSort();

                target.add(projects);
            }
        });

        return projectSort;
    }

    @SuppressWarnings("serial")
    private AjaxCheckBox createOnlyActiveCustomersAndProjectsCheckbox(final String id) {
        AjaxCheckBox deactivateBox = new AjaxCheckBox(id) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateCustomersAndProjects(target);
            }
        };

        deactivateBox.setOutputMarkupId(true);

        return deactivateBox;
    }

    @SuppressWarnings("serial")
    private AjaxCheckBox createOnlyBillableCheckbox(String id) {
        AjaxCheckBox activeBox = new AjaxCheckBox(id, new PropertyModel<Boolean>(getDefaultModel(), "reportCriteria.userSelectedCriteria.onlyBillableProjects")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateCustomersAndProjects(target);
            }
        };

        activeBox.setMarkupId(id);
        return activeBox;
    }

    private void updateCustomersAndProjects(AjaxRequestTarget target) {
        updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);
        target.add(customers);
        target.add(projects);

        target.appendJavaScript(getCustomerFilterRegistrationScript());
        target.appendJavaScript(getProjectFilterRegistrationScript());
    }


    private void addUserSelection(WebMarkupContainer parent) {
        users = new ListMultipleChoice<User>("reportCriteria.userSelectedCriteria.users",
                new PropertyModel<List<User>>(getDefaultModel(), "reportCriteria.availableCriteria.users"),
                new DomainObjectChoiceRenderer<User>());
        users.setOutputMarkupId(true);
        users.setMaxRows(MAX_CRITERIA_ROW);
        parent.add(users);

        // hide active checkbox
        final AjaxCheckBox deactivateBox = new AjaxCheckBox("reportCriteria.userSelectedCriteria.onlyActiveUsers") {
            private static final long serialVersionUID = 2585047163449150793L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_USERS_AND_DEPTS);
                target.add(users);
                target.add(departments);
            }
        };

        parent.add(deactivateBox);

        Label filterToggleText = new Label("onlyActiveUsersLabel", new ResourceModel("report.onlyActiveUsers"));
        parent.add(filterToggleText);
    }

    private void addUserDepartmentSelection(WebMarkupContainer parent) {
        departments = new ListMultipleChoice<UserDepartment>("reportCriteria.userSelectedCriteria.userDepartments",
                new PropertyModel<List<UserDepartment>>(getDefaultModel(), "reportCriteria.availableCriteria.userDepartments"),
                new DomainObjectChoiceRenderer<UserDepartment>());
        departments.setMaxRows(MAX_CRITERIA_ROW);
        departments.setOutputMarkupId(true);

        parent.add(departments);
    }

    private Fragment addDepartmentsAndUsers(String id) {
        Fragment fragment = new Fragment(id, "userDepartmentCriteria", this);

        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("deptUserBorder");
        fragment.add(blueBorder);

        addUserDepartmentSelection(blueBorder);
        addUserSelection(blueBorder);

        return fragment;
    }

    private void updateReportCriteria(ReportCriteriaUpdateType updateType) {
        ReportCriteriaBackingBean backingBean = getBackingBeanFromModel();

        ReportCriteria reportCriteria = reportCriteriaService.syncUserReportCriteria(backingBean.getReportCriteria(), updateType);
        backingBean.setReportCriteria(reportCriteria);
    }

    private ReportCriteriaBackingBean getBackingBeanFromModel() {
        return (ReportCriteriaBackingBean) getDefaultModelObject();
    }

    @SuppressWarnings("serial")
    private void addSubmitButtons(Form<ReportCriteriaBackingBean> form) {
        // Submit
        AjaxButton submitButton = new AjaxButton("createReport", form) {
            @Override
            protected final void onSubmit(AjaxRequestTarget target, Form<?> form) {
                EventPublisher.publishAjaxEvent(this, new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_UPDATED));
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(form);
            }
        };

        // default submit
        form.add(submitButton);

        // reset button
        AjaxButton resetButton = new AjaxButton("resetCriteria") {
            @Override
            protected final void onSubmit(AjaxRequestTarget target, Form<?> form) {
                EventPublisher.publishAjaxEvent(this, new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_RESET));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                onSubmit(target, form);
            }
        };

        resetButton.setDefaultFormProcessing(false);

        form.add(resetButton);
    }

    private void addDates(Form<ReportCriteriaBackingBean> form, IModel<ReportCriteriaBackingBean> model) {
        startDatePicker = createDatePicker("reportCriteria.userSelectedCriteria.reportRange.dateStart", new PropertyModel<Date>(model, "reportCriteria.userSelectedCriteria.reportRange.dateStart"));
        form.add(startDatePicker);

        endDatePicker = createDatePicker("reportCriteria.userSelectedCriteria.reportRange.dateEnd", new PropertyModel<Date>(model, "reportCriteria.userSelectedCriteria.reportRange.dateEnd"));
        form.add(endDatePicker);

        quickSelections = new ArrayList<WebMarkupContainer>();

        quickSelections.add(getQuickWeek());
        quickSelections.add(getQuickMonth());
        quickSelections.add(getQuickQuarter());

        for (WebMarkupContainer cont : quickSelections) {
            form.add(cont);
        }
    }

    private DatePicker createDatePicker(String id, IModel<Date> model) {
        DatePicker datePicker = new LocalizedDatePicker(id, model);

        datePicker.setOutputMarkupId(true);
        datePicker.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = -5588313671121851508L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                AjaxEvent event = new AjaxEvent(QuickDateAjaxEventType.DATE_CHANGED);
                EventPublisher.publishAjaxEvent(ReportCriteriaPanel.this, event);
            }
        });

        return datePicker;
    }

    private WebMarkupContainer getQuickWeek() {
        List<QuickWeek> weeks = new ArrayList<QuickWeek>();
        Calendar currentDate = new GregorianCalendar();
        int currentWeek = -AMOUNT_OF_QUICKWEEKS;

        EhourConfig config = ((EhourWebSession) getSession()).getEhourConfig();

        currentDate.setFirstDayOfWeek(config.getFirstDayOfWeek());
        currentDate.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
        currentDate.add(Calendar.WEEK_OF_YEAR, currentWeek);

        for (; currentWeek < AMOUNT_OF_QUICKWEEKS; currentWeek++) {
            weeks.add(new QuickWeek(currentDate, config));

            currentDate.add(Calendar.WEEK_OF_YEAR, 1);
        }

        return new QuickDropDownChoice<QuickWeek>("quickWeek", weeks, new QuickWeekRenderer(config));
    }

    /**
     * Add quick month selection
     */
    private WebMarkupContainer getQuickMonth() {
        List<QuickMonth> months = new ArrayList<QuickMonth>();
        Calendar currentDate = new GregorianCalendar();
        int currentMonth = -AMOUNT_OF_QUICKMONTHS;

        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        currentDate.setFirstDayOfWeek(Calendar.SUNDAY);
        currentDate.add(Calendar.MONTH, currentMonth);

        for (; currentMonth < AMOUNT_OF_QUICKMONTHS; currentMonth++) {
            months.add(new QuickMonth(currentDate));

            currentDate.add(Calendar.MONTH, 1);
        }

        return new QuickDropDownChoice<QuickMonth>("quickMonth", months, new QuickMonthRenderer());
    }


    private WebMarkupContainer getQuickQuarter() {
        List<QuickQuarter> quarters = new ArrayList<QuickQuarter>();
        Calendar currentDate = new GregorianCalendar();
        int currentQuarter = -AMOUNT_OF_QUICKQUARTERS;

        int quarter = currentDate.get(Calendar.MONTH) / AMOUNT_OF_QUICKQUARTERS;
        currentDate.set(Calendar.MONTH, quarter * AMOUNT_OF_QUICKQUARTERS); // abuse rounding off
        currentDate.add(Calendar.MONTH, currentQuarter * AMOUNT_OF_QUICKQUARTERS);

        for (; currentQuarter < AMOUNT_OF_QUICKQUARTERS; currentQuarter++) {
            quarters.add(new QuickQuarter(currentDate));

            currentDate.add(Calendar.MONTH, AMOUNT_OF_QUICKQUARTERS);
        }

        return new QuickDropDownChoice<QuickQuarter>("quickQuarter", quarters, new QuickQuarterRenderer());
    }

    public final boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        if (ajaxEvent.getEventType() == QuickDateAjaxEventType.DATE_CHANGED) {
            updateDates(ajaxEvent.getTarget());
            updateReportCriteria(ReportCriteriaUpdateType.UPDATE_ALL);
            ajaxEvent.getTarget().add(customers);
            ajaxEvent.getTarget().add(projects);
        }

        return true;
    }

    private void updateDates(AjaxRequestTarget target) {
        target.add(startDatePicker);
        target.add(endDatePicker);

        for (WebMarkupContainer cont : quickSelections) {
            target.add(cont);
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptReferenceHeaderItem.forReference(CRITERIA_JS));
        response.render(CssReferenceHeaderItem.forReference(CRITERIA_CSS));
        response.render(OnDomReadyHeaderItem.forScript(getCustomerFilterRegistrationScript()));
        response.render(OnDomReadyHeaderItem.forScript(getProjectFilterRegistrationScript()));
    }

    private String getCustomerFilterRegistrationScript() {
        return "initFilter('#customerSelect', '#customerFilterInput')";
    }

    private String getProjectFilterRegistrationScript() {
        return "initFilter('#projectSelect', '#projectFilterInput')";
    }
}