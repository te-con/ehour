package net.rrm.ehour.ui.reportchart.detailed;

import java.io.Serializable;

import net.rrm.ehour.report.reports.element.ReportElement;

import org.apache.wicket.model.IModel;

public interface TrendChartImageFactory<RE extends ReportElement> extends Serializable
{
	/**
	 * Create image
	 * @param seriesColumn column resource key of selected column for series separation
	 * @param model
	 * @return
	 */
	public AbstractTrendChartImage<RE> getTrendChartImage(String seriesColumn, IModel model);
}
