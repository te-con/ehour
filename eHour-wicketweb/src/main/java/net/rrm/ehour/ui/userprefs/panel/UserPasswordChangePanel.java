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

package net.rrm.ehour.ui.userprefs.panel;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean;
import net.rrm.ehour.ui.admin.user.panel.PasswordInputSnippet;
import net.rrm.ehour.ui.admin.user.panel.UserAdminFormPanel;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.user.service.UserService;
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

import static net.rrm.ehour.ui.admin.user.panel.UserEditAjaxEventType.PASSWORD_CHANGED;
import static net.rrm.ehour.ui.common.event.CommonAjaxEventType.SUBMIT_ERROR;

/**
 * User preferences form
 **/

public class UserPasswordChangePanel extends AbstractFormSubmittingPanel<UserBackingBean>
{
	private static final long serialVersionUID = 7670153126514499168L;

	@SpringBean
	private UserService	userService;
	
	private final static Logger LOGGER = Logger.getLogger(UserAdminFormPanel.class);
	private WebComponent serverMessage;
	private Form<UserBackingBean> form;

	public UserPasswordChangePanel(String id, User user) throws ObjectNotFoundException
	{
		super(id);
		
		setDefaultModel(createModel(user));
		Border greyBorder = new GreyRoundedBorder("border", new ResourceModel("userprefs.title"), WebGeo.W_CONTENT_XSMALL);
		add(greyBorder);
		
		setOutputMarkupId(true);
		
		form = new Form<UserBackingBean>("userForm");
		form.setOutputMarkupId(true);
		
		// password inputs
		form.add(new PasswordInputSnippet("password", form));
		
		// data save label
		serverMessage = new WebComponent("serverMessage");
		serverMessage.setOutputMarkupId(true);
		form.add(serverMessage);


        FormConfig formConfig = new FormConfig().forForm(form).withSubmitTarget(this)
                .withSubmitEventType(PASSWORD_CHANGED)
                .withErrorEventType(SUBMIT_ERROR);

		FormUtil.setSubmitActions(formConfig);
		
		greyBorder.add(form);		
	}
	
	@Override
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		UserBackingBean userBackingBean = (UserBackingBean) backingBean;
		
		if (type == PASSWORD_CHANGED) {
            userService.changePassword(userBackingBean.getOriginalUsername(), userBackingBean.getUser().getUpdatedPassword());

            Label replacementLabel = new Label("serverMessage", new ResourceModel("userprefs.saved"));
            replacementLabel.setOutputMarkupId(true);
            replacementLabel.add(new SimpleAttributeModifier("class", "smallText"));

            serverMessage.replaceWith(replacementLabel);
            serverMessage = replacementLabel;

            target.addComponent(replacementLabel);
            target.addComponent(form);
        }
	}	

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

    private IModel<UserBackingBean> createModel(User user) throws ObjectNotFoundException
	{
		User dbUser = userService.getUser(user.getUserId());
		return new CompoundPropertyModel<UserBackingBean>(new UserBackingBean(dbUser));
	}
}
