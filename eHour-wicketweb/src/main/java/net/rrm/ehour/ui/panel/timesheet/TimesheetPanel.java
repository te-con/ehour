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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.panel.timesheet.util.TimesheetAssembler;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * The main panel - timesheet form
 **/

public class TimesheetPanel extends Panel
{
	private static final long serialVersionUID = 7704288648724599187L;

	@SpringBean
	private TimesheetService	timesheetService;
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
		
		config = session.getEhourConfig();
		
		dateFormatter = new SimpleDateFormat("w, MMMM yyyy", config.getLocale());

		// dom id's
		this.setOutputMarkupId(true);
		
		// grey & blue frame border
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("timesheetFrame", "Week " + dateFormatter.format(forWeek.getTime()));
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");

		add(greyBorder);
		greyBorder.add(blueBorder);

		// the timesheet we're working on
		final Timesheet timesheet = getTimesheet(user,  forWeek);
		
		// add form
		Form timesheetForm = new Form("timesheetForm");
		timesheetForm.setOutputMarkupId(true);
		
		// submit is by ajax
		timesheetForm.add(new AjaxSubmitButton("submitButton", timesheetForm)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                persistTimesheetEntries(timesheet);
                moveToNextWeek(timesheet.getWeekStart(), target);
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}			
//			@Override
//            protected void onError(AjaxRequestTarget target, Form form) {
//                // repaint the feedback panel so errors are shown
//                target.addComponent(feedback);
//            }
        });	
		
		// add form labels
		buildForm(timesheetForm, timesheet);
		blueBorder.add(timesheetForm);
		
		// add label dates
		addDateLabels(blueBorder, timesheet);
		
	}

//	private void addWeekNavigations(Component parent)
//	{
//	}
	
	/**
	 * 
	 */
	private void addDateLabels(WebMarkupContainer parent, Timesheet timesheet)
	{
		parent.add(new Label("sundayLabel", new DateModel(timesheet.getDateSequence()[0], config, DateModel.DATESTYLE_DAYLONG )));
		parent.add(new Label("mondayLabel", new DateModel(timesheet.getDateSequence()[1], config, DateModel.DATESTYLE_DAYLONG )));
		parent.add(new Label("tuesdayLabel", new DateModel(timesheet.getDateSequence()[2], config, DateModel.DATESTYLE_DAYLONG )));
		parent.add(new Label("wednesdayLabel", new DateModel(timesheet.getDateSequence()[3], config, DateModel.DATESTYLE_DAYLONG )));
		parent.add(new Label("thursdayLabel", new DateModel(timesheet.getDateSequence()[4], config, DateModel.DATESTYLE_DAYLONG )));
		parent.add(new Label("fridayLabel", new DateModel(timesheet.getDateSequence()[5], config, DateModel.DATESTYLE_DAYLONG )));
		parent.add(new Label("saturdayLabel", new DateModel(timesheet.getDateSequence()[6], config, DateModel.DATESTYLE_DAYLONG )));
		
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
		
		timesheetService.persistTimesheet(timesheetEntries, null);
	}
	
	/**
	 * Build form
	 * @param parent
	 * @param timesheet
	 */
	private void buildForm(Form form, final Timesheet timesheet)
	{
		ListView customers = new ListView("customers", new ArrayList<Customer>(timesheet.getCustomers().keySet()))
		{
			{
				// TODO
				setReuseItems(true);
			}
			
			@Override
			protected void populateItem(ListItem item)
			{
				Customer	customer = (Customer)item.getModelObject();
				
				item.add(new Label("customer", customer.getName()));
//				item.add(new Label("customerDesc", customer.getDescription()));
				
				item.add(new TimesheetRowList("rows", timesheet.getCustomers().get(customer)));
			}
		};
		
		form.add(customers);
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
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class TimesheetRowList extends ListView
	{
		TimesheetRowList(String id, final List model)
		{
			super(id, model);
			setReuseItems(true);
		}
		
		@Override
		protected void populateItem(ListItem item)
		{
			TimesheetRow	row = (TimesheetRow)item.getModelObject();
			
			item.add(new Label("project", row.getProjectAssignment().getProject().getName()));
			item.add(new Label("projectCode", row.getProjectAssignment().getProject().getProjectCode()));
//			item.add(new Label("projectDesc", row.getProjectAssignment().getProject().getDescription()));
			
			item.add(new TextField("sunday", new PropertyModel(row, "timesheetCells[0].timesheetEntry.hours")));
			item.add(new TextField("monday", new PropertyModel(row, "timesheetCells[1].timesheetEntry.hours")));
			item.add(new TextField("tuesday", new PropertyModel(row, "timesheetCells[2].timesheetEntry.hours")));
			item.add(new TextField("wednesday", new PropertyModel(row, "timesheetCells[3].timesheetEntry.hours")));
			item.add(new TextField("thursday", new PropertyModel(row, "timesheetCells[4].timesheetEntry.hours")));
			item.add(new TextField("friday", new PropertyModel(row, "timesheetCells[5].timesheetEntry.hours")));
			item.add(new TextField("saturday", new PropertyModel(row, "timesheetCells[6].timesheetEntry.hours")));
		}
	}
}
