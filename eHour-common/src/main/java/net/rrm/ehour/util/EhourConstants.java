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

package net.rrm.ehour.util;

import net.rrm.ehour.domain.ProjectAssignmentType;

public class EhourConstants
{
	// update ProjectAssignmentType as well!
	public static final int ASSIGNMENT_DATE = 0;
	public static final int ASSIGNMENT_TIME_ALLOTTED_FIXED = 2;
	public static final int ASSIGNMENT_TIME_ALLOTTED_FLEX = 3;

	public static final ProjectAssignmentType ASSIGNMENT_TYPE_DATE = new ProjectAssignmentType(ASSIGNMENT_DATE);
	public static final ProjectAssignmentType ASSIGNMENT_TYPE_TIME_ALLOTTED_FIXED = new ProjectAssignmentType(ASSIGNMENT_TIME_ALLOTTED_FIXED);
	public static final ProjectAssignmentType ASSIGNMENT_TYPE_TIME_ALLOTTED_FLEX = new ProjectAssignmentType(ASSIGNMENT_TIME_ALLOTTED_FLEX);
}

