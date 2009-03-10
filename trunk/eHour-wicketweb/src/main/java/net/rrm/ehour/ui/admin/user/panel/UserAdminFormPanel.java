/**
 * Created on Aug 11, 2007
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

package net.rrm.ehour.ui.admin.user.panel;

import java.util.List;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.renderers.UserRoleRenderer;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourConstants;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * User Form Panel for admin
 **/

public class UserAdminFormPanel extends AbstractFormSubmittingPanel
{
	private static final long serialVersionUID = -7427807216389657732L;

	@SpringBean
	private UserService	userService;
	private final static Logger logger = Logger.getLogger(UserAdminFormPanel.class);
	
	/**
	 * 
	 * @param id
	 * @param userModel
	 * @param roles
	 * @param departments
	 */
	public UserAdminFormPanel(String id,
							CompoundPropertyModel userModel,
							List<UserRole> roles,
							List<UserDepartment> departments)
	{
		super(id, userModel);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("userForm");

		// password inputs
		form.add(new PasswordInputSnippet("password", form));
		
		// username
		RequiredTextField	usernameField = new RequiredTextField("user.username");
		form.add(usernameField);
		usernameField.add(new StringValidator.MaximumLengthValidator(32));
		usernameField.add(new DuplicateUsernameValidator());
		usernameField.setLabel(new ResourceModel("admin.user.username"));
		usernameField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("userValidationError", usernameField));
		
		// first & last name
		TextField	firstNameField = new TextField("user.firstName");
		form.add(firstNameField);

		TextField	lastNameField = new RequiredTextField("user.lastName");
		form.add(lastNameField);
		lastNameField.setLabel(new ResourceModel("admin.user.lastName"));
		lastNameField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("lastNameValidationError", lastNameField));
		
		// email
		form.add(new EmailInputSnippet("email"));
		
		// department
		DropDownChoice userDepartment = new DropDownChoice("user.userDepartment", departments, new ChoiceRenderer("name"));
		userDepartment.setRequired(true);
		userDepartment.setLabel(new ResourceModel("admin.user.department"));
		userDepartment.add(new ValidatingFormComponentAjaxBehavior());
		form.add(userDepartment);
		form.add(new AjaxFormComponentFeedbackIndicator("departmentValidationError", userDepartment));
		
		// user roles
		ListMultipleChoice userRoles = new ListMultipleChoice("user.userRoles", roles, new UserRoleRenderer());
		userRoles.setMaxRows(4);
		userRoles.setLabel(new ResourceModel("admin.user.roles"));
		userRoles.setRequired(true);
		userRoles.add(new ValidatingFormComponentAjaxBehavior());
		form.add(userRoles);
		form.add(new AjaxFormComponentFeedbackIndicator("rolesValidationError", userRoles));

		// active
		form.add(new CheckBox("user.active"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
	
		//
		FormUtil.setSubmitActions(form
									,((UserBackingBean)userModel.getObject()).getUser().isDeletable()
									,this
									,UserEditAjaxEventType.USER_UPDATED
									,UserEditAjaxEventType.USER_DELETED
									,((EhourWebSession)getSession()).getEhourConfig());
		
		greyBorder.add(form);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.panel.noentry.AbstractAjaxAwareAdminPanel#processFormSubmit(net.rrm.ehour.ui.common.model.AdminBackingBean, int)
	 */
	@Override
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		UserBackingBean userBackingBean = (UserBackingBean) backingBean;
		
		if (type == UserEditAjaxEventType.USER_UPDATED)
		{
			persistUser(userBackingBean);
		}
		else if (type == UserEditAjaxEventType.USER_DELETED)
		{
			deleteUser(userBackingBean);
		}		
	}		
	
	/**
	 * Persist user
	 * @param userBackingBean
	 * @throws ObjectNotUniqueException 
	 * @throws PasswordEmptyException 
	 */
	private void persistUser(UserBackingBean userBackingBean) throws PasswordEmptyException, ObjectNotUniqueException
	{
		if (userBackingBean.isPm())
		{
			logger.debug("Re-adding PM role after edit");
			userBackingBean.getUser().addUserRole(new UserRole(EhourConstants.ROLE_PROJECTMANAGER));
		}
		
		userService.persistUser(userBackingBean.getUser());
	}
	
	/**
	 * 
	 * @param userBackingBean
	 */
	private void deleteUser(UserBackingBean userBackingBean)
	{
		userService.deleteUser(userBackingBean.getUser().getUserId());
	}	
	
	/**
	 * Duplicate username validator
	 * @author Thies
	 *
	 */
	private class DuplicateUsernameValidator extends AbstractValidator
	{
		private static final long serialVersionUID = 542950054849279025L;

		@Override
		protected void onValidate(IValidatable validatable)
		{
			String username = (String)validatable.getValue();
			String orgUsername = ((UserBackingBean)((CompoundPropertyModel)getModel()).getObject()).getOriginalUsername();

			if (orgUsername != null && orgUsername.length() > 0 && username.equalsIgnoreCase(orgUsername))
			{
				return;
			}
			else if (userService.getUser(username) != null)
			{
				error(validatable, "admin.user.errorUsernameExists");
			}
		}
	}
}
