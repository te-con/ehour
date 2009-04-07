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
