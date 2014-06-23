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

import com.google.common.base.Optional;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("projectDao")
public class ProjectDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<Project, Integer> implements ProjectDao {
    protected static final Optional<String> CACHEREGION = Optional.of("query.Project");

    public ProjectDaoHibernateImpl() {
        super(Project.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> findAllActive() {
        return findByNamedQuery("Project.findAllActive", CACHEREGION);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> findDefaultProjects() {
        return findByNamedQuery("Project.findAllActiveDefault", CACHEREGION);
    }

    @Override
    public List<Project> findProjectForCustomers(List<Customer> customers, boolean onlyActive) {
        String hqlName = onlyActive ? "Project.findActiveProjectsForCustomers" : "Project.findAllProjectsForCustomers";

        return findByNamedQueryAndNamedParam(hqlName, "customers", customers.toArray(), CACHEREGION);
    }

    @Override
    public List<Project> findActiveProjectsWhereUserIsPM(User user) {
        return findByNamedQueryAndNamedParam("Project.findActiveProjectsWhereUserIsPM", "user", user, CACHEREGION);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Project> findAllProjectsWithPmSet() {
        Criteria criteria = getSession().createCriteria(Project.class);
        criteria.add(Restrictions.isNotNull("projectManager"));

        return (List<Project>) criteria.list();
    }
}
