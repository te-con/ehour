package net.rrm.ehour.export.service.element

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import net.rrm.ehour.domain.Audit
import net.rrm.ehour.domain.AuditActionType
import net.rrm.ehour.domain.ProjectAssignmentMother
import net.rrm.ehour.domain.TimesheetEntry
import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserDepartmentMother
import net.rrm.ehour.domain.UserMother
import net.rrm.ehour.export.service.ParseStatus
import net.rrm.ehour.export.service.PrimaryKeyCache
import net.rrm.ehour.persistence.export.dao.ExportType
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 11:54:54 PM
 */
class DomainObjectParserTest
{
  private DomainObjectParserDaoTestValidator daoValidator;
  private PrimaryKeyCache keyCache
  ParseStatus status

  @Before
  void setUp()
  {
    keyCache = new PrimaryKeyCache()
    status = new ParseStatus()
  }


  private DomainObjectParser createResolver(String xmlData)
  {
    return createResolver(xmlData, null, null)
  }

  private DomainObjectParser createResolver(String xmlData, def returnOnFind, def onFind)
  {
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

    // skip the startdoc
    eventReader.nextTag()

    daoValidator = (returnOnFind == null) ? new DomainObjectParserDaoValidatorImpl() : new DomainObjectParserDaoTestValidator(returnOnFind, onFind)

    return new DomainObjectParser(eventReader, daoValidator, status);
  }

  @Test
  void shouldParseTwoTimesheetEntries()
  {
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
""", ProjectAssignmentMother.createProjectAssignment(1), 1)

    def type = ExportType.TIMESHEET_ENTRY;

    List<TimesheetEntry> result = resolver.parse(type.getDomainObjectClass());

    assertEquals 2, result.size()

    assertNotNull result[0].entryId.entryDate
    assertNotNull result[0].entryId.projectAssignment
    assertEquals 8.0, result[0].hours, 0
    assertEquals "jaja", result[0].comment
    assertTrue resolver.keyCache.isEmpty()
    assertEquals 2, daoValidator.totalPersistCount
  }

  @Test
  void shouldParseUserAndStoreNewKeyInCacheMap()
  {
    def department = UserDepartmentMother.createUserDepartment()

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

    def type = ExportType.USERS

    List<User> result = resolver.parse(type.getDomainObjectClass());

    assertEquals 2, result.size()

    assertNotNull result[0].userId
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
  void shouldParseEnum()
  {
    def resolver = createResolver(""" <AUDITS CLASS="net.rrm.ehour.domain.Audit"><AUDIT>
   <AUDIT_ID>173</AUDIT_ID>
   <USER_ID>2</USER_ID>
   <USER_FULLNAME>Edeling, Thies</USER_FULLNAME>
   <AUDIT_DATE>2010-01-12 16:20:51.0</AUDIT_DATE>
   <SUCCESS>Y</SUCCESS>
   <AUDIT_ACTION_TYPE>LOGIN</AUDIT_ACTION_TYPE>
  </AUDIT></AUDITS>
""", UserMother.createUser(), 2)

    def type = ExportType.AUDIT

    def department = UserDepartmentMother.createUserDepartment()

    List<Audit> result = resolver.parse(type.getDomainObjectClass());

    assertEquals 1, result.size()

    assertEquals AuditActionType.LOGIN, result[0].auditActionType
  }

  @Test
  void shouldRetrieveManyToOne()
  {
    def user = UserMother.createUser()

    def resolver = createResolver(""" <AUDITS CLASS="net.rrm.ehour.domain.Audit"><AUDIT>
   <AUDIT_ID>173</AUDIT_ID>
   <USER_ID>2</USER_ID>
   <USER_FULLNAME>Edeling, Thies</USER_FULLNAME>
   <AUDIT_DATE>2010-01-12 16:20:51.0</AUDIT_DATE>
   <SUCCESS>Y</SUCCESS>
   <AUDIT_ACTION_TYPE>LOGIN</AUDIT_ACTION_TYPE>
  </AUDIT></AUDITS>
""", user, 2)

    def type = ExportType.AUDIT

    List<Audit> result = resolver.parse(type.getDomainObjectClass());

    assertEquals 1, result.size()

    assertEquals AuditActionType.LOGIN, result[0].auditActionType
    assertEquals 1, daoValidator.totalPersistCount
    assertEquals user, result[0].user
  }

  private class DomainObjectParserDaoTestValidator<T> extends DomainObjectParserDaoValidatorImpl
  {
    private T returnObject;
    private Serializable primaryKey;

    DomainObjectParserDaoTestValidator(T returnObject, Serializable primaryKey)
    {
      this.primaryKey = primaryKey;
      this.returnObject = returnObject;
    }

    public <T> T find(Serializable pk, Class<T> type)
    {
      return pk.equals(this.primaryKey) ? returnObject : null
    }
  };
}
