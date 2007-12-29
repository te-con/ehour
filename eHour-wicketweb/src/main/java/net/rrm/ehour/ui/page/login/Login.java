/**
 * Created on May 29, 2007
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

package net.rrm.ehour.ui.page.login;

import java.io.Serializable;

import net.rrm.ehour.ui.page.admin.mainconfig.MainConfig;
import net.rrm.ehour.ui.page.report.aggregate.AggregatedReportPage;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonUIStaticData;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Login page 
 **/

@SuppressWarnings("unchecked")
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
	
	private void setupForm()
	{
		//add(new StyleSheetReference("loginStyle", new CompressedResourceReference(LoginPage.class, "style/ehourLogin.css")));

		add(new Label("pageTitle", new ResourceModel("login.login.header")));
		add(new SignInForm("loginform", new SimpleUser()));

	}

	/**
	 * The class <code>SignInForm</code> is a subclass of the Wicket
	 * {@link Form} class that attempts to authenticate the login request using
	 * Wicket auth (which again delegates to Acegi Security).
	 */
	public final class SignInForm extends Form
	{
		private static final long serialVersionUID = -4355842488508724254L;

		/**
		 * 
		 * @param id
		 * @param model
		 */
		public SignInForm(String id, SimpleUser model)
		{
			super(id, new CompoundPropertyModel(model));

			FeedbackPanel	feedback = new LoginFeedbackPanel("feedback");
			feedback.setMaxMessages(1);
			
			add(feedback);
			TextField usernameInput = new RequiredTextField("username");
			usernameInput.setPersistent(true);
			add(usernameInput);
			add(new PasswordTextField("password").setResetPassword(true));
			add(new Button("signin", new ResourceModel("login.login.submit")));
			
			// TODO layout is off when feedback panel uses its space
			Label demoMode = new Label("demoMode", new ResourceModel("login.demoMode"));
			add(demoMode);
			demoMode.setVisible(((EhourWebSession)getSession()).getEhourConfig().isInDemoMode());
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
					Class<? extends WebPage> homepage = getHomepageForRole(session.getRoles());
					
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
		
		/**
		 * 
		 * @param roles
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private Class<? extends WebPage> getHomepageForRole(Roles roles)
		{
			Class<? extends WebPage>	homepage;
			
			if (roles.contains(CommonUIStaticData.ROLE_ADMIN))
			{
				homepage = MainConfig.class;
			}
			else if (roles.contains(CommonUIStaticData.ROLE_REPORT))
			{
				homepage = AggregatedReportPage.class;
			}
			else
			{
				homepage = getApplication().getHomePage();
			}
		
			return homepage;
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