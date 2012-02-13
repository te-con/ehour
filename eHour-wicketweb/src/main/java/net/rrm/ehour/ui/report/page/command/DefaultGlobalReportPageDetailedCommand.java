package net.rrm.ehour.ui.report.page.command;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel;
import net.rrm.ehour.ui.report.trend.DetailedReportModel;
import net.rrm.ehour.ui.report.util.ReportUtil;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.Collections;
import java.util.List;

public class DefaultGlobalReportPageDetailedCommand implements GlobalReportPageDetailedCommand
{
	private static final long serialVersionUID = 6466499805557067390L;

	public List<ITab> createDetailedReportTabs(final ReportCriteriaBackingBean backingBean)
	{
        ITab detailedTab = new AbstractTab(new KeyResourceModel("report.title.detailed"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getDetailedReportPanel(panelId, backingBean.getReportCriteria());
			}
		};		
		
		return Collections.singletonList(detailedTab);
	}

	private Panel getDetailedReportPanel(String id, ReportCriteria reportCriteria)
	{
        DetailedReportModel detailedReport = createAndCacheDetailedReport(reportCriteria);
		return new DetailedReportPanel(id, detailedReport);
	}

    private DetailedReportModel createAndCacheDetailedReport(ReportCriteria reportCriteria) {
        DetailedReportModel detailedReport = new DetailedReportModel(reportCriteria);
        ReportUtil.storeInCache(detailedReport);
        return detailedReport;
    }
}
