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

package net.rrm.ehour.ui.common.component.header;

import net.rrm.ehour.ui.common.menu.MenuDefinition$;
import net.rrm.ehour.ui.common.menu.TreeBasedMenu;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.ui.login.page.Login;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * Main navigation panel
 **/

public class HeaderPanel extends AbstractBasePanel<Void>
{
	private static final long serialVersionUID = 854412484275829659L;

	public HeaderPanel(String id)
	{
		super(id);

		add(new BookmarkablePageLink<Void>("homeLink", AuthUtil.getHomepageForRole(EhourWebSession.getSession().getRoles())));

		add(createNav("nav"));

		add(new BookmarkablePageLink<Login>("logoffLink", Login.class));
		add(addLoggedInUser("prefsLink"));
	}

    private Component createNav(String id) {
        return new TreeBasedMenu(id, MenuDefinition$.MODULE$.createMenuDefinition());
    }

	private Link<UserPreferencePage> addLoggedInUser(String id)
	{
		BookmarkablePageLink<UserPreferencePage> link = new BookmarkablePageLink<UserPreferencePage>(id, UserPreferencePage.class);

		Label loggedInUserLabel = new Label("loggedInUser", new Model<String>(AuthUtil.getUser().getFullName()) );
		link.add(loggedInUserLabel);

		return link;
	}
}