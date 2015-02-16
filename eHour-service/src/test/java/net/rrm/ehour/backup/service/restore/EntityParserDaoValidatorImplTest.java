package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class EntityParserDaoValidatorImplTest {
    private EntityParserDaoValidatorImpl subject;

    @Before
    public void set_up() {
        subject = new EntityParserDaoValidatorImpl();
    }

    @Test
    public void when_persisting_should_return_proper_id_for_generated_int() {
        assertReturnedIdClassEquals(Integer.class, UserObjectMother.createUser());
    }

    @Test
    public void when_persisting_should_return_proper_id_for_string() {
        assertReturnedIdClassEquals(String.class, UserRole.ADMIN);
    }

    @Test
    public void when_persisting_should_return_integer_type_for_embeddable_id() {
        assertReturnedIdClassEquals(TimesheetEntryId.class, TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5f));
    }

    @Test
    public void when_finding_should_set_proper_id_for_generated_int() {
        User user = subject.find(1, User.class);
        assertEquals(1, user.getUserId().intValue());
    }

    @Test
    public void when_finding_should_set_proper_id_for_string() {
        UserRole userRole = subject.find("ADMIN", UserRole.class);
        assertEquals("ADMIN", userRole.getRole());
    }

    @Test
    public void when_finding_should_set_proper_id_for_embeddable_id() {
        TimesheetEntryId timesheetEntryId = new TimesheetEntryId(new Date(), ProjectAssignmentObjectMother.createProjectAssignment(1));

        TimesheetEntry entry = subject.find(timesheetEntryId, TimesheetEntry.class);
        assertEquals(timesheetEntryId, entry.getEntryId());
    }

    private <T extends DomainObject<?, ?>> void assertReturnedIdClassEquals(Class<?> expectedClass, T entity) {
        Serializable id = subject.persist(entity);
        assertEquals(expectedClass, id.getClass());
    }

}