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

import net.rrm.ehour.domain.Customer;

public class CustomerRowKey extends ChartRowKey
{
	private Customer	customer;
	
	/**
	 * 
	 * @param customer
	 */
	public CustomerRowKey(Customer customer)
	{
		this.customer = customer;
	}
	

	/* (non-Javadoc)
	 * @see net.rrm.ehour.web.report.charts.rowkey.ChartRowKey#getName()
	 */
	public String getName()
	{
		return customer.getCode();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey#getId()
	 */
	public Integer getId()
	{
		return customer.getCustomerId();
	}
}
