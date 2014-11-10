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

import net.rrm.ehour.domain.User;
import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.Comparator;

public class UserComparator implements Comparator<User> {
    private boolean firstNameFirst;

    public UserComparator(boolean firstNameFirst) {
        this.firstNameFirst = firstNameFirst;
    }

    public int compare(User o1, User o2) {
        if (firstNameFirst) {
            return new CompareToBuilder().append(o1.getFirstName(), o2.getFirstName())
                    .append(o1.getLastName(), o2.getLastName())
                    .toComparison();
        } else {
            return new CompareToBuilder().append(o1.getLastName(), o2.getLastName())
                    .append(o1.getFirstName(), o2.getFirstName())
                    .toComparison();
        }
    }
}
