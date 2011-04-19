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

import java.util.Collections;

import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

/**
 * @author Thies
 * 
 */
public class UpdatingTimeSeries extends TimeSeries
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpdatingTimeSeries(Comparable<?> name)
	{
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jfree.data.time.TimeSeries#addOrUpdate(org.jfree.data.time.RegularTimePeriod, java.lang.Number)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TimeSeriesDataItem addOrUpdate(RegularTimePeriod period, Number value)
	{
		if (period == null)
		{
			throw new IllegalArgumentException("Null 'period' argument.");
		}
		TimeSeriesDataItem overwritten = null;

		TimeSeriesDataItem key = new TimeSeriesDataItem(period, value);
		int index = Collections.binarySearch(this.data, key);
		if (index >= 0)
		{
			TimeSeriesDataItem existing = (TimeSeriesDataItem) this.data.get(index);
			
			overwritten = (TimeSeriesDataItem) existing.clone();
			// update it rather than replace it
			existing.setValue(value.floatValue() + existing.getValue().floatValue());
			removeAgedItems(false); // remove old items if necessary, but
			// don't notify anyone, because that
			// happens next anyway...
			fireSeriesChanged();
		} else
		{
			this.data.add(-index - 1, new TimeSeriesDataItem(period, value));

			// check if this addition will exceed the maximum item count...
			if (getItemCount() > this.getMaximumItemCount())
			{
				this.data.remove(0);
			}

			removeAgedItems(false); // remove old items if necessary, but
			// don't notify anyone, because that
			// happens next anyway...
			fireSeriesChanged();
		}
		return overwritten;

	}

}
