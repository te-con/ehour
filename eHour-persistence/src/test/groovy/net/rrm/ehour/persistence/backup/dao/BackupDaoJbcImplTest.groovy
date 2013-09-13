package net.rrm.ehour.persistence.backup.dao

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 4:47:00 PM
 */
class BackupDaoJbcImplTest extends AbstractAnnotationDaoTest
{
  @Autowired
  private BackupDao exportDao;

    BackupDaoJbcImplTest()
  {
    super("dataset-timesheet.xml")
  }

  @Test
  void shouldFindAllTimesheetEntries()
  {
    def list = exportDao.findForType(BackupEntityType.TIMESHEET_ENTRY)

    assertEquals(12, list.size())
  }
}
