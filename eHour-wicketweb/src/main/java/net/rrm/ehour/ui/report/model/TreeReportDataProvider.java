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

package net.rrm.ehour.ui.report.model;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Iterator;
import java.util.List;

/**
 * DataProvider for tree report nodes, not the most efficient memory-wise
 */

public class TreeReportDataProvider implements IDataProvider<TreeReportElement> {
    private static final long serialVersionUID = 4346207207281976523L;

    private List<TreeReportElement> nodes;

    public TreeReportDataProvider(List<TreeReportElement> nodes) {
        this.nodes = nodes;
    }

    @Override
    public IModel<TreeReportElement> model(TreeReportElement object) {
        return new Model<>(object);
    }

    @Override
    public Iterator<? extends TreeReportElement> iterator(long first, long count) {
        return nodes.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return nodes.size();
    }

    @Override
    public void detach() {
    }
}
