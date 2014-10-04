package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.backup.dao.BackupEntityType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 11:54:54 PM
 */
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class DomainObjectParserTest {
    private DomainObjectParserDaoTestValidator daoValidator;
    private PrimaryKeyCache keyCache;
    private ParseSession status;

    @Before
    public void setUp() {
        keyCache = new PrimaryKeyCache();
        status = new ParseSession();
    }

    private DomainObjectParser createResolver(String xmlData) throws XMLStreamException {
        return createResolver(xmlData, null, null);
    }

    private DomainObjectParser createResolver(String xmlData, DomainObject<Integer, ?> returnOnFind, Integer onFind) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

        // skip the startdoc
        eventReader.nextTag();

        daoValidator = (DomainObjectParserDaoTestValidator) (returnOnFind == null ? new DomainObjectParserDaoValidatorImpl() : new DomainObjectParserDaoTestValidator(returnOnFind, onFind));

        return new DomainObjectParser(eventReader, daoValidator, keyCache);
    }

    @Test
    public void shouldParseTwoTimesheetEntries() throws XMLStreamException, InstantiationException, IllegalAccessException {
        DomainObjectParser resolver = createResolver("<TIMESHEET_ENTRIES CLASS=\"net.rrm.ehour.domain.TimesheetEntry\">\n<TIMESHEET_ENTRY>\n<ASSIGNMENT_ID>1</ASSIGNMENT_ID>\n<ENTRY_DATE>2007-03-26</ENTRY_DATE>\n<HOURS>8.0</HOURS>\n   <COMMENT>jaja</COMMENT>\n  </TIMESHEET_ENTRY>\n  <TIMESHEET_ENTRY>\n   <ASSIGNMENT_ID>2</ASSIGNMENT_ID>\n   <ENTRY_DATE>2007-03-26</ENTRY_DATE>\n   <HOURS>0.0</HOURS>\n  </TIMESHEET_ENTRY>\n  </TIMESHEET_ENTRIES>\n", ProjectAssignmentObjectMother.createProjectAssignment(1), 1);

        BackupEntityType type = BackupEntityType.TIMESHEET_ENTRY;

        keyCache.putKey(ProjectAssignment.class, 1, 1);
        keyCache.putKey(ProjectAssignment.class, 2, 2);

        List<TimesheetEntry> result = resolver.parse((Class<TimesheetEntry>) type.getDomainObjectClass(), status);

        assertEquals(2, result.size());

        assertNotNull(result.get(0).getEntryId().getEntryDate());
        assertNotNull(result.get(0).getEntryId().getProjectAssignment());
        assertEquals(8.0, result.get(0).getHours(), 0);
        assertEquals("jaja", result.get(0).getComment());
        assertEquals(2, daoValidator.getTotalPersistCount());
    }

    @Test
    public void shouldParseUserAndStoreNewKeyInCacheMap() throws XMLStreamException, InstantiationException, IllegalAccessException {
        UserDepartment department = UserDepartmentObjectMother.createUserDepartment();

        DomainObjectParser resolver = createResolver("<USERLIST>\n  <USERS>\n   <USER_ID>1</USER_ID>\n   <USERNAME>admin</USERNAME>\n   <PASSWORD>1d798ca9dba7df61bf399a02695f9f50034bad66</PASSWORD>\n   <FIRST_NAME>eHour</FIRST_NAME>\n   <LAST_NAME>Admin</LAST_NAME>\n   <DEPARTMENT_ID>1</DEPARTMENT_ID>\n   <EMAIL>t@t.net</EMAIL>\n   <ACTIVE>Y</ACTIVE>\n  </USERS>\n  <USERS>\n   <USER_ID>3</USER_ID>\n   <USERNAME>thies</USERNAME>\n   <PASSWORD>e2e90187007d55ae40678e11e0c9581cb7bb9928</PASSWORD>\n   <FIRST_NAME>Thies</FIRST_NAME>\n   <LAST_NAME>Edeling</LAST_NAME>\n   <DEPARTMENT_ID>1</DEPARTMENT_ID>\n   <EMAIL>thies@te-con.nl</EMAIL>\n   <ACTIVE>Y</ACTIVE>\n   <SALT>6367</SALT>\n  </USERS>\n  </USERLIST>\n", department, 1);

        BackupEntityType type = BackupEntityType.USERS;

        keyCache.putKey(UserDepartment.class, 1, 1);

        List<User> result = resolver.parse((Class<User>) type.getDomainObjectClass(), status);

        assertEquals(2, result.size());

        User user = result.get(0);
        assertEquals("admin", user.getUsername());
        assertEquals("1d798ca9dba7df61bf399a02695f9f50034bad66", user.getPassword());
        assertEquals("eHour", user.getFirstName());
        assertEquals("Admin", user.getLastName());
        assertEquals(department, user.getUserDepartment());
        assertEquals("t@t.net", user.getEmail());
        assertTrue(user.isActive());

        assertFalse(resolver.getKeyCache().isEmpty());

        Serializable userPk = resolver.getKeyCache().getKey(User.class, 3);
        assertNotNull(userPk);
        assertEquals("2", userPk);
    }

    @Test
    public void shouldParseEnum() throws IllegalAccessException, XMLStreamException, InstantiationException {
        DomainObjectParser resolver = createResolver(" <AUDITS CLASS=\"net.rrm.ehour.domain.Audit\"><AUDIT>\n   <AUDIT_ID>173</AUDIT_ID>\n   <USER_ID>2</USER_ID>\n   <USER_FULLNAME>Edeling, Thies</USER_FULLNAME>\n   <AUDIT_DATE>2010-01-12 16:20:51.0</AUDIT_DATE>\n   <SUCCESS>Y</SUCCESS>\n   <AUDIT_ACTION_TYPE>LOGIN</AUDIT_ACTION_TYPE>\n  </AUDIT></AUDITS>\n", UserObjectMother.createUser(), 2);

        BackupEntityType type = BackupEntityType.AUDIT;

        List<Audit> result = resolver.parse((Class<Audit>) type.getDomainObjectClass(), status);

        assertEquals(1, result.size());

        assertEquals(AuditActionType.LOGIN, result.get(0).getAuditActionType());
    }

    @Test
    public void shouldRetrieveManyToOne() throws IllegalAccessException, XMLStreamException, InstantiationException {
        User user = UserObjectMother.createUser();

        DomainObjectParser resolver = createResolver(" <AUDITS CLASS=\"net.rrm.ehour.domain.Audit\"><AUDIT>\n   <AUDIT_ID>173</AUDIT_ID>\n   <USER_ID>2</USER_ID>\n   <USER_FULLNAME>Edeling, Thies</USER_FULLNAME>\n   <AUDIT_DATE>2010-01-12 16:20:51.0</AUDIT_DATE>\n   <SUCCESS>Y</SUCCESS>\n   <AUDIT_ACTION_TYPE>LOGIN</AUDIT_ACTION_TYPE>\n  </AUDIT></AUDITS>\n", user, 2);

        BackupEntityType type = BackupEntityType.AUDIT;

        keyCache.putKey(User.class, 2, 2);

        List<Audit> result = resolver.parse((Class<Audit>) type.getDomainObjectClass(), status);

        assertEquals(1, result.size());

        assertEquals(AuditActionType.LOGIN, result.get(0).getAuditActionType());
        assertEquals(1, daoValidator.getTotalPersistCount());
        assertEquals(user, result.get(0).getUser());
    }

    private class DomainObjectParserDaoTestValidator<T extends Serializable> extends DomainObjectParserDaoValidatorImpl {
        public DomainObjectParserDaoTestValidator(T returnObject, Serializable primaryKey) {
            this.primaryKey = primaryKey;
            this.returnObject = returnObject;
        }

        @Override
        public <W extends Serializable> W find(Serializable primaryKey, Class<W> type) {
            if (this.primaryKey == primaryKey && returnObject.getClass() == type) {
                return (W) returnObject;
            } else {
                return null;
            }
        }

        private T returnObject;
        private Serializable primaryKey;
    }
}
