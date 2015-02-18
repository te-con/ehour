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

package net.rrm.ehour.ui.common.page;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventListener;
import net.rrm.ehour.ui.common.header.HeaderPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.update.LatestVersionLinkPanel;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * Base layout of all pages, adds header panel
 */

public abstract class AbstractBasePage<T> extends WebPage implements AjaxEventListener {
    private static final long serialVersionUID = 7090746921483608658L;
    public static final String NEW_VERSION_ID = "newVersion";
    private final ResourceModel pageTitle;

    private static final CssResourceReference EHOUR_CSS = new CssResourceReference(AbstractBasePage.class, "ehour.css");

    public AbstractBasePage(ResourceModel pageTitle) {
        super();
        this.pageTitle = pageTitle;

    }

    public AbstractBasePage(ResourceModel pageTitle, IModel<T> model) {
        super(model);

        this.pageTitle = pageTitle;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        setupPage(pageTitle);
    }

    private void setupPage(ResourceModel pageTitle) {
        addOrReplace(new HeaderPanel("mainNav"));
        addOrReplace(new Label("pageTitle", pageTitle));
        addOrReplace(new LatestVersionLinkPanel(NEW_VERSION_ID));
    }

    @Override
    public final void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(EHOUR_CSS));
        onRenderHead(response);
    }

    protected void onRenderHead(IHeaderResponse response) {
    }

    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        return true;
    }

    public void publishAjaxEvent(AjaxEvent ajaxEvent) {
        ajaxEventReceived(ajaxEvent);
    }

    protected final EhourWebSession getEhourWebSession() {
        return EhourWebSession.getSession();
    }

    protected EhourConfig getConfig() {
        return EhourWebSession.getEhourConfig();
    }

    @SuppressWarnings({"unchecked"})
    public IModel<T> getPageModel() {
        return (IModel<T>) getDefaultModel();
    }
}
