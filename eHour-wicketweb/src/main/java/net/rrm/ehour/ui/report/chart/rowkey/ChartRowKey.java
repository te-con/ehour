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

package net.rrm.ehour.ui.report.chart.rowkey;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * RowKey wrapper 
 **/

public abstract class ChartRowKey implements Comparable<ChartRowKey>
{
	/**
	 * Get identifier
	 * @return
	 */
	public abstract Integer getId();
	
	/**
	 * Get row name
	 * @return
	 */
	public abstract Serializable getName();
	

	@Override
	public String toString()
	{
		return (String)getName();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof ChartRowKey))
		{
			return false;
		}
		ChartRowKey rhs = (ChartRowKey) object;
		return new EqualsBuilder()
					.append(getId(), rhs.getId())
					.isEquals();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(2072873729, -1118826425)
						.append(getId())
						.toHashCode();
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(ChartRowKey object)
	{
		return new CompareToBuilder()
					.append(this.getName().toString().toLowerCase(), object.getName().toString().toLowerCase())
					.toComparison();
	}
}
