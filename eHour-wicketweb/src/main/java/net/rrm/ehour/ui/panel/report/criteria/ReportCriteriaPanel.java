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
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdate;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.report.criteria.filter.ReportCriteriaSelectorPanel;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickMonth;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickMonthRenderer;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickQuarter;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickQuarterRenderer;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeek;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeekRenderer;
import net.rrm.ehour.ui.renderers.DomainObjectChoiceRenderer;
import net.rrm.ehour.ui.sort.CustomerComparator;
import net.rrm.ehour.ui.sort.ProjectComparator;
import net.rrm.ehour.ui.sort.UserComparator;
import net.rrm.ehour.ui.sort.UserDepartmentComparator;
import net.rrm.ehour.ui.util.CommonUIStaticData;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;
import org.wicketstuff.dojo.toggle.DojoFadeToggle;

/**
 * Report criteria panel
 **/

@SuppressWarnings("serial")
public class ReportCriteriaPanel extends Panel implements AjaxAwareContainer
{
	private static final long serialVersionUID = -7865322191390719584L;
	
	@SpringBean
	private	ReportCriteriaService	reportCriteriaService;
	private DojoDatePicker 			startDatePicker;
	private DojoDatePicker 			endDatePicker;
	private	ListMultipleChoice 		projects;
	private	ListMultipleChoice 		users;
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public ReportCriteriaPanel(String id, ReportCriteria criteria)
	{
		super(id, new CompoundPropertyModel(new ReportCriteriaBackingBean(criteria)));
		
		sortReportCriteria(criteria);
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);	
		
		addDates(greyBorder);
		
		addCustomerSelection(greyBorder);
		addProjectSelection(greyBorder);
		addUserDepartmentSelection(greyBorder);
		addUserSelection(greyBorder);
	}
	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		switch (type)
		{
			case CommonUIStaticData.AJAX_ENTRYSELECTOR_FILTER_CHANGE:
			{
				EntrySelectorFilter	filter = (EntrySelectorFilter)params;
				updateFilterChange(filter);
				break;
			}
		}
	}	
	
	/**
	 * Update the appropiate selector list
	 * @param filter
	 */
	private void updateFilterChange(EntrySelectorFilter filter)
	{
		ReportCriteriaBackingBean backingBean = getBackingBeanFromModel();

		// TODO this could be a bit cleaner by setting the target directly in the model
		if (filter.getOnId().equals("projectList"))
		{
			backingBean.getReportCriteria().getUserCriteria().setOnlyActiveProjects(filter.isActivateToggle());
			updateReportCriteria(ReportCriteriaUpdate.UPDATE_PROJECTS);
		}
		else if (filter.getOnId().equals("customerList"))
		{
			backingBean.getReportCriteria().getUserCriteria().setOnlyActiveCustomers(filter.isActivateToggle());
			updateReportCriteria(ReportCriteriaUpdate.UPDATE_CUSTOMERS);
		}
		else if (filter.getOnId().equals("userList"))
		{
			backingBean.getReportCriteria().getUserCriteria().setOnlyActiveUsers(filter.isActivateToggle());
			backingBean.getReportCriteria().getUserCriteria().setUserFilter(filter.getCleanFilterInput());
			updateReportCriteria(ReportCriteriaUpdate.UPDATE_USERS);
		}
		
	}
	
	/**
	 * Update report criteria
	 * @param filter
	 */
	private void updateReportCriteria(ReportCriteriaUpdate updateType)
	{
		ReportCriteriaBackingBean backingBean = getBackingBeanFromModel();
		ReportCriteria reportCriteria = reportCriteriaService.syncUserReportCriteria(backingBean.getReportCriteria(), updateType);
		sortReportCriteria(reportCriteria);
		backingBean.setReportCriteria(reportCriteria);
	}

	/**
	 * Get backingbean from model
	 * @return
	 */
	private ReportCriteriaBackingBean getBackingBeanFromModel()
	{
		return (ReportCriteriaBackingBean)getModel().getObject();
	}
	
	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addUserSelection(WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("itemListHolder", "userListHolder", ReportCriteriaPanel.this);
		Form form = new Form("userSelection");

		users = new ListMultipleChoice("reportCriteria.availableCriteria.users",
								new PropertyModel(getModel(), "reportCriteria.userCriteria.users"),
								new PropertyModel(getModel(), "reportCriteria.availableCriteria.users"),
								new DomainObjectChoiceRenderer());
		users.setOutputMarkupId(true);
		users.setMaxRows(4);
		
		form.add(users);
		fragment.add(form);
		
		ReportCriteriaSelectorPanel entrySelectorPanel = new ReportCriteriaSelectorPanel("userList", 
																		fragment,
																		new StringResourceModel("report.filter", this, null),
																		new StringResourceModel("report.hideInactive", this, null),
																		this);

		parent.add(entrySelectorPanel);
	}	
	
	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addUserDepartmentSelection(WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("itemListHolder", "departmentListHolder", ReportCriteriaPanel.this);
		Form form = new Form("departmentSelection");

		ListMultipleChoice depts = new ListMultipleChoice("reportCriteria.availableCriteria.userDepartments",
								new PropertyModel(getModel(), "reportCriteria.userCriteria.userDepartments"),
								new PropertyModel(getModel(), "reportCriteria.availableCriteria.userDepartments"),
								new DomainObjectChoiceRenderer());
		depts.setMaxRows(4);
		
		depts.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			protected void onUpdate(AjaxRequestTarget target)
            {
				// show only projects for selected users
				updateReportCriteria(ReportCriteriaUpdate.UPDATE_USERS);
                target.addComponent(users);
            }
        });	
		
		form.add(depts);
		fragment.add(form);
		
		ReportCriteriaSelectorPanel entrySelectorPanel = new ReportCriteriaSelectorPanel("departmentList", 
																		fragment,
																		null,
																		null,
																		this);

		parent.add(entrySelectorPanel);
	}	
	
	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addCustomerSelection(WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("itemListHolder", "customerListHolder", ReportCriteriaPanel.this);
		Form form = new Form("customerSelection");

		ListMultipleChoice customers = new ListMultipleChoice("reportCriteria.availableCriteria.customers",
								new PropertyModel(getModel(), "reportCriteria.userCriteria.customers"),
								new PropertyModel(getModel(), "reportCriteria.availableCriteria.customers"),
								new DomainObjectChoiceRenderer());
		customers.setMaxRows(4);
		customers.setOutputMarkupId(true);
		
		customers.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			protected void onUpdate(AjaxRequestTarget target)
            {
				// show only projects for selected customers
				updateReportCriteria(ReportCriteriaUpdate.UPDATE_PROJECTS);
                target.addComponent(projects);
            }
        });	
		
		form.add(customers);
		fragment.add(form);
		
		ReportCriteriaSelectorPanel entrySelectorPanel = new ReportCriteriaSelectorPanel("customerList", 
																		fragment,
																		null,
																		new StringResourceModel("report.hideInactive", this, null),
																		this);

		parent.add(entrySelectorPanel);
	}
	
	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addProjectSelection(WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("itemListHolder", "projectListHolder", ReportCriteriaPanel.this);
		Form form = new Form("projectSelection");

		projects = new ListMultipleChoice("reportCriteria.availableCriteria.projects",
											new PropertyModel(getModel(), "reportCriteria.userCriteria.projects"),
											new PropertyModel(getModel(), "reportCriteria.availableCriteria.projects"),
											new DomainObjectChoiceRenderer());
		projects.setMaxRows(4);
		projects.setOutputMarkupId(true);
		form.add(projects);
		fragment.add(form);
		
		ReportCriteriaSelectorPanel entrySelectorPanel = new ReportCriteriaSelectorPanel("projectList", 
																		fragment,
																		null,
																		new StringResourceModel("report.hideInactive", this, null),
																		this);

		parent.add(entrySelectorPanel);
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

	/**
	 * 
	 * @param reportCriteria
	 */
	private void sortReportCriteria(ReportCriteria reportCriteria)
	{
		Collections.sort(reportCriteria.getAvailableCriteria().getCustomers(), new CustomerComparator());
		Collections.sort(reportCriteria.getAvailableCriteria().getProjects(), new ProjectComparator());
		Collections.sort(reportCriteria.getAvailableCriteria().getUserDepartments(), new UserDepartmentComparator());
		Collections.sort(reportCriteria.getAvailableCriteria().getUsers(), new UserComparator(false));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
	{
		ajaxRequestReceived(target, type, null);
	}
}
