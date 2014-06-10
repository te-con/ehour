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
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.report.criteria.Sort;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator on project's name
 */

public class ProjectComparator implements Comparator<Project>, Serializable {
    private static final long serialVersionUID = -2100973629921877419L;

    private Sort sort;
    private CustomerComparator customerComparator;

    public ProjectComparator() {
        this(Sort.NAME);
    }

    public ProjectComparator(Sort sort) {
        this.sort = sort;
    }

    public int compare(Project o1, Project o2) {
        switch (sort) {
            case CODE:
                return compareOnProjectCode(o1, o2);
            case PARENT_CODE_FIRST:
                return compareOnCustomerCodeAndThenProjectCode(o1, o2);
            case NAME:
            default:
                return compareOnName(o1, o2);
        }
    }

    private int compareOnProjectCode(Project o1, Project o2) {
        return o1.getProjectCode().compareToIgnoreCase(o2.getProjectCode());
    }

    private int compareOnCustomerCodeAndThenProjectCode(Project o1, Project o2) {
        Customer customer1 = o1.getCustomer();
        Customer customer2 = o2.getCustomer();

        if (customerComparator == null) {
            customerComparator = new CustomerComparator(Sort.CODE);
        }

        int customerCompare = customerComparator.compare(customer1, customer2);

        return customerCompare == 0 ? compareOnProjectCode(o1, o2) : customerCompare;
    }

    private int compareOnName(Project o1, Project o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }

}
