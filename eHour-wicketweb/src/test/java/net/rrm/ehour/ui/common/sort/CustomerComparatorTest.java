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

package net.rrm.ehour.ui.common.sort;

import static org.junit.Assert.assertEquals;
import net.rrm.ehour.domain.Customer;

import org.junit.Test;

/**
 * TODO 
 **/

public class CustomerComparatorTest
{
	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.ui.common.sort.CustomerComparator#compare(net.rrm.ehour.persistence.persistence.domain.Customer, net.rrm.ehour.persistence.persistence.domain.Customer)}.
	 */
	@Test
	public void testCompare()
	{
		Customer c1 = new Customer();
		c1.setName("aa");
		Customer c2 = new Customer();
		c2.setName("bb");
		
		assertEquals(-1, new CustomerComparator().compare(c1, c2));
		
	}

}
