package net.rrm.ehour.export.service

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import net.rrm.ehour.domain.ProjectAssignment
import net.rrm.ehour.domain.ProjectAssignmentMother
import net.rrm.ehour.domain.TimesheetEntry
import net.rrm.ehour.persistence.export.dao.ExportType
import net.rrm.ehour.persistence.export.dao.ImportDao
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 11:54:54 PM
 */
class DomainObjectResolverTest {

  @Mock
  private ImportDao importDao;

  private PrimaryKeyCache keyCache

  @Before
  void setUp() {
    MockitoAnnotations.initMocks this

    keyCache = new PrimaryKeyCache()
  }


  @Test
  void displayAnnotations() {
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader("""<TIMESHEET_ENTRIES>
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
"""));

    def resolver = new DomainObjectResolver(eventReader, importDao);

    def type = ExportType.TIMESHEET_ENTRY;

    // skip the startdoc
    eventReader.nextEvent();

    when(importDao.find(Mockito.any(Integer.class), any(ProjectAssignment.class))).thenReturn(ProjectAssignmentMother.createProjectAssignment(1));

    List<TimesheetEntry> result = resolver.parse(type, type.getDomainObjectClass());

    assertEquals 2, result.size()

    assertNotNull result[0].entryId.entryDate
    assertNotNull result[0].entryId.projectAssignment
    assertEquals 8.0, result[0].hours, 0
    assertEquals "jaja", result[0].comment
  }
}
