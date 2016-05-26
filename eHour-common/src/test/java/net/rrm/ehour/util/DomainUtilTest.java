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

import net.rrm.ehour.domain.Project;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DomainUtilTest {
    @Test
    public void testGetPKsFromDomainObjects() {
        List<Project> projectIds = new ArrayList<>();

        projectIds.add(new Project(1));
        projectIds.add(new Project(2));
        projectIds.add(new Project(3));
        projectIds.add(new Project(4));

        List<Integer> ints = DomainUtil.getIdsFromDomainObjects(projectIds);

        assertEquals(4, ints.size());
    }
}
