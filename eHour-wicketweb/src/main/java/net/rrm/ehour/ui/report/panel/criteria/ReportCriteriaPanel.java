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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.EventPublisher;
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
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Base report criteria panel which adds the quick date selections
 **/

public class ReportCriteriaPanel extends AbstractAjaxPanel<ReportCriteriaBackingBean> 
{
	private static final int MAX_CRITERIA_ROW = 4;

	private static final long serialVersionUID = 161160822264046559L;
	
	@SpringBean
	private	ReportCriteriaService	reportCriteriaService;
	private DateTextField 			startDatePicker;
	private DateTextField 			endDatePicker;
	private	ListMultipleChoice<Project> projects;
	private ListMultipleChoice<Customer> customers;
	private	ListMultipleChoice<User> users;
	private ListMultipleChoice<UserDepartment> departments;
	private List<WebMarkupContainer>	quickSelections;
	
	/**
	 * Constructor which sets up the basic borded form
	 * @param id
	 * @param model
	 */
	public ReportCriteriaPanel(String id, IModel<ReportCriteriaBackingBean> model)
	{
		super(id, model);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border", WebGeo.W_CONTENT_WIDE);
		add(greyBorder);

		setOutputMarkupId(true);	
		
		Form<ReportCriteriaBackingBean> form = new Form<ReportCriteriaBackingBean>("criteriaForm");
		greyBorder.add(form);
		
		addDates(form, model);

		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("customerProjectsBorder");
		form.add(blueBorder);

		addCustomerSelection(model.getObject(), blueBorder);
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
		
		DropDownChoice<ReportType> reportTypeSelection = new DropDownChoice<ReportType>("reportType", reportTypes, new ReportTypeRenderer());
		reportTypeSelection.setRequired(true);
		reportTypeSelection.setLabel(new ResourceModel("report.type.name"));
		parent.add(new AjaxFormComponentFeedbackIndicator("reportTypeSelectionError", reportTypeSelection));
		parent.add(reportTypeSelection);	
		
	}
	
	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addCustomerSelection(ReportCriteriaBackingBean bean, WebMarkupContainer parent)
	{
		customers = new ListMultipleChoice<Customer>("reportCriteria.userCriteria.customers",
				new PropertyModel<List<Customer>>(bean, "reportCriteria.availableCriteria.customers"),
				new DomainObjectChoiceRenderer<Customer>());
		
		customers.setMaxRows(MAX_CRITERIA_ROW);	

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
		AjaxCheckBox deactivateBox = createOnlyActiveCheckbox();		
		parent.add(deactivateBox);
		
		parent.add(createOnlyBillableCheckbox("reportCriteria.userCriteria.onlyBillableCustomers"));
	}

	@SuppressWarnings("serial")
	private AjaxCheckBox createOnlyActiveCheckbox()
	{
		AjaxCheckBox	deactivateBox = new AjaxCheckBox("reportCriteria.userCriteria.onlyActiveCustomers")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS);
				target.addComponent(customers);
			}
		};
		
		deactivateBox.setOutputMarkupId(true);
		
		return deactivateBox;
	}

	@SuppressWarnings("serial")
	private AjaxCheckBox createOnlyBillableCheckbox(String id)
	{
		AjaxCheckBox deactivateBox = new AjaxCheckBox(id, new PropertyModel<Boolean>(getDefaultModel(), "reportCriteria.userCriteria.onlyBillableProjects"))
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				updateReportCriteria(ReportCriteriaUpdateType.UPDATE_CUSTOMERS);
				updateReportCriteria(ReportCriteriaUpdateType.UPDATE_PROJECTS);
				target.addComponent(customers);
				target.addComponent(projects);
				
				// bahh!
				target.addComponent(ReportCriteriaPanel.this.get("border:criteriaForm:customerProjectsBorder:reportCriteria.userCriteria.onlyBillableCustomers"));
				target.addComponent(ReportCriteriaPanel.this.get("border:criteriaForm:customerProjectsBorder:reportCriteria.userCriteria.onlyBillableProjects"));
			}
		};
		
		deactivateBox.setMarkupId(id);
		return deactivateBox;
	}

	/**
	 * Add project selection
	 * @param parent
	 */
	private void addProjectSelection(WebMarkupContainer parent)
	{
		projects = new ListMultipleChoice<Project>("reportCriteria.userCriteria.projects",
											new PropertyModel<List<Project>>(getDefaultModel(), "reportCriteria.availableCriteria.projects"),
											new DomainObjectChoiceRenderer<Project>());
		projects.setMaxRows(MAX_CRITERIA_ROW);
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
		
		parent.add(createOnlyBillableCheckbox("reportCriteria.userCriteria.onlyBillableProjects"));
	}		
	

	/**
	 * Add customer selection
	 * @param parent
	 */
	private void addUserSelection(WebMarkupContainer parent)
	{
		users = new ListMultipleChoice<User>("reportCriteria.userCriteria.users",
								new PropertyModel<List<User>>(getDefaultModel(), "reportCriteria.availableCriteria.users"),
								new DomainObjectChoiceRenderer<User>());
		users.setOutputMarkupId(true);
		users.setMaxRows(MAX_CRITERIA_ROW);
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
		departments = new ListMultipleChoice<UserDepartment>("reportCriteria.userCriteria.userDepartments",
								new PropertyModel<List<UserDepartment>>(getDefaultModel(), "reportCriteria.availableCriteria.userDepartments"),
								new DomainObjectChoiceRenderer<UserDepartment>());
		departments.setMaxRows(MAX_CRITERIA_ROW);
		
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
	private void addDepartmentsAndUsers(Form<ReportCriteriaBackingBean> form)
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
	private void updateReportCriteria(ReportCriteriaUpdateType updateType)
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
		return (ReportCriteriaBackingBean)getDefaultModelObject();
	}	
	
	/**
	 * Add submit link
	 * @param parent
	 */
	@SuppressWarnings("serial")
	private void addCreateReportSubmit(Form<ReportCriteriaBackingBean> form)
	{
		// Submit
		AjaxButton submitButton = new AjaxButton("createReport", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				EventPublisher.publishAjaxEvent(this, new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_UPDATED));
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}
			
			@Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.addComponent(form);
            }			
        };
        
		// default submit
		form.add(submitButton);
		
		// reset button
		AjaxButton resetButton = new AjaxButton("resetCriteria")
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
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
            protected void onError(AjaxRequestTarget target, Form<?> form)
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
	private void addDates(Form<ReportCriteriaBackingBean> form, IModel<ReportCriteriaBackingBean> model)
	{
        startDatePicker = new DateTextField("reportCriteria.userCriteria.reportRange.dateStart", 
        										new PropertyModel<Date>(model, "reportCriteria.userCriteria.reportRange.dateStart"), new StyleDateConverter("S-", false));
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

		endDatePicker = new DateTextField("reportCriteria.userCriteria.reportRange.dateEnd", 
												new PropertyModel<Date>(model, "reportCriteria.userCriteria.reportRange.dateEnd"), new StyleDateConverter("S-", false));
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
		
		return new QuickDropDownChoice<QuickWeek>("quickWeek", weeks, new QuickWeekRenderer(config));
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

		return  new QuickDropDownChoice<QuickMonth>("quickMonth", months, new QuickMonthRenderer());
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
		
		return new QuickDropDownChoice<QuickQuarter>("quickQuarter", quarters, new QuickQuarterRenderer());
	}	

	/**
	 * Sort available report criteria
	 * @param reportCriteria
	 */
	private void sortReportCriteria(ReportCriteria reportCriteria)
	{
		Collections.sort((reportCriteria.getAvailableCriteria()).getCustomers(), new CustomerComparator());
		Collections.sort((reportCriteria.getAvailableCriteria()).getProjects(), new ProjectComparator());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.panel.BaseAjaxPanel#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	public final boolean ajaxEventReceived(AjaxEvent ajaxEvent)
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