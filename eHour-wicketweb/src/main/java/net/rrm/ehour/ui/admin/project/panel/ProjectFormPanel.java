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

package net.rrm.ehour.ui.admin.project.panel;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.admin.project.common.ProjectAjaxEventType;
import net.rrm.ehour.ui.admin.project.dto.ProjectAdminBackingBean;
import net.rrm.ehour.ui.admin.project.dto.ProjectAdminBackingBeanImpl;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.sort.CustomerComparator;
import net.rrm.ehour.ui.common.sort.UserComparator;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Project admin form
 **/

public class ProjectFormPanel extends AbstractFormSubmittingPanel<ProjectAdminBackingBean>
{
	@SpringBean
	private ProjectService	projectService;
	@SpringBean
	private CustomerService	customerService;
	@SpringBean
	private	UserService		userService;

	private static final long serialVersionUID = -8677950352090140144L;
	
	public ProjectFormPanel(String id, IModel<ProjectAdminBackingBean> model)
	{
		super(id, model);
		
		setOutputMarkupId(true);
		
		setUpPage(this, model);
	}

	private void setUpPage(WebMarkupContainer parent, IModel<ProjectAdminBackingBean> model)
	{
		Border border = new GreySquaredRoundedBorder("border");
		add(border);

		final Form<Void> form = new Form<Void>("projectForm");
		addFormComponents(form);

		FormUtil.setSubmitActions(form
									,model.getObject().getProject().isDeletable()
									,this
									,ProjectAjaxEventType.PROJECT_UPDATED
									,ProjectAjaxEventType.PROJECT_DELETED
									,EhourWebSession.getSession().getEhourConfig());

		border.add(form);

	}

	/**
	 * Add form components
	 * @param form
	 */
	private void addFormComponents(Form<Void> form)
	{
		addCustomer(form);
		addDescriptionAndContact(form);
		addGeneralInfo(form);
		addMisc(form);
		form.add(getProjectManager());
		
		form.add(new CheckBox("project.billable"));
		
	}
	
	
	/**
	 * Add form components to form
	 * @param parent
	 */
	private void addGeneralInfo(WebMarkupContainer parent)
	{
		// name
		RequiredTextField<String> nameField = new RequiredTextField<String>("project.name");
		parent.add(nameField);
		nameField.add(new StringValidator.MaximumLengthValidator(64));
		nameField.setLabel(new ResourceModel("admin.project.name"));
		nameField.add(new ValidatingFormComponentAjaxBehavior());
		parent.add(new AjaxFormComponentFeedbackIndicator("nameValidationError", nameField));

		// project code
		RequiredTextField<String> codeField = new RequiredTextField<String>("project.projectCode");
		parent.add(codeField);
		codeField.add(new StringValidator.MaximumLengthValidator(16));
		codeField.setLabel(new ResourceModel("admin.project.code"));
		codeField.add(new ValidatingFormComponentAjaxBehavior());
		parent.add(new AjaxFormComponentFeedbackIndicator("codeValidationError", codeField));
	}
	
	private void addCustomer(WebMarkupContainer parent)
	{
		// customers
		DropDownChoice<Customer> customerDropdown = new DropDownChoice<Customer>("project.customer", getCustomers(), new ChoiceRenderer<Customer>("fullName"));
		customerDropdown.setRequired(true);
		customerDropdown.setLabel(new ResourceModel("admin.project.customer"));
		customerDropdown.add(new ValidatingFormComponentAjaxBehavior());
		parent.add(customerDropdown);
		parent.add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerDropdown));
	}
	
	private DropDownChoice<User> getProjectManager()
	{
		// project manager
		DropDownChoice<User> projectManager = new DropDownChoice<User>("project.projectManager", getEligablePms(), new ChoiceRenderer<User>("fullName"));
		projectManager.setLabel(new ResourceModel("admin.project.projectManager"));
		
		return projectManager;
	}
	
	private void addDescriptionAndContact(WebMarkupContainer parent)
	{
		// description
		TextArea<String> textArea = new KeepAliveTextArea("project.description");
		textArea.setLabel(new ResourceModel("admin.project.description"));
        parent.add(textArea);

		// contact
		TextField<String> contactField = new TextField<String>("project.contact");
		parent.add(contactField);
	}

	private void addMisc(WebMarkupContainer parent)
	{
		// default project
		parent.add(new CheckBox("project.defaultProject"));
		
		// active
		parent.add(new CheckBox("project.active"));

		// data save label
		parent.add(new ServerMessageLabel("serverMessage", "formValidationError"));		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.common.panel.noentry.AbstractAjaxAwareAdminPanel#processFormSubmit(net.rrm.ehour.persistence.persistence.ui.common.model.AdminBackingBean, int)
	 */
	@Override
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		ProjectAdminBackingBeanImpl projectBackingBean = (ProjectAdminBackingBeanImpl) backingBean;
		
		if (type == ProjectAjaxEventType.PROJECT_UPDATED)
		{
			persistProject(projectBackingBean);
		}
		else if (type == ProjectAjaxEventType.PROJECT_DELETED)
		{
			deleteProject(projectBackingBean);
		}		
	}	

	/**
	 * Persist project
	 */
	private void persistProject(ProjectAdminBackingBean backingBean) 
	{
		projectService.persistProject(backingBean.getProject());
	}
	
	/**
	 * Delete project
	 * @throws ParentChildConstraintException 
	 */
	private void deleteProject(ProjectAdminBackingBean backingBean) throws ParentChildConstraintException
	{
		projectService.deleteProject(backingBean.getProject().getProjectId());
	} 
	
	/**
	 * Get customers for customer dropdown
	 */
	private List<Customer> getCustomers()
	{
		List<Customer> customers;
		
		customers = customerService.getCustomers(true);
		
		if (customers != null)
		{
			Collections.sort(customers, new CustomerComparator());
		}
		else
		{
			customers = new ArrayList<Customer>();
		}
		
		return customers;
	}
	
	/**
	 * Get users for PM selection
	 * @return
	 */
	private List<User> getEligablePms()
	{
		List<User> users;

		users = userService.getUsersWithEmailSet();
		
		if (users != null)
		{
			Collections.sort(users, new UserComparator(false));
		}
		else
		{
			users = new ArrayList<User>();
		}
		
		return users;
	}	
}
