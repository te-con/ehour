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

package net.rrm.ehour.ui.admin.project.dto;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.common.model.AdminBackingBean;

/**
 * Project admin backing bean
 **/

public interface ProjectAdminBackingBean extends AdminBackingBean
{
	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.admin.project.panel.dto.ProjectAdminBackingBean#getProject()
	 */
	public Project getProject();

	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.admin.project.panel.dto.ProjectAdminBackingBean#setProject(net.rrm.ehour.persistence.persistence.domain.Project)
	 */
	public void setProject(Project project);
}
