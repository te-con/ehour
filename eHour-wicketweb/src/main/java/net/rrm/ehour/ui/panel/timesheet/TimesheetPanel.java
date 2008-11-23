/**
 * Created on Jun 21, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.timesheet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.CustomerFoldPreference;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.ui.ajax.AjaxEvent;
import net.rrm.ehour.ui.ajax.AjaxUtil;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.ajax.OnClickDecorator;
import net.rrm.ehour.ui.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.component.CommonModifiers;
import net.rrm.ehour.ui.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.component.KeepAliveTextArea;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.panel.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.panel.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.panel.timesheet.model.TimesheetModel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonWebUtil;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;

/**
 * The main panel - timesheet form
 **/

public class TimesheetPanel extends Panel implements Serializable
{
	private static final long serialVersionUID = 7704288648724599187L;

	@SpringBean
	private UserService userService;

	private EhourConfig config;
	private WebComponent serverMsgLabel;
	private Form timesheetForm;

	/**
	 * Construct timesheetPanel for entering hours
	 * 
	 * @param id
	 * @param user
	 * @param forWeek
	 */
	public TimesheetPanel(String id, User user, Calendar forWeek)
	{
		super(id);

		EhourWebSession session = (EhourWebSession) getSession();
		GrandTotal grandTotals = new GrandTotal();

		config = session.getEhourConfig();

		// dom id's
		this.setOutputMarkupId(true);

		// set the model
		TimesheetModel timesheet = new TimesheetModel(user, forWeek);
		setModel(timesheet);

		// grey & blue frame border
		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("timesheetFrame", getWeekNavigation(forWeek, timesheet.getWeekStart(), timesheet.getWeekEnd()), CommonWebUtil.GREYFRAME_WIDTH);
		add(greyBorder);

		// add form
		timesheetForm = new Form("timesheetForm");
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

		// server message
		serverMsgLabel = new WebComponent("serverMessage");
		serverMsgLabel.setOutputMarkupId(true);
		commentsFrame.add(serverMsgLabel);

		// add CSS & JS
		add(new StyleSheetReference("timesheetStyle", new CompressedResourceReference(TimesheetPanel.class, "style/timesheetForm.css")));
		addJavascript(this);
	}

	/**
	 * Add week navigation to title
	 * 
	 * @param parent
	 * @param forWeek
	 */
	@SuppressWarnings("serial")
	private WebMarkupContainer getWeekNavigation(Calendar forWeek, final Date weekStart, final Date weekEnd)
	{
		Fragment titleFragment = new Fragment("title", "title", TimesheetPanel.this);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", config.getLocale());

		Calendar cal = DateUtil.getCalendar(config);
		cal.setTime(weekStart);

		IModel weekLabelModel = new StringResourceModel("timesheet.weekTitle", this, null, new Object[] { cal.get(Calendar.WEEK_OF_YEAR), dateFormatter.format(weekStart), dateFormatter.format(weekEnd) });

		titleFragment.add(new Label("titleLabel", weekLabelModel));

		AjaxLink previousWeekLink = new AjaxLink("previousWeek")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				moveWeek(weekStart, target, -1);
			}
		};

		titleFragment.add(previousWeekLink);

		AjaxLink nextWeekLink = new AjaxLink("nextWeek")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				moveWeek(weekStart, target, 1);
			}
		};

		titleFragment.add(nextWeekLink);

		return titleFragment;
	}

	/**
	 * Create comments input
	 * 
	 * @param parent
	 * @param timesheet
	 */
	private MarkupContainer createCommentsInput(WebMarkupContainer parent)
	{
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("commentsFrame");

		Timesheet timesheet = (Timesheet) getModelObject();

		TextArea textArea = new KeepAliveTextArea("commentsArea", new PropertyModel(timesheet, "comment.comment"));
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
	private void addGrandTotals(WebMarkupContainer parent, GrandTotal grandTotals, Date weekStart)
	{
		Label total;

		Calendar dateIterator = new GregorianCalendar();
		dateIterator.setTime(weekStart);

		for (int i = 1; i <= 7; i++, dateIterator.add(Calendar.DAY_OF_YEAR, 1))
		{
			total = new Label("day" + i + "Total", new FloatModel(new PropertyModel(grandTotals, "getValues[" + (dateIterator.get(Calendar.DAY_OF_WEEK) - 1) + "]"), config));
			total.setOutputMarkupId(true);
			parent.add(total);

			grandTotals.addOrder(i, dateIterator.get(Calendar.DAY_OF_WEEK) - 1);
		}

		total = new Label("grandTotal", new FloatModel(new PropertyModel(grandTotals, "grandTotal"), config));
		total.setOutputMarkupId(true);
		parent.add(total);
	}

	/**
	 * Set submit actions for form
	 * 
	 * @param form
	 * @param timesheet
	 */
	private void setSubmitActions(Form form, MarkupContainer parent)
	{
		// default submit
		parent.add(new AjaxButton("submitButton", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				List<ProjectAssignmentStatus> failedProjects = persistTimesheetEntries();

				// success
				if (failedProjects.isEmpty())
				{
					target.addComponent(updatePostPersistMessage());
				} else
				{
					target.addComponent(updateErrorMessage());
				}

				addFailedProjectMessages(failedProjects, target);

				AjaxUtil.publishAjaxEvent(this, new AjaxEvent(target, TimesheetAjaxEventType.TIMESHEET_SUBMIT));
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form form)
			{
				form.visitFormComponents(new FormHighlighter(target));
			}
		});

		// reset, should fetch the original contents
		AjaxButton resetButton = new AjaxButton("resetButton", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				// basically fake a week click
				AjaxUtil.publishAjaxEvent(this, new AjaxEvent(target, TimesheetAjaxEventType.WEEK_NAV));
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
	private void addFailedProjectMessages(List<ProjectAssignmentStatus> failedProjects, final AjaxRequestTarget target)
	{
		((Timesheet) getModelObject()).updateFailedProjects(failedProjects);

		timesheetForm.visitChildren(Label.class, new IVisitor()
		{
			public Object component(Component component)
			{
				Label label = (Label) component;
				if (label.getId().equals("status"))
				{
					target.addComponent(label);
				}

				return IVisitor.CONTINUE_TRAVERSAL;
			}
		});
	}

	/**
	 * Set message that the hours are saved
	 * 
	 * @param timesheet
	 * @param target
	 */
	private Label updatePostPersistMessage()
	{
		// server message
		IModel model = new StringResourceModel("timesheet.weekSaved", TimesheetPanel.this, null, new Object[] { new PropertyModel(getModel(), "totalBookedHours"), new DateModel(new PropertyModel(getModel(), "weekStart"), config, DateModel.DATESTYLE_FULL_SHORT),
				new DateModel(new PropertyModel(getModel(), "weekEnd"), config, DateModel.DATESTYLE_FULL_SHORT) });

		return updateServerMessage(model);

	}

	/**
	 * 
	 * @return
	 */
	private Label updateErrorMessage()
	{
		IModel model = new StringResourceModel("timesheet.errorPersist", TimesheetPanel.this, null, new Object[] {});

		return updateServerMessage(model);
	}

	/**
	 * Update server message
	 * 
	 * @param model
	 */
	private Label updateServerMessage(IModel model)
	{
		Label label = new Label("serverMessage", model);
		label.add(new SimpleAttributeModifier("style", "timesheetPersisted"));
		label.setOutputMarkupId(true);
		serverMsgLabel.replaceWith(label);
		serverMsgLabel = label;
		return label;
	}

	/**
	 * Add date labels (sun/mon etc)
	 */
	private void addDateLabels(WebMarkupContainer parent)
	{
		Label label;

		for (int i = 1, j = 0; i <= 7; i++, j++)
		{
			label = new Label("day" + i + "Label", new DateModel(new PropertyModel(getModelObject(), "dateSequence[" + j + "]"), config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
			label.setEscapeModelStrings(false);
			parent.add(label);
		}
	}

	/**
	 * Move to next week after succesfull form submit or week navigation
	 * 
	 * @param target
	 */
	private void moveWeek(Date onScreenDate, AjaxRequestTarget target, int weekDiff)
	{
		EhourWebSession session = (EhourWebSession) getSession();
		Calendar cal = DateUtil.getCalendar(config);

		cal.setTime(onScreenDate);
		cal.add(Calendar.WEEK_OF_YEAR, weekDiff);

		// should update calendar as well
		session.setNavCalendar(cal);

		AjaxUtil.publishAjaxEvent(this, new AjaxEvent(target, TimesheetAjaxEventType.WEEK_NAV));
	}

	/**
	 * Persist timesheet entries
	 * 
	 * @param timesheet
	 * @throws OverBudgetException
	 */
	private List<ProjectAssignmentStatus> persistTimesheetEntries()
	{
		return ((TimesheetModel) getModel()).persistTimesheet();
	}

	/**
	 * Build form
	 * 
	 * @param parent
	 * @param timesheet
	 */
	private GrandTotal buildForm(final Form form, WebMarkupContainer parent)
	{
		final GrandTotal grandTotals = new GrandTotal();

		ListView customers = new ListView("customers", new PropertyModel(getModelObject(), "customerList"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				final Customer customer = (Customer) item.getModelObject();

				Timesheet timesheet = (Timesheet) TimesheetPanel.this.getModelObject();

				// check for any preference
				CustomerFoldPreference foldPreference = timesheet.getFoldPreferences().get(customer);

				if (foldPreference == null)
				{
					foldPreference = new CustomerFoldPreference(timesheet.getUser(), customer, false);
				}

				boolean hidden = (foldPreference != null && foldPreference.isFolded());

				AjaxLink foldLink = getFoldLink(customer.getCustomerId().toString(), foldPreference);
				foldLink.add(new SimpleAttributeModifier("class", hidden ? "timesheetFoldedImg" : "timesheetFoldImg"));
				item.add(foldLink);

				item.add(getCustomerLabel(customer, foldPreference));
				// item.add(new Label("customerDesc",
				// customer.getDescription()));

				item.add(new TimesheetRowList("rows", timesheet.getTimesheetRows(customer), hidden, grandTotals, form));
			}
		};
		customers.setReuseItems(true);

		parent.add(customers);

		return grandTotals;
	}

	/**
	 * Add fold link
	 * 
	 * @param id
	 * @param foldPreference
	 * @return
	 */
	private AjaxLink getFoldLink(final String id, final CustomerFoldPreference foldPreference)
	{
		AjaxLink foldLink = new AjaxLink("foldLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				foldRow(foldPreference);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new OnClickDecorator("toggleProjectRow", id);
			}
		};

		foldLink.add(new SimpleAttributeModifier("id", "foldcss" + id));


		return foldLink;
	}

	/**
	 * Get customer label
	 * 
	 * @param customer
	 * @return
	 */
	private Label getCustomerLabel(final Customer customer, final CustomerFoldPreference foldPreference)
	{
		Label label;
		label = new Label("customer", customer.getName());

		label.add(new AjaxEventBehavior("onclick")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target)
			{
				foldRow(foldPreference);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new OnClickDecorator("toggleProjectRow", customer.getCustomerId().toString());
			}
		});

		return label;
	}

	/**
	 * Fold row
	 * @param foldPreference
	 */
	private void foldRow(CustomerFoldPreference foldPreference)
	{
		foldPreference.toggleFolded();
		userService.persistCustomerFoldPreference(foldPreference);
	}

	/**
	 * Add javascript with replaced images TODO add js directly to header
	 * 
	 * @param container
	 */
	private void addJavascript(WebMarkupContainer container)
	{
		PackagedTextTemplate js = new PackagedTextTemplate(TimesheetPanel.class, "js/timesheet.js");

		Map<String, CharSequence> map = new HashMap<String, CharSequence>();

		add(TextTemplateHeaderContributor.forJavaScript(js, new Model((Serializable) map)));
	}
}
