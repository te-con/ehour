package net.rrm.ehour.backup.service.restore

import net.rrm.ehour.backup.domain.ParseSession
import net.rrm.ehour.domain.*
import net.rrm.ehour.persistence.backup.dao.BackupEntityType
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory

import static org.junit.Assert.*

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 11:54:54 PM
 */
class DomainObjectParserTest {
    DomainObjectParserDaoTestValidator daoValidator;
    PrimaryKeyCache keyCache
    ParseSession status

    @Mock
    DomainObjectParserDao parserDao

    @Before
    void setUp() {
        keyCache = new PrimaryKeyCache()
        status = new ParseSession()

        MockitoAnnotations.initMocks this
    }

    private DomainObjectParser createResolver(String xmlData) {
        return createResolver(xmlData, null, null)
    }

    private DomainObjectParser createResolver(String xmlData, def returnOnFind, def onFind) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData))

        // skip the startdoc
        eventReader.nextTag()

        daoValidator = (returnOnFind == null) ? new DomainObjectParserDaoValidatorImpl() : new DomainObjectParserDaoTestValidator(returnOnFind, onFind)

        return new DomainObjectParser(eventReader, daoValidator, keyCache);
    }

    @Test
    void shouldParseTwoTimesheetEntries() {
        def resolver = createResolver("""<TIMESHEET_ENTRIES CLASS="net.rrm.ehour.domain.TimesheetEntry">
  <TIMESHEET_ENTRY>
   <ASSIGNMENT_ID>1</ASSIGNMENT_ID>
   <ENTRY_DATE>2007-03-26</ENTRY_DATE>
   <HOURS>8.0</HOURS>
   <COMMENT>jaja</COMMENT>
  </TIMESHEET_ENTRY>
  <TIMESHEET_ENTRY>
   <ASSIGNMENT_ID>2</ASSIGNMENT_ID>
   <ENTRY_DATE>2007-03-26</ENTRY_DATE>
   <HOURS>0.0</HOURS>
  </TIMESHEET_ENTRY>
  </TIMESHEET_ENTRIES>
""", ProjectAssignmentObjectMother.createProjectAssignment(1), 1)

        def type = BackupEntityType.TIMESHEET_ENTRY;

        keyCache.putKey(ProjectAssignment.class, 1, 1)
        keyCache.putKey(ProjectAssignment.class, 2, 2)

        List<TimesheetEntry> result = resolver.parse(type.getDomainObjectClass(), status);

        assertEquals 2, result.size()

        assertNotNull result[0].entryId.entryDate
        assertNotNull result[0].entryId.projectAssignment
        assertEquals 8.0, result[0].hours, 0
        assertEquals "jaja", result[0].comment
        assertEquals 2, daoValidator.totalPersistCount
    }

    @Test
    void shouldParseUserAndStoreNewKeyInCacheMap() {
        def department = UserDepartmentObjectMother.createUserDepartment()

        def resolver = createResolver("""<USERLIST>
  <USERS>
   <USER_ID>1</USER_ID>
   <USERNAME>admin</USERNAME>
   <PASSWORD>1d798ca9dba7df61bf399a02695f9f50034bad66</PASSWORD>
   <FIRST_NAME>eHour</FIRST_NAME>
   <LAST_NAME>Admin</LAST_NAME>
   <DEPARTMENT_ID>1</DEPARTMENT_ID>
   <EMAIL>t@t.net</EMAIL>
   <ACTIVE>Y</ACTIVE>
  </USERS>
  <USERS>
   <USER_ID>3</USER_ID>
   <USERNAME>thies</USERNAME>
   <PASSWORD>e2e90187007d55ae40678e11e0c9581cb7bb9928</PASSWORD>
   <FIRST_NAME>Thies</FIRST_NAME>
   <LAST_NAME>Edeling</LAST_NAME>
   <DEPARTMENT_ID>1</DEPARTMENT_ID>
   <EMAIL>thies@te-con.nl</EMAIL>
   <ACTIVE>Y</ACTIVE>
   <SALT>6367</SALT>
  </USERS>
  </USERLIST>
""", department, 1)

        def type = BackupEntityType.USERS

        keyCache.putKey(UserDepartment.class, 1, 1)

        List<User> result = resolver.parse(type.getDomainObjectClass(), status);

        assertEquals 2, result.size()

        assertEquals "admin", result[0].username
        assertEquals "1d798ca9dba7df61bf399a02695f9f50034bad66", result[0].password
        assertEquals "eHour", result[0].firstName
        assertEquals "Admin", result[0].lastName
        assertEquals department, result[0].userDepartment
        assertEquals "t@t.net", result[0].email
        assertTrue result[0].active
        assertFalse resolver.keyCache.isEmpty()

        def userPk = resolver.keyCache.getKey(User.class, 3)
        assertNotNull userPk
        assertEquals "2", userPk
    }

    @Test
    void shouldParseEnum() {
        def resolver = createResolver(""" <AUDITS CLASS="net.rrm.ehour.domain.Audit"><AUDIT>
   <AUDIT_ID>173</AUDIT_ID>
   <USER_ID>2</USER_ID>
   <USER_FULLNAME>Edeling, Thies</USER_FULLNAME>
   <AUDIT_DATE>2010-01-12 16:20:51.0</AUDIT_DATE>
   <SUCCESS>Y</SUCCESS>
   <AUDIT_ACTION_TYPE>LOGIN</AUDIT_ACTION_TYPE>
  </AUDIT></AUDITS>
""", UserObjectMother.createUser(), 2)

        def type = BackupEntityType.AUDIT

        List<Audit> result = resolver.parse(type.getDomainObjectClass(), status);

        assertEquals 1, result.size()

        assertEquals AuditActionType.LOGIN, result[0].auditActionType
    }

    @Test
    void shouldRetrieveManyToOne() {
        def user = UserObjectMother.createUser()

        def resolver = createResolver(""" <AUDITS CLASS="net.rrm.ehour.domain.Audit"><AUDIT>
   <AUDIT_ID>173</AUDIT_ID>
   <USER_ID>2</USER_ID>
   <USER_FULLNAME>Edeling, Thies</USER_FULLNAME>
   <AUDIT_DATE>2010-01-12 16:20:51.0</AUDIT_DATE>
   <SUCCESS>Y</SUCCESS>
   <AUDIT_ACTION_TYPE>LOGIN</AUDIT_ACTION_TYPE>
  </AUDIT></AUDITS>
""", user, 2)

        def type = BackupEntityType.AUDIT

        keyCache.putKey(User.class, 2, 2)

        List<Audit> result = resolver.parse(type.getDomainObjectClass(), status);

        assertEquals 1, result.size()

        assertEquals AuditActionType.LOGIN, result[0].auditActionType
        assertEquals 1, daoValidator.totalPersistCount
        assertEquals user, result[0].user
    }

    private class DomainObjectParserDaoTestValidator<T> extends DomainObjectParserDaoValidatorImpl {
        private T returnObject;
        private Serializable primaryKey;

        DomainObjectParserDaoTestValidator(T returnObject, Serializable primaryKey) {
            this.primaryKey = primaryKey;
            this.returnObject = returnObject;
        }

        @Override
        public <T extends Serializable> T find(Serializable pk, Class<T> type) {
            return pk.equals(this.primaryKey) ? returnObject : null as T
        }
    };
}
