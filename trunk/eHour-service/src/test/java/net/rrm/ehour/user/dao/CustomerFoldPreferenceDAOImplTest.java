/**
 * Created on Jun 30, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.user.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO 
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings("unchecked")
public class CustomerFoldPreferenceDAOImplTest extends BaseDAOTest 
{
	@Autowired
	private CustomerFoldPreferenceDAO	customerFoldPreferenceDAO;

	@Test
	public void testGetPreferenceForUser()
	{
		List<Customer> custs = new ArrayList<Customer>();
		custs.add(new Customer(1));
		custs.add(new Customer(2));
		custs.add(new Customer(3));
		
		List res = customerFoldPreferenceDAO.getPreferenceForUser(new User(1), custs);
		
		assertEquals(3, res.size());
	}
}
