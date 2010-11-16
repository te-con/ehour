package net.rrm.ehour.export.service

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
    def resolver = new DomainObjectResolver();

    def type = ExportType.TIMESHEET_ENTRY;

    resolver.parse(type, type.getDomainObjectClass());
  }
}
