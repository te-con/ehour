/**
 * Created on Feb 3, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.web.report.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.RectangleInsets;

/**
 * TODO 
 **/

public class ChartUtil
{
	public static void changeChartStyle(JFreeChart chart)
	{
		TextTitle	title;
		Font		chartTitleFont = new Font("SansSerif", Font.BOLD, 12);
		
		chart.setBackgroundPaint(new Color(0xf9f9f9));
		chart.setAntiAlias(true);
		title = chart.getTitle();
		title.setFont(chartTitleFont);
		title.setPaint(new Color(0x536e87));
		chart.setTitle(title);
		
		CategoryPlot plot;
		AxisSpace space;
		RectangleInsets insets;
		Rectangle2D rect;
		rect = new Rectangle(0, 0, 2, 2);
		
//		rect2D = new Rectangle2D(0);
		
		plot = chart.getCategoryPlot();
		insets = new RectangleInsets(0, 0, 0, 0);
//		plot.setAxisOffset(insets);
//		insets = plot.getAxisOffset();
//		insets.createInsetRectangle(rect);
		
//		System.out.println(insets..);
//		insets.
//		space.setLeft(0);
		
//		System.out.println(chart.getCategoryPlot());
	}
}
