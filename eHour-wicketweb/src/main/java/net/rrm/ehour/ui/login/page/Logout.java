package net.rrm.ehour.ui.login.page;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;

public class Logout extends WebPage {
    public Logout() {
        EhourWebSession.getSession().signOut();

        throw new RestartResponseAtInterceptPageException(Login.class);
    }
}
