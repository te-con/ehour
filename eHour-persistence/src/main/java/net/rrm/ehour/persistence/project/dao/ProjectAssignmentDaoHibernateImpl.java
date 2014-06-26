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
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateScalaImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CRUD stuff on PA do
 */
@Repository("projectAssignmentDao")
public class ProjectAssignmentDaoHibernateImpl
        extends AbstractGenericDaoHibernateScalaImpl<ProjectAssignment, Integer>
        implements ProjectAssignmentDao {
    private static final Optional<String> CACHEREGION = Optional.of("query.ProjectAssignment");

    public ProjectAssignmentDaoHibernateImpl() {
        super(ProjectAssignment.class);
    }

    @Override
    public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId, DateRange range) {
        List<ProjectAssignment> results;

        String[] keys = new String[]{"dateStart", "dateEnd", "userId"};
        Object[] params = new Object[]{range.getDateStart(), range.getDateEnd(), userId};

        results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUserInRange"
                , keys
                , params
                , CACHEREGION);


        return results;
    }

    @Override
    public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId) {
        String[] keys = new String[]{"projectId", "userId"};
        Integer[] params = new Integer[]{projectId, userId};
        List<ProjectAssignment> results;

        results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUserForProject"
                , keys
                , params
                , CACHEREGION);

        return results;
    }

    @Override
    public List<ProjectAssignment> findProjectAssignmentsForUser(User user) {
        List<ProjectAssignment> results;

        results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUser"
                , "user"
                , user
                , CACHEREGION);

        return results;
    }

    @Override
    public List<ProjectAssignment> findProjectAssignmentsForProject(Project project, DateRange range) {
        List<ProjectAssignment> results;
        String[] keys = new String[]{"dateStart", "dateEnd", "project"};
        Object[] params = new Object[]{range.getDateStart(), range.getDateEnd(), project};

        results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForProjectInRange"
                , keys
                , params
                , CACHEREGION);

        return results;
    }

    @Override
    public List<ProjectAssignment> findAllProjectAssignmentsForProject(Project project) {
        return findProjectAssignmentsForProject(project, false);
    }

    @Override
    public List<ProjectAssignment> findAllActiveProjectAssignmentsForProject(Project project) {
        return findProjectAssignmentsForProject(project, true);
    }

    @SuppressWarnings("unchecked")
    private List<ProjectAssignment> findProjectAssignmentsForProject(Project project, Boolean onlyActive) {
        Criteria crit = getSession().createCriteria(ProjectAssignment.class);

        if (onlyActive) {
            crit.add(Restrictions.eq("active", true));
        }

        crit.add(Restrictions.eq("project", project));

        return crit.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProjectAssignmentType> findProjectAssignmentTypes() {
        return (List<ProjectAssignmentType>)getSession().createCriteria(ProjectAssignmentType.class).list();
    }

    @Override
    public List<ProjectAssignment> findProjectAssignmentsForCustomer(Customer customer, DateRange range) {
        List<ProjectAssignment> results;
        String[] keys = new String[]{"dateStart", "dateEnd", "customer"};
        Object[] params = new Object[]{range.getDateStart(), range.getDateEnd(), customer};

        results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForCustomerInRange"
                , keys
                , params
                , CACHEREGION);

        return results;
    }
}
