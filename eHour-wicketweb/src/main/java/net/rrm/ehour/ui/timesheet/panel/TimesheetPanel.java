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

import com.richemont.jira.JiraConst;
import com.richemont.jira.JiraHelper;
import com.richemont.jira.JiraService;
import com.richemont.windchill.ProxyWindActivity;
import com.richemont.windchill.WindChillUpdateService;
import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
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
import net.rrm.ehour.ui.timesheet.model.TimesheetModel;
import net.rrm.ehour.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.attributes.ThrottlingSettings;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.*;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import javax.json.JsonArray;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The main panel - timesheet form
 */

public class TimesheetPanel extends AbstractBasePanel<Timesheet> {
    private static final long serialVersionUID = 7704288648724599187L;

    private static Logger LOGGER = Logger.getLogger(TimesheetPanel.class);

    private static final JavaScriptResourceReference GUARDFORM_JS = new JavaScriptResourceReference(TimesheetPanel.class, "guardform.js");
    private static final JavaScriptResourceReference TIMESHEET_JS = new JavaScriptResourceReference(TimesheetPanel.class, "timesheet.js");
    private static final CssResourceReference TIMESHEET_CSS = new CssResourceReference(TimesheetPanel.class, "css/timesheetForm.css");

    private EhourConfig config;
    private WebComponent serverMsgLabel;
    private Form<TimesheetModel> timesheetForm;

    @SpringBean
    private WindChillUpdateService windChillUpdateService;

    @SpringBean
    private JiraService jiraService;


    private boolean isModerating = false;

    /**
     * Construct timesheetPanel for entering hours
     */
    public TimesheetPanel(String id, User user, Calendar forWeek) {
        this(id, user, forWeek, false);

    }

    public TimesheetPanel(String id, User user, Calendar forWeek, boolean isModerating) {
        super(id);

        this.isModerating = isModerating;

        config = EhourWebSession.getEhourConfig();

        this.setOutputMarkupId(true);

        // set the model
        TimesheetModel timesheetModel = new TimesheetModel(user, forWeek);
        setDefaultModel(timesheetModel);

        // grey & blue frame border
        WebMarkupContainer weekNavigation = getWeekNavigation(timesheetModel.getWeekStart(), timesheetModel.getWeekEnd(), user);
        WebMarkupContainer filter = createFilter(CustomTitledGreyRoundedBorder.RIGHT_ID, timesheetModel);

        CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("timesheetFrame", weekNavigation, filter);
        add(greyBorder);

        Form<TimesheetModel> timesheetForm = buildForm(timesheetModel);
        this.timesheetForm = timesheetForm;
        greyBorder.add(timesheetForm);

    }

    private Form<TimesheetModel> buildForm(final TimesheetModel timesheet) {
        GrandTotal grandTotals;// add form
        Form<TimesheetModel> timesheetForm = new Form<TimesheetModel>("timesheetForm");
        timesheetForm.setMarkupId("timesheetForm");
        timesheetForm.setOutputMarkupId(true);
        timesheetForm.setModel(new Model<TimesheetModel>(timesheet));

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

        // create paginator
        blueBorder.add(createPaginationDropdown(timesheet));

        return timesheetForm;
    }

    private DropDownChoice<Integer> createPaginationDropdown(final TimesheetModel timesheetModel) {
        List<Integer> options = new ArrayList<Integer>();
        int maxPages = timesheetModel.getObject().getMaxPages();
        for (int i = 0; i <= maxPages; i++) {
            options.add(i);
        }

        final DropDownChoice<Integer> pagination = new DropDownChoice<Integer>("pagination", new PropertyModel<Integer>(timesheetModel, "page"), options,
                new IChoiceRenderer<Integer>() {
                    @Override
                    public Object getDisplayValue(Integer object) {
                        return (object + 1);
                    }

                    @Override
                    public String getIdValue(Integer object, int index) {
                        return Integer.toString(object - 1);
                    }
                }
        );
        pagination.add(new FormReloadBehavior(timesheetModel, "onchange"));

        pagination.setVisible(maxPages > 0);
        return pagination;
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(GUARDFORM_JS));
        response.render(JavaScriptHeaderItem.forReference(TIMESHEET_JS));
        response.render(CssHeaderItem.forReference(TIMESHEET_CSS));

        String msg = new ResourceModel("timesheet.dirtyForm").getObject();
        String escapedMsg = msg.replace("'", "\\\'");

        response.render(JavaScriptHeaderItem.forScript(String.format("var WARNING_MSG = '%s';", escapedMsg), "msg"));

        response.render(OnDomReadyHeaderItem.forScript("initializeFoldLinks();"));
    }

    private WebMarkupContainer createFilter(String id, TimesheetModel timesheetModel) {
        Fragment f = new Fragment(id, "filter", this);

        // create filter
        TextField activityFilter = new TextField<String>("activityFilter", new PropertyModel<String>(timesheetModel, "filter"));
        activityFilter.add(new FormReloadBehavior(timesheetModel, "onkeyup"));
        activityFilter.setOutputMarkupId(true);
        f.add(activityFilter);

        return f;
    }
    /**
     * Add week navigation to title
     */
    @SuppressWarnings("serial")
    private WebMarkupContainer getWeekNavigation(final Date weekStart, final Date weekEnd, User user) {
        Fragment titleFragment = new Fragment("title", CustomTitledGreyRoundedBorder.TITLE_ID, TimesheetPanel.this);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", config.getFormattingLocale());

        int weekOfYear = DateUtil.getWeekNumberForDate(weekStart, config.getFirstDayOfWeek());


        IModel<String> weekLabelModel;

        if (EhourWebSession.getUser().getUserId().equals(user.getUserId())) {
            weekLabelModel = new MessageResourceModel("timesheet.weekTitle", this, weekOfYear, dateFormatter.format(weekStart), dateFormatter.format(weekEnd));
        } else {
            weekLabelModel = new MessageResourceModel("timesheet.weekTitleModerating", this, weekOfYear, dateFormatter.format(weekStart), dateFormatter.format(weekEnd), user.getFullName());
        }

        titleFragment.add(new Label("titleLabel", weekLabelModel));

        GuardedAjaxLink<Void> previousWeekLink = new GuardedWeekLink("previousWeek", weekStart, -1);
        titleFragment.add(previousWeekLink);

        GuardedAjaxLink<Void> nextWeekLink = new GuardedWeekLink("nextWeek", weekStart, 1);
        titleFragment.add(nextWeekLink);
        
        return titleFragment;
    }

    private MarkupContainer createCommentsInput(WebMarkupContainer parent) {
        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("commentsFrame");

        Timesheet timesheet = (Timesheet) getDefaultModelObject();

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

    private String getStringResource(String resourceName){
        IModel<String> model = new StringResourceModel(resourceName, TimesheetPanel.this, null);
        return  model.getObject();
    }

    private void addFailedProjectMessages(List<ActivityStatus> failedProjects, final AjaxRequestTarget target)
    {
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
        IModel<String> model = new MessageResourceModel("timesheet.weekSaved",
                TimesheetPanel.this,
                new PropertyModel<Date>(getDefaultModel(), "totalBookedHours"),
                new DateModel(new PropertyModel<Date>(getDefaultModel(), "weekStart"), config, DateModel.DATESTYLE_FULL_SHORT),
                new DateModel(new PropertyModel<Date>(getDefaultModel(), "weekEnd"), config, DateModel.DATESTYLE_FULL_SHORT));

        return updateServerMessage(model);

    }

    private Label updateErrorMessage() {
        IModel<String> model = new StringResourceModel("timesheet.errorPersist", TimesheetPanel.this, null);
        return updateServerMessage_err(model);
    }


    private Label updateErrorMessageWaiting() {
        IModel<String> model = new StringResourceModel("timesheet.waiting", TimesheetPanel.this, null);
        Label label = new Label("serverMessage", model);
        String styleAttr = "color: #006400;";  // green
        label.add(AttributeModifier.replace("style", styleAttr));
        //label.add(new SimpleAttributeModifier("style", "timesheetPersisted"));
        label.setOutputMarkupId(true);
        /**
         label.add (new AjaxEventBehavior("beforeRender") {
         protected void onEvent(AjaxRequestTarget target) {
         System.out.println("ajax here!");
         }
         });
         **/
        serverMsgLabel.replaceWith(label);
        serverMsgLabel = label;

        return label;

    }


    private Label updateMultiLineErrorMessage(String errorDesc) {
        Label label = new Label("serverMessage", errorDesc);
        String styleAttr = "color: #8B0000;";  // red
        label.add(AttributeModifier.replace("style", styleAttr));
        label.setOutputMarkupId(true);
        serverMsgLabel.replaceWith(label);
        serverMsgLabel = label;
        return label;
    }



    /**
     * Update server message
     *
     * @param model
     */
    private Label updateServerMessage_err(IModel<String> model) {
        Label label = new Label("serverMessage", model);
        //String styleAttr = "color: #8B0000;";  // red
        label.add(AttributeModifier.replace("style", "serverMessage"));
        label.setOutputMarkupId(true);
        serverMsgLabel.replaceWith(label);
        serverMsgLabel = label;
        return label;
    }

    private Label updateServerMessage(IModel<String> model) {
        Label label = new Label("serverMessage", model);
        String styleAttr = "color: #006400;";  // green
        label.add(AttributeModifier.replace("style", styleAttr));
        label.setOutputMarkupId(true);
        serverMsgLabel.replaceWith(label);
        serverMsgLabel = label;
        return label;
    }

    /**
     * Add date labels (sun/mon etc)
     */
    private void addDateLabels(WebMarkupContainer parent) {
        Timesheet timesheet = getPanelModelObject();

        for (int i = 1, j = 0; i <= 7; i++, j++) {
            String id = "day" + i + "Label";

            Fragment headerFragment = new Fragment(id, "dayHeader", TimesheetPanel.this);

            PropertyModel<Date> model = new PropertyModel<Date>(getDefaultModelObject(), "dateSequence[" + j + "]");
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

    /**
     * Persist timesheet entries
     */
    private List<ActivityStatus> persistTimesheetEntries()
    {
        return ((TimesheetModel) getDefaultModel()).persistTimesheet();
    }

    private GrandTotal buildForm(final Form<TimesheetModel> form, WebMarkupContainer parent) {
        final GrandTotal grandTotals = new GrandTotal();

        ListView<Project> projects = new ListView<Project>("projects", new PropertyModel<List<Project>>(getDefaultModelObject(), "projectList")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Project> item) {
                final Project project = item.getModelObject();

                Timesheet timesheet = (Timesheet) TimesheetPanel.this.getDefaultModelObject();
                item.add(new Label("project", project.getName()));

                item.add(new TimesheetRowList("rows", timesheet.getTimesheetRows(project), grandTotals, form, isModerating, TimesheetPanel.this));
            }
        };
        projects.setReuseItems(true);

        parent.add(projects);

        return grandTotals;
    }

    private class SubmitButton extends AjaxButton {

        private static final long serialVersionUID = 1L;

        public SubmitButton(String id, Form<?> form) {
            super(id, form);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            LOGGER.fatal("Start persisting");

            List<ActivityStatus> failedProjects = persistTimesheetEntries();
            LOGGER.fatal("End persisting");

            List<String> failedActivities = sendDataToThirdParty();
            String err_msg = "";
            String err_msg_style = "style='color: #8B0000';";

            LOGGER.info("Nb of failed activities : " + failedActivities.size());
            boolean feedback_msg_err_jira = false;
            boolean feedback_msg_err_pjl = false;
            if (failedActivities.size() > 0) { // KO
                LOGGER.info("" + failedActivities.size() + " failed activities...");
                for (String s : failedActivities) {
                    LOGGER.debug("\t" + s);
                    if (s.contains(JiraConst.ACTIVITY_CODE_PREFIX_FOR_JIRA)) {
                        feedback_msg_err_jira = true;
                    } else {
                        feedback_msg_err_pjl = true;
                    }
                }
            }
            LOGGER.debug("feedback_msg_err_pjl=" + feedback_msg_err_pjl);
            LOGGER.debug("feedback_msg_err_jira=" + feedback_msg_err_jira);
            if (feedback_msg_err_jira) err_msg += getStringResource("timesheet.errorPersist.jira") + "<br>";
            if (feedback_msg_err_pjl) err_msg += getStringResource("timesheet.errorPersist.pjl") + "<br>";
            err_msg = "<p " + err_msg_style + " >" + err_msg + "<p>";
            LOGGER.debug("err_msg=" + err_msg.replace("\n", "\t"));
            target.add(updateMultiLineErrorMessage(err_msg).setEscapeModelStrings(false));

            //target.addComponent(updateErrorMessage("timesheet.errorPersist.pjl"));

            if (!feedback_msg_err_jira && !feedback_msg_err_pjl) {
                // success
                if (failedProjects.isEmpty()) {   // OK
                    target.add(updatePostPersistMessage());
                } else {  // KO
                    target.add(updateErrorMessage());
                }
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

    /**
     * LLI
     * 01/05/2012
     * TODO: Thies, move this to a separate class
     */
    private List<String> sendDataToThirdParty() {
        TimesheetModel model = (TimesheetModel) getDefaultModel();
        Timesheet timesheet = model.getObject();

        HttpServletRequest request = (HttpServletRequest) getRequest().getContainerRequest();
        User user = EhourWebSession.getUser();

        List<TimesheetEntry> entries = timesheet.getTimesheetEntries();
        List<Activity> pjlActivities = new ArrayList<Activity>();
        List<Activity> jiraActivities = new ArrayList<Activity>();
        List<TimesheetEntry> jiraEntries = new ArrayList<TimesheetEntry>();
        List<String> jiraFailedActivitieslist = new ArrayList<String>();     // Jira Activities
        List<ProxyWindActivity> newWindActivitieslist; // Activities created in PJL for Jira
        List<String> windFailedActivitieslist = new ArrayList<String>();   // Native PJL actvities

        LOGGER.info("\n\n");
        LOGGER.info("*****************************************************************");
        LOGGER.info("****************** START UPDATE PROCESS  ************************");
        LOGGER.info("*****************************************************************");

        LOGGER.info("\n\n");
        LOGGER.info("****************** UPDATE STEP 1 : IDENTIFY all updated activities in ehour  ************************");

        LOGGER.debug("Get a total of activities " + entries.size() + " for user " + user.getName());
        Activity a;
        for (TimesheetEntry entry : entries) {
            if (entry.getUpdatedHours() != null){

                if (entry.getEntryId().getActivity().getCode().startsWith(JiraConst.ACTIVITY_CODE_PREFIX_FOR_JIRA)){
                    // Jira activities
                    a = entry.getEntryId().getActivity();
                    if (!jiraActivities.contains(a) ) jiraActivities.add(entry.getEntryId().getActivity());
                    // Jira Timesheet entries
                    jiraEntries.add(entry);
                } else {
                    // PJL activities
                    pjlActivities.add(entry.getEntryId().getActivity());
                }
                //LOGGER.debug("sendToProjectLink: " + entry.getEntryId().getActivity().getName() + " ADDED for " + entry.getUpdatedHours());
            }
        }
        entries.clear();

        LOGGER.info("\n\n");
        LOGGER.info("****************** UPDATE STEP 2 : Update JIRA from eHour ************************");
        if (jiraEntries != null){
            try {
                jiraFailedActivitieslist = jiraService.updateJiraIssues(user, jiraEntries);
            } catch (Exception e) {
                e.printStackTrace();
                for (Activity activity :jiraActivities){
                    jiraFailedActivitieslist.add( activity.getCode() );    //// LLI : TBD
                }
            }
            jiraEntries.clear();
        }else {
            LOGGER.info("Nothing to do.");
        }

        LOGGER.info("\n\n");
        LOGGER.info("****************** UPDATE STEP 3 : Update WINDCHILL from eHour for Jira ************************");
        JsonArray jSonNewJiraActivities = (JsonArray) request.getSession().getAttribute("MissingPjlActivity");

        if (jSonNewJiraActivities != null){
            try {
                newWindActivitieslist = windChillUpdateService.createMissingPjlActivities ( user, jSonNewJiraActivities, jiraActivities );
                //LOGGER.info("jSonNewJiraActivities has been updated with latest modification:");
                //LOGGER.debug(jSonNewJiraActivities);

                windFailedActivitieslist = JiraHelper.updateActivityId(newWindActivitieslist);

                LOGGER.info("\n\n");
                LOGGER.info("****************** UPDATE STEP 3bis :  Update eHour user session with latest modifications ************************");

                //List<ProxyWindActivity> resultActivitiesList, JsonArray jSonAllJiraActivities
                jSonNewJiraActivities = windChillUpdateService.updateSessionParam ( newWindActivitieslist, jSonNewJiraActivities ) ;
                request.getSession().setAttribute("MissingPjlActivity" , jSonNewJiraActivities );

                LOGGER.info("user session has been updated");
                LOGGER.debug("" + jSonNewJiraActivities);
                LOGGER.debug("\n\n");
                LOGGER.debug("" + request.getSession().getAttribute("MissingPjlActivity"));

            } catch (Exception e) {
                e.printStackTrace();

                LOGGER.error( "ERROR !!!!! No response from PJL: createMissingPjlActivities() returned null");
                LOGGER.error(e.getMessage());
                for (Activity activity :jiraActivities){
                    windFailedActivitieslist.add( activity.getCode() );    //// LLI : TBD
                }
            }

            LOGGER.error("\n");
            LOGGER.error( "Update Windchill from eHour for Jira: Are there any failed activities ?");
            LOGGER.error("jira Failed Activities list = " + windFailedActivitieslist);

        } else {
            LOGGER.info("Nothing to do.");
        }



        LOGGER.info("\n\n");
        LOGGER.info("****************** UPDATE STEP 4 :  Update WINDCHILL from eHour ************************");
        if ( pjlActivities != null & pjlActivities.size() >0 ){
            try {
                windFailedActivitieslist.addAll( windChillUpdateService.updateProjectLink(user, pjlActivities) );

            } catch (Exception e) {
                LOGGER.error( "ERROR !!!!! Update PJL from eHour: updateProjectLink()");
                LOGGER.error(e.getMessage());
                for (Activity activity :pjlActivities){
                    windFailedActivitieslist.add(activity.getCode());    //// LLI : TBD
                }
            }

            LOGGER.error("\n");
            LOGGER.error( "Update Windchill from eHour for otheractivities (just PJL, not Jira): Are there any failed activities ?");
            LOGGER.error("wind Failed Activities list = " + jiraFailedActivitieslist);
        }

        // all failed activities
        if ( windFailedActivitieslist.size()>0 ) jiraFailedActivitieslist.addAll(windFailedActivitieslist);

        LOGGER.error("\n");
        LOGGER.error( "Are there any failed activities ?");
        LOGGER.error("ALL FailedActivities list = " + jiraFailedActivitieslist);

        return jiraFailedActivitieslist;

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

    private class FormReloadBehavior extends AjaxFormComponentUpdatingBehavior {
        private final TimesheetModel timesheetModel;

        public FormReloadBehavior(TimesheetModel timesheetModel, String event) {
            super(event);
            this.timesheetModel = timesheetModel;
        }

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            Form<TimesheetModel> replacementForm = buildForm(timesheetModel);
            timesheetForm.replaceWith(replacementForm);
            timesheetForm = replacementForm;

            target.add(replacementForm);

            target.appendJavaScript("initializeFoldLinks();");

        }

        @Override
        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
            super.updateAjaxAttributes(attributes);

            attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
            attributes.setThrottlingSettings(new ThrottlingSettings("id", Duration.milliseconds(1500), true));
        }

    }
}
