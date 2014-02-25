/*
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

import com.google.common.collect.Lists;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.Sort;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
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
import net.rrm.ehour.ui.common.wicket.Container;
import net.rrm.ehour.ui.report.panel.criteria.quick.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

import static net.rrm.ehour.report.criteria.UserSelectedCriteria.ReportType;

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
    private static final String CUSTOMER_FILTER_INPUT_ID = "#customerFilterInput";
    private static final String PROJECT_FILTER_INPUT_ID = "#projectFilterInput";
    private static final String DEPARTMENT_FILTER_INPUT_ID = "#departmentFilterInput";
    private static final String USER_FILTER_INPUT_ID = "#userFilterInput";

    @SpringBean
    private ReportCriteriaService reportCriteriaService;

    private LocalizedDatePicker startDatePicker;
    private LocalizedDatePicker endDatePicker;
    private ListMultipleChoice<Project> projects;
    private ListMultipleChoice<Customer> customers;
    private ListMultipleChoice<User> users;
    private List<WebMarkupContainer> quickSelections;
    private ListMultipleChoice<UserDepartment> departments;
    private WebMarkupContainer lockedDateSelection;

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

        ReportCriteriaBackingBean criteriaBackingBean = model.getObject();
        addCustomerSelection(criteriaBackingBean, blueBorder);
        addProjectSelection(criteriaBackingBean, blueBorder);

        UserSelectedCriteria userSelectedCriteria = criteriaBackingBean.getReportCriteria().getUserSelectedCriteria();
        boolean showDepartmentAndOtherUsers = userSelectedCriteria.isForGlobalReport() || userSelectedCriteria.isForPm();
        form.add(showDepartmentAndOtherUsers ? addDepartmentsAndUsers(criteriaBackingBean, ID_USERDEPT_PLACEHOLDER) : new PlaceholderPanel(ID_USERDEPT_PLACEHOLDER));

        addSubmitButtons(form);

        form.add(addReportTypeSelection("reportRole", userSelectedCriteria));
    }

    private WebMarkupContainer addReportTypeSelection(String id, final UserSelectedCriteria criteria) {
        List<ReportType> types = determineReportRoleTypes();

        if (types.size() > 1) {
            final DropDownChoice<ReportType> choice = new DropDownChoice<ReportType>(id, new PropertyModel<ReportType>(criteria, "selectedReportType"), types, new IChoiceRenderer<ReportType>() {
                @Override
                public Object getDisplayValue(ReportType object) {

                    switch (object) {
                        case REPORT:
                            return new ResourceModel("report.useRole.global").wrapOnAssignment(ReportCriteriaPanel.this).getObject();
                        case PM:
                            return new ResourceModel("report.useRole.pm").wrapOnAssignment(ReportCriteriaPanel.this).getObject();
                        default:
                            return new ResourceModel("report.useRole.single").wrapOnAssignment(ReportCriteriaPanel.this).getObject();
                    }
                }

                @Override
                public String getIdValue(ReportType object, int index) {
                    return Integer.toString(index);
                }
            });

            choice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    ReportType type = choice.getModelObject();
                    switch (type) {
                        case REPORT:
                            criteria.setReportTypeToGlobal();
                            break;
                        case PM:
                            criteria.setReportTypeToPM(getEhourWebSession().getUser());
                            break;
                        case INDIVIDUAL_USER:
                        default:
                            criteria.setReportTypeToIndividualUser(getEhourWebSession().getUser());
                            break;
                    }

                    send(getPage(), Broadcast.EXACT, new ReportTypeChangeEvent(target, type));
                }
            });

            return choice;
        } else {
            WebMarkupContainer container = new WebMarkupContainer(id);
            container.setVisible(false);
            return container;
        }
    }

    public static class ReportTypeChangeEvent {
        public final AjaxRequestTarget target;
        public final ReportType reportType;

        public ReportTypeChangeEvent(AjaxRequestTarget target, ReportType reportType) {
            this.target = target;
            this.reportType = reportType;
        }
    }

    private List<ReportType> determineReportRoleTypes() {
        List<ReportType> types = Lists.newArrayList();

        EhourWebSession session = getEhourWebSession();

        if (session.isWithReportRole()) {
            types.add(ReportType.REPORT);
        }

        if (session.isWithPmRole()) {
            types.add(ReportType.PM);
        }

        types.add(ReportType.INDIVIDUAL_USER);
        return types;
    }

    private void addCustomerSelection(final ReportCriteriaBackingBean bean, WebMarkupContainer parent) {
        customers = new ListMultipleChoice<Customer>("reportCriteria.userSelectedCriteria.customers",
                new PropertyModel<Collection<Customer>>(bean, "reportCriteria.userSelectedCriteria.customers"),
                new PropertyModel<List<Customer>>(bean, "reportCriteria.availableCriteria.customers"),
                new DomainObjectChoiceRenderer<Customer>());
        customers.setMarkupId("customerSelect");
        customers.setMaxRows(MAX_CRITERIA_ROW);

        customers.setOutputMarkupId(true);

        customers.add(new AjaxFormComponentUpdatingBehavior("change") {
            private static final long serialVersionUID = -5588313671121851508L;

            protected void onUpdate(AjaxRequestTarget target) {
                // show only projects for selected customers
                List<Customer> preCustomers = Lists.newArrayList(bean.getReportCriteria().getAvailableCriteria().getCustomers());

                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);

                List<Customer> postCustomers = bean.getReportCriteria().getAvailableCriteria().getCustomers();

                if (!preCustomers.containsAll(postCustomers)) {
                    updateCustomers(target);
                }
                updateProjects(target);
            }
        });

        parent.add(customers);

        final AjaxCheckBox deactivateBox = new AjaxCheckBox("reportCriteria.userSelectedCriteria.onlyActiveCustomers") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateCustomersAndProjects(target);
            }
        };
        deactivateBox.setOutputMarkupId(true);
        parent.add(deactivateBox);

        final DropDownChoice<Sort> customerSort = createCustomerSort();
        parent.add(customerSort);

        parent.add(new AjaxLink<Void>("clearCustomer") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                bean.getReportCriteria().getUserSelectedCriteria().resetCustomerSelection();

                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);

                updateCustomers(target);
                updateProjects(target);

                target.appendJavaScript(getCustomerFilterClearScript());

                target.add(customerSort);
                target.add(deactivateBox);
            }
        });
    }

    private DropDownChoice<Sort> createCustomerSort() {
        DropDownChoice<Sort> customerSort = new DropDownChoice<Sort>("customerSort", new PropertyModel<Sort>(getPanelModelObject(), "reportCriteria.userSelectedCriteria.customerSort"), Arrays.asList(Sort.values()), new SortRenderer());
        customerSort.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                ReportCriteria reportCriteria = getPanelModelObject().getReportCriteria();
                reportCriteria.updateCustomerSort();

                target.add(customers);
                applyClientSideCustomerFilter(target);
            }
        });

        customerSort.setOutputMarkupId(true);

        return customerSort;
    }

    private void addProjectSelection(final ReportCriteriaBackingBean bean, WebMarkupContainer parent) {
        projects = new ListMultipleChoice<Project>("reportCriteria.userSelectedCriteria.projects",
                new PropertyModel<List<Project>>(bean, "reportCriteria.availableCriteria.projects"),
                new DomainObjectChoiceRenderer<Project>());
        projects.setMaxRows(MAX_CRITERIA_ROW);
        projects.setOutputMarkupId(true);
        projects.setMarkupId("projectSelect");

        projects.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

            }
        });

        parent.add(projects);

        final AjaxCheckBox onlyActiveCheckbox = new AjaxCheckBox("reportCriteria.userSelectedCriteria.onlyActiveProjects") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);
                updateCustomers(target);
                updateProjects(target);
            }
        };
        onlyActiveCheckbox.setOutputMarkupId(true);
        parent.add(onlyActiveCheckbox);

        final AjaxCheckBox billableCheckbox = new AjaxCheckBox("reportCriteria.userSelectedCriteria.onlyBillableProjects", new PropertyModel<Boolean>(ReportCriteriaPanel.this.getDefaultModel(), "reportCriteria.userSelectedCriteria.onlyBillableProjects")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);
                updateProjects(target);
            }
        };

        billableCheckbox.setMarkupId("reportCriteria.userSelectedCriteria.onlyBillableProjects");
        billableCheckbox.setOutputMarkupId(true);
        parent.add(billableCheckbox);

        final DropDownChoice<Sort> projectSort = createProjectSort();
        parent.add(projectSort);

        parent.add(new AjaxLink<Void>("clearProject") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                bean.getReportCriteria().getUserSelectedCriteria().resetProjectSelection();

                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);

                updateCustomers(target);
                updateProjects(target);

                target.appendJavaScript(getProjectFilterClearScript());

                target.add(projectSort);
                target.add(billableCheckbox);
                target.add(onlyActiveCheckbox);
            }
        });
    }

    private DropDownChoice<Sort> createProjectSort() {
        DropDownChoice<Sort> projectSort = new DropDownChoice<Sort>("projectSort", new PropertyModel<Sort>(getPanelModelObject(), "reportCriteria.userSelectedCriteria.projectSort"), Arrays.asList(Sort.values()), new SortRenderer());
        projectSort.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                ReportCriteria reportCriteria = getPanelModelObject().getReportCriteria();
                reportCriteria.updateProjectSort();

                target.add(projects);
                applyClientSideProjectFilter(target);
            }
        });

        projectSort.setOutputMarkupId(true);

        return projectSort;
    }

    private void updateCustomersAndProjects(AjaxRequestTarget target) {
        updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS_AND_PROJECTS);

        updateProjects(target);
        updateCustomers(target);
    }

    private void updateProjects(AjaxRequestTarget target) {
        target.add(projects);
        applyClientSideProjectFilter(target);
    }

    private void applyClientSideProjectFilter(AjaxRequestTarget target) {
        target.appendJavaScript(getProjectFilterRegistrationScript());
    }

    private void updateCustomers(AjaxRequestTarget target) {
        target.add(customers);
        applyClientSideCustomerFilter(target);
    }

    private void applyClientSideCustomerFilter(AjaxRequestTarget target) {
        target.appendJavaScript(getCustomerFilterRegistrationScript());
    }

    private void addUserSelection(final ReportCriteriaBackingBean bean, WebMarkupContainer parent) {
        users = new ListMultipleChoice<User>("reportCriteria.userSelectedCriteria.users",
                new PropertyModel<List<User>>(getDefaultModel(), "reportCriteria.availableCriteria.users"),
                new DomainObjectChoiceRenderer<User>());
        users.setMarkupId("userSelect");
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

                // reapply the filter to the possible new contents of the dropdowns
                target.appendJavaScript(getUserFilterRegistrationScript());
            }
        };

        parent.add(deactivateBox);

        Label filterToggleText = new Label("onlyActiveUsersLabel", new ResourceModel("report.onlyActiveUsers"));
        parent.add(filterToggleText);

        parent.add(new AjaxLink<Void>("clearUser") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                bean.getReportCriteria().getUserSelectedCriteria().resetUserSelection();

                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_USERS_AND_DEPTS);

                updateUsers(target);
                updateDepartments(target);

                target.appendJavaScript(getUserFilterClearScript());
            }
        });
    }

    private void updateDepartments(AjaxRequestTarget target) {
        target.add(departments);
        applyClientSideDepartmentFilter(target);
    }

    private void applyClientSideDepartmentFilter(AjaxRequestTarget target) {
        target.appendJavaScript(getDepartmentFilterRegistrationScript());
    }

    private void updateUsers(AjaxRequestTarget target) {
        target.add(users);
        applyClientSideUserFilter(target);
    }

    private void applyClientSideUserFilter(AjaxRequestTarget target) {
        target.appendJavaScript(getUserFilterRegistrationScript());
    }

    private Fragment addDepartmentsAndUsers(ReportCriteriaBackingBean bean, String id) {
        Fragment fragment = new Fragment(id, "userDepartmentCriteria", this);

        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("deptUserBorder");
        fragment.add(blueBorder);

        addUserDepartmentSelection(bean, blueBorder);
        addUserSelection(bean, blueBorder);

        return fragment;
    }

    private void addUserDepartmentSelection(final ReportCriteriaBackingBean bean, WebMarkupContainer parent) {
        departments = new ListMultipleChoice<UserDepartment>("reportCriteria.userSelectedCriteria.userDepartments",
                new PropertyModel<List<UserDepartment>>(getDefaultModel(), "reportCriteria.availableCriteria.userDepartments"),
                new DomainObjectChoiceRenderer<UserDepartment>());
        departments.setMarkupId("departmentSelect");
        departments.setMaxRows(MAX_CRITERIA_ROW);
        departments.setOutputMarkupId(true);

        // update projects when customer(s) selected
        departments.add(new AjaxFormComponentUpdatingBehavior("change") {
            private static final long serialVersionUID = 1L;

            protected void onUpdate(AjaxRequestTarget target) {
                // show only projects for selected customers
                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_USERS_AND_DEPTS);
                target.add(users);
            }
        });

        parent.add(departments);

        parent.add(new AjaxLink<Void>("clearDepartment") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                bean.getReportCriteria().getUserSelectedCriteria().resetUserDepartmentSelection();

                updateReportCriteria(ReportCriteriaUpdateType.UPDATE_USERS_AND_DEPTS);

                updateUsers(target);
                updateDepartments(target);

                target.appendJavaScript(getDepartmentFilterClearScript());
            }
        });
    }

    private void updateReportCriteria(ReportCriteriaUpdateType updateType) {
        ReportCriteriaBackingBean backingBean = getPanelModelObject();

        ReportCriteria reportCriteria = reportCriteriaService.syncUserReportCriteria(backingBean.getReportCriteria(), updateType);
        backingBean.getReportCriteria().setAvailableCriteria(reportCriteria.getAvailableCriteria());
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

        quickSelections.add(createQuickWeek());
        quickSelections.add(createQuickMonth());
        quickSelections.add(createQuickQuarter());

        for (WebMarkupContainer cont : quickSelections) {
            cont.setOutputMarkupId(true);
            form.add(cont);
        }

        lockedDateSelection = createLockedPeriodRangeDropdown("lockedDate");
        form.add(lockedDateSelection);
    }

    private WebMarkupContainer createLockedPeriodRangeDropdown(String id) {
        ReportCriteriaBackingBean panelModelObject = getPanelModelObject();

        List<TimesheetLock> locks = panelModelObject.getReportCriteria().getAvailableCriteria().getTimesheetLocks();

        if (locks == null || locks.isEmpty()) {
            Container placeholder = new Container(id);
            placeholder.setVisible(false);
            return placeholder;
        }
        return new DateDropDownChoice<TimesheetLock>(id, new PropertyModel<TimesheetLock>(getPanelModel(), "reportForLock"),
                locks,
                new IChoiceRenderer<TimesheetLock>() {
                    @Override
                    public Object getDisplayValue(TimesheetLock object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(TimesheetLock object, int index) {
                        return Integer.toString(index);
                    }
                });

    }

    private LocalizedDatePicker createDatePicker(String id, IModel<Date> model) {
        LocalizedDatePicker datePicker = new LocalizedDatePicker(id, model);

        datePicker.setOutputMarkupId(true);
        datePicker.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = -5588313671121851508L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                ReportCriteriaBackingBean backingBean = ReportCriteriaPanel.this.getPanelModelObject();
                backingBean.resetQuickSelections();

                for (WebMarkupContainer cont : quickSelections) {
                    target.add(cont);
                }
            }
        });

        return datePicker;
    }

    private WebMarkupContainer createQuickWeek() {
        List<QuickWeek> weeks = new ArrayList<QuickWeek>();
        Calendar currentDate = new GregorianCalendar();
        int currentWeek = -AMOUNT_OF_QUICKWEEKS;

        EhourConfig config = EhourWebSession.getEhourConfig();

        currentDate.setFirstDayOfWeek(config.getFirstDayOfWeek());
        currentDate.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
        currentDate.add(Calendar.WEEK_OF_YEAR, currentWeek);

        for (; currentWeek < AMOUNT_OF_QUICKWEEKS; currentWeek++) {
            weeks.add(new QuickWeek(currentDate, config));

            currentDate.add(Calendar.WEEK_OF_YEAR, 1);
        }

        return new DateDropDownChoice<QuickWeek>("quickWeek", weeks, new QuickWeekRenderer(config));
    }

    /**
     * Add quick month selection
     */
    private WebMarkupContainer createQuickMonth() {
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

        return new DateDropDownChoice<QuickMonth>("quickMonth", months, new QuickMonthRenderer());
    }


    private WebMarkupContainer createQuickQuarter() {
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

        return new DateDropDownChoice<QuickQuarter>("quickQuarter", quarters, new QuickQuarterRenderer());
    }

    @Override
    public void onEvent(IEvent<?> event) {
        Object payload = event.getPayload();

        if (payload instanceof DateDropDownChoice.DateChangedPayload) {
            updateDates(((DateDropDownChoice.DateChangedPayload) payload).getTarget());
        }
    }

    private void updateDates(AjaxRequestTarget target) {
        target.add(startDatePicker);
        target.add(endDatePicker);
        target.add(lockedDateSelection);

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
        response.render(OnDomReadyHeaderItem.forScript(getDepartmentFilterRegistrationScript()));
        response.render(OnDomReadyHeaderItem.forScript(getUserFilterRegistrationScript()));
    }

    private String getCustomerFilterRegistrationScript() {
        return String.format("initFilter('#customerSelect', '%s')", CUSTOMER_FILTER_INPUT_ID);
    }

    private String getProjectFilterRegistrationScript() {
        return String.format("initFilter('#projectSelect', '%s')", PROJECT_FILTER_INPUT_ID);
    }

    private CharSequence getDepartmentFilterRegistrationScript() {
        return String.format("initFilter('#departmentSelect', '%s')", DEPARTMENT_FILTER_INPUT_ID);
    }

    private CharSequence getUserFilterRegistrationScript() {
        return String.format("initFilter('#userSelect', '%s')", USER_FILTER_INPUT_ID);
    }

    private String getCustomerFilterClearScript() {
        return getFilterClearScript(CUSTOMER_FILTER_INPUT_ID);
    }

    private String getProjectFilterClearScript() {
        return getFilterClearScript(PROJECT_FILTER_INPUT_ID);
    }

    private String getDepartmentFilterClearScript() {
        return getFilterClearScript(DEPARTMENT_FILTER_INPUT_ID);
    }

    private String getUserFilterClearScript() {
        return getFilterClearScript(USER_FILTER_INPUT_ID);
    }

    private String getFilterClearScript(String inputId) {
        return String.format("clearFilter('%s');", inputId);
    }
}