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

package net.rrm.ehour.ui.common.report;

import org.apache.wicket.util.convert.IConverter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Report column
 * 
 * The converterArgs may look iffy, cloning a model could as well be accomplished
 * with a clone() however that also means that for each model the constructor args should
 * be stored globally in the object. Now reflection is cpu wise more costly while storing
 * constructor args is more memory costly. May matter with large reports.
 **/

public class ReportColumn implements Serializable
{
	public static enum ColumnType { OTHER, DATE, RATE, HOUR, TURNOVER, COMMENT }

    public static enum DisplayType {VISIBLE, ALLOW_DUPLICATES, CHART_SERIES_COLUMN }

    private static final long serialVersionUID = -6736366461333244457L;
	
	private List<DisplayType> displayTypes;
	private	String		columnHeaderResourceKey;
	private IConverter	converter;
	
	private ColumnType	columnType = ColumnType.OTHER;
	
	public ReportColumn(String columnHeaderResourceKey)
	{
		this(columnHeaderResourceKey, DisplayType.VISIBLE, DisplayType.ALLOW_DUPLICATES, DisplayType.CHART_SERIES_COLUMN);
	}

	public ReportColumn(String columnHeaderResourceKey, DisplayType...displayTypes)
	{
		this(columnHeaderResourceKey, ColumnType.OTHER, displayTypes);
	}
	
	public ReportColumn(String columnHeaderResourceKey, ColumnType columnType, DisplayType...displayTypes)
	{
		this(columnHeaderResourceKey, columnType, null, displayTypes);
	}

	public ReportColumn(String columnHeaderResourceKey, ColumnType columnType, IConverter converter, DisplayType...displayTypes)
	{

		this.columnHeaderResourceKey = columnHeaderResourceKey;
		this.converter = converter;
		this.displayTypes = Arrays.asList(displayTypes);
		this.columnType = columnType;
	}
	
	/**
	 * @return the visible
	 */
	public boolean isVisible()
	{
		return displayTypes.contains(DisplayType.VISIBLE);
	}

	/**
	 * @return the columnHeaderResourceKey
	 */
	public String getColumnHeaderResourceKey()
	{
		return columnHeaderResourceKey;
	}

	/**
	 * @return the converter
	 */
	public IConverter getConverter()
	{
		return converter;
	}

	/**
	 * @return the columnType
	 */
	public ColumnType getColumnType()
	{
		return columnType;
	}

	/**
	 * @return the allowDuplicates
	 */
	public boolean isAllowDuplicates()
	{
		return displayTypes.contains(DisplayType.ALLOW_DUPLICATES);
	}

	/**
	 * @return the chartSeriesColumn
	 */
	public boolean isChartSeriesColumn()
	{
		return displayTypes.contains(DisplayType.CHART_SERIES_COLUMN);
	}
}
