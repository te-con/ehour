package net.rrm.ehour.export.service

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import net.rrm.ehour.domain.TimesheetEntry
import net.rrm.ehour.persistence.export.dao.ExportType
import org.junit.Test

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 11:54:54 PM
 */
class DomainObjectResolverTest {

  @Test
  void displayAnnotations()
  {
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader("""<TIMESHEET_ENTRY>
   <ASSIGNMENT_ID>5</ASSIGNMENT_ID>
   <ENTRY_DATE>2008-10-29</ENTRY_DATE>
   <HOURS>2.0</HOURS>
   <UPDATE_DATE>2008-11-03 23:20:42.0</UPDATE_DATE>
  </TIMESHEET_ENTRY>"""));

    def resolver = new DomainObjectResolver();

    def type = ExportType.TIMESHEET_ENTRY;

    // skip the startdoc
    eventReader.nextEvent();

    def event = eventReader.nextEvent();

    def result = (TimesheetEntry) resolver.parse(type, type.getDomainObjectClass(), event, eventReader);

//    println result.
  }
}
