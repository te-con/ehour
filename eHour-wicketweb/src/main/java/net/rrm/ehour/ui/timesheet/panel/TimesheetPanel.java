package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.form.FormHighlighter;
import net.rrm.ehour.ui.common.formguard.GuardedAjaxLink;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.common.TimesheetAjaxEventType;
import net.rrm.ehour.ui.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.timesheet.model.PersistableTimesheetModel;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import net.rrm.ehour.ui.timesheet.model.TimesheetModel;
import net.rrm.ehour.ui.timesheet.panel.renderer.SectionRenderFactory;
import net.rrm.ehour.ui.timesheet.panel.renderer.SectionRenderFactoryCollection;
import net.rrm.ehour.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TimesheetPanel extends AbstractBasePanel<TimesheetContainer> {
    private static final long serialVersionUID = 7704288648724599187L;

    private static final JavaScriptResourceReference GUARDFORM_JS = new JavaScriptResourceReference(TimesheetPanel.class, "guardform.js");
    private static final JavaScriptResourceReference TIMESHEET_JS = new JavaScriptResourceReference(TimesheetPanel.class, "timesheet.js");
    private static final CssResourceReference TIMESHEET_CSS = new CssResourceReference(TimesheetPanel.class, "css/timesheetForm.css");
    public static final String SERVER_MESSAGE_ID = "serverMessage";

    private static final Logger LOGGER = Logger.getLogger(TimesheetPanel.class);

    private EhourConfig config;
    private Component serverMsgLabel;
    private Form<TimesheetContainer> timesheetForm;

    @SpringBean
    private PersistableTimesheetModel<TimesheetContainer> model;

    @SpringBean
    private SectionRenderFactoryCollection sectionFactory;

    @SpringBean
    private TimesheetLockService timesheetLockService;

    public TimesheetPanel(String id, User user, Calendar forWeek) {
        super(id);

        config = EhourWebSession.getEhourConfig();

        this.setOutputMarkupId(true);

        // set the model
        model.init(user, forWeek);
        setDefaultModel(model);

        Timesheet timesheet = model.getObject().getTimesheet();

        // grey & blue frame border
        WebMarkupContainer weekNavigation = getWeekNavigation(timesheet.getWeekStart(), timesheet.getWeekEnd());

        CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("timesheetFrame", weekNavigation);
        add(greyBorder);

        this.timesheetForm = buildForm(model);
        greyBorder.add(timesheetForm);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(GUARDFORM_JS));
        response.render(CssHeaderItem.forReference(TIMESHEET_CSS));

        String msg = new ResourceModel("timesheet.dirtyForm").getObject();
        String escapedMsg = msg.replace("'", "\\\'");

        response.render(JavaScriptHeaderItem.forScript(String.format("var WARNING_MSG = '%s';", escapedMsg), "msg"));

        response.render(JavaScriptHeaderItem.forReference(TIMESHEET_JS));
        response.render(OnDomReadyHeaderItem.forScript("window.timesheet = new Timesheet();window.timesheet.init();"));
    }

    private RepeatingView renderSections() {
        RepeatingView options = new RepeatingView("sections");

        for (SectionRenderFactory renderFactory : sectionFactory.getRenderFactories()) {
            options.add(renderFactory.renderForId(options.newChildId(), getPanelModel()));
        }

        return options;
    }

    /**
     * Add week navigation to title
     */
    @SuppressWarnings("serial")
    private WebMarkupContainer getWeekNavigation(final Date weekStart, final Date weekEnd) {
        Fragment titleFragment = new Fragment("title", "title", TimesheetPanel.this);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", config.getFormattingLocale());

        int weekOfYear = DateUtil.getWeekNumberForDate(weekStart, config.getFirstDayOfWeek());

        IModel<String> weekLabelModel = new MessageResourceModel("timesheet.weekTitle", this, weekOfYear, dateFormatter.format(weekStart), dateFormatter.format(weekEnd));

        titleFragment.add(new Label("titleLabel", weekLabelModel));

        GuardedAjaxLink<Void> previousWeekLink = new GuardedWeekLink("previousWeek", weekStart, -1);
        titleFragment.add(previousWeekLink);

        GuardedAjaxLink<Void> nextWeekLink = new GuardedWeekLink("nextWeek", weekStart, 1);
        titleFragment.add(nextWeekLink);

        return titleFragment;
    }

    private void addGrandTotals(WebMarkupContainer parent, GrandTotal grandTotals, Date weekStart) {
        Calendar dateIterator = new GregorianCalendar();
        dateIterator.setTime(weekStart);

        for (int i = 1; i <= 7; i++, dateIterator.add(Calendar.DAY_OF_YEAR, 1)) {
            final int index = dateIterator.get(Calendar.DAY_OF_WEEK) - 1;

            Label total = new Label("day" + i + "Total", new PropertyModel<Float>(grandTotals, "getValues[" + index + "]")) {
                @Override
                public void onEvent(IEvent<?> event) {
                    if (event.getPayload() instanceof TimesheetInputModifiedEvent) {
                        TimesheetInputModifiedEvent payload = (TimesheetInputModifiedEvent) event.getPayload();

                        if (payload.getForDayOfWeek() == index) {
                            payload.getTarget().add(this);
                        }
                    }
                }
            };

            total.setOutputMarkupId(true);
            parent.add(total);
        }

        Label grandTotal = new Label("grandTotal", new PropertyModel<Float>(grandTotals, "grandTotal")) {
            @Override
            public void onEvent(IEvent<?> event) {
                if (event.getPayload() instanceof TimesheetInputModifiedEvent) {
                    TimesheetInputModifiedEvent payload = (TimesheetInputModifiedEvent) event.getPayload();
                    payload.getTarget().add(this);
                }
            }
        };

        grandTotal.setOutputMarkupId(true);
        parent.add(grandTotal);
    }

    private void setSubmitActions(Form<?> form, MarkupContainer parent, final Timesheet timesheet) {
        // default submit
        SubmitButton submitButton = new SubmitButton("submitButton", form, timesheet);
        submitButton.setOutputMarkupId(true);
        submitButton.setMarkupId("submit");
        parent.add(submitButton);

        // reset, should fetch the original contents
        AjaxButton resetButton = new AjaxButton("resetButton", form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                // basically fake a week click
                EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.WEEK_NAV));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                // reset doesn't error
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                List<IAjaxCallListener> callListeners = attributes.getAjaxCallListeners();

                callListeners.add(new JavaScriptConfirmation(new ResourceModel("timesheet.confirmReset")));
                callListeners.add(new LoadingSpinnerDecorator());
            }


            @Override
            public boolean isVisible() {
                return !timesheet.isAllLocked();
            }
        };

        resetButton.setDefaultFormProcessing(false);
        parent.add(resetButton);
    }

    private void addFailedProjectMessages(List<ProjectAssignmentStatus> failedProjects, final AjaxRequestTarget target) {
        getPanelModelObject().getTimesheet().updateFailedProjects(failedProjects);

        timesheetForm.visitChildren(Label.class, new IVisitor<Label, Void>() {
            @Override
            public void component(Label label, IVisit visit) {
                if ("status".equals(label.getId())) {
                    label.setVisible(true);
                    target.add(label);
                }
            }
        });
    }

    /**
     * Set message that the hours are saved
     */
    private Label updatePostPersistMessage() {
        // server message
        IModel<String> serverMsg = new StringResourceModel("timesheet.weekSaved",
                TimesheetPanel.this,
                null,
                new PropertyModel<Date>(getDefaultModel(), "timesheet.totalBookedHours"),
                new DateModel(new PropertyModel<Date>(getDefaultModel(), "timesheet.weekStart"), config, DateModel.DATESTYLE_FULL_SHORT),
                new DateModel(new PropertyModel<Date>(getDefaultModel(), "timesheet.weekEnd"), config, DateModel.DATESTYLE_FULL_SHORT));

        Label label = new Label(SERVER_MESSAGE_ID, serverMsg);
        label.add(AttributeModifier.replace("style", "timesheetPersisted"));
        label.setOutputMarkupId(true);
        serverMsgLabel.replaceWith(label);
        serverMsgLabel = label;
        return label;

    }

    private WebMarkupContainer updateErrorMessage(String msgModel) {
        IModel<String> model = new MessageResourceModel(msgModel, TimesheetPanel.this);

        Fragment fragment = new Fragment(SERVER_MESSAGE_ID, "persistenceError", this);
        fragment.add(new Label("msg", model));
        fragment.add(AttributeModifier.replace("style", "padding: 5px 10px"));
        fragment.add(AttributeModifier.replace("class", "warningBanner"));

        fragment.setOutputMarkupId(true);

        serverMsgLabel.replaceWith(fragment);
        serverMsgLabel = fragment;
        return fragment;
    }

    /**
     * Add date labels (sun/mon etc)
     */
    private void addDateLabels(WebMarkupContainer parent) {
        Timesheet timesheet = getPanelModelObject().getTimesheet();

        for (int i = 1, j = 0; i <= 7; i++, j++) {
            String id = "day" + i + "Label";

            Fragment headerFragment = new Fragment(id, "dayHeader", TimesheetPanel.this);

            PropertyModel<Date> model = new PropertyModel<>(getDefaultModelObject(), "timesheet.dateSequence[" + j + "]");
            headerFragment.add(new Label("weekDay", new DateModel(model, config, DateModel.DATESTYLE_TIMESHEET_DAYONLY)));
            headerFragment.add(new Label("day", new DateModel(model, config, DateModel.DATESTYLE_DAYONLY)));

            Fragment lockFragment;
            if (timesheet.isLocked(j)) {
                lockFragment = new Fragment("lock", "lockedDay", this);

                WebMarkupContainer container = new WebMarkupContainer("lockedContainer");
                container.add(AttributeModifier.replace("title", new MessageResourceModel("timesheet.daylocked", TimesheetPanel.this)));

                lockFragment.add(container);
            } else {
                lockFragment = new Fragment("lock", "unlockedDay", this);
            }

            headerFragment.add(lockFragment);
            parent.add(headerFragment);
        }
    }

    /**
     * Move to next week after succesfull form submit or week navigation
     */
    private void moveWeek(Date onScreenDate, int weekDiff) {
        EhourWebSession session = EhourWebSession.getSession();
        Calendar cal = DateUtil.getCalendar(config);

        cal.setTime(onScreenDate);
        cal.add(Calendar.WEEK_OF_YEAR, weekDiff);

        // should update calendar as well
        session.setNavCalendar(cal);

        EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.WEEK_NAV));
    }

    @SuppressWarnings("unchecked")
    private List<ProjectAssignmentStatus> persistTimesheetEntries() throws TimesheetModel.UnknownPersistenceException {
        return ((PersistableTimesheetModel) getPanelModel()).persist();
    }

    private Form<TimesheetContainer> buildForm(IModel<TimesheetContainer> timesheetModel) {
        // add form
        Form<TimesheetContainer> timesheetForm = new Form<>("timesheetForm");
        timesheetForm.setMarkupId("timesheetForm");
        timesheetForm.setOutputMarkupId(true);
        timesheetForm.setModel(timesheetModel);

        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
        timesheetForm.add(blueBorder);

        // setup form
        GrandTotal grandTotals = buildForm(timesheetForm, blueBorder);

        // add last row with grand totals
        Timesheet timesheet = timesheetModel.getObject().getTimesheet();
        addGrandTotals(blueBorder, grandTotals, timesheet.getWeekStart());

        // add label dates
        addDateLabels(blueBorder);

        // attach onsubmit ajax events
        setSubmitActions(timesheetForm, timesheetForm, timesheet);

        SubmitButton submitButtonTop = new SubmitButton("submitButtonTop", timesheetForm, timesheet);
        submitButtonTop.setOutputMarkupId(true);
        submitButtonTop.setMarkupId("submitButtonTop");
        blueBorder.add(submitButtonTop);

        // server message
        serverMsgLabel = new WebComponent(SERVER_MESSAGE_ID);
        serverMsgLabel.setOutputMarkupId(true);
        timesheetForm.add(serverMsgLabel);

        timesheetForm.add(renderSections());

        return timesheetForm;
    }


    private GrandTotal buildForm(final Form<?> form, WebMarkupContainer parent) {
        final GrandTotal grandTotals = new GrandTotal();

        ListView<Customer> customers = new ListView<Customer>("customers", new PropertyModel<List<Customer>>(getDefaultModelObject(), "timesheet.customerList")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Customer> item) {
                final Customer customer = item.getModelObject();

                Timesheet timesheet = TimesheetPanel.this.getPanelModelObject().getTimesheet();
                item.add(new Label("customer", customer.getName()));

                item.add(createTimesheetRows("rows", grandTotals, form, timesheet.getTimesheetRows(customer)));
            }
        };
        customers.setReuseItems(true);

        parent.add(customers);

        return grandTotals;
    }

    protected TimesheetRowList createTimesheetRows(String id, GrandTotal grandTotals, Form<?> form, List<TimesheetRow> rows) {
        return new TimesheetRowList(id, rows, grandTotals, getPanelModel(), form, TimesheetPanel.this);
    }

    private class SubmitButton extends AjaxButton {

        private static final long serialVersionUID = 1L;
        private final Timesheet timesheet;

        public SubmitButton(String id, Form<?> form, Timesheet timesheet) {
            super(id, form);
            this.timesheet = timesheet;
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            try {
                List<ProjectAssignmentStatus> failedProjects = persistTimesheetEntries();

                if (failedProjects.isEmpty()) {
                    target.add(updatePostPersistMessage());
                } else {
                    target.add(updateErrorMessage("timesheet.errorPersist"));
                }

                addFailedProjectMessages(failedProjects, target);

                EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.TIMESHEET_SUBMIT));
            } catch (TimesheetModel.UnknownPersistenceException e) {
                LOGGER.error("Failed to persist", e);
                target.add(updateErrorMessage("timesheet.generalPersistenceError"));
            }
        }

        @Override
        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
            super.updateAjaxAttributes(attributes);

            attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
        }

        @Override
        protected void onError(final AjaxRequestTarget target, Form<?> form) {
            form.visitFormComponents(new FormHighlighter(target));
        }

        @Override
        public boolean isVisible() {
            return !timesheet.isAllLocked();
        }
    }

    private class GuardedWeekLink extends GuardedAjaxLink<Void> {
        private int delta;
        private Date weekStart;

        private GuardedWeekLink(String id, Date weekStart, int delta) {
            super(id);
            this.delta = delta;
            this.weekStart = weekStart;
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            moveWeek(weekStart, delta);
        }
    }
}
