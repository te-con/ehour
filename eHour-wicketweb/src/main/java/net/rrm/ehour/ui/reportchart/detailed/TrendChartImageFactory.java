package net.rrm.ehour.ui.reportchart.detailed;

import java.io.Serializable;

import net.rrm.ehour.report.reports.element.ReportElement;

import org.apache.wicket.model.IModel;

public interface TrendChartImageFactory<RE extends ReportElement> extends Serializable
{
	public AbstractTrendChartImage<RE> getTrendChartImage(int seriesColumn, IModel model);
}
