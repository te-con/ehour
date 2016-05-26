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

package net.rrm.ehour.persistence.report.dao;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings({"deprecation"})
public class DetailedReportDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private DetailedReportDao detailedReportDao;

    public DetailedReportDaoHibernateImplTest() {
        super("dataset-detailedreport.xml");
    }

    @Test
    public void shouldGetHoursPerDayForUsers() {
        DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;)
                new Date(2008 - 1900, 1, 3));
        List<Integer> userIds = new ArrayList<>();
        userIds.add(1);
        List<FlatReportElement> results = detailedReportDao.getHoursPerDayForUsers(userIds, dateRange);

        assertEquals(10, results.size());

        assertNotNull(results.get(0).getProjectId());
    }

    @Test
    public void shouldGetHoursPerDayForProjects() {
        DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;)
                new Date(2008 - 1900, 1, 3));
        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(2);
        List<FlatReportElement> results = detailedReportDao.getHoursPerDayForProjects(projectIds, dateRange);

        assertEquals(4, results.size());

        assertEquals(2, results.get(0).getProjectId().intValue());
    }


    @Test
    public void shouldGetHoursPerDayForProjectsAndUsers() {
        DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;)
                new Date(2008 - 1900, 1, 3));
        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(2);
        List<Integer> userIds = new ArrayList<>();
        userIds.add(1);

        List<FlatReportElement> results = detailedReportDao.getHoursPerDayForProjectsAndUsers(projectIds, userIds, dateRange);

        assertEquals(2, results.size());

        assertEquals(2, results.get(0).getProjectId().intValue());
    }

    @Test
    public void shouldGetHoursPerDay() {
        DateRange dateRange = new DateRange(new Date(2006 - 1900, 5 - 1, 1), // deprecated? hmm ;)
                new Date(2008 - 1900, 1, 3));

        List<FlatReportElement> results = detailedReportDao.getHoursPerDay(dateRange);

        assertEquals(12, results.size());
    }
}
