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

import java.io.Serializable;

import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Login page 
 **/

public class LoginPage extends WebPage
{
	public LoginPage()
	{
		this(null);
	}

	/**
	 * Creates a new sign-in page with the given parameters (ignored).
	 * @param parameters page parameters (ignored)
	 */
	public LoginPage(final PageParameters parameters)
	{
		add(new SignInForm("loginform", new SimpleUser()));

		EhourWebSession YourAppSession = EhourWebSession.getYourAppSession();
		if (YourAppSession.isSignedIn())
		{
			error("already logged in");
		}
	}

	/**
	 * The class <code>SignInForm</code> is a subclass of the Wicket
	 * {@link Form} class that attempts to authenticate the login request using
	 * Wicket auth (which again delegates to Acegi Security).
	 */
	public final class SignInForm extends Form
	{
		public SignInForm(String id, SimpleUser model)
		{
			super(id, new CompoundPropertyModel(model));
			add(new FeedbackPanel("feedback"));
			add(new TextField("username").setRequired(true));
			add(new PasswordTextField("password").setResetPassword(true));
		}

		/**
		 * Called upon form submit. Attempts to authenticate the user.
		 */
		protected void onSubmit()
		{
			if (EhourWebSession.getYourAppSession().isSignedIn())
			{
				// Already logged in, ignore the submit.
				error("already logged in");

			} else
			{
				SimpleUser user = ((SimpleUser) getModel().getObject());
				String username = user.getUsername();
				String password = user.getPassword();

				// Attempt to authenticate.
				EhourWebSession session = (EhourWebSession) Session.get();
				if (session.signIn(username, password))
				{

					Roles roles = session.getRoles();
					if (!continueToOriginalDestination())
					{
						// Continue to the application home page.
						setResponsePage(getApplication().getHomePage());

						// TODO
							System.out.println("User '" + username + "' directed to application" + " homepage (" + getApplication().getHomePage().getName() + ").");

					} else
					{
							System.out.println("User '" + username + "' continues to original destination.");
					}

				} else
				{
					System.out.println("Could not authenticate user '" + username + "'. Transferring back to sign-in page.");
					error("invalid user/pass");
				}
			}

			// ALWAYS do a redirect, no matter where we are going to. The point is that the
			// submitting page should be gone from the browsers history.
			setRedirect(true);
		}
	}

	/**
	 * Simple bean that represents the properties for a login attempt (username
	 * and clear text password).
	 */
	public static class SimpleUser implements Serializable
	{
		private static final long serialVersionUID = -5617176504597041829L;

		private String username;

		private String password;

		public String getUsername()
		{
			return username;
		}

		public void setUsername(String username)
		{
			this.username = username;
		}

		public String getPassword()
		{
			return password;
		}

		public void setPassword(String password)
		{
			this.password = password;
		}
	}
}