/**
 * Created on May 8, 2007
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

package net.rrm.ehour.ui.page.user;

import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.calendar.CalendarPanel;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.service.UserService;
import wicket.spring.injection.annot.SpringBean;

/**
 * Overview page 
 **/

public class OverviewPage extends BasePage
{
	@SpringBean
	private	UserService	userService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6873845464139697303L;

	public OverviewPage()
	{
		super("overview", null);
		
		add(new CalendarPanel("sidePanel"));
		
		User user = userService.getUser(1);
		System.out.println(user.getLastName());
//		add(new ProjectOverviewPanel("projectOverviewPanel"));
	}

}
