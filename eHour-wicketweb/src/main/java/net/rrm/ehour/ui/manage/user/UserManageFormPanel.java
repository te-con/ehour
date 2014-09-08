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

package net.rrm.ehour.ui.manage.user;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static net.rrm.ehour.ui.manage.user.UserManageAjaxEventType.*;

/**
 * User Form Panel for admin
 */

public class UserManageFormPanel extends AbstractUserFormPanelTemplate<UserManageBackingBean> {
    private static final long serialVersionUID = -7427807216389657732L;

    @SpringBean
    private UserService userService;

    public UserManageFormPanel(String id,
                               CompoundPropertyModel<UserManageBackingBean> userModel) {
        super(id, userModel);
    }

    @Override
    protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        UserManageBackingBean userManageBackingBean = (UserManageBackingBean) backingBean;

        boolean eventHandled;

        try {
            User user = userManageBackingBean.getUser();
            eventHandled = false;

            if (type == USER_CREATED) {
                userService.persistNewUser(user, user.getPassword());
            } else if (type == USER_UPDATED) {
                userService.persistEditedUser(user);

                String password = user.getPassword();
                if (StringUtils.isNotBlank(password)) {
                    userService.changePassword(user.getUsername(), password);
                }
            } else if (type == USER_DELETED) {
                deleteUser(userManageBackingBean);
            }
        } catch (ObjectNotUniqueException obnu) {
            backingBean.setServerMessage(obnu.getMessage());
            target.add(this);
            eventHandled = true;
        }

        return !eventHandled;
    }

    private void deleteUser(UserManageBackingBean userManageBackingBean) {
        userService.deleteUser(userManageBackingBean.getUser().getUserId());
    }
}
