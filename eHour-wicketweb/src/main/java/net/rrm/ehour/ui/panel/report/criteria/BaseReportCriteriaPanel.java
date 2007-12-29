/**
 * Created on Dec 29, 2007
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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickMonth;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickMonthRenderer;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickQuarter;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickQuarterRenderer;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeek;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeekRenderer;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonUIStaticData;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;
import org.wicketstuff.dojo.toggle.DojoFadeToggle;

/**
 * Base report criteria panel which adds the quick date selections
 **/

public abstract class BaseReportCriteriaPanel extends Panel
{
	private DojoDatePicker 			startDatePicker;
	private DojoDatePicker 			endDatePicker;

	
	/**
	 * Constructor which sets up the basic borded form
	 * @param id
	 * @param model
	 */
	public BaseReportCriteriaPanel(String id, IModel model)
	{
		super(id, model);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", 600);
		add(greyBorder);
		
		setOutputMarkupId(true);	
		
		Form form = new Form("criteriaForm");
		greyBorder.add(form);
		
		addDates(form);

		fillCriteriaForm(form);
		
		addCreateReportSubmit(form);		
	}

	/**
	 * Add specific criteria to form
	 * @param form
	 */
	protected abstract void fillCriteriaForm(Form form);
	
	/**
	 * Add submit link
	 * @param parent
	 */
	private void addCreateReportSubmit(Form form)
	{
		AjaxButton submitButton = new AjaxButton("createReport", form)
		{
			private static final long serialVersionUID = 4373085964708354107L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				((AjaxAwareContainer)getPage()).ajaxRequestReceived(target, CommonUIStaticData.AJAX_FORM_SUBMIT);
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}
        };
        
		// default submit
		form.add(submitButton);
	}	
	
	/**
	 * Add dates
	 * @param parent
	 */
	protected void addDates(Form form)
	{
		EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		startDatePicker = new DojoDatePicker("reportCriteria.userCriteria.reportRange.dateStart", 
				DateUtil.getPatternForDateLocale(config.getLocale()));
		
		startDatePicker.setToggle(new DojoFadeToggle(200));
		startDatePicker.setOutputMarkupId(true);
		startDatePicker.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			private static final long serialVersionUID = -5588313671121851508L;

			protected void onUpdate(AjaxRequestTarget target)
            {
				// don't do anything, just update the model. bit of a workaround
				// needed since everything is in it's own model
            }
        });			
		
		form.add(startDatePicker);

		endDatePicker = new DojoDatePicker("reportCriteria.userCriteria.reportRange.dateEnd",
											DateUtil.getPatternForDateLocale(config.getLocale()));
		endDatePicker.setToggle(new DojoFadeToggle(200));
		endDatePicker.setOutputMarkupId(true);
		endDatePicker.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			private static final long serialVersionUID = -6093637632560292532L;

			protected void onUpdate(AjaxRequestTarget target)
            {
				// don't do anything, just update the model. bit of a workaround
				// needed since everything is in it's own model
            }
        });			
	
		form.add(endDatePicker);
		
		addQuickWeek(form);
		addQuickMonth(form);
		addQuickQuarter(form);
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
			private static final long serialVersionUID = 507045565542332885L;

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
			private static final long serialVersionUID = 4659137545215879099L;

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
			private static final long serialVersionUID = -2936644573667701113L;

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
