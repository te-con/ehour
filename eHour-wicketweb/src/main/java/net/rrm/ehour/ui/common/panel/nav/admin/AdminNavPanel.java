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

package net.rrm.ehour.ui.common.panel.nav.admin;

import net.rrm.ehour.ui.admin.assignment.page.AssignmentAdmin;
import net.rrm.ehour.ui.admin.customer.page.CustomerAdmin;
import net.rrm.ehour.ui.admin.department.DepartmentAdminPage;
import net.rrm.ehour.ui.admin.project.page.ProjectAdmin;
import net.rrm.ehour.ui.admin.user.page.UserAdminPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Navigation panel for admin
 **/

public class AdminNavPanel extends Panel
{
	private static final long serialVersionUID = -7877416542663086633L;

	public AdminNavPanel(String id)
	{
		super(id);

		add(new BookmarkablePageLink<UserAdminPage>("userAdmin", UserAdminPage.class));
		add(new BookmarkablePageLink<DepartmentAdminPage>("deptAdmin", DepartmentAdminPage.class));
		add(new BookmarkablePageLink<CustomerAdmin>("customerAdmin", CustomerAdmin.class));
		add(new BookmarkablePageLink<ProjectAdmin>("projectAdmin", ProjectAdmin.class));
		add(new BookmarkablePageLink<AssignmentAdmin>("assignmentAdmin", AssignmentAdmin.class));
	}
}
