/**
 * 
 */
package net.rrm.ehour.ui.reportchart;

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
