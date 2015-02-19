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

package net.rrm.ehour.domain;

/**
 * Project Assignment type
 **/
public enum ProjectAssignmentType {
    ASSIGNMENT_DATE(0),
    ASSIGNMENT_TIME_ALLOTTED_FIXED(2),
    ASSIGNMENT_TIME_ALLOTTED_FLEX(3);

    private final int id;

    private ProjectAssignmentType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isDateType() {
        return this == ASSIGNMENT_DATE;
    }

    public boolean isFixedAllottedType() {
        return this == ASSIGNMENT_TIME_ALLOTTED_FIXED;
    }

    public boolean isFlexAllottedType() {
        return this == ASSIGNMENT_TIME_ALLOTTED_FLEX;
    }

    public boolean isAllottedType() {
        return isFixedAllottedType() || isFlexAllottedType();
    }

    public static ProjectAssignmentType findById(final int id) {
        for (ProjectAssignmentType pat : values()) {
            if (pat.id == id) {
                return pat;
            }
        }
        throw new IllegalArgumentException("No Project assignment type with id = " + id);
    }
}