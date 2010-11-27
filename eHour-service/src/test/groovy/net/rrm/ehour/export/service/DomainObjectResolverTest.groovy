package net.rrm.ehour.export.service

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import net.rrm.ehour.persistence.export.dao.ExportType
import net.rrm.ehour.persistence.export.dao.ImportDao
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import net.rrm.ehour.domain.*
import static org.junit.Assert.*
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 11:54:54 PM
 */
class DomainObjectResolverTest
{

  @Mock
  private ImportDao importDao;

  private PrimaryKeyCache keyCache

  @Before
  void setUp()
  {
    MockitoAnnotations.initMocks this

    keyCache = new PrimaryKeyCache()
  }


  private DomainObjectResolver createResolver(String xmlData)
  {
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

    // skip the startdoc
    def event = eventReader.nextTag()

    return new DomainObjectResolver(eventReader, importDao);
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
""")

    def type = ExportType.TIMESHEET_ENTRY;

    when(importDao.find(Mockito.any(Integer.class), Mockito.any(ProjectAssignment.class))).thenReturn(ProjectAssignmentMother.createProjectAssignment(1));

    List<TimesheetEntry> result = resolver.parse(type.getDomainObjectClass());

    assertEquals 2, result.size()

    assertNotNull result[0].entryId.entryDate
    assertNotNull result[0].entryId.projectAssignment
    assertEquals 8.0, result[0].hours, 0
    assertEquals "jaja", result[0].comment
    assertTrue resolver.keyCache.isEmpty()
  }

  @Test
  void shouldParseUserAndStoreNewKeyInCacheMap()
  {
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
   <USER_ID>2</USER_ID>
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
""")

    def type = ExportType.USERS

    def department = UserDepartmentMother.createUserDepartment()

    when(importDao.find(Mockito.any(Integer.class), Mockito.any(UserDepartment.class))).thenReturn(department)
    when(importDao.persist(Mockito.any(User.class))).thenReturn(5);

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

    def userPk = resolver.keyCache.getKey(User.class, 2)
    assertNotNull userPk
    assertEquals 5, userPk
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
""")

    def type = ExportType.AUDIT

    def department = UserDepartmentMother.createUserDepartment()

    when(importDao.find(Mockito.any(Integer.class), Mockito.any(User.class))).thenReturn(UserMother.createUser())
    when(importDao.persist(Mockito.any(User.class))).thenReturn(5);

    List<Audit> result = resolver.parse(type.getDomainObjectClass());

    assertEquals 1, result.size()

    assertEquals AuditActionType.LOGIN, result[0].auditActionType

  }

}
