package net.rrm.ehour.ui.report.page.command;

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel;
import net.rrm.ehour.ui.report.trend.DetailedReport;
import net.rrm.ehour.ui.report.util.ReportUtil;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

public class DefaultGlobalReportPageDetailedCommand implements GlobalReportPageDetailedCommand
{
	private static final long serialVersionUID = 6466499805557067390L;

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.page.command.GlobalReportPageDetailedCommand#createDetailedReportTabs(net.rrm.ehour.persistence.persistence.ui.report.panel.criteria.ReportCriteriaBackingBean)
	 */
	public List<ITab> createDetailedReportTabs(ReportCriteriaBackingBean backingBean)
	{
		final ReportCriteria criteria = backingBean.getReportCriteria();
		
		ITab detailedTab = new AbstractTab(new KeyResourceModel("report.title.detailed"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getDetailedReportPanel(panelId, criteria);
			}
		};		
		
		return Collections.singletonList(detailedTab);
	}

	
	/**
	 * Get detailed report panel
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getDetailedReportPanel(String id, ReportCriteria reportCriteria)
	{
		DetailedReport detailedReport = new DetailedReport(reportCriteria, EhourWebSession.getSession().getEhourConfig().getLocale());
		ReportUtil.storeInCache(detailedReport);
		DetailedReportPanel panel = new DetailedReportPanel(id, detailedReport);
		
		return panel;
	}	
}
