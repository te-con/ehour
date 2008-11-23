/**
 * Created on Aug 20, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.admin.department.panel;

import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.ui.admin.department.common.DepartmentAjaxEventType;
import net.rrm.ehour.ui.admin.department.dto.DepartmentAdminBackingBean;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * user department form panel
 **/

public class DepartmentFormPanel extends AbstractFormSubmittingPanel
{
	private static final long serialVersionUID = -6469066920645156569L;

	@SpringBean
	private UserService userService;
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public DepartmentFormPanel(String id, CompoundPropertyModel model)
	{
		super(id, model);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("deptForm");
		
		// name
		RequiredTextField	nameField = new RequiredTextField("department.name");
		form.add(nameField);
		nameField.add(new StringValidator.MaximumLengthValidator(64));
		nameField.setLabel(new ResourceModel("admin.dept.name"));
		nameField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));
			
		// code
		RequiredTextField	codeField = new RequiredTextField("department.code");
		form.add(codeField);
		codeField.add(new StringValidator.MaximumLengthValidator(16));
		codeField.setLabel(new ResourceModel("admin.dept.code"));
		codeField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
	
		//
		FormUtil.setSubmitActions(form
									,((DepartmentAdminBackingBean)model.getObject()).getDepartment().isDeletable()
									,this
									,DepartmentAjaxEventType.DEPARTMENT_UPDATED
									,DepartmentAjaxEventType.DEPARTMENT_DELETED
									,((EhourWebSession)getSession()).getEhourConfig());
		
		greyBorder.add(form);
	}
	
	
	/**
	 * Persist dept
	 * @param backingBean
	 * @throws ObjectNotUniqueException 
	 */
	private void persistDepartment(DepartmentAdminBackingBean backingBean) throws ObjectNotUniqueException
	{
		userService.persistUserDepartment(backingBean.getDepartment());
	}
	
	
	/**
	 * Delete department
	 * @param backingBean
	 */
	private void deleteDepartment(DepartmentAdminBackingBean backingBean)
	{
		userService.deleteDepartment(backingBean.getDepartment().getDepartmentId());
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.panel.noentry.AbstractAjaxAwareAdminPanel#processFormSubmit(net.rrm.ehour.ui.common.model.AdminBackingBean, int)
	 */
	@Override
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		DepartmentAdminBackingBean departmentBackingBean = (DepartmentAdminBackingBean) backingBean;
		
		if (type == DepartmentAjaxEventType.DEPARTMENT_UPDATED)
		{
			persistDepartment(departmentBackingBean);
		}
		else if (type == DepartmentAjaxEventType.DEPARTMENT_DELETED)
		{
			deleteDepartment(departmentBackingBean);
		}		
	}	
}
