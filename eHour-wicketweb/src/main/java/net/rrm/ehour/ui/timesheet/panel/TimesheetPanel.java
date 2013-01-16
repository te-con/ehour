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
import net.rrm.ehour.ui.common.formguard.GuardDirtyFormBehavior;
import net.rrm.ehour.ui.common.formguard.GuardedAjaxLink;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.common.FormHighlighter;
import net.rrm.ehour.ui.timesheet.common.TimesheetAjaxEventType;
import net.rrm.ehour.ui.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.model.TimesheetModel;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * The main panel - timesheet form
 */

public class TimesheetPanel extends Panel implements Serializable {
    private static final long serialVersionUID = 7704288648724599187L;

    private EhourConfig config;
    private WebComponent serverMsgLabel;
    private Form<TimesheetModel> timesheetForm;

    public TimesheetPanel(String id, User user, Calendar forWeek) {
        super(id);

        EhourWebSession session = EhourWebSession.getSession();
        GrandTotal grandTotals;

        config = session.getEhourConfig();

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
        timesheetForm.add(new GuardDirtyFormBehavior(new ResourceModel("timesheet.dirtyForm")));
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

        blueBorder.add(new SubmitButton("submitButtonTop", timesheetForm));

        // server message
        serverMsgLabel = new WebComponent("serverMessage");
        serverMsgLabel.setOutputMarkupId(true);
        commentsFrame.add(serverMsgLabel);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        CssReferenceHeaderItem cssReferenceHeaderItem = CssHeaderItem.forReference(new PackageResourceReference(TimesheetPanel.class, "css/timesheetForm.css"));
        response.render(cssReferenceHeaderItem);
    }

    /**
     * Add week navigation to title
     */
    @SuppressWarnings("serial")
    private WebMarkupContainer getWeekNavigation(final Date weekStart, final Date weekEnd) {
        Fragment titleFragment = new Fragment("title", "title", TimesheetPanel.this);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", config.getLocale());

        int weekOfYear = DateUtil.getWeekNumberForDate(weekStart, config.getFirstDayOfWeek());

        IModel<String> weekLabelModel = new MessageResourceModel("timesheet.weekTitle", this, weekOfYear, dateFormatter.format(weekStart), dateFormatter.format(weekEnd));

        titleFragment.add(new Label("titleLabel", weekLabelModel));

        GuardedAjaxLink<Void> previousWeekLink = new GuardedWeekLink("previousWeek", weekStart, -1);
        titleFragment.add(previousWeekLink);

        GuardedAjaxLink<Void> nextWeekLink = new GuardedWeekLink("nextWeek", weekStart, 1);
        titleFragment.add(nextWeekLink);

        return titleFragment;
    }

    /**
     * Create comments input
     *
     * @param parent
     */
    private MarkupContainer createCommentsInput(WebMarkupContainer parent) {
        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("commentsFrame");

        Timesheet timesheet = (Timesheet) getDefaultModelObject();

        KeepAliveTextArea textArea = new KeepAliveTextArea("commentsArea", new PropertyModel<String>(timesheet, "comment.comment"));
        textArea.add(CommonModifiers.tabIndexModifier(2));
        blueBorder.add(textArea);
        parent.add(blueBorder);

        return blueBorder;
    }

    /**
     * Add grand totals to form
     *
     * @param parent
     * @param grandTotals
     */
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

    /**
     * Set submit actions for form
     *
     * @param form
     */
    private void setSubmitActions(Form<?> form, MarkupContainer parent) {
        // default submit
        parent.add(new SubmitButton("submitButton", form));

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
        };

        resetButton.add(new JavaScriptConfirmation("onclick", new ResourceModel("timesheet.confirmReset")));

        resetButton.setDefaultFormProcessing(false);
        parent.add(resetButton);
    }

    /**
     * Add failed projects to overview
     *
     * @param failedProjects
     * @param target
     */
    private void addFailedProjectMessages(List<ProjectAssignmentStatus> failedProjects, final AjaxRequestTarget target) {
        ((Timesheet) getDefaultModelObject()).updateFailedProjects(failedProjects);

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
                TimesheetPanel.this,
                null,
                new PropertyModel<Date>(getDefaultModel(), "totalBookedHours"),
                new DateModel(new PropertyModel<Date>(getDefaultModel(), "weekStart"), config, DateModel.DATESTYLE_FULL_SHORT),
                new DateModel(new PropertyModel<Date>(getDefaultModel(), "weekEnd"), config, DateModel.DATESTYLE_FULL_SHORT));

        return updateServerMessage(model);

    }

    private Label updateErrorMessage() {
        IModel<String> model = new StringResourceModel("timesheet.errorPersist", TimesheetPanel.this, null);

        return updateServerMessage(model);
    }

    /**
     * Update server message
     *
     * @param model
     */
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
        for (int i = 1, j = 0; i <= 7; i++, j++) {
            Label label = new Label("day" + i + "Label", new DateModel(new PropertyModel<Date>(getDefaultModelObject(), "dateSequence[" + j + "]"), config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
            label.setEscapeModelStrings(false);
            parent.add(label);
        }
    }

    /**
     * Move to next week after succesfull form submit or week navigation
     */
    private void moveWeek(Date onScreenDate, int weekDiff) {
        EhourWebSession session = (EhourWebSession) getSession();
        Calendar cal = DateUtil.getCalendar(config);

        cal.setTime(onScreenDate);
        cal.add(Calendar.WEEK_OF_YEAR, weekDiff);

        // should update calendar as well
        session.setNavCalendar(cal);

        EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.WEEK_NAV));
    }

    /**
     * Persist timesheet entries
     */
    private List<ProjectAssignmentStatus> persistTimesheetEntries() {
        return ((TimesheetModel) getDefaultModel()).persistTimesheet();
    }

    /**
     * Build form
     *
     * @param parent
     */
    private GrandTotal buildForm(final Form<?> form, WebMarkupContainer parent) {
        final GrandTotal grandTotals = new GrandTotal();

        ListView<Customer> customers = new ListView<Customer>("customers", new PropertyModel<List<Customer>>(getDefaultModelObject(), "customerList")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Customer> item) {
                final Customer customer = item.getModelObject();

                Timesheet timesheet = (Timesheet) TimesheetPanel.this.getDefaultModelObject();
                item.add(new Label("customer", customer.getName()));

                item.add(new TimesheetRowList("rows", timesheet.getTimesheetRows(customer), grandTotals, form));
            }
        };
        customers.setReuseItems(true);

        parent.add(customers);

        return grandTotals;
    }

    private class SubmitButton extends AjaxButton {

        private static final long serialVersionUID = 1L;

        public SubmitButton(String id, Form<?> form) {
            super(id, form);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            List<ProjectAssignmentStatus> failedProjects = persistTimesheetEntries();

            // success
            if (failedProjects.isEmpty()) {
                target.add(updatePostPersistMessage());
            } else {
                target.add(updateErrorMessage());
            }

            addFailedProjectMessages(failedProjects, target);

            EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.TIMESHEET_SUBMIT));

            target.appendJavaScript("wicket.guardform.clean();");
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
