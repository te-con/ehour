package net.rrm.ehour.ui.report.page;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;

import org.apache.wicket.extensions.markup.html.tabs.ITab;

public interface ReportTabBuilder extends Serializable
{
	/**
	 * Create report tabs for an aggregate report
	 * @param backingBean
	 * @return
	 */
	List<ITab> createAggregateReportTabs(ReportCriteriaBackingBean backingBean);
}
