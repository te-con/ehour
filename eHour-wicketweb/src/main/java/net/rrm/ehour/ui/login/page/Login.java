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
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.persistence.CookieValuePersister;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

import java.io.Serializable;

/**
 * Login page
 */

public class Login extends WebPage {
    private static final long serialVersionUID = -134022212692477120L;
    private static final String EHOUR_USERNAME = "ehour.username";

    @Override
    protected void onBeforeRender() {
        EhourWebSession session = EhourWebSession.getSession();

        if (session.isSignedIn()) {
            redirectToHomepage(session);
        }

        setupForm();

        super.onBeforeRender();
    }

    private void setupForm() {
        addOrReplace(new Label("pageTitle", new ResourceModel("login.login.header")));

        SimpleUser user = preloadUsernameFromCookie();

        addOrReplace(new SignInForm("loginform", user));
    }

    private SimpleUser preloadUsernameFromCookie() {
        CookieValuePersister persister = new CookieValuePersister();
        String cookieUsername = persister.load(EHOUR_USERNAME);

        SimpleUser user = new SimpleUser();
        user.setUsername(cookieUsername);
        return user;
    }

    private void redirectToHomepage(EhourWebSession session) {
        Class<? extends Page> homepage = AuthUtil.getHomepageForRole(session.getRoles());
        setResponsePage(homepage);
    }

    public class SignInForm extends Form<SimpleUser> {
        private static final long serialVersionUID = -4355842488508724254L;

        public SignInForm(String id, SimpleUser model) {
            super(id, new CompoundPropertyModel<SimpleUser>(model));

            FeedbackPanel feedback = new LoginFeedbackPanel("feedback");
            feedback.setMaxMessages(1);
            add(feedback);

            TextField<String> usernameInput = new RequiredTextField<String>("username");
            usernameInput.setMarkupId("username");
            usernameInput.setOutputMarkupId(true);
            add(usernameInput);

            PasswordTextField password = new PasswordTextField("password").setResetPassword(true);
            password.setMarkupId("password");
            password.setOutputMarkupId(true);
            add(password);

            // layout is off when feedback panel uses its space
            Label demoMode = new Label("demoMode", new ResourceModel("login.demoMode"));
            add(demoMode);
            demoMode.setVisible(EhourWebSession.getSession().getEhourConfig().isInDemoMode());

            add(new Label("version", ((EhourWebApplication) this.getApplication()).getVersion()));
        }

        @Override
        protected void onSubmit() {

            SimpleUser user = getModelObject();
            String username = user.getUsername();
            String password = user.getPassword();

            EhourWebSession session = EhourWebSession.getSession();

            if (session.signIn(username, password)) {
                CookieValuePersister persister = new CookieValuePersister();
                persister.save(EHOUR_USERNAME, username);

                redirectToHomepage(session);
            } else {
                error(getLocalizer().getString("login.login.failed", this));
            }

            setRedirect(true);
        }
    }

    public final class LoginFeedbackPanel extends FeedbackPanel {
        private static final long serialVersionUID = 1931344611905158185L;

        public LoginFeedbackPanel(final String id) {
            super(id);
        }
    }

    /**
     * Simple bean that represents the properties for a login attempt (username
     * and clear text password).
     */
    public static class SimpleUser implements Serializable {
        private static final long serialVersionUID = -5617176504597041829L;

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}