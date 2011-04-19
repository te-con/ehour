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

package net.rrm.ehour.ui.login.page;

import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.AuthUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

import java.io.Serializable;

/**
 * Login page
 **/

public class Login extends WebPage
{
	private static final long serialVersionUID = -134022212692477120L;
	private	static Logger logger = Logger.getLogger(Login.class);

	/**
	 *
	 */
	public Login()
	{
		this(null);
	}

	/**
	 * Check if the session exists, kill the session and redirect to the homepage.
	 * This will trigger the authentication but at least the redirect is properly setup
	 * @param parameters page parameters (ignored)
	 */
	public Login(final PageParameters parameters)
	{
		EhourWebSession session = (EhourWebSession)getSession();

		if (session.isSignedIn())
		{
			if (logger.isInfoEnabled())
			{
				logger.info("User already signed in, logging out and redirecting to " + getApplication().getHomePage());
			}

			session.signOut();
			setResponsePage(getApplication().getHomePage());
		}
		else
		{
			setupForm();
		}
	}

	/**
	 * Set up login form
	 */
	private void setupForm()
	{
		add(new Label("pageTitle", new ResourceModel("login.login.header")));
		add(new SignInForm("loginform", new SimpleUser()));
	}

	/**
	 * The class <code>SignInForm</code> is a subclass of the Wicket
	 * {@link Form} class that attempts to authenticate the login request using
	 * Wicket auth (which again delegates to Acegi Security).
	 */
	public final class SignInForm extends Form<SimpleUser>
	{
		private static final long serialVersionUID = -4355842488508724254L;

		/**
		 *
		 * @param id
		 * @param model
		 */
		public SignInForm(String id, SimpleUser model)
		{
			super(id, new CompoundPropertyModel<SimpleUser>(model));

			FeedbackPanel	feedback = new LoginFeedbackPanel("feedback");
			feedback.setMaxMessages(1);

			add(feedback);
			TextField<String> usernameInput = new RequiredTextField<String>("username");
			usernameInput.setPersistent(true);
			add(usernameInput);
			add(new PasswordTextField("password").setResetPassword(true));
			add(new Button("signin", new ResourceModel("login.login.submit")));

			// TODO layout is off when feedback panel uses its space
			Label demoMode = new Label("demoMode", new ResourceModel("login.demoMode"));
			add(demoMode);
			demoMode.setVisible(EhourWebSession.getSession().getEhourConfig().isInDemoMode());



			add(new Label("version", ((EhourWebApplication) this.getApplication()).getVersion()));
		}

		/**
		 * Called upon form submit. Attempts to authenticate the user.
		 */
		protected void onSubmit()
		{
			if (EhourWebSession.getSession().isSignedIn())
			{
				// now this really shouldn't happen as the session is killed in the constructor
				error("already logged in");

			} else
			{
				SimpleUser user = ((SimpleUser) getModel().getObject());
				String username = user.getUsername();
				String password = user.getPassword();

				// Attempt to authenticate.
				EhourWebSession session = (EhourWebSession) Session.get();

				// When authenticated decide the redirect
				if (session.signIn(username, password))
				{
					Class<? extends Page> homepage = AuthUtil.getHomepageForRole(session.getRoles());

					if (logger.isDebugEnabled())
					{
						logger.debug("User '" + username + "' redirected to " + homepage.getName());
					}

					setResponsePage(homepage);
				}
				else
				{
					error(getLocalizer().getString("login.login.failed", this));
				}
			}

			// ALWAYS do a redirect, no matter where we are going to. The point is that the
			// submitting page should be gone from the browsers history.
			setRedirect(true);
		}
	}

	/**
	 *
	 * @author Thies
	 *
	 */
	public final class LoginFeedbackPanel extends FeedbackPanel
	{
		private static final long serialVersionUID = 1931344611905158185L;

		/**
		 * @see org.apache.wicket.Component#Component(String)
		 */
		public LoginFeedbackPanel(final String id)
		{
			super(id);
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