/**
 * Created on Jul 9, 2007
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

import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.panel.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.panel.timesheet.dto.ProjectTotalModel;
import net.rrm.ehour.ui.panel.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.validator.DoubleRangeWithNullValidator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Representation of a timesheet row
 **/

public class TimesheetRowList extends ListView
{
	private static final long serialVersionUID = -6905022018110510887L;

	private int 			counter;
	private final boolean 	hidden;
	private	EhourConfig		config;
	private final GrandTotal	grandTotals;
	private	Form			form;
	
	/**
	 * 
	 * @param id
	 * @param model
	 * @param hidden
	 */
	public TimesheetRowList(String id, final List<TimesheetRow> model, boolean hidden, GrandTotal grandTotals, Form form)
	{
		super(id, model);
		setReuseItems(true);
		counter = 1;
		this.hidden = hidden;
		this.grandTotals = grandTotals;
		this.form = form;
		
		config = ((EhourWebSession)getSession()).getEhourConfig();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
	 */
	@Override
	protected void populateItem(ListItem item)
	{
		final TimesheetRow row = (TimesheetRow) item.getModelObject();

		// add id to row
		item.add(new AttributeModifier("id", true, new AbstractReadOnlyModel()
		{
			public Object getObject()
			{
				return "pw" + row.getProjectAssignment().getProject().getCustomer().getCustomerId().toString() + counter++;
			}
		}));

		item.add(new Label("project", row.getProjectAssignment().getProject().getName()));
		item.add(new Label("projectCode", row.getProjectAssignment().getProject().getProjectCode()));

		item.add(createValidatedTextField("sunday", row, 0));
		item.add(createValidatedTextField("monday", row, 1));
		item.add(createValidatedTextField("tuesday", row, 2));
		item.add(createValidatedTextField("wednesday", row, 3));
		item.add(createValidatedTextField("thursday", row, 4));
		item.add(createValidatedTextField("friday", row, 5));
		item.add(createValidatedTextField("saturday", row, 6));

		Label	totalHours = new Label("total", new FloatModel(new ProjectTotalModel(row), config));
		totalHours.setOutputMarkupId(true);
		item.add(totalHours);

		if (hidden)
		{
			item.add(new AttributeModifier("style", true, new AbstractReadOnlyModel()
			{
				public Object getObject()
				{
					return "display: none";
				}
			}));
		}
	}
	
	/**
	 * Get validated text field
	 * @param id
	 * @param row
	 * @param index
	 * @return
	 */
	private TextField createValidatedTextField(String id, TimesheetRow row, int index)
	{
		final TimesheetTextField	dayInput;
		
		dayInput = new TimesheetTextField(id, new FloatModel(
										new PropertyModel(row, "timesheetCells[" + index + "].timesheetEntry.hours"), config, null),
										Float.class
									);
		dayInput.add(new DoubleRangeWithNullValidator(0, 24));
		dayInput.setOutputMarkupId(true);
		
		AjaxFormValidatingBehavior behavior = new AjaxFormValidatingBehavior(form, "onchange")
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				// update the project total
				target.addComponent(dayInput.getParent().get("total"));
				
				// TODO bit brittle
				System.out.println(((MarkupContainer)dayInput.findParent(Form.class).get("blueFrame")).get("grandTotal"));
				
				form.visitFormComponents(new FormHighlighter(target));
			}		
			
			@Override
			protected void onError(AjaxRequestTarget target)
			{
				form.visitFormComponents(new FormHighlighter(target));
			}			
		};
		
		dayInput.add(behavior);
		
		// TODO make dynamic model
		if (row.getTimesheetCells()[index] != null 
				&& row.getTimesheetCells()[index].getTimesheetEntry() != null 
				&& row.getTimesheetCells()[index].getTimesheetEntry().getHours() != null)
		{
			grandTotals.addValue(index, row.getTimesheetCells()[index].getTimesheetEntry().getHours().floatValue());
		}
		
		return dayInput;
	}
}