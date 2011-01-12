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

package net.rrm.ehour.ui.report.chart;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Base class for image (jfreechart) based charts
 */

public abstract class AbstractReportChartImage<EL extends ReportElement> extends AbstractChartImage
{
    private static final long serialVersionUID = 3184831758116251336L;
    private final static Logger LOG = Logger.getLogger(AbstractReportChartImage.class);

    /**
     * @param id
     * @param dataModel
     * @param width
     * @param height
     */
    public AbstractReportChartImage(String id,
                                    IModel<ReportData> dataModel,
                                    int width,
                                    int height)
    {
        super(id, dataModel, width, height);
    }

    /**
     * Generate chart
     *
     * @return
     */
    protected JFreeChart generateChart()
    {
        ReportData report = (ReportData) getDefaultModelObject();
        return getChart(report);
    }

    /**
     * @param reportData
     * @param forId
     * @param reportName
     * @return
     */
    public JFreeChart getChart(ReportData reportData)
    {
        String reportNameKey = getReportNameKey();
        String reportName = getLocalizer().getString(reportNameKey, this);
        LOG.debug("Creating " + reportName + " chart");

        DefaultCategoryDataset dataset = createDataset(reportData);

        JFreeChart chart = ChartFactory.createBarChart(reportName, // chart title
                null, // domain axis label
                getLocalizer().getString(getValueAxisLabelKey(), this), // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL, // orientation
                false, // include legend
                false, // tooltips?
                false // URLs?
        );

        chart.setBackgroundPaint(new Color(0xf9f9f9));
        chart.setAntiAlias(true);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinePaint(new Color(0, 0, 0, 50));
        plot.setBackgroundPaint(new Color(0xf9f9f9));

        TextTitle title;
        Font chartTitleFont = new Font("SansSerif", Font.BOLD, 12);

        title = chart.getTitle();
        title.setFont(chartTitleFont);
        title.setPaint(new Color(0x536e87));

        chart.setTitle(title);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        Font rendererTitleFont = new Font("SansSerif", Font.PLAIN, 8);
        renderer.setBaseItemLabelFont(rendererTitleFont);
        renderer.setBaseItemLabelPaint(new Color(0xf9f9f9));
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setItemMargin(1.0);

        // set up gradient paints for series...
        GradientPaint gradientPaint = new GradientPaint(0.0f, 0.0f, new Color(0xbfd9f6),
                                                        0.0f, 0.0f, new Color(0xa3bcd8));
        renderer.setSeriesPaint(0, gradientPaint);

        return chart;
    }

    /**
     * Create dataset for this chart
     *
     * @param reportData
     * @return
     */
    private DefaultCategoryDataset createDataset(ReportData reportData)
    {
        Map<ChartRowKey, Number> valueMap = createChartRowMap(reportData);

        List<ChartRowKey> keys = new ArrayList<ChartRowKey>(valueMap.keySet());

        Collections.sort(keys);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String valueAxisLabel = getValueAxisLabelKey();

        for (ChartRowKey rowKeyAgg : keys)
        {
            dataset.addValue(valueMap.get(rowKeyAgg), valueAxisLabel, rowKeyAgg);
        }

        return dataset;
    }

    @SuppressWarnings("unchecked")
    private Map<ChartRowKey, Number> createChartRowMap(ReportData reportData)
    {
        Map<ChartRowKey, Number> valueMap = new HashMap<ChartRowKey, Number>();
        ChartRowKey rowKey;
        Number value;

        for (ReportElement element : reportData.getReportElements())
        {
            rowKey = getRowKey((EL) element);

            value = getColumnValue((EL) element);

            if (value == null)
            {
                value = new Double(0);
            }

            if (valueMap.containsKey(rowKey))
            {
                value = value.doubleValue() + valueMap.get(rowKey).doubleValue();
                valueMap.put(rowKey, value);
            } else
            {
                valueMap.put(rowKey, value);
            }
        }

        return valueMap;
    }

    /**
     * Get report name
     *
     * @return
     */
    protected abstract String getReportNameKey();

    /**
     * Get value axis label
     *
     * @return
     */
    protected abstract String getValueAxisLabelKey();

    /**
     * Get row key from report importer
     *
     * @param aggregate
     * @return
     */
    protected abstract ChartRowKey getRowKey(EL element);

    /**
     * Get column value from report importer
     *
     * @param aggregate
     * @return
     */
    protected abstract Number getColumnValue(EL element);
}
