/**
 * Created on Aug 20, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page.admin.department;

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.sort.UserDepartmentComparator;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Department admin page
 **/

public class DepartmentAdmin  extends BaseTabbedAdminPage
{
	private static final long serialVersionUID = -6686097898699382233L;

	@SpringBean
	private UserService			userService;
	private	static final Logger	logger = Logger.getLogger(DepartmentAdmin.class);
	
	
	/**
	 * 
	 * @param pageTitle
	 * @param addTabTitle
	 * @param editTabTitle
	 */
	public DepartmentAdmin(ResourceModel pageTitle, ResourceModel addTabTitle, ResourceModel editTabTitle)
	{
		super(pageTitle, addTabTitle, editTabTitle);
		
		List<UserDepartment>	departments;
		departments = getUserDepartments();

	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getAddPanel(String panelId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Panel getEditPanel(String panelId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AdminBackingBean getNewAddBackingBean()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AdminBackingBean getNewEditBackingBean()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Panel getNoSelectionPanel(String panelId)
	{
		// TODO Auto-generated method stub
		return null;
	}
	

	/**
	 * Get the user departments from the backend
	 * @return
	 */
	private List<UserDepartment> getUserDepartments()
	{
		List<UserDepartment>	userDepartments = userService.getUserDepartments();
		Collections.sort(userDepartments, new UserDepartmentComparator());
		
		return userDepartments;
	}	
}
