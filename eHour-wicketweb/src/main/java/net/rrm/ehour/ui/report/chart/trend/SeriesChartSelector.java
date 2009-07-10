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

package net.rrm.ehour.ui.report.chart.trend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.ReportColumn;
import net.rrm.ehour.ui.common.report.ReportConfig;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 * Series selector for trend charts
 * @author Thies
 *
 */
public class SeriesChartSelector<RE extends ReportElement> extends Panel
{
	private static final long serialVersionUID = 1L;
	private AbstractTrendChartImage<RE>	img;
	

	/**
	 * 
	 * @param id
	 * @param config
	 * @param targetImage
	 */
	public SeriesChartSelector(String id, ReportConfig config, final AbstractTrendChartImage<RE> targetImage, final TrendChartImageFactory<RE> imgFactory)
	{
		super(id);
		
		this.img = targetImage;
		
		List<Serializable> columns = new ArrayList<Serializable>();
		
		for (ReportColumn column : config.getReportColumns())
		{
			if (column.isChartSeriesColumn())
			{
				columns.add(column);
			}
		}
		
		final IModel model = new Model();
		
		final DropDownChoice columnSelection = new DropDownChoice("serieChartSelector", model, columns, new TreeReportColumnRenderer());

		columnSelection.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 507045565542332885L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				String selectedColumn = null;
				
				if (model.getObject() != null)
				{
					selectedColumn = ((ReportColumn) model.getObject()).getColumnHeaderResourceKey();
				}
				
				if (selectedColumn != null)
				{
					AbstractTrendChartImage<RE> newImg = imgFactory.getTrendChartImage(selectedColumn, targetImage.getModel());
					
					img.replaceWith(newImg);
					target.addComponent(newImg);
					
					img = newImg;
				}
			}
		});	
		
		add(columnSelection);
	}

	/**
	 * 
	 * @author Thies
	 *
	 */
	private class TreeReportColumnRenderer implements IChoiceRenderer
	{
		private static final long serialVersionUID = 1L;

		public Object getDisplayValue(Object object)
		{
			ReportColumn col = (ReportColumn)object;
			
			return (new ResourceModel(col.getColumnHeaderResourceKey())).getObject();
		}

		public String getIdValue(Object object, int index)
		{
			return Integer.toString(index);
		}
		
	}
}
