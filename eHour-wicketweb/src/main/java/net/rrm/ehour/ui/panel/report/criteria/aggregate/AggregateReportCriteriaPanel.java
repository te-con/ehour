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

package net.rrm.ehour.ui.panel.report.criteria.aggregate;

import java.util.Collections;

import net.rrm.ehour.report.criteria.AggregateAvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdate;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.panel.report.criteria.BaseReportCriteriaPanel;
import net.rrm.ehour.ui.renderers.DomainObjectChoiceRenderer;
import net.rrm.ehour.ui.sort.UserComparator;
import net.rrm.ehour.ui.sort.UserDepartmentComparator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Report criteria panel
 **/

@SuppressWarnings("serial")
public class AggregateReportCriteriaPanel extends BaseReportCriteriaPanel 
{
	private static final long serialVersionUID = -7865322191390719584L;
	
	private	ListMultipleChoice 		users;
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AggregateReportCriteriaPanel(String id, IModel model)
	{
		super(id, model);
		
		sortReportCriteria(getBackingBeanFromModel().getReportCriteria());
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
				updateReportCriteria(ReportCriteriaUpdate.UPDATE_USERS);
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
		ListMultipleChoice depts = new ListMultipleChoice("reportCriteria.userCriteria.userDepartments",
								new PropertyModel(getModel(), "reportCriteria.availableCriteria.userDepartments"),
								new DomainObjectChoiceRenderer());
		depts.setMaxRows(4);
		
		// update projects when customer(s) selected
		depts.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			protected void onUpdate(AjaxRequestTarget target)
            {
				// show only projects for selected customers
				updateReportCriteria(ReportCriteriaUpdate.UPDATE_USERS);
                target.addComponent(users);
            }
        });			
		
		parent.add(depts);
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
	 * 
	 * @param reportCriteria
	 */
	@Override
	protected void sortReportCriteria(ReportCriteria reportCriteria)
	{
		super.sortReportCriteria(reportCriteria);
		Collections.sort(((AggregateAvailableCriteria)reportCriteria.getAvailableCriteria()).getUserDepartments(), new UserDepartmentComparator());
		Collections.sort(((AggregateAvailableCriteria)reportCriteria.getAvailableCriteria()).getUsers(), new UserComparator(false));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.report.criteria.BaseReportCriteriaPanel#fillCriteriaForm(org.apache.wicket.markup.html.form.Form)
	 */
	@Override
	protected void fillCriteriaForm(Form form)
	{
		addDepartmentsAndUsers(form);
	}
}
