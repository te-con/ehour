package net.rrm.ehour.persistence.timesheet.dao

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ProjectAssignment
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 7:36:19 PM
 */
class TimesheetDaoHibernateImplTest extends AbstractAnnotationDaoTest {
    @Autowired
    private TimesheetDao timesheetDAO;

    TimesheetDaoHibernateImplTest() {
        super("dataset-timesheet.xml");
    }

    @Test
    void shouldGetTimesheetEntriesInRangeForUser() {
        Calendar dateStart = new GregorianCalendar(2006, 10 - 1, 1);
        Calendar dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
        DateRange dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

        def results = timesheetDAO.getTimesheetEntriesInRange(1, dateRange);

        assertEquals(9, results.size());
    }

    @Test
    void shouldGetTimesheetEntriesInRangeForAssignment() {
        Calendar dateStart = new GregorianCalendar(2006, 10 - 1, 1);
        Calendar dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
        DateRange dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

        def results = timesheetDAO.getTimesheetEntriesInRange(new ProjectAssignment(2), dateRange);

        assertEquals(2, results.size());
    }

<<<<<<< HEAD
    @Test
    void shouldGetTimesheetEntriesInRange() {
        Calendar dateStart = new GregorianCalendar(2006, 10 - 1, 1);
        Calendar dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
        DateRange dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

        def results = timesheetDAO.getTimesheetEntriesInRange(dateRange);
=======
	@Test
	void shouldGetTimesheetEntriesInRangeForActivity()
	{
		Calendar 	dateStart = new GregorianCalendar(2006, 10 - 1, 1);
		Calendar 	dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
		DateRange	dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

		Activity activity = new Activity();
		activity.setId(2);
		
		def results = timesheetDAO.getTimesheetEntriesInRange(activity, dateRange);
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure

        assertEquals(9, results.size());
    }

    @Test
    void shouldGetBookedHoursperDayInRange() {
        Calendar dateStart = new GregorianCalendar(2006, 10 - 1, 1);
        Calendar dateEnd = new GregorianCalendar(2006, 11 - 1, 1);
        DateRange dateRange = new DateRange(dateStart.getTime(), dateEnd.getTime());

        def results = timesheetDAO.getBookedHoursperDayInRange(new Integer(1), dateRange);

        assertEquals(6, results.size());

        assertEquals(6.5, results[3].hours.floatValue(), 0.01);

        assertEquals(-1, results[2].hours.floatValue(), 0.01);
    }

<<<<<<< HEAD
    @Test
    void shouldGetTimesheetEntriesBefore() {
        def res = timesheetDAO.getTimesheetEntriesBefore(new ProjectAssignment(1), new Date(2006 - 1900, 10 - 1, 3));
        assertEquals(1, res.size());
    }

    @Test
    void shouldGetTimesheetEntriesAfter() {
        def res = timesheetDAO.getTimesheetEntriesAfter(new ProjectAssignment(1), new Date(2006 - 1900, 10 - 1, 4));
=======
	@Test
	void shouldGetTimesheetEntriesBefore()
	{
		Activity activity = new Activity();
		activity.setId(1);
		
		def res = timesheetDAO.getTimesheetEntriesBefore(activity, new Date(2006 - 1900, 10 - 1, 3));
		assertEquals(1, res.size());
	}

	@Test
	void shouldGetTimesheetEntriesAfter()
	{
		Activity activity = new Activity();
		activity.setId(1);
		
		def res = timesheetDAO.getTimesheetEntriesAfter(activity, new Date(2006 - 1900, 10 - 1, 4));
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure

        assertEquals(3, res.size());
    }


<<<<<<< HEAD
    @Test
    void shouldGetLatestTimesheetEntryForAssignment() {
        def entry = timesheetDAO.getLatestTimesheetEntryForAssignment(1);
        assertEquals(9.2f, entry.hours, 0.01f);
    }

    @Test
    void shouldDeleteTimesheetEntries() {
=======
	@Test
	void shouldGetLatestTimesheetEntryForAssignment()
	{
		def entry = timesheetDAO.getLatestTimesheetEntryForActivity(1);
		assertEquals(9.2f, entry.hours, 0.01f);
	}


	@Test
	void shouldDeleteTimesheetEntries()
	{
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
        def ids = [5]

        int deleted = timesheetDAO.deleteTimesheetEntries(ids);

<<<<<<< HEAD
        assertEquals(2, deleted);
    }
=======
		assertEquals(2, deleted);
	}
	
	@Test
	void shouldFindAll()
	{
		def results = timesheetDAO.findAll();
		assertEquals(12, results.size());
	}
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
}



