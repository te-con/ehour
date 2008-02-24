/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.panel.admin.project.form.dto;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.model.AdminBackingBeanImpl;

/**
 * Project admin backing bean
 **/

public class ProjectAdminBackingBeanImpl extends AdminBackingBeanImpl implements ProjectAdminBackingBean
{
	private static final long serialVersionUID = 5862844398838328155L;
	
	private	Project	project;
	

	public ProjectAdminBackingBeanImpl(Project project)
	{
		this.project = project;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBean#getProject()
	 */
	public Project getProject()
	{
		return project;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBean#setProject(net.rrm.ehour.domain.Project)
	 */
	public void setProject(Project project)
	{
		this.project = project;
	}
}
