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

import com.richemont.jira.JiraConst;
import com.richemont.jira.JiraIssue;
import com.richemont.jira.JiraService;
import com.richemont.windchill.WindChillService;
import com.richemont.windchill.WindChillUpdateService;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.json.JsonArray;

import static net.rrm.ehour.ui.common.util.AuthUtil.Homepage;

/**
 * Login page
 */

public class Login extends WebPage {
    private static final long serialVersionUID = -134022212692477120L;

    private static Logger LOGGER = Logger.getLogger(Login.class);

    @SpringBean
    private AuthUtil authUtil;

    public boolean isEnabled() {
        return enabled == null || Boolean.parseBoolean(enabled);
    }

    @Value("${ehour.windchill.enabled}")
    private String enabled;

    @SpringBean
    private WindChillService chillService;

    @SpringBean
    private WindChillUpdateService chillUpdateService;

    @SpringBean
    private JiraService jiraService;

    @SpringBean
    private UserService userService;

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
        addOrReplace(new Label("pageTitle", new ResourceModel("login.login.header")));

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
            super(id, new CompoundPropertyModel<SimpleUser>(model));
            usernameInput = new RequiredTextField<String>("username");
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

        /**
         * get Jira / PJL activities
         * added by LLI for Richemont
         * nov 2013
         */
        @Override
        protected void onSubmit() {

            SimpleUser user = getModelObject();
            String username = user.getUsername();
            String password = user.getPassword();
            boolean isSyncLink = false;

            EhourWebSession session = EhourWebSession.getSession();

            // When authenticated decide the redirect
            if (session.signIn(username, password)) {
                HttpServletRequest request = (HttpServletRequest) getRequest().getContainerRequest();
                User assignedUser = userService.getAuthorizedUser(username);

                // eHour: Chargement des activites
                LOGGER.info("\n\n");
                LOGGER.info("****************** GET ALL ACTIVITIES from login ****************************");
                Map<String, Activity> allAssignedActivitiesByCode = chillService.getAllAssignedActivitiesByCode(assignedUser);

                // JIRA --> eHour
                LOGGER.info("\n\n");
                LOGGER.info("****************** START JIRA SYNC (get Jira issues) ****************************");

                boolean isJiraUser = userService.isLdapUserMemberOf(username, JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF);
                LOGGER.info("Found user in " + JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF + " createJiraIssuesForUser() for user " + username);

                Map<JiraIssue, Activity> activitiesMasteredByJira = null;
                try {
                    if (isJiraUser){
                        activitiesMasteredByJira = jiraService.createJiraIssuesForUser(allAssignedActivitiesByCode, username);
                        if (activitiesMasteredByJira == null) {
                            isSyncLink = false;
                        }else{
                            isSyncLink = true;
                            LOGGER.info("\n\n");
                            LOGGER.info("****************** Identify missing activity in PJL from JIRA->EHOUR ********************");
                            JsonArray activitiesPjlToBeCreated = jiraService.identifyMissingPjlActivity (activitiesMasteredByJira);
                            LOGGER.debug("JsonArray for activitiesPjlToBeCreated:");
                            LOGGER.debug(activitiesPjlToBeCreated);

                            request.getSession().setAttribute("MissingPjlActivity", activitiesPjlToBeCreated);
                        }
                    }else{
                        LOGGER.info("User do not exist in " + JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF + " : skip createJiraIssuesForUser() for user " + username);
                        isSyncLink = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    isSyncLink = false;
                }


                // WINDCHILL --> eHour
                LOGGER.info("\n\n");
                LOGGER.info("****************** START WINDCHILL SYNC (get PJL activities) ************************");
                if (!isEnabled()) {
                    LOGGER.info("WARNING: Windchill sync is disabled");
                } else {
                    try {
                        isSyncLink = chillService.updateDataForUser(allAssignedActivitiesByCode , username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                allAssignedActivitiesByCode.clear();
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