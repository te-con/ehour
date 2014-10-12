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

package net.rrm.ehour.ui.manage.customer;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;

/**
 * Backing bean for customer administration
 */

public class CustomerAdminBackingBean extends AdminBackingBeanImpl<Customer> {
    private static final long serialVersionUID = 343538274642620123L;
    private Customer customer;

    private String originalCustomerName;
    private String originalCustomerCode;

    public CustomerAdminBackingBean(Customer customer) {
        this.customer = customer;

        this.originalCustomerCode = customer.getCode();
        this.originalCustomerName = customer.getName();
    }

    /**
     * Create fresh new customer admin backing bean
     *
     * @return
     */
    public static CustomerAdminBackingBean createCustomerAdminBackingBean() {
        final Customer customer = new Customer();
        customer.setActive(true);

        return new CustomerAdminBackingBean(customer);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the originalCustomerName
     */
    public String getOriginalCustomerName() {
        return originalCustomerName;
    }

    /**
     * @param originalCustomerName the originalCustomerName to set
     */
    public void setOriginalCustomerName(String originalCustomerName) {
        this.originalCustomerName = originalCustomerName;
    }

    /**
     * @return the originalCustomerCode
     */
    public String getOriginalCustomerCode() {
        return originalCustomerCode;
    }

    /**
     * @param originalCustomerCode the originalCustomerCode to set
     */
    public void setOriginalCustomerCode(String originalCustomerCode) {
        this.originalCustomerCode = originalCustomerCode;
    }

    @Override
    public Customer getDomainObject() {
        return getCustomer();
    }

    @Override
    public boolean isDeletable() {
        return customer != null && customer.isDeletable();
    }
}
