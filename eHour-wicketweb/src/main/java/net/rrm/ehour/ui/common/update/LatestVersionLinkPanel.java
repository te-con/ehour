package net.rrm.ehour.ui.common.update;

import net.rrm.ehour.update.UpdateService;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.http.Cookie;
import java.util.List;

public class LatestVersionLinkPanel extends Panel {
    private static final int EXPIRY = 60 * 60 * 24 * 30;

    @SpringBean
    private UpdateService updateService;

    public LatestVersionLinkPanel(String id) {
        super(id);

        add(addLatestVersionBlock("newVersion"));
    }

    private WebMarkupContainer addLatestVersionBlock(String id) {
        boolean latestVersion = updateService.isLatestVersion();

        if (latestVersion) {
            return new WebMarkupContainer(id);
        } else {
            final String latestAvailableVersion = updateService.getLatestVersionNumber().or("unknown");

            final String cookieName = createCookieName(latestAvailableVersion);

            boolean seenCookieFound = findSeenCookie(cookieName);

            if (seenCookieFound) {
                return new WebMarkupContainer(id);
            }

            Fragment newVersionFragment = new Fragment(id, "newVersionFragment", this);
            add(newVersionFragment);

            Link ehourLink = new Link("link") {
                @Override
                public void onClick() {
                    setSeenCookie();
                    setResponsePage(new RedirectPage("http://www.ehour.nl/"));
                }

                private void setSeenCookie() {
                    Cookie cookie = new Cookie(cookieName, "seen");
                    cookie.setPath("/");
                    cookie.setMaxAge(EXPIRY);
                    ((WebResponse) getRequestCycle().getResponse()).addCookie(cookie);
                }
            };
            newVersionFragment.add(ehourLink);

            Label label = new Label("latestVersion", latestAvailableVersion);
            newVersionFragment.add(label);

            return newVersionFragment;
        }
    }

    private boolean findSeenCookie(String cookieName) {
        boolean seenCookieFound = false;

        List<Cookie> cookies = ((WebRequest)getRequestCycle().getRequest()).getCookies();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();

            if (cookieName.equals(name) && "seen".equals(cookie.getValue())) {
                seenCookieFound = true;
                break;
            }
        }
        return seenCookieFound;
    }

    private String createCookieName(String latestAvailableVersion) {
        return "ehour_update_" + latestAvailableVersion;
    }
}
