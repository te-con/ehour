/**
 * Created on Jan 20, 2008
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

package net.rrm.ehour.ui.common.sort;

import static org.junit.Assert.assertEquals;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.ui.common.sort.CustomerComparator;

import org.junit.Test;

/**
 * TODO 
 **/

public class CustomerComparatorTest
{
	/**
	 * Test method for {@link net.rrm.ehour.ui.common.sort.CustomerComparator#compare(net.rrm.ehour.domain.Customer, net.rrm.ehour.domain.Customer)}.
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
