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

package net.rrm.ehour.ui.report;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * DataProvider for tree report nodes, not the most efficient memory-wise 
 **/

public class TreeReportDataProvider implements IDataProvider
{
	private static final long serialVersionUID = 4346207207281976523L;

	private List<TreeReportElement> nodes;
	
	/**
	 * 
	 * @param nodes
	 */
	public TreeReportDataProvider(List<TreeReportElement> nodes)
	{
		this.nodes = nodes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	public Iterator<TreeReportElement> iterator(int first, int count)
	{
		return nodes.subList(first, first + count).iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel model(Object object)
	{
		return new Model((TreeReportElement)object);
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
