/**
 * Created on Aug 13, 2007
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

package net.rrm.ehour.ui.page.admin.user;

import net.rrm.ehour.user.domain.User;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;

/**
 * Link fragment
 **/

public class UserLinkFragment extends Fragment
{
	private static final long serialVersionUID = -6089664048887709376L;
	private	final Integer	userId;
	
	public UserLinkFragment(String id, String markUpId, MarkupContainer markupContainer, User user)
	{
		super(id, markUpId, markupContainer);
		
		userId = user.getUserId();
		
		AjaxLink	link = new AjaxLink("")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				System.out.println("click on " + userId);
			}
		};
		
		add(link);
		add(new Label("item", user.getLastName() + ", " + user.getFirstName() + (user.isActive() ? "" : "*")));
	}
}