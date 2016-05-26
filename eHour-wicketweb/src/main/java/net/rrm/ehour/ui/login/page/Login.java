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
import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;

import static net.rrm.ehour.ui.common.util.AuthUtil.Homepage;

/**
 * Login page
 */

public class Login extends WebPage {
    private static final long serialVersionUID = -134022212692477120L;

    @SpringBean
    private AuthUtil authUtil;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        EhourWebSession session = EhourWebSession.getSession();

        if (session.isSignedIn()) {
            Homepage homepage = authUtil.getHomepageForRole(session.getRoles());
            throw new RestartResponseAtInterceptPageException(homepage.homePage, homepage.parameters);
        }

        setupForm();

        String version = EhourWebApplication.get().getVersion();

        if (version != null && version.contains("SNAPSHOT")) {
            version = String.format("%s beta", version.substring(0, version.indexOf("-SNAPSHOT")));

            addOrReplace(new Label("build", String.format("build %s", EhourWebApplication.get().getBuild())));
        } else {
            Label build = new Label("build", "");
            build.setVisible(false);
            addOrReplace(build);
        }

        add(new Label("version", version));

        super.onBeforeRender();
    }

    private void setupForm() {
        SignInForm loginForm = new SignInForm("loginform", new SimpleUser());
        addOrReplace(loginForm);

        FeedbackPanel feedback = new LoginFeedbackPanel("feedback");
        feedback.setMaxMessages(1);
        loginForm.add(feedback);
    }

    private void redirectToHomepage(EhourWebSession session) {
        Homepage homepage = authUtil.getHomepageForRole(session.getRoles());
        setResponsePage(homepage.homePage, homepage.parameters);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
        response.render(JavaScriptHeaderItem.forScript("$(document).ready(function () { var user = $(\"#username\"); if (!user.val()) $(user).focus(); else $(\"#password\").focus(); });", "onready-master"));
    }

    public class SignInForm extends StatelessForm<SimpleUser> {
        private static final long serialVersionUID = -4355842488508724254L;
        private final TextField<String> usernameInput;

        public SignInForm(String id, SimpleUser model) {
            super(id, new CompoundPropertyModel<>(model));
            usernameInput = new RequiredTextField<>("username");
            usernameInput.setMarkupId("username");
            usernameInput.setOutputMarkupId(true);
            add(usernameInput);

            PasswordTextField password = new PasswordTextField("password").setResetPassword(true);
            password.setMarkupId("password");
            password.setOutputMarkupId(true);
            add(password);

            Label demoMode = new Label("demoMode", new ResourceModel("login.demoMode"));
            add(demoMode);
            demoMode.setVisible(EhourWebSession.getEhourConfig().isInDemoMode());
        }

        @Override
        protected void onSubmit() {
            SimpleUser user = getModelObject();
            String username = user.getUsername();
            String password = user.getPassword();

            EhourWebSession session = EhourWebSession.getSession();

            if (session.signIn(username, password)) {
                redirectToHomepage(session);
            } else {
                SignInForm.this.error(getLocalizer().getString("login.login.failed", this));
            }
        }
    }

    public static final class LoginFeedbackPanel extends FeedbackPanel {
        private static final long serialVersionUID = 1931344611905158185L;

        public LoginFeedbackPanel(final String id) {
            super(id);
        }

        @Override
        protected void onConfigure() {
            detach();
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