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
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.user.service.LdapUser;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * User management page using 2 tabs, an entrySelector panel and the UserForm panel
 */
public class UserManagePage extends AbstractUserManagePageTemplate<LdapUserBackingBean> {
    @SpringBean
    private UserService userService;

    private static final long serialVersionUID = 1883278850247747252L;

    protected LdapUserBackingBean createEditBean(Integer userId) throws ObjectNotFoundException {
        User user = userService.getUser(userId);
        LdapUser ldapUser = userService.getLdapUser(user.getUsername()).get(0);
        ldapUser.setUser(user);
        return new LdapUserBackingBean(ldapUser);
    }

    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new UserAddPlaceholderPanel(panelId);
    }

    @Override
    protected LdapUserBackingBean getNewAddBaseBackingBean() {
        return new LdapUserBackingBean(new LdapUser("test", "test", "Test", "Test"));
    }

    @Override
    protected LdapUserBackingBean getNewEditBaseBackingBean() {
        return new LdapUserBackingBean(new LdapUser("test", "test", "Test", "Test"));
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        LdapUserBackingBean bean = getTabbedPanel().getEditBackingBean();

        return new UserFormPanel(panelId, new CompoundPropertyModel<LdapUserBackingBean>(bean));
    }
}
