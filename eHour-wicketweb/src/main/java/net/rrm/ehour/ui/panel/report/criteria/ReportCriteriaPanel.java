/**
 * Created on Sep 18, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.panel.report.criteria;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickMonth;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickMonthRenderer;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickQuarter;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickQuarterRenderer;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeek;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeekRenderer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;
import org.wicketstuff.dojo.toggle.DojoFadeToggle;

/**
 * Report criteria panel
 **/

public class ReportCriteriaPanel extends Panel
{
	private static final long serialVersionUID = -7865322191390719584L;
	private DojoDatePicker 	startDatePicker;
	private DojoDatePicker 	endDatePicker;
	
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public ReportCriteriaPanel(String id, ReportCriteria criteria)
	{
		super(id, new CompoundPropertyModel(new ReportCriteriaBackingBean(criteria)));
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);	
		
		addDates(greyBorder);
	}

	/**
	 * Add dates
	 * @param parent
	 */
	private void addDates(WebMarkupContainer parent)
	{
		startDatePicker = new DojoDatePicker("reportCriteria.userCriteria.reportRange.dateStart", "dd/MM/yyyy");
		startDatePicker.setToggle(new DojoFadeToggle(200));
		startDatePicker.setOutputMarkupId(true);
		parent.add(startDatePicker);

		endDatePicker = new DojoDatePicker("reportCriteria.userCriteria.reportRange.dateEnd", "dd/MM/yyyy");
		endDatePicker.setToggle(new DojoFadeToggle(200));
		endDatePicker.setOutputMarkupId(true);
		parent.add(endDatePicker);
		
		addQuickWeek(parent);
		addQuickMonth(parent);
		addQuickQuarter(parent);
	}
	
	/**
	 * Add quick week selection
	 * @param parent
	 */
	private void addQuickWeek(WebMarkupContainer parent)
	{
		List<QuickWeek>	weeks = new ArrayList<QuickWeek>();	
		Calendar		currentDate = new GregorianCalendar();
		int 			currentWeek = -8;
		
		currentDate.add(Calendar.WEEK_OF_YEAR, currentWeek);
		
		for (; currentWeek < 8; currentWeek++)
		{
			weeks.add(new QuickWeek(currentDate));
			
			currentDate.add(Calendar.WEEK_OF_YEAR, 1);
		}
		
		final DropDownChoice quickWeekSelection = new DropDownChoice("quickWeek", weeks, new QuickWeekRenderer());

		quickWeekSelection.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(startDatePicker);
				target.addComponent(endDatePicker);
			}
			
		});
		
		parent.add(quickWeekSelection);
	}
	
	/**
	 * Add quick month selection
	 * @param parent
	 */
	private void addQuickMonth(WebMarkupContainer parent)
	{
		List<QuickMonth>	months = new ArrayList<QuickMonth>();	
		Calendar			currentDate = new GregorianCalendar();
		int 				currentMonth = -6;
		
		currentDate.add(Calendar.MONTH, currentMonth);
		
		for (; currentMonth < 6; currentMonth++)
		{
			months.add(new QuickMonth(currentDate));
			
			currentDate.add(Calendar.MONTH, 1);
		}
		
		final DropDownChoice quickMonthSelection = new DropDownChoice("quickMonth", months, new QuickMonthRenderer());

		quickMonthSelection.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(startDatePicker);
				target.addComponent(endDatePicker);
			}
			
		});
		
		parent.add(quickMonthSelection);
	}	
	
	
	/**
	 * Add quick month selection
	 * @param parent
	 */
	private void addQuickQuarter(WebMarkupContainer parent)
	{
		List<QuickQuarter>	quarters = new ArrayList<QuickQuarter>();	
		Calendar			currentDate = new GregorianCalendar();
		int 				currentQuarter = -3;
		
		int quarter = currentDate.get(Calendar.MONTH) / 3;
		currentDate.set(Calendar.MONTH, quarter * 3); // abuse rounding off
		currentDate.add(Calendar.MONTH, currentQuarter * 3);
		
		for (; currentQuarter < 3; currentQuarter++)
		{
			quarters.add(new QuickQuarter(currentDate));
			
			currentDate.add(Calendar.MONTH, 3);
		}
		
		final DropDownChoice quickQuarterSelection = new DropDownChoice("quickQuarter", quarters, new QuickQuarterRenderer());

		quickQuarterSelection.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(startDatePicker);
				target.addComponent(endDatePicker);
			}
			
		});
		
		parent.add(quickQuarterSelection);
	}	
}
