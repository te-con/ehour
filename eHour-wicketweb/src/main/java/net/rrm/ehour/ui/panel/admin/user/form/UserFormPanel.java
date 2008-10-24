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

package net.rrm.ehour.ui.panel.admin.user.form;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.panel.admin.AbstractAjaxAwareAdminPanel;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;
import net.rrm.ehour.ui.panel.admin.user.form.dto.UserBackingBean;
import net.rrm.ehour.ui.renderers.UserRoleRenderer;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * User Form Panel for admin
 **/

public class UserFormPanel extends AbstractAjaxAwareAdminPanel
{
	private static final long serialVersionUID = -7427807216389657732L;

	@SpringBean
	private UserService	userService;
	
	
	private DropDownChoice userDropDownList;
	private RequiredTextField	usernameField;
	private TextField	firstNameField;
	private TextField	lastNameField;
	private TextField	emailField;
	private CompoundPropertyModel userModel;
	private static String DEFAULT_PASSWORD = "Test123*";
	TreeMap<String,User> ldapUsers ;
	
	
	Boolean MODIFY_MODE = false;
	
	/**
	 * 
	 * @param id
	 * @param userModel
	 * @param roles
	 * @param departments
	 */
	public UserFormPanel(String id,
							CompoundPropertyModel userModel,
							List<UserRole> roles,
							List<UserDepartment> departments)
	{
		super(id, userModel);
		
		MODIFY_MODE= ((UserBackingBean)userModel.getObject()).getUser().isDeletable();
		
		setUserModel(userModel);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("userForm");

		// username
		
		/*RequiredTextField	usernameField = new RequiredTextField("user.username");
		form.add(usernameField);
		usernameField.add(new StringValidator.MaximumLengthValidator(32));
		usernameField.add(new DuplicateUsernameValidator());
		usernameField.setLabel(new ResourceModel("admin.user.username"));
		usernameField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("userValidationError", usernameField));*/
		//List<String> ldapUsers = userService.getAllLdapUsersNameNotInDB();
		
		EhourWebSession session = (EhourWebSession)getSession();
		
		if(!MODIFY_MODE)
			{ 
			ldapUsers = (TreeMap<String, User>) session.getSessionAttribute("ldapUsers");
			
			if( ldapUsers==null || ldapUsers.isEmpty())
				{ldapUsers = userService.getLdapUsersTreeNotInDB();
				 session.setSessionAttribute("ldapUsers", ldapUsers);
				}
			
			else
				{
				ldapUsers = userService.getResynchronizedLdapUsersTreeNotInDB(ldapUsers);
				session.setSessionAttribute("ldapUsers", ldapUsers);
				}
			
			}
			
		else
		  ldapUsers= new TreeMap<String, User>();
		
		DropDownChoice userDropDownList = new DropDownChoice("ldapLogin", Arrays.asList(ldapUsers.keySet().toArray()) );
		userDropDownList.setRequired( false);
		userDropDownList.setEnabled(!MODIFY_MODE );
		userDropDownList.setLabel(new ResourceModel("admin.user.ldap.username"));
		userDropDownList.add(new ValidatingFormComponentAjaxBehavior());
		
		//System.err.println( "=============> Id =" +  getId() );
		
	
		userDropDownList.add(new AjaxFormComponentUpdatingBehavior("onchange") {
		        protected void onUpdate(AjaxRequestTarget target) {
	                // updates the email first name and last name fields
	            	
	            	// here I need to retrieve the user that had been selected from the screen 
	            	
		        	String  selectedUser = ((UserBackingBean) getUserModel().getObject()).getLdapLogin();
		        	
		        	User targetUser = ldapUsers.get(selectedUser);		        	
		        	((UserBackingBean) getUserModel().getObject()).getUser().setUsername(targetUser.getUsername());
		        	((UserBackingBean) getUserModel().getObject()).getUser().setLastName(targetUser.getLastName());
		        	((UserBackingBean) getUserModel().getObject()).getUser().setFirstName(targetUser.getFirstName());
		        	((UserBackingBean) getUserModel().getObject()).getUser().setEmail(targetUser.getEmail());
		        	
		        	// because a password is needed we set the password to a default one Test123*
		        	((UserBackingBean) getUserModel().getObject()).getUser().setPassword( DEFAULT_PASSWORD );
		        	
		        	//System.err.println("selected user is : " + selectedUser);
		           
	                target.addComponent(usernameField);
	                target.addComponent(emailField);
	                target.addComponent(firstNameField);
	                target.addComponent(lastNameField);
	            }
	        }); 
		
		
		form.add(userDropDownList);
		//form.add(new AjaxFormComponentFeedbackIndicator("userValidationError", userDropDownList));
		
			
		// password & confirm
		/** removed for cas ldap integration ****
		 * ********************************************/
		/*PasswordTextField	passwordTextField = new PasswordTextField("user.password");
		passwordTextField.setLabel(new ResourceModel("admin.user.password"));
		passwordTextField.setRequired(false);	// passwordField defaults to required
		passwordTextField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(new AjaxFormComponentFeedbackIndicator("passwordValidationError", passwordTextField));
		form.add(passwordTextField);

		PasswordTextField	confirmPasswordTextField = new PasswordTextField("confirmPassword");
		confirmPasswordTextField.setRequired(false);	// passwordField defaults to required
		confirmPasswordTextField.add(new ValidatingFormComponentAjaxBehavior());
		form.add(confirmPasswordTextField);
		form.add(new AjaxFormComponentFeedbackIndicator("confirmPasswordValidationError", confirmPasswordTextField));
		form.add(new ConfirmPasswordValidator(passwordTextField, confirmPasswordTextField));
		 ***/
		
		
		
		usernameField = new RequiredTextField("user.username");
		form.add(usernameField);
		usernameField.add(new StringValidator.MaximumLengthValidator(32));
		usernameField.add(new DuplicateUsernameValidator());
		usernameField.setLabel(new ResourceModel("admin.user.ldap.username"));
		usernameField.add(new ValidatingFormComponentAjaxBehavior());
		usernameField.setEnabled(false);
		form.add(new AjaxFormComponentFeedbackIndicator("userValidationError", usernameField));
		
		// first & last name
		
		firstNameField = new TextField("user.firstName");
		firstNameField.setOutputMarkupId(true);
		firstNameField.setEnabled(false);
		form.add(firstNameField);

		lastNameField = new RequiredTextField("user.lastName");
		lastNameField.setOutputMarkupId(true);
		form.add(lastNameField);
		lastNameField.setLabel(new ResourceModel("admin.user.lastName"));
		lastNameField.add(new ValidatingFormComponentAjaxBehavior());
		lastNameField.setEnabled(false);
		form.add(new AjaxFormComponentFeedbackIndicator("lastNameValidationError", lastNameField));
		
		// email
		emailField = new TextField("user.email");
		emailField.add(EmailAddressValidator.getInstance());
		emailField.add(new ValidatingFormComponentAjaxBehavior());
		emailField.setEnabled(false);
		emailField.setOutputMarkupId(true);
		form.add(emailField);
		form.add(new AjaxFormComponentFeedbackIndicator("emailValidationError", emailField));
		
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
									,((EhourWebSession)getSession()).getEhourConfig());
		
		greyBorder.add(form);
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
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class ConfirmPasswordValidator extends AbstractFormValidator
	{
		private static final long serialVersionUID = -7176398632862551019L;
		private FormComponent[] components;
		
		/**
		 * 
		 * @param passwordField
		 * @param confirmField
		 */
		public ConfirmPasswordValidator(FormComponent passwordField, FormComponent confirmField)
		{
			components = new FormComponent[]{passwordField, confirmField};
		}

		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.html.form.validation.IFormValidator#getDependentFormComponents()
		 */
		public FormComponent[] getDependentFormComponents()
		{
			return components;
		}

		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
		 */
		public void validate(Form form)
		{
			String orgPassword = ((UserBackingBean)((CompoundPropertyModel)getModel()).getObject()).getOriginalPassword();
			
			if ("".equals(Objects.stringValue(components[0].getInput(), false))
					&& (orgPassword == null || orgPassword.equals("")))
			{
				error(components[0], "Required");
			}
			else if (!Objects.stringValue(components[0].getInput(), false)
					.equals(Objects.stringValue(components[1].getInput(), false)))
			{
				error(components[1], "admin.user.errorConfirmPassNeeded");
			}
		}
	}

	public CompoundPropertyModel getUserModel() {
		return userModel;
	}

	public void setUserModel(CompoundPropertyModel userModel) {
		this.userModel = userModel;
	}
	
	
	
}
