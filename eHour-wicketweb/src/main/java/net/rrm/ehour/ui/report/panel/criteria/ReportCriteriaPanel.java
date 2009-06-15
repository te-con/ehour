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

package net.rrm.ehour.ui.report.panel.criteria;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxUtil;
import net.rrm.ehour.ui.common.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.renderers.DomainObjectChoiceRenderer;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.sort.CustomerComparator;
import net.rrm.ehour.ui.common.sort.ProjectComparator;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickDateAjaxEventType;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickDropDownChoice;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickMonth;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickMonthRenderer;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickQuarter;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickQuarterRenderer;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickWeek;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickWeekRenderer;
import net.rrm.ehour.ui.report.panel.criteria.type.ReportType;
import net.rrm.ehour.ui.report.panel.criteria.type.ReportTypeRenderer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Base report criteria panel which adds the quick date selections
 **/

public class ReportCriteriaPanel extends AbstractAjaxPanel 
{
	private static final long serialVersionUID = 161160822264046559L;
	
	@SpringBean
	private	ReportCriteriaService	reportCriteriaService;
	private DateTextField 			startDatePicker;
	private DateTextField 			endDatePicker;
	private	ListMultipleChoice 		projects;
	private FormComponent 			customers;
	private	ListMultipleChoice 		users;
	private ListMultipleChoice 		departments;
	private List<WebMarkupContainer>	quickSelections;
	
	/**
	 * Constructor which sets up the basic borded form
	 * @param id
	 * @param model
	 */
	public ReportCriteriaPanel(String id, IModel model)
	{
		this(id, model, true);
	}

	/**
	 * 
	 * @param id
	 * @param model
	 * @param multipleCustomer
	 */
	public ReportCriteriaPanel(String id, IModel model, boolean multipleCustomer)
	{
		super(id, model);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", WebGeo.W_CONTENT_WIDE);
		add(greyBorder);
		
		setOutputMarkupId(true);	
		
		Form form = new Form("criteriaForm");
		greyBorder.add(form);
		
		addDates(form, model);

		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("customerProjectsBorder");
		form.add(blueBorder);

		addCustomerSelection(blueBorder);
		addProjectSelection(blueBorder);
		addDepartmentsAndUsers(form);
		
		addCreateReportSubmit(form);	
		
		addReportTypeSelection(form);
	}	

	/**
	 * Add report type selection to form
	 * @param parent
	 */
	private void addReportTypeSelection(WebMarkupContainer parent)
	{
		List<ReportType>	reportTypes = new ArrayList<ReportType>();	

		reportTypes.add(ReportType.AGGREGATE);
		reportTypes.add(ReportType.DETAILED);
		
		final DropDownChoice reportTypeSelection = new DropDownChoice("reportType", reportTypes, new ReportTypeRenderer());
		reportTypeSelection.setRequired(true);
		reportTypeSelection.setLabel(new ResourceModel("report.type.name"));
		parent.add(new AjaxFormComponentFeedbackIndicator("reportTypeSelectionError", reportTypeSelection));
		parent.add(reportTypeSelection);	
		
	}
	
	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addCustomerSelection(WebMarkupContainer parent)
	{
		customers = new ListMultipleChoice("reportCriteria.userCriteria.customers",
				new PropertyModel(getModel(), "reportCriteria.availableCriteria.customers"),
				new DomainObjectChoiceRenderer());
		
		((ListMultipleChoice)customers).setMaxRows(4);	

		customers.setOutputMarkupId(true);

		// update projects when customer(s) selected
		customers.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			private static final long serialVersionUID = -5588313671121851508L;

			protected void onUpdate(AjaxRequestTarget target)
            {
				// show only projects for selected customers
				updateReportCriteria(ReportCriteriaUpdateType.UPDATE_PROJECTS);
                target.addComponent(projects);
            }
        });	
		
		parent.add(customers);
		
		// hide active/inactive customers checkbox 
		final AjaxCheckBox	deactivateBox = new AjaxCheckBox("reportCriteria.userCriteria.onlyActiveCustomers")
		{
			private static final long serialVersionUID = 2585047163449150793L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS);
				target.addComponent(customers);
			}
		};		
		
		parent.add(deactivateBox);
		
		Label filterToggleText = new Label("onlyActiveCustomersLabel", new ResourceModel("report.hideInactive"));
		parent.add(filterToggleText);
	}
	
	/**
	 * Add project selection
	 * @param parent
	 */
	private void addProjectSelection(WebMarkupContainer parent)
	{
		projects = new ListMultipleChoice("reportCriteria.userCriteria.projects",
											new PropertyModel(getModel(), "reportCriteria.availableCriteria.projects"),
											new DomainObjectChoiceRenderer());
		projects.setMaxRows(4);
		projects.setOutputMarkupId(true);
		parent.add(projects);
		
		// hide active/inactive projects checkbox 
		final AjaxCheckBox	deactivateBox = new AjaxCheckBox("reportCriteria.userCriteria.onlyActiveProjects")
		{
			private static final long serialVersionUID = 2585047163449150793L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				updateReportCriteria(ReportCriteriaUpdateType.UPDATE_PROJECTS);
				target.addComponent(projects);
			}
		};		
		
		parent.add(deactivateBox);
		
		Label filterToggleText = new Label("onlyActiveProjectsLabel", new ResourceModel("report.hideInactive"));
		parent.add(filterToggleText);		
	}		
	

	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addUserSelection(WebMarkupContainer parent)
	{
		users = new ListMultipleChoice("reportCriteria.userCriteria.users",
								new PropertyModel(getModel(), "reportCriteria.availableCriteria.users"),
								new DomainObjectChoiceRenderer());
		users.setOutputMarkupId(true);
		users.setMaxRows(4);
		parent.add(users);
		
		// hide active checkbox
		final AjaxCheckBox	deactivateBox = new AjaxCheckBox("reportCriteria.userCriteria.onlyActiveUsers")
		{
			private static final long serialVersionUID = 2585047163449150793L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				updateReportCriteria(ReportCriteriaUpdateType.UPDATE_USERS);
				target.addComponent(users);
			}
		};
		
		parent.add(deactivateBox);
		
		Label filterToggleText = new Label("onlyActiveUsersLabel", new ResourceModel("report.hideInactive"));
		parent.add(filterToggleText);
		
		// filter
		
//		
//		
//		ReportCriteriaSelectorPanel entrySelectorPanel = new ReportCriteriaSelectorPanel("userList", 
//																		fragment,
//																		new StringResourceModel("report.filter", this, null),
//																		new StringResourceModel("report.hideInactive", this, null),
//																		this);
//
//		parent.add(entrySelectorPanel);
	}	
	
	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addUserDepartmentSelection(WebMarkupContainer parent)
	{
		departments = new ListMultipleChoice("reportCriteria.userCriteria.userDepartments",
								new PropertyModel(getModel(), "reportCriteria.availableCriteria.userDepartments"),
								new DomainObjectChoiceRenderer());
		departments.setMaxRows(4);
		
		// update projects when customer(s) selected
		departments.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target)
            {
				// show only projects for selected customers
				updateReportCriteria(ReportCriteriaUpdateType.UPDATE_USERS);
                target.addComponent(users);
            }
        });			
		
		parent.add(departments);
	}	

	/**
	 * Add user departments and users
	 * @param form
	 */
	private void addDepartmentsAndUsers(Form form)
	{
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("deptUserBorder");
		form.add(blueBorder);
		
		addUserDepartmentSelection(blueBorder);
		addUserSelection(blueBorder);
	}	
	
	
	/**
	 * Update report criteria
	 * @param filter
	 */
	protected void updateReportCriteria(ReportCriteriaUpdateType updateType)
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
	protected ReportCriteriaBackingBean getBackingBeanFromModel()
	{
		return (ReportCriteriaBackingBean)getModel().getObject();
	}	
	
	/**
	 * Add submit link
	 * @param parent
	 */
	private void addCreateReportSubmit(Form form)
	{
		// Submit
		AjaxButton submitButton = new AjaxButton("createReport", form)
		{
			private static final long serialVersionUID = 4373085964708354107L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				AjaxUtil.publishAjaxEvent(this, new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_UPDATED));
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}
			
			
			@Override
            protected void onError(AjaxRequestTarget target, Form form)
			{
				target.addComponent(form);
            }			
        };
        
		// default submit
		form.add(submitButton);
		
		// reset button
		AjaxButton resetButton = new AjaxButton("resetCriteria")
		{
			private static final long serialVersionUID = 4373085964708354107L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				// reset user criteria
				ReportCriteriaBackingBean backingBean = getBackingBeanFromModel();
				backingBean.setQuickWeek(null);
				backingBean.setQuickMonth(null);
				backingBean.setQuickQuarter(null);
				
				ReportCriteria reportCriteria = new ReportCriteria(new UserCriteria());
				backingBean.setReportCriteria(reportCriteria);
				reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteriaUpdateType.UPDATE_ALL);
				
				target.addComponent(projects);
				target.addComponent(customers);
				target.addComponent(users);
				target.addComponent(departments);
				
				updateDates(target);
            }
			
			@Override
            protected void onError(AjaxRequestTarget target, Form form)
			{
				onSubmit(target, form);
            }				
        };	
        
        form.add(resetButton);
	}	
	
	/**
	 * Add dates
	 * @param parent
	 */
	protected void addDates(Form form, IModel model)
	{
        startDatePicker = new DateTextField("reportCriteria.userCriteria.reportRange.dateStart", new PropertyModel(model,
        														"reportCriteria.userCriteria.reportRange.dateStart"), new StyleDateConverter("S-", false));
        startDatePicker.add(new DatePicker());		
		
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

		endDatePicker = new DateTextField("reportCriteria.userCriteria.reportRange.dateEnd", new PropertyModel(model,
											"reportCriteria.userCriteria.reportRange.dateEnd"), new StyleDateConverter("S-", false));
        endDatePicker.add(new DatePicker());		
		
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
		
		quickSelections = new ArrayList<WebMarkupContainer>();
		
		quickSelections.add(getQuickWeek());
		quickSelections.add(getQuickMonth());
		quickSelections.add(getQuickQuarter());
		
		
		for (WebMarkupContainer cont : quickSelections)
		{
			form.add(cont);
		}
	}
	
	/**
	 * Add quick week selection
	 * @param parent
	 */
	private WebMarkupContainer getQuickWeek()
	{
		List<QuickWeek>	weeks = new ArrayList<QuickWeek>();	
		Calendar		currentDate = new GregorianCalendar();
		int 			currentWeek = -8;

		EhourConfig config = ((EhourWebSession)getSession()).getEhourConfig();
		
		currentDate.setFirstDayOfWeek(config.getFirstDayOfWeek());
		currentDate.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
		currentDate.add(Calendar.WEEK_OF_YEAR, currentWeek);
		
		for (; currentWeek < 8; currentWeek++)
		{
			weeks.add(new QuickWeek(currentDate, config));
			
			currentDate.add(Calendar.WEEK_OF_YEAR, 1);
		}
		
		return new QuickDropDownChoice("quickWeek", weeks, new QuickWeekRenderer(config));
	}
	
	/**
	 * Add quick month selection
	 * @param parent
	 */
	private WebMarkupContainer getQuickMonth()
	{
		List<QuickMonth>	months = new ArrayList<QuickMonth>();	
		Calendar			currentDate = new GregorianCalendar();
		int 				currentMonth = -6;
		
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		currentDate.setFirstDayOfWeek(Calendar.SUNDAY);
		currentDate.add(Calendar.MONTH, currentMonth);
		
		for (; currentMonth < 6; currentMonth++)
		{
			months.add(new QuickMonth(currentDate));
			
			currentDate.add(Calendar.MONTH, 1);
		}

		return  new QuickDropDownChoice("quickMonth", months, new QuickMonthRenderer());
	}	
	
	
	/**
	 * Add quick month selection
	 * @param parent
	 */
	private WebMarkupContainer getQuickQuarter()
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
		
		return new QuickDropDownChoice("quickQuarter", quarters, new QuickQuarterRenderer());
	}	

	/**
	 * Sort available report criteria
	 * @param reportCriteria
	 */
	protected void sortReportCriteria(ReportCriteria reportCriteria)
	{
		Collections.sort((reportCriteria.getAvailableCriteria()).getCustomers(), new CustomerComparator());
		Collections.sort((reportCriteria.getAvailableCriteria()).getProjects(), new ProjectComparator());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.panel.BaseAjaxPanel#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == QuickDateAjaxEventType.ADMIN_QUICK_DATE_CHANGED)
		{
			updateDates(ajaxEvent.getTarget());
		}
		
		return true;
	} 

	/**
	 * Update dates and quick selections
	 * @param target
	 */
	private void updateDates(AjaxRequestTarget target)
	{
		target.addComponent(startDatePicker);
		target.addComponent(endDatePicker);
		
		for (WebMarkupContainer cont : quickSelections)
		{
			target.addComponent(cont);
		}
	}
}