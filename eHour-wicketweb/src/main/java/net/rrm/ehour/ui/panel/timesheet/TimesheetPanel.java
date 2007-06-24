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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.panel.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.panel.timesheet.util.TimesheetAssembler;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.user.domain.User;

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
	private TimesheetService		timesheetService;

	/**
	 * 
	 * @param id
	 * @param user
	 * @param forWeek
	 */
	public TimesheetPanel(String id, User user, Calendar forWeek)
	{
		super(id);
		this.setOutputMarkupId(true);
		
		EhourWebSession session = (EhourWebSession)getSession();
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("timesheetFrame", "Week");
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");

		add(greyBorder);
		greyBorder.add(blueBorder);
		
		final Timesheet timesheet = getTimesheet(user,  forWeek, session.getEhourConfig());
		
		Form timesheetForm = new Form("timesheetForm")
		{
			@Override
			public void onSubmit()
			{
				for (Customer customer : timesheet.getCustomers().keySet())
				{
					List<TimesheetRow> rows = timesheet.getCustomers().get(customer);
					
					System.out.println(customer.getName());
					
					for (TimesheetRow timesheetRow : rows)
					{
						for (int i = 0; i < 7; i++)
						{
							System.out.println(timesheetRow.getTimesheetCells()[i].getTimesheetEntry().getHours());
						}
						
					}
				}
			}
		};	
		
		buildForm(timesheetForm, timesheet);
		blueBorder.add(timesheetForm);
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
	
	private Timesheet getTimesheet(User user, Calendar forWeek, EhourConfig config)
	{
		WeekOverview	weekOverview;
		Timesheet		timesheet;
		
		weekOverview = timesheetService.getWeekOverview(user.getUserId(), forWeek);
		
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
			
			if (row.getTimesheetCells()[5] != null && row.getTimesheetCells()[5].getTimesheetEntry()!= null)
			{
				System.out.println(row.getTimesheetCells()[5].getTimesheetEntry().getHours());
			}
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
