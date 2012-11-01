package net.rrm.ehour.ui.report.page.command;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;

import org.apache.wicket.extensions.markup.html.tabs.ITab;

public interface GlobalReportPageDetailedCommand extends Serializable
{
	/**
	 * Create detailed report tabs
	 * @param backingBean
	 * @return
	 */
	List<ITab> createDetailedReportTabs(ReportCriteriaBackingBean backingBean);
}
