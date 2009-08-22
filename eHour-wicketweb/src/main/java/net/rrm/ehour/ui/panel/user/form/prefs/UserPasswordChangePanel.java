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

package net.rrm.ehour.ui.panel.user.form.prefs;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean;
import net.rrm.ehour.ui.admin.user.panel.PasswordInputSnippet;
import net.rrm.ehour.ui.admin.user.panel.UserAdminFormPanel;
import net.rrm.ehour.ui.admin.user.panel.UserEditAjaxEventType;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.ajax.GenericAjaxEventType;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourConstants;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * User preferences form
 **/

public class UserPasswordChangePanel extends AbstractFormSubmittingPanel
{
	private static final long serialVersionUID = 7670153126514499168L;

	@SpringBean
	private UserService	userService;
	
	private final static Logger LOGGER = Logger.getLogger(UserAdminFormPanel.class);
	private WebComponent serverMessage;
	private Form		form;

	/**
	 * 
	 * @param id
	 * @param userModel
	 * @throws ObjectNotFoundException 
	 */
	public UserPasswordChangePanel(String id, User user) throws ObjectNotFoundException	
	{
		super(id);
		
		setModel(createModel(user));
		Border greyBorder = new GreyRoundedBorder("border", new ResourceModel("userprefs.title"), WebGeo.W_CONTENT_XSMALL);
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		form = new Form("userForm");
		form.setOutputMarkupId(true);
		
		// password inputs
		form.add(new PasswordInputSnippet("password", form));
		
		// data save label
		serverMessage = new WebComponent("serverMessage");
		serverMessage.setOutputMarkupId(true);
		form.add(serverMessage);	
		//
		FormUtil.setSubmitActions(form
									,false
									,this
									,UserEditAjaxEventType.USER_UPDATED
									,UserEditAjaxEventType.USER_DELETED
									,GenericAjaxEventType.SUBMIT_ERROR
									,((EhourWebSession)getSession()).getEhourConfig());
		
		greyBorder.add(form);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.AbstractAjaxAwareAdminPanel#processFormSubmit(net.rrm.ehour.ui.model.AdminBackingBean, int)
	 */
	@Override
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		UserBackingBean userBackingBean = (UserBackingBean) backingBean;
		
		if (type == UserEditAjaxEventType.USER_UPDATED)
		{
			persistUser(userBackingBean);
			
			Label replacementLabel = new Label("serverMessage", new ResourceModel("userprefs.saved"));
			replacementLabel.setOutputMarkupId(true);
			replacementLabel.add(new SimpleAttributeModifier("class", "smallText"));

			serverMessage.replaceWith(replacementLabel);
			serverMessage = replacementLabel;
			
			target.addComponent(replacementLabel);
			target.addComponent(form);
		}
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.AbstractAjaxAwareAdminPanel#processFormSubmitError(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected boolean processFormSubmitError(AjaxRequestTarget target)
	{
		WebComponent replacement = new WebComponent("serverMessage");
		replacement.setOutputMarkupId(true);
		serverMessage.replaceWith(replacement);
		serverMessage = replacement;
		target.addComponent(replacement);
		
		return false;
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
			LOGGER.debug("Re-adding PM role after edit");
			userBackingBean.getUser().addUserRole(userService.getUserRole(EhourConstants.ROLE_PROJECTMANAGER));
		}
		
		userService.persistUser(userBackingBean.getUser());
	}	
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws ObjectNotFoundException 
	 */
	private IModel createModel(User user) throws ObjectNotFoundException
	{
		User dbUser = userService.getUser(user.getUserId());
		return new CompoundPropertyModel(new UserBackingBean(dbUser));
	}
}
