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

package net.rrm.ehour.ui.report.page;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.builder.ReportTabFactory;
import net.rrm.ehour.ui.report.builder.ReportTabs;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.report.panel.criteria.ReportTabbedPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

import static net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaPanel.ReportTypeChangeEvent;

@AuthorizeInstantiation(value = {UserRole.ROLE_USER, UserRole.ROLE_REPORT, UserRole.ROLE_PROJECTMANAGER})
public class ReportPage extends AbstractReportPage<ReportCriteriaBackingBean> {
    private static final long serialVersionUID = 6614404841734599622L;
    private static final CssResourceReference REPORT_CSS = new CssResourceReference(ReportPage.class, "report.css");

    private ReportTabbedPanel tabPanel;

    @SpringBean
    private ReportTabs reportTabs;

    public ReportPage() {
        super(new ResourceModel("report.global.title"));
    }

    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof ReportTypeChangeEvent) {
            ReportTypeChangeEvent e = (ReportTypeChangeEvent)event.getPayload();

            reset();

            e.target.add(tabPanel);
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        reset();
    }

    @Override
    protected void onRenderHead(IHeaderResponse response) {
        response.render(CssReferenceHeaderItem.forReference(REPORT_CSS));
   }

    private void reset() {
        final ReportCriteria reportCriteria = getReportCriteria();
        final IModel<ReportCriteriaBackingBean> model = new CompoundPropertyModel<>(new ReportCriteriaBackingBean(reportCriteria));
        setDefaultModel(model);

        List<ITab> tabList = new ArrayList<>();

        tabList.add(new AbstractTab(getReportTitle(reportCriteria.getUserSelectedCriteria())) {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public Panel getPanel(String panelId) {
                return new ReportCriteriaPanel(panelId, model);
            }
        });

        tabPanel = new ReportTabbedPanel("reportContainer", tabList);
        addOrReplace(tabPanel);
    }

    private IModel<String> getReportTitle(UserSelectedCriteria userSelectedCriteria) {
        if (userSelectedCriteria.isForPm()) {
            return new KeyResourceModel("report.criteria.title.pm");
        } else if (userSelectedCriteria.isForGlobalReport()) {
            return new KeyResourceModel("report.criteria.title.global");
        } else {
            return new MessageResourceModel("report.criteria.title.user", this, EhourWebSession.getUser().getFullName());
        }

    }

    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_UPDATED) {
            updateCriteria(ajaxEvent);
        } else if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_RESET) {
            resetCriteria(ajaxEvent);
        }

        return false;
    }

    private void resetCriteria(AjaxEvent ajaxEvent) {
        getEhourWebSession().setUserSelectedCriteria(null);

        reset();

        ajaxEvent.getTarget().add(tabPanel);
    }

    private void updateCriteria(AjaxEvent ajaxEvent) {
        ReportCriteriaBackingBean backingBean = (ReportCriteriaBackingBean) getDefaultModelObject();

        clearTabs();

        addReportTabs(backingBean);

        ajaxEvent.getTarget().add(tabPanel);
    }


    /**
     * Clear tabs except for the first one
     */
    private void clearTabs() {
        List<ITab> tabs = tabPanel.getTabs();

        while (tabs.size() > 1) {
            tabs.remove(1);
        }
    }

    private void addReportTabs(ReportCriteriaBackingBean backingBean) {
        List<ReportTabFactory> tabFactories = reportTabs.getTabFactories();

        List<ITab> tabs = Lists.newArrayList();

        for (ReportTabFactory tabFactory : tabFactories) {
            Optional<ITab> reportTab = tabFactory.createReportTab(backingBean.getReportCriteria());

            if (reportTab.isPresent()) {
                tabs.add(reportTab.get());
            }
        }

        addTabs(tabs);

        tabPanel.setSelectedTab(1);
    }

    private void addTabs(List<ITab> tabs) {
        for (ITab iTab : tabs) {
            tabPanel.addTab(iTab);
        }
    }
}
