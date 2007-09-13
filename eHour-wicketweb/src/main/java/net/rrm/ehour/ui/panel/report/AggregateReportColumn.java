/**
 * Created on Sep 12, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.report;

import java.io.Serializable;

import org.apache.wicket.model.IModel;

/**
 * Report column
 **/

public class AggregateReportColumn implements Serializable
{
	private static final long serialVersionUID = -6736366461333244457L;
	private boolean	visible = true;
	private	String	columnHeaderResourceKey;
	private IModel	conversionModel;
	
	public AggregateReportColumn()
	{
	}

	public AggregateReportColumn(String columnHeaderResourceKey)
	{
		this(columnHeaderResourceKey, true);
	}

	public AggregateReportColumn(String columnHeaderResourceKey, boolean visible)
	{
		this(columnHeaderResourceKey, null, visible);
	}

	public AggregateReportColumn(String columnHeaderResourceKey, IModel conversionModel)
	{
		this(columnHeaderResourceKey, conversionModel, true);
	}

	public AggregateReportColumn(String columnHeaderResourceKey, IModel conversionModel, boolean visible)
	{
		this.columnHeaderResourceKey = columnHeaderResourceKey;
		this.conversionModel = conversionModel;
		this.visible = visible;
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
	public IModel getConversionModel()
	{
		return conversionModel;
	}
	/**
	 * @param conversionModel the conversionModel to set
	 */
	public void setConversionModel(IModel conversionModel)
	{
		this.conversionModel = conversionModel;
	}
}
