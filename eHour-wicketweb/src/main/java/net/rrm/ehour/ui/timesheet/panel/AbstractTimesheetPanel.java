package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.common.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
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
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import net.rrm.ehour.ui.timesheet.model.TimesheetModel;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AbstractTimesheetPanel extends AbstractBasePanel<TimesheetContainer> {
    private static final long serialVersionUID = 7704288648724599187L;

    private static final JavaScriptResourceReference GUARDFORM_JS = new JavaScriptResourceReference(AbstractTimesheetPanel.class, "guardform.js");
    private static final JavaScriptResourceReference TIMESHEET_JS = new JavaScriptResourceReference(AbstractTimesheetPanel.class, "timesheet.js");
    private static final CssResourceReference TIMESHEET_CSS = new CssResourceReference(AbstractTimesheetPanel.class, "css/timesheetForm.css");

    private EhourConfig config;
    private WebComponent serverMsgLabel;
    private Form<TimesheetModel> timesheetForm;

    public AbstractTimesheetPanel(String id, User user, Calendar forWeek) {
        super(id);

        GrandTotal grandTotals;

        config = EhourWebSession.getEhourConfig();

        this.setOutputMarkupId(true);

        // set the model
        TimesheetModel timesheet = new TimesheetModel(user, forWeek);
        setDefaultModel(timesheet);

        // grey & blue frame border
        CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("timesheetFrame",
                getWeekNavigation(timesheet.getWeekStart(), timesheet.getWeekEnd())
        );
        add(greyBorder);

        // add form
        timesheetForm = new Form<TimesheetModel>("timesheetForm");
        timesheetForm.setMarkupId("timesheetForm");
        timesheetForm.setOutputMarkupId(true);
        greyBorder.add(timesheetForm);

        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
        timesheetForm.add(blueBorder);

        // setup form
        grandTotals = buildForm(timesheetForm, blueBorder);

        // add last row with grand totals
        addGrandTotals(blueBorder, grandTotals, timesheet.getWeekStart());

        // add label dates
        addDateLabels(blueBorder);

        // add comments section
        MarkupContainer commentsFrame = createCommentsInput(timesheetForm);

        // attach onsubmit ajax events
        setSubmitActions(timesheetForm, commentsFrame);

        SubmitButton submitButtonTop = new SubmitButton("submitButtonTop", timesheetForm);
        submitButtonTop.setOutputMarkupId(true);
        submitButtonTop.setMarkupId("submitButtonTop");
        blueBorder.add(submitButtonTop);

        // server message
        serverMsgLabel = new WebComponent("serverMessage");
        serverMsgLabel.setOutputMarkupId(true);
        commentsFrame.add(serverMsgLabel);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(GUARDFORM_JS));
        response.render(CssHeaderItem.forReference(TIMESHEET_CSS));

        String msg = new ResourceModel("timesheet.dirtyForm").getObject();
        String escapedMsg = msg.replace("'", "\\\'");

        response.render(JavaScriptHeaderItem.forScript(String.format("var WARNING_MSG = '%s';", escapedMsg), "msg"));

        response.render(JavaScriptHeaderItem.forReference(TIMESHEET_JS));
        response.render(OnDomReadyHeaderItem.forScript("initializeFoldLinks();"));
    }

    /**
     * Add week navigation to title
     */
    @SuppressWarnings("serial")
    private WebMarkupContainer getWeekNavigation(final Date weekStart, final Date weekEnd) {
        Fragment titleFragment = new Fragment("title", "title", AbstractTimesheetPanel.this);
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

    private MarkupContainer createCommentsInput(WebMarkupContainer parent) {
        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("commentsFrame");

        Timesheet timesheet = getPanelModelObject().getTimesheet();

        KeepAliveTextArea textArea = new KeepAliveTextArea("commentsArea", new PropertyModel<String>(timesheet, "comment.comment"));
        textArea.add(CommonModifiers.tabIndexModifier(2));
        blueBorder.add(textArea);
        parent.add(blueBorder);

        return blueBorder;
    }

    private void addGrandTotals(WebMarkupContainer parent, GrandTotal grandTotals, Date weekStart) {
        Label total;

        Calendar dateIterator = new GregorianCalendar();
        dateIterator.setTime(weekStart);

        for (int i = 1; i <= 7; i++, dateIterator.add(Calendar.DAY_OF_YEAR, 1)) {
            total = new Label("day" + i + "Total", new PropertyModel<Float>(grandTotals, "getValues[" + (dateIterator.get(Calendar.DAY_OF_WEEK) - 1) + "]"));
            total.setOutputMarkupId(true);
            parent.add(total);

            grandTotals.addOrder(i, dateIterator.get(Calendar.DAY_OF_WEEK) - 1);
        }

        total = new Label("grandTotal", new PropertyModel<Float>(grandTotals, "grandTotal"));
        total.setOutputMarkupId(true);
        parent.add(total);
    }

    private void setSubmitActions(Form<?> form, MarkupContainer parent) {
        // default submit
        SubmitButton submitButton = new SubmitButton("submitButton", form);
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
        };

        resetButton.setDefaultFormProcessing(false);
        parent.add(resetButton);
    }

    private void addFailedProjectMessages(List<ProjectAssignmentStatus> failedProjects, final AjaxRequestTarget target) {
        getPanelModelObject().getTimesheet().updateFailedProjects(failedProjects);

        timesheetForm.visitChildren(Label.class, new IVisitor<Label, Void>() {
            @Override
            public void component(Label label, IVisit visit) {
                if (label.getId().equals("status")) {
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
        IModel<String> model = new StringResourceModel("timesheet.weekSaved",
                AbstractTimesheetPanel.this,
                null,
                new PropertyModel<Date>(getDefaultModel(), "timesheet.totalBookedHours"),
                new DateModel(new PropertyModel<Date>(getDefaultModel(), "timesheet.weekStart"), config, DateModel.DATESTYLE_FULL_SHORT),
                new DateModel(new PropertyModel<Date>(getDefaultModel(), "timesheet.weekEnd"), config, DateModel.DATESTYLE_FULL_SHORT));

        return updateServerMessage(model);

    }

    private Label updateErrorMessage() {
        IModel<String> model = new StringResourceModel("timesheet.errorPersist", AbstractTimesheetPanel.this, null);

        return updateServerMessage(model);
    }

    private Label updateServerMessage(IModel<String> model) {
        Label label = new Label("serverMessage", model);
        label.add(AttributeModifier.replace("style", "timesheetPersisted"));
        label.setOutputMarkupId(true);
        serverMsgLabel.replaceWith(label);
        serverMsgLabel = label;
        return label;
    }

    /**
     * Add date labels (sun/mon etc)
     */
    private void addDateLabels(WebMarkupContainer parent) {
        Timesheet timesheet = getPanelModelObject().getTimesheet();

        for (int i = 1, j = 0; i <= 7; i++, j++) {
            String id = "day" + i + "Label";

            Fragment headerFragment = new Fragment(id, "dayHeader", AbstractTimesheetPanel.this);

            PropertyModel<Date> model = new PropertyModel<Date>(getDefaultModelObject(), "timesheet.dateSequence[" + j + "]");
            headerFragment.add(new Label("weekDay", new DateModel(model, config, DateModel.DATESTYLE_TIMESHEET_DAYONLY)));
            headerFragment.add(new Label("day", new DateModel(model, config, DateModel.DATESTYLE_DAYONLY)));

            Fragment lockFragment;
            if (timesheet.isLocked(j)) {
                lockFragment = new Fragment("lock", "lockedDay", this);

                WebMarkupContainer container = new WebMarkupContainer("lockedContainer");
                container.add(AttributeModifier.replace("title", new MessageResourceModel("timesheet.daylocked", AbstractTimesheetPanel.this)));

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

    private List<ProjectAssignmentStatus> persistTimesheetEntries() {
        return ((TimesheetModel) getDefaultModel()).persist();
    }

    private GrandTotal buildForm(final Form<?> form, WebMarkupContainer parent) {
        final GrandTotal grandTotals = new GrandTotal();

        ListView<Customer> customers = new ListView<Customer>("customers", new PropertyModel<List<Customer>>(getDefaultModelObject(), "timesheet.customerList")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Customer> item) {
                final Customer customer = item.getModelObject();

                Timesheet timesheet = AbstractTimesheetPanel.this.getPanelModelObject().getTimesheet();
                item.add(new Label("customer", customer.getName()));

                item.add(createTimesheetRows("rows", grandTotals, form, timesheet.getTimesheetRows(customer)));
            }
        };
        customers.setReuseItems(true);

        parent.add(customers);

        return grandTotals;
    }

    protected TimesheetRowList createTimesheetRows(String id, GrandTotal grandTotals, Form<?> form, List<TimesheetRow> rows) {
        return new TimesheetRowList(id, rows, grandTotals, form, AbstractTimesheetPanel.this);
    }

    private class SubmitButton extends AjaxButton {

        private static final long serialVersionUID = 1L;

        public SubmitButton(String id, Form<?> form) {
            super(id, form);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            List<ProjectAssignmentStatus> failedProjects = persistTimesheetEntries();

            if (failedProjects.isEmpty()) {
                target.add(updatePostPersistMessage());
            } else {
                target.add(updateErrorMessage());
            }

            addFailedProjectMessages(failedProjects, target);

            EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.TIMESHEET_SUBMIT));
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
