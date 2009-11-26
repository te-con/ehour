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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.common.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.timesheet.common.FormHighlighter;
import net.rrm.ehour.ui.timesheet.common.TimesheetAjaxEventType;
import net.rrm.ehour.ui.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.model.TimesheetModel;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * The main panel - timesheet form
 **/

public class TimesheetPanel extends Panel implements Serializable
{
	private static final long serialVersionUID = 7704288648724599187L;

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

		this.setOutputMarkupId(true);

		// set the model
		TimesheetModel timesheet = new TimesheetModel(user, forWeek);
		setDefaultModel(timesheet);

		// grey & blue frame border
		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("timesheetFrame", 
																						getWeekNavigation(forWeek, timesheet.getWeekStart(), timesheet.getWeekEnd()), 
																						WebGeo.W_CONTENT_MEDIUM);
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

		// add CSS
		add(new StyleSheetReference("timesheetStyle", new CompressedResourceReference(TimesheetPanel.class, "css/timesheetForm.css")));
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
	private void addGrandTotals(WebMarkupContainer parent, GrandTotal grandTotals, Date weekStart)
	{
		Label total;

		Calendar dateIterator = new GregorianCalendar();
		dateIterator.setTime(weekStart);

		for (int i = 1; i <= 7; i++, dateIterator.add(Calendar.DAY_OF_YEAR, 1))
		{
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

				EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.TIMESHEET_SUBMIT));
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
				EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.WEEK_NAV));
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
		((Timesheet) getDefaultModelObject()).updateFailedProjects(failedProjects);

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
		IModel model = new StringResourceModel("timesheet.weekSaved", TimesheetPanel.this, null, new Object[] { new PropertyModel(getDefaultModel(), "totalBookedHours"), new DateModel(new PropertyModel(getDefaultModel(), "weekStart"), config, DateModel.DATESTYLE_FULL_SHORT),
				new DateModel(new PropertyModel(getDefaultModel(), "weekEnd"), config, DateModel.DATESTYLE_FULL_SHORT) });

		return updateServerMessage(model);

	}

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
			label = new Label("day" + i + "Label", new DateModel(new PropertyModel(getDefaultModelObject(), "dateSequence[" + j + "]"), config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
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

		EventPublisher.publishAjaxEvent(this, new AjaxEvent(TimesheetAjaxEventType.WEEK_NAV));
	}

	/**
	 * Persist timesheet entries
	 * 
	 * @param timesheet
	 * @throws OverBudgetException
	 */
	private List<ProjectAssignmentStatus> persistTimesheetEntries()
	{
		return ((TimesheetModel) getDefaultModel()).persistTimesheet();
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

		ListView customers = new ListView("customers", new PropertyModel(getDefaultModelObject(), "customerList"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				final Customer customer = (Customer) item.getModelObject();

				Timesheet timesheet = (Timesheet) TimesheetPanel.this.getDefaultModelObject();
				item.add(getCustomerLabel(customer));

				item.add(new TimesheetRowList("rows", timesheet.getTimesheetRows(customer), grandTotals, form));
			}
		};
		customers.setReuseItems(true);

		parent.add(customers);

		return grandTotals;
	}

	/**
	 * Get customer label
	 * 
	 * @param customer
	 * @return
	 */
	private Label getCustomerLabel(final Customer customer)
	{
		Label label = new Label("customer", customer.getName());
		return label;
	}
}
