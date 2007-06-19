/**
 * Created on May 29, 2007
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

package net.rrm.ehour.ui.page.login;

import net.rrm.ehour.ui.authentication.EhourLoginContext;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.security.WaspSession;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.strategies.LoginException;

/**
 * Login page 
 **/

public class LoginPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public LoginPage()
	{
		// stateless so the login page will not throw a timeout exception
		// note that is only a hint we need to have stateless components on the
		// page for this to work, like a statelessform
		setStatelessHint(true);
		add(new FeedbackPanel("feedback")
		{
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.Component#isVisible()
			 */
			public boolean isVisible()
			{
				return anyMessage();
			}
		});
		String panelId = "signInPanel";
		newUserPasswordSignInPanel(panelId);
	}

	/**
	 * Creeert een sign in panel voor instellingen die hun authenticatie enkel
	 * baseren op username/wachtwoord.
	 * 
	 * @param panelId
	 * @param info
	 */
	protected void newUserPasswordSignInPanel(String panelId)
	{
		add(new UsernamePasswordSignInPanel(panelId)
		{
			/** Voor serializatie. */
			private static final long serialVersionUID = 1L;

			public boolean signIn(String username, String password)
			{
				EhourLoginContext ctx=new EhourLoginContext(username, password);
				try
				{
					((WaspSession)getSession()).login(ctx);
				}
				catch (LoginException e)
				{
					return false;
				}
				return true;
			}
		});
	}
}