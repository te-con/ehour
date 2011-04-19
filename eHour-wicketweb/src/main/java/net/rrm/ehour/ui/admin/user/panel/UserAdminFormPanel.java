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

package net.rrm.ehour.ui.admin.user.panel;

import java.util.List;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.renderers.UserRoleRenderer;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.user.service.UserService;

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

public class UserAdminFormPanel extends AbstractFormSubmittingPanel<UserBackingBean>
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
							CompoundPropertyModel<UserBackingBean> userModel,
							List<UserRole> roles,
							List<UserDepartment> departments)
	{
		super(id, userModel);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form<Void> form = new Form<Void>("userForm");

		// password inputs
		form.add(new PasswordInputSnippet("password", form));
		
		// username
		RequiredTextField<String> usernameField = new RequiredTextField<String>("user.username");
		form.add(usernameField);
		usernameField.add(new StringValidator.MaximumLengthValidator(32));
		usernameField.add(new DuplicateUsernameValidator());
		usernameField.setLabel(new ResourceModel("admin.user.username"));
		usernameField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("userValidationError", usernameField));
		
		// first & last name
		TextField<String> firstNameField = new TextField<String>("user.firstName");
		form.add(firstNameField);

		TextField<String> lastNameField = new RequiredTextField<String>("user.lastName");
		form.add(lastNameField);
		lastNameField.setLabel(new ResourceModel("admin.user.lastName"));
		lastNameField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("lastNameValidationError", lastNameField));
		
		// email
		form.add(new EmailInputSnippet("email"));
		
		// department
		DropDownChoice<UserDepartment> userDepartment = new DropDownChoice<UserDepartment>("user.userDepartment", departments, new ChoiceRenderer<UserDepartment>("name"));
		userDepartment.setRequired(true);
		userDepartment.setLabel(new ResourceModel("admin.user.department"));
		userDepartment.add(new ValidatingFormComponentAjaxBehavior());
		form.add(userDepartment);
		form.add(new AjaxFormComponentFeedbackIndicator("departmentValidationError", userDepartment));
		
		// user roles
		ListMultipleChoice<UserRole> userRoles = new ListMultipleChoice<UserRole>("user.userRoles", roles, new UserRoleRenderer());
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
	 * @see net.rrm.ehour.persistence.persistence.ui.common.panel.noentry.AbstractAjaxAwareAdminPanel#processFormSubmit(net.rrm.ehour.persistence.persistence.ui.common.model.AdminBackingBean, int)
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
			userBackingBean.getUser().addUserRole(UserRole.PROJECTMANAGER);
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
	private class DuplicateUsernameValidator extends AbstractValidator<String>
	{
		private static final long serialVersionUID = 542950054849279025L;

		@Override
		protected void onValidate(IValidatable<String> validatable)
		{
			String username = validatable.getValue();
			String orgUsername = ((UserBackingBean)getDefaultModelObject()).getOriginalUsername();

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
