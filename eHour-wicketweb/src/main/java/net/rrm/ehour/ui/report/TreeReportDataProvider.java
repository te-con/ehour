/**
 * Created on Jan 5, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.ui.report;

import java.util.Iterator;
import java.util.List;

import net.rrm.ehour.ui.report.node.ReportNode;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * DataProvider for tree report nodes, not the most efficient memory-wise 
 **/

public class TreeReportDataProvider implements IDataProvider
{
	private static final long serialVersionUID = 4346207207281976523L;

	private List<ReportNode> nodes;
	
	/**
	 * 
	 * @param nodes
	 */
	public TreeReportDataProvider(List<ReportNode> nodes)
	{
		this.nodes = nodes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	public Iterator<ReportNode> iterator(int first, int count)
	{
		return nodes.subList(first, first + count).iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel model(Object object)
	{
		return new Model((ReportNode)object);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
	 */
	public int size()
	{
		return nodes.size();
	}

	public void detach()
	{
	}

}
