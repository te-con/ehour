/**
 * Created on Jun 21, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.timesheet.domain.TimesheetComment;
import net.rrm.ehour.timesheet.domain.TimesheetCommentId;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.ajax.OnClickDecorator;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.component.KeepAliveTextArea;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.panel.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.panel.timesheet.util.TimesheetAssembler;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.CustomerFoldPreference;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.JavaScriptReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

/**
 * The main panel - timesheet form
 * TODO split up
 **/

public class TimesheetPanel extends Panel implements Serializable
{
	private static final long serialVersionUID = 7704288648724599187L;

	@SpringBean
	private TimesheetService	timesheetService;
	@SpringBean
	private UserService			userService;
	
	private	EhourConfig			config;

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
		SimpleDateFormat	dateFormatter;
		GrandTotal			grandTotals = new GrandTotal();
		
		config = session.getEhourConfig();
		dateFormatter = new SimpleDateFormat("w, MMMM yyyy", config.getLocale());

		// dom id's
		this.setOutputMarkupId(true);
		
		// grey & blue frame border
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("timesheetFrame", "Week " + dateFormatter.format(forWeek.getTime()));
		add(greyBorder);

		// the timesheet we're working on
		final Timesheet timesheet = getTimesheet(user,  forWeek);

		// add form
		Form timesheetForm = new Form("timesheetForm");
		timesheetForm.setOutputMarkupId(true);
		greyBorder.add(timesheetForm);
		
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		timesheetForm.add(blueBorder);

		// setup form
		grandTotals = buildForm(blueBorder, timesheet);
		
		// add last row with grand totals
		addGrandTotals(blueBorder, grandTotals);

		// attach onsubmit ajax events
		setSubmitActions(timesheetForm, timesheet);
		
		// add label dates
		addDateLabels(blueBorder, timesheet);

		createCommentsInput(timesheetForm, timesheet);
		
		// TODO replace with dojo widget
//		add(new FeedbackPanel("feedback"));
		
		// add CSS & JS
		add(new StyleSheetReference("timesheetStyle", new CompressedResourceReference(TimesheetPanel.class, "style/timesheetForm.css")));
		add(new JavaScriptReference("timesheetJS", new CompressedResourceReference(TimesheetPanel.class, "js/timesheet.js")));
	}

	/**
	 * Create comments input
	 * @param parent
	 * @param timesheet
	 */
	private void createCommentsInput(WebMarkupContainer parent, Timesheet timesheet)
	{
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("commentsFrame");
		
		if (timesheet.getComment() == null)
		{
			timesheet.setComment(new TimesheetComment());
		}
		
		TextArea	textArea = new KeepAliveTextArea("commentsArea", new PropertyModel(timesheet, "comment.comment"));
		
		blueBorder.add(textArea);
		parent.add(blueBorder);
	}
	
	/**
	 * Add grand totals to form
	 * @param parent
	 * @param grandTotals
	 */
	private void addGrandTotals(WebMarkupContainer parent, GrandTotal grandTotals)
	{
		parent.add(new Label("sundayTotal", new FloatModel(new PropertyModel(grandTotals, "getValues[0]"), config)));
		parent.add(new Label("mondayTotal", new FloatModel(new PropertyModel(grandTotals, "getValues[1]"), config)));
		parent.add(new Label("tuesdayTotal", new FloatModel(new PropertyModel(grandTotals, "getValues[2]"), config)));
		parent.add(new Label("wednesdayTotal", new FloatModel(new PropertyModel(grandTotals, "getValues[3]"), config)));
		parent.add(new Label("thursdayTotal", new FloatModel(new PropertyModel(grandTotals, "getValues[4]"), config)));
		parent.add(new Label("fridayTotal", new FloatModel(new PropertyModel(grandTotals, "getValues[5]"), config)));
		parent.add(new Label("saturdayTotal", new FloatModel(new PropertyModel(grandTotals, "getValues[6]"), config)));
		
		parent.add(new Label("grandTotal", new FloatModel(new PropertyModel(grandTotals, "grandTotal"), config)));
	}
	
	/**
	 * Set submit actions for form
	 * @param form
	 * @param timesheet
	 */
	private void setSubmitActions(Form form, final Timesheet timesheet)
	{
		// submit is by ajax
		form.add(new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
                persistTimesheetEntries(timesheet);
                moveToNextWeek(timesheet.getWeekStart(), target);
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}			
			
			protected void onError(final AjaxRequestTarget target, Form form)
			{
                form.visitFormComponents(new FormHighlighter(target));
            }
        });			

		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onchange",  Duration.seconds(3));
	}
	
	/**
	 * Form validation
	 * @author Thies
	 *
	 */
	private class FormHighlighter implements FormComponent.IVisitor, Serializable 
    {
		private transient AjaxRequestTarget	target;
		public FormHighlighter(AjaxRequestTarget target)
		{
			this.target = target;
		}
        public Object formComponent(IFormVisitorParticipant visitor)
        {
        	FormComponent formComponent = (FormComponent)visitor;
            
            if (!formComponent.isValid())
            {
            	formComponent.add(new AttributeModifier("style", true, new AbstractReadOnlyModel()
	            {
	                public Object getObject()
	                {
	                    return "color: #ff0000";
	                }
	            }));                        	

                target.addComponent(formComponent);
            }
            
            return formComponent;
        }
    }
	
	/**
	 * Add date labels (sun/mon etc)
	 */
	private void addDateLabels(WebMarkupContainer parent, Timesheet timesheet)
	{
		Label	label;
		
		label = new Label("sundayLabel", new DateModel(timesheet.getDateSequence()[0], config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
		label.setEscapeModelStrings(false);
		parent.add(label);

		label = new Label("mondayLabel", new DateModel(timesheet.getDateSequence()[1], config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
		label.setEscapeModelStrings(false);
		parent.add(label);

		label = new Label("tuesdayLabel", new DateModel(timesheet.getDateSequence()[2], config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
		label.setEscapeModelStrings(false);
		parent.add(label);

		label = new Label("wednesdayLabel", new DateModel(timesheet.getDateSequence()[3], config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
		label.setEscapeModelStrings(false);
		parent.add(label);

		Label labelA = new Label("thursdayLabel", new DateModel(timesheet.getDateSequence()[4], config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
		labelA.setEscapeModelStrings(false);
		parent.add(labelA);
		
		label = new Label("fridayLabel", new DateModel(timesheet.getDateSequence()[5], config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
		label.setEscapeModelStrings(false);
		parent.add(label);

		label = new Label("saturdayLabel", new DateModel(timesheet.getDateSequence()[6], config, DateModel.DATESTYLE_TIMESHEET_DAYLONG));
		label.setEscapeModelStrings(false);
		parent.add(label);
	}
	
	/**
	 * Move to next week after succesfull form submit
	 * @param target
	 */
	private void moveToNextWeek(Date onScreenDate, AjaxRequestTarget target)
	{
		EhourWebSession session = (EhourWebSession)getSession();
		Calendar	cal = DateUtil.getCalendar(config);
		int			event;

		cal.setTime(onScreenDate);
		cal.add(Calendar.WEEK_OF_YEAR, 1);

		// should update calendar as well
		event = CommonStaticData.AJAX_CALENDARPANEL_MONTH_CHANGE;
		session.setNavCalendar(cal);
		
		((BasePage)getPage()).ajaxRequestReceived(target, event);
	}
	
	/**
	 * Persist timesheet entries
	 * @param timesheet
	 */
	private void persistTimesheetEntries(Timesheet timesheet)
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
		
		timesheetService.persistTimesheet(timesheetEntries, timesheet.getComment());
	}
	
	/**
	 * Build form
	 * @param parent
	 * @param timesheet
	 */
	private GrandTotal buildForm(WebMarkupContainer parent, final Timesheet timesheet)
	{
		final GrandTotal	grandTotals = new GrandTotal();
		
		ListView customers = new ListView("customers", new ArrayList<Customer>(timesheet.getCustomers().keySet()))
		{
			{
				setReuseItems(true);
			}
			
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
				
				item.add(getCustomerLabel(customer, foldPreference));
//				item.add(new Label("customerDesc", customer.getDescription()));

				boolean hidden = (foldPreference != null && foldPreference.isFolded());
				
				item.add(new TimesheetRowList("rows", timesheet.getCustomers().get(customer), hidden, grandTotals));
			}
		};
		
		parent.add(customers);
		
		return grandTotals;
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
		
		TimesheetAssembler assembler = new TimesheetAssembler(config);
		timesheet = assembler.createTimesheetForm(weekOverview);
		
		return timesheet;
	}	
}
