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

package net.rrm.ehour.sort;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.report.criteria.Sort;

import java.util.Comparator;

/**
 * Customer name comparator
 */

public class CustomerComparator implements Comparator<Customer> {
    private Sort sort;

    public CustomerComparator() {
        this(Sort.NAME);
    }

    public CustomerComparator(Sort sort) {
        this.sort = sort;
    }

    public int compare(Customer o1, Customer o2) {
        if (sort == Sort.NAME) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        } else {
            return o1.getCode().compareToIgnoreCase(o2.getCode());
        }
    }
}
