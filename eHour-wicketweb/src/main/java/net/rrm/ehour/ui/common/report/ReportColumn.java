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

import java.io.Serializable;

import org.apache.wicket.model.IModel;

/**
 * Report column
 * 
 * The conversionModelArgs may look iffy, cloning a model could as well be accomplished
 * with a clone() however that also means that for each model the constructor args should
 * be stored globally in the object. Now reflection is cpu wise more costly while storing
 * constructor args is more memory costly. May matter with large reports.
 **/

public class ReportColumn implements Serializable
{
	public static enum ColumnType { OTHER, DATE, RATE, HOUR, TURNOVER, COMMENT };
	public static boolean COLUMN_VISIBLE = true;
	public static boolean COLUMN_INVISIBLE = false;
	public static boolean COLUMN_ALLOWDUPLICATES = true;
	public static boolean COLUMN_NODUPLICATES = false;
	
	private static final long serialVersionUID = -6736366461333244457L;
	private boolean					visible = true;
	private	String						columnHeaderResourceKey;
	private Class<? extends IModel>		conversionModel;
	private Object[]						conversionModelConstructorParams;
	private Class<?>[]					conversionModelConstructorParamTypes; // needed because types can't always be determined of proxied objects 
	private boolean					allowDuplicates;
	private boolean					chartSeriesColumn;
	
	private ColumnType	columnType = ColumnType.OTHER;
	

	public ReportColumn(String columnHeaderResourceKey)
	{
		this(columnHeaderResourceKey, true);
	}

	public ReportColumn(String columnHeaderResourceKey, ColumnType columnType)
	{
		this(columnHeaderResourceKey, null, null, true, columnType);
	}

	public ReportColumn(String columnHeaderResourceKey, boolean visible)
	{
		this(columnHeaderResourceKey, null, null, visible);
	}
	
	public ReportColumn(String columnHeaderResourceKey, boolean visible, boolean allowDuplicates)
	{
		this(columnHeaderResourceKey, null, visible, allowDuplicates, ColumnType.OTHER);
	}	

	public ReportColumn(String columnHeaderResourceKey, boolean visible, boolean allowDuplicates, boolean chartSeriesColumn)
	{
		this(columnHeaderResourceKey, null, visible, allowDuplicates, ColumnType.OTHER);
		
		this.chartSeriesColumn = chartSeriesColumn;
	}	
	
	
	public ReportColumn(String columnHeaderResourceKey, Class<? extends IModel> conversionModel)
	{
		this(columnHeaderResourceKey, conversionModel, null, true);
	}

	public ReportColumn(String columnHeaderResourceKey, Class<? extends IModel> conversionModel, boolean visible, ColumnType columnType)
	{
		this(columnHeaderResourceKey, conversionModel, new Object[]{}, visible, columnType);
	}
	
	public ReportColumn(String columnHeaderResourceKey, Class<? extends IModel> conversionModel, boolean visible, boolean allowDuplicates, ColumnType columnType)
	{
		this(columnHeaderResourceKey, conversionModel, new Object[]{}, null, visible, allowDuplicates, columnType);
	}	
	
	public ReportColumn(String columnHeaderResourceKey, Class<? extends IModel> conversionModel, Object[] conversionModelArgs, boolean visible)
	{
		this(columnHeaderResourceKey, conversionModel, conversionModelArgs, visible, ColumnType.OTHER);
	}
	
	public ReportColumn(String columnHeaderResourceKey, Class<? extends IModel> conversionModel, Object[] conversionModelArgs, boolean visible, ColumnType columnType)
	{
		this(columnHeaderResourceKey, conversionModel, conversionModelArgs, null, visible, columnType);
	}
	
	/**
	 * 
	 * @param columnHeaderResourceKey
	 * @param conversionModel
	 * @param conversionModelArgs
	 * @param conversionModelArgsTypes
	 * @param visible
	 * @param columnType
	 */
	@SuppressWarnings("unchecked")
	public ReportColumn(String columnHeaderResourceKey, Class<? extends IModel> conversionModel, 
							Object[] conversionModelArgs, 
							Class[] conversionModelArgsTypes,
							boolean visible, ColumnType columnType)
	{
		this(columnHeaderResourceKey, conversionModel, conversionModelArgs, conversionModelArgsTypes, visible, false, columnType);
	}
	
	/**
	 * 
	 * @param columnHeaderResourceKey
	 * @param conversionModel
	 * @param conversionModelArgs
	 * @param conversionModelArgsTypes
	 * @param visible
	 * @param allowDuplicates
	 * @param columnType
	 */
	@SuppressWarnings("unchecked")
	public ReportColumn(String columnHeaderResourceKey, Class<? extends IModel> conversionModel, 
							Object[] conversionModelArgs, 
							Class[] conversionModelArgsTypes,
							boolean visible, boolean allowDuplicates, ColumnType columnType)
	{
		this.columnHeaderResourceKey = columnHeaderResourceKey;
		this.conversionModel = conversionModel;
		this.visible = visible;
		this.allowDuplicates = allowDuplicates;
		this.columnType = columnType;
		this.conversionModelConstructorParams = conversionModelArgs;
		this.conversionModelConstructorParamTypes = conversionModelArgsTypes;
		
	}
	
	/**
	 * @return the visible
	 */
	public boolean isVisible()
	{
		return visible;
	}
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	/**
	 * @return the columnHeaderResourceKey
	 */
	public String getColumnHeaderResourceKey()
	{
		return columnHeaderResourceKey;
	}
	/**
	 * @param columnHeaderResourceKey the columnHeaderResourceKey to set
	 */
	public void setColumnHeaderResourceKey(String columnHeaderResourceKey)
	{
		this.columnHeaderResourceKey = columnHeaderResourceKey;
	}
	/**
	 * @return the conversionModel
	 */
	public Class<? extends IModel> getConversionModel()
	{
		return conversionModel;
	}
	/**
	 * @param conversionModel the conversionModel to set
	 */
	public void setConversionModel(Class<? extends IModel> conversionModel)
	{
		this.conversionModel = conversionModel;
	}

	/**
	 * @return the columnType
	 */
	public ColumnType getColumnType()
	{
		return columnType;
	}

	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(ColumnType columnType)
	{
		this.columnType = columnType;
	}

	/**
	 * @return the conversionModelConstructorParams
	 */
	public Object[] getConversionModelConstructorParams()
	{
		return conversionModelConstructorParams;
	}

	/**
	 * @param conversionModelConstructorParams the conversionModelConstructorParams to set
	 */
	public void setConversionModelConstructorParams(Object[] conversionModelConstructorParams)
	{
		this.conversionModelConstructorParams = conversionModelConstructorParams;
	}

	/**
	 * @return the conversionModelConstructorParamTypes
	 */
	public Class<?>[] getConversionModelConstructorParamTypes()
	{
		return conversionModelConstructorParamTypes;
	}

	/**
	 * @param conversionModelConstructorParamTypes the conversionModelConstructorParamTypes to set
	 */
	public void setConversionModelConstructorParamTypes(Class<?>[] conversionModelConstructorParamTypes)
	{
		this.conversionModelConstructorParamTypes = conversionModelConstructorParamTypes;
	}

	/**
	 * @return the allowDuplicates
	 */
	public boolean isAllowDuplicates()
	{
		return allowDuplicates;
	}

	/**
	 * @param allowDuplicates the allowDuplicates to set
	 */
	public void setAllowDuplicates(boolean allowDuplicates)
	{
		this.allowDuplicates = allowDuplicates;
	}

	/**
	 * @return the chartSeriesColumn
	 */
	public boolean isChartSeriesColumn()
	{
		return chartSeriesColumn;
	}

	/**
	 * @param chartSeriesColumn the chartSeriesColumn to set
	 */
	public void setChartSeriesColumn(boolean chartSeriesColumn)
	{
		this.chartSeriesColumn = chartSeriesColumn;
	}
}
