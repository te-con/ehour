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

package net.rrm.ehour.ui.common.component.datatable;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;

public class AjaxDataTable<T> extends DataTable<T>
{
	private static final long serialVersionUID = -667262986972743788L;

	@SuppressWarnings("unchecked")
	public AjaxDataTable(String id, final List<IColumn<T>> columns, ISortableDataProvider<T> dataProvider, int rowsPerPage)
	{
		this(id, (IColumn<T>[]) columns.toArray(), dataProvider, rowsPerPage);
	}


	public AjaxDataTable(String id, final IColumn<T>[] columns, ISortableDataProvider<T> dataProvider, int rowsPerPage)
	{
		super(id, columns, dataProvider, rowsPerPage);
		setOutputMarkupId(true);
		setVersioned(false);
		addBottomToolbar(new AjaxNavigationToolbar(this));
		addTopToolbar(new AjaxFallbackHeadersToolbar(this, dataProvider));
		addBottomToolbar(new NoRecordsToolbar(this));
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable#newRowItem(java.lang.String, int, org.apache.wicket.model.IModel)
	 */
	protected Item<T> newRowItem(String id, int index, IModel<T> model)
	{
		return new OddEvenItem<T>(id, index, model);
	}
}
