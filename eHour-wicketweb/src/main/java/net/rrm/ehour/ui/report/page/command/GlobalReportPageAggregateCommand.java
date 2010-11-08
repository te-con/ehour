package net.rrm.ehour.ui.report.page.command;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;

import org.apache.wicket.extensions.markup.html.tabs.ITab;

public interface GlobalReportPageAggregateCommand extends Serializable
{
	/**
	 * Create report tabs for an aggregate report
	 * @param backingBean
	 * @return
	 */
	public List<ITab> createAggregateReportTabs(ReportCriteriaBackingBean backingBean);
}
