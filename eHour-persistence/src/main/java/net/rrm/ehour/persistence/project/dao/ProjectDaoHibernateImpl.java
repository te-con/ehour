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

package net.rrm.ehour.persistence.project.dao;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("projectDao")
public class ProjectDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<Project, Integer> implements ProjectDao {
    protected static final String CACHEREGION = "query.Project";

    public ProjectDaoHibernateImpl() {
        super(Project.class);
    }

    @SuppressWarnings("unchecked")
    public List<Project> findAllActive() {
        return getHibernateTemplate().findByNamedQuery("Project.findAllActive");
    }

    @SuppressWarnings("unchecked")
    public List<Project> findDefaultProjects() {
        return getHibernateTemplate().findByNamedQuery("Project.findAllActiveDefault");
    }

    /**
     * Get projects for customer respecting the active flag
     */
    public List<Project> findProjectForCustomers(List<Customer> customers, boolean onlyActive) {
        String hqlName = onlyActive ? "Project.findActiveProjectsForCustomers" : "Project.findAllProjectsForCustomers";

        List<Project> results = findByNamedQueryAndNamedParam(hqlName,
                "customers",
                customers.toArray(),
                true,
                CACHEREGION);

        return results;
    }

    @Override
    public List<Project> findActiveProjectsWhereUserIsPM(User user) {
        return findByNamedQueryAndNamedParam("Project.findActiveProjectsWhereUserIsPM",
                "user", user,
                true, CACHEREGION);
    }
}
