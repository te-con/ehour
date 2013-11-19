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

package net.rrm.ehour.project.util;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.*;

/**
 * @author Thies on Aug 24, 2009 10:40:38 PM
 */
public class ProjectUtil {
    private ProjectUtil() {
    }

    public static List<Project> filterBillable(Collection<Project> projects) {
        return filterBillability(projects, true);
    }

    public static List<Project> filterUnbillable(Collection<Project> projects) {
        return filterBillability(projects, false);
    }

    private static List<Project> filterBillability(Collection<Project> projects, boolean billable) {
        List<Project> sortedProjects = new ArrayList<Project>();

        for (Project project : projects) {
            if (project.isBillable() == billable) {
                sortedProjects.add(project);
            }
        }

        Collections.sort(sortedProjects, new Comparator<Project>() {
            @Override
            public int compare(Project o1, Project o2) {
                return new CompareToBuilder()
                        .append(o1.getCustomer(), o2.getCustomer())
                        .append(o1.getName(), o2.getName())
                        .append(o1.getProjectCode(), o2.getProjectCode())
                        .append(o1.getProjectId(), o2.getProjectId()).toComparison();
            }
        });
        return sortedProjects;
    }

    public static List<Project> filterProjectsOnPm(User requiredPm, Collection<Project> projects) {
        List<Project> pmProjects = Lists.newArrayList();

        for (Project project : projects) {
            if (requiredPm.equals(project.getProjectManager())) {
                pmProjects.add(project);
            }
        }

        return pmProjects;
    }
}
