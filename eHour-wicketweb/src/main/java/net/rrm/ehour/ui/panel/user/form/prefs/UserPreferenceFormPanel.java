/**
 * Created on Oct 25, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.ui.panel.user.form.prefs;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.ui.ajax.AjaxEventType;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.component.ServerMessageLabel;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.panel.admin.AbstractAjaxAwareAdminPanel;
import net.rrm.ehour.ui.panel.admin.common.FormUtil;
import net.rrm.ehour.ui.panel.user.form.EmailInputSnippet;
import net.rrm.ehour.ui.panel.user.form.PasswordInputSnippet;
import net.rrm.ehour.ui.panel.user.form.UserEditAjaxEventType;
import net.rrm.ehour.ui.panel.user.form.admin.UserAdminFormPanel;
import net.rrm.ehour.ui.panel.user.form.admin.dto.UserBackingBean;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourConstants;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * User preferences form
 **/

public class UserPreferenceFormPanel extends AbstractAjaxAwareAdminPanel
{
	private static final long serialVersionUID = 7670153126514499168L;

	@SpringBean
	private UserService	userService;
	private final static Logger logger = Logger.getLogger(UserAdminFormPanel.class);
	
	/**
	 * 
	 * @param id
	 * @param userModel
	 */
	public UserPreferenceFormPanel(String id, User user)	
	{
		super(id);
		
		setModel(createModel(user));
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("border");
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		final Form form = new Form("userForm");
		
		// password inputs
		form.add(new PasswordInputSnippet("password", form));
		
		// email
		form.add(new EmailInputSnippet("email"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
	
		//
		FormUtil.setSubmitActions(form
									,false
									,this
									,UserEditAjaxEventType.USER_UPDATED
									,UserEditAjaxEventType.USER_DELETED
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
	 * @param user
	 * @return
	 */
	private IModel createModel(User user)
	{
		return new CompoundPropertyModel(new UserBackingBean(user));
	}
}
