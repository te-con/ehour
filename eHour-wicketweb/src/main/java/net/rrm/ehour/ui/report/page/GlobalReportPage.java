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

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.report.panel.criteria.ReportTabbedPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

@AuthorizeInstantiation(value = {"ROLE_CONSULTANT", "ROLE_REPORT"})
public class GlobalReportPage extends AbstractReportPage<ReportCriteriaBackingBean> {
    private static final long serialVersionUID = 6614404841734599622L;

    private ReportTabbedPanel tabPanel;

    @SpringBean
    private ReportTabBuilder reportTabCreationBuilder;

    public GlobalReportPage() {
        super(new ResourceModel("report.global.title"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        reset();
    }

    private void reset() {
        final ReportCriteria reportCriteria = getReportCriteria();
        final IModel<ReportCriteriaBackingBean> model = new CompoundPropertyModel<ReportCriteriaBackingBean>(new ReportCriteriaBackingBean(reportCriteria));
        setDefaultModel(model);

        List<ITab> tabList = new ArrayList<ITab>();

        tabList.add(new AbstractTab(new KeyResourceModel("report.criteria.title")) {
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

    @Override
    public boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_UPDATED) {
            updateCriteria(ajaxEvent);
        } else if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_RESET) {
            resetCriteria(ajaxEvent);
        }

        return false;
    }

    private void resetCriteria(AjaxEvent ajaxEvent) {
        EhourWebSession.getSession().setUserCriteria(null);

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
        List<ITab> tabs = reportTabCreationBuilder.createReportTabs(backingBean);

        addTabs(tabs);

        tabPanel.setSelectedTab(1);
    }

    private void addTabs(List<ITab> tabs) {
        for (ITab iTab : tabs) {
            tabPanel.addTab(iTab);
        }
    }

}
