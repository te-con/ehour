package net.rrm.ehour.export.service

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import net.rrm.ehour.persistence.export.dao.ExportType
import net.rrm.ehour.persistence.export.dao.ImportDao
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

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

    def result = resolver.parse(type, type.getDomainObjectClass());

    Assert.assertEquals (2, result.size())

    println result[0];
  }
}
