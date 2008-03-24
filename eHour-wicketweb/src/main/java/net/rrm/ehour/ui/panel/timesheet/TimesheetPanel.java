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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.CustomerFoldPreference;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.project.status.ProjectAssignmentStatusService;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
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
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.panel.timesheet.util.TimesheetAssembler;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonWebUtil;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.DateUtil;

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
	private TimesheetService	timesheetService;
	@SpringBean
	private UserService			userService;
	
	private	EhourConfig		config;
	private WebComponent	serverMsgLabel;
	
	/**
	 * Construct timesheetPanel for entering hours
	 * @param id
	 * @param user
	 * @param forWeek
	 */
	public TimesheetPanel(String id, User user, Calendar forWeek)
	{
		super(id);
		
		EhourWebSession 	session = (EhourWebSession)getSession();
		GrandTotal			grandTotals = new GrandTotal();
		
		config = session.getEhourConfig();

		// dom id's
		this.setOutputMarkupId(true);

		// the timesheet we're working on
		final Timesheet timesheet = getTimesheet(user, forWeek);

		// grey & blue frame border
		CustomTitledGreyRoundedBorder greyBorder = new CustomTitledGreyRoundedBorder("timesheetFrame", 
																				getWeekNavigation(forWeek, 
																									timesheet.getWeekStart(), 
																									timesheet.getWeekEnd()),
																				CommonWebUtil.GREYFRAME_WIDTH);
		add(greyBorder);

		// add form
		Form timesheetForm = new Form("timesheetForm");
		timesheetForm.setOutputMarkupId(true);
		greyBorder.add(timesheetForm);
		
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		timesheetForm.add(blueBorder);

		// setup form
		grandTotals = buildForm(timesheetForm, blueBorder, timesheet);
		
		// add last row with grand totals
		addGrandTotals(blueBorder, grandTotals);
		
		// add label dates
		addDateLabels(blueBorder, timesheet);

		// add comments section
		MarkupContainer commentsFrame = createCommentsInput(timesheetForm, timesheet);

		// attach onsubmit ajax events
		setSubmitActions(timesheetForm, commentsFrame, timesheet);
		
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
	 * @param parent
	 * @param forWeek
	 */
	@SuppressWarnings("serial")
	private WebMarkupContainer getWeekNavigation(Calendar forWeek, final Date weekStart, final Date weekEnd)
	{
		Fragment titleFragment = new Fragment("title", "title", TimesheetPanel.this);
		SimpleDateFormat	weekDateFormatter, dateFormatter;
		weekDateFormatter = new SimpleDateFormat("w", config.getLocale());
		dateFormatter = new SimpleDateFormat("dd MMM yyyy", config.getLocale());
		
		IModel weekLabelModel = new StringResourceModel("timesheet.weekTitle",
								this, null,
								new Object[]{weekDateFormatter.format(forWeek.getTime()),
												dateFormatter.format(weekStart),
												dateFormatter.format(weekEnd)});

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
	 * @param parent
	 * @param timesheet
	 */
	private MarkupContainer createCommentsInput(WebMarkupContainer parent, Timesheet timesheet)
	{
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("commentsFrame");
		
		if (timesheet.getComment() == null)
		{
			timesheet.setComment(new TimesheetComment());
		}
		
		TextArea	textArea = new KeepAliveTextArea("commentsArea", new PropertyModel(timesheet, "comment.comment"));
		textArea.add(CommonModifiers.tabIndexModifier(2));
		blueBorder.add(textArea);
		parent.add(blueBorder);
		
		return blueBorder;
	}
	
	/**
	 * Add grand totals to form
	 * @param parent
	 * @param grandTotals
	 */
	private void addGrandTotals(WebMarkupContainer parent, GrandTotal grandTotals)
	{
		Label		total;
		
		for (int i = 0; i < CommonWebUtil.weekDays.length; i++)
		{
			total = new Label(CommonWebUtil.weekDays[i] + "Total", new FloatModel(new PropertyModel(grandTotals, "getValues[" + i + "]"), config));
			total.setOutputMarkupId(true);
			parent.add(total);
		}

		total = new Label("grandTotal", new FloatModel(new PropertyModel(grandTotals, "grandTotal"), config));
		total.setOutputMarkupId(true);
		parent.add(total);
	}
	
	/**
	 * Set submit actions for form
	 * @param form
	 * @param timesheet
	 */
	private void setSubmitActions(Form form, MarkupContainer parent, final Timesheet timesheet)
	{
		// default submit
		parent.add(new AjaxButton("submitButton", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				List<ProjectAssignmentStatus> failedProjects =  persistTimesheetEntries(timesheet);
				
				if (failedProjects.isEmpty())
				{
					target.addComponent(updatePostPersistMessage(timesheet));
				}
				else
				{
					for (ProjectAssignmentStatus projectStatus : failedProjects)
					{
						for (ProjectAssignmentStatus.Status status : projectStatus.getStatusses())
						{
//							System.out.println(innerStatus);
						}
								
//						System.out.println(status.get
					} 
					
					target.addComponent(updateErrorMessage());
				}
					
                ((AjaxAwareContainer)getPage()).ajaxRequestReceived(target, CommonWebUtil.AJAX_FORM_SUBMIT);
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
				((AjaxAwareContainer)getPage()).ajaxRequestReceived(target, CommonWebUtil.AJAX_CALENDARPANEL_WEEK_CLICK);
            }			
		};

		resetButton.add(new JavaScriptConfirmation("onclick", new ResourceModel("timesheet.confirmReset")));
		
		resetButton.setDefaultFormProcessing(false);
		parent.add(resetButton);
	}
	
	private void addProjectStatus(ProjectAssignmentStatus projectStatus)
	{
		for (ProjectAssignmentStatus.Status status : projectStatus.getStatusses())
		{
			if (status.equals(ProjectAssignmentStatus.Status.OVER_ALLOTTED)
					|| status.equals(ProjectAssignmentStatus.Status.OVER_OVERRUN))
			{
				
			}
		}
		
	}
	

	/**
	 * Set message that the hours are saved
	 * @param timesheet
	 * @param target
	 */
	private Label updatePostPersistMessage(Timesheet timesheet)
	{
		// server message
		IModel model = new StringResourceModel("timesheet.weekSaved", 
													TimesheetPanel.this, 
													null,
													new Object[]{timesheet.getTotalBookedHours(),
																	new DateModel(timesheet.getWeekStart(), config, DateModel.DATESTYLE_FULL_SHORT),
																	new DateModel(timesheet.getWeekEnd(), config, DateModel.DATESTYLE_FULL_SHORT)});

		return updateServerMessage(model);
		
	}
	
	private Label updateErrorMessage()
	{
		IModel model = new StringResourceModel("timesheet.errorPersist",
													TimesheetPanel.this, 
													null,
													new Object[]{});

		return updateServerMessage(model);
	}
	
	/**
	 * Update server message
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
	private void addDateLabels(WebMarkupContainer parent, Timesheet timesheet)
	{
		Label	label;
		
		for (int i = 0; i < CommonWebUtil.weekDays.length; i++)
		{
			label = new Label(CommonWebUtil.weekDays[i] + "Label", new DateModel(timesheet.getDateSequence()[i], config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
			label.setEscapeModelStrings(false);
			parent.add(label);
		}
	}
	
	/**
	 * Move to next week after succesfull form submit or week navigation
	 * @param target
	 */
	private void moveWeek(Date onScreenDate, AjaxRequestTarget target, int weekDiff)
	{
		EhourWebSession session = (EhourWebSession)getSession();
		Calendar	cal = DateUtil.getCalendar(config);
		int			event;

		cal.setTime(onScreenDate);
		cal.add(Calendar.WEEK_OF_YEAR, weekDiff);

		// should update calendar as well
		event = CommonWebUtil.AJAX_CALENDARPANEL_WEEK_NAV;
		session.setNavCalendar(cal);
		
		((AjaxAwareContainer)getPage()).ajaxRequestReceived(target, event);
	}
	
	/**
	 * Persist timesheet entries
	 * @param timesheet
	 * @throws OverBudgetException 
	 */
	private List<ProjectAssignmentStatus> persistTimesheetEntries(Timesheet timesheet)
	{
		List<TimesheetEntry>	timesheetEntries = new ArrayList<TimesheetEntry>();
		
		Collection<List<TimesheetRow>> rows = timesheet.getCustomers().values();
		
		for (List<TimesheetRow> list : rows)
		{
			for (TimesheetRow timesheetRow : list)
			{
				timesheetEntries.addAll(timesheetRow.getTimesheetEntries());
			}
		}
		
		// check comment id
		if (timesheet.getComment().getCommentId() == null)
		{
			TimesheetCommentId id = new TimesheetCommentId();
			id.setUserId(timesheet.getUser().getUserId());
			id.setCommentDate(timesheet.getWeekStart());
			
			timesheet.getComment().setCommentId(id);
		}
		
		return timesheetService.persistTimesheet(timesheetEntries, timesheet.getComment());
	}
	
	/**
	 * Build form
	 * @param parent
	 * @param timesheet
	 */
	private GrandTotal buildForm(final Form form, WebMarkupContainer parent, final Timesheet timesheet)
	{
		final GrandTotal	grandTotals = new GrandTotal();
		
		ListView customers = new ListView("customers", new ArrayList<Customer>(timesheet.getCustomers().keySet()))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				final Customer	customer = (Customer)item.getModelObject();

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
//				item.add(new Label("customerDesc", customer.getDescription()));
				
				item.add(new TimesheetRowList("rows", timesheet.getCustomers().get(customer), hidden, grandTotals, form));
			}
		};
		customers.setReuseItems(true);
		
		parent.add(customers);
		
		return grandTotals;
	}

	/**
	 * Add fold link
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
				foldPreference.toggleFolded();
				userService.persistCustomerFoldPreference(foldPreference);
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
	 * @param customer
	 * @return
	 */
	private Label getCustomerLabel(final Customer customer, final CustomerFoldPreference foldPreference)
	{
		Label	label;
		label = new Label("customer", customer.getName());

		label.add(new AjaxEventBehavior("onclick")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target)
			{
				foldPreference.toggleFolded();
				userService.persistCustomerFoldPreference(foldPreference);
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
	 * Get timesheet for week
	 * @param user
	 * @param forWeek
	 */
	private Timesheet getTimesheet(User user, Calendar forWeek)
	{
		WeekOverview	weekOverview;
		Timesheet		timesheet;
		
		weekOverview = timesheetService.getWeekOverview(user, forWeek);
		
		timesheet = getTimesheetAssembler(config).createTimesheetForm(weekOverview);
		
		return timesheet;
	}

	/**
	 * Get timesheet assembler
	 * @param config
	 * @return
	 */
	protected TimesheetAssembler getTimesheetAssembler(EhourConfig config)
	{
		return new TimesheetAssembler(config);
	}
	
	/**
	 * Add javascript with replaced images
	 * TODO add js directly to header
	 * @param container
	 */
	private void addJavascript(WebMarkupContainer container)
	{
		PackagedTextTemplate js = new PackagedTextTemplate(TimesheetPanel.class, "js/timesheet.js");

		Map<String, CharSequence> map = new HashMap<String, CharSequence>();
		
		add(TextTemplateHeaderContributor.forJavaScript(js, new Model((Serializable)map)));
	}	
}
