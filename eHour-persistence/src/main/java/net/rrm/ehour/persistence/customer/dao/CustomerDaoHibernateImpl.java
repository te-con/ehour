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

package net.rrm.ehour.persistence.customer.dao;

import com.google.common.base.Optional;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Customer DAO
 */

@Repository("customerDao")
public class CustomerDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<Customer, Integer> implements CustomerDao {
    private static final Optional<String> CACHEREGION = Optional.of("query.Customer");

    public CustomerDaoHibernateImpl() {
        super(Customer.class);
    }

    @Override
    public Customer findOnNameAndCode(String name, String code) {
        String[] keys = new String[]{"name", "code"};
        String[] params = new String[]{name.toLowerCase(), code.toLowerCase()};

        List<Customer> results = findByNamedQueryAndNamedParam("Customer.findByNameAndCode", keys, params, CACHEREGION);

        return results != null && results.size() > 0 ? results.get(0) : null;
    }

    @Override
    public List<Customer> findAllActive() {
        return findByNamedQueryAndNamedParam("Customer.findAllWithActive", "active", true, CACHEREGION);
    }

}
