package net.rrm.ehour.persistence.backup.dao

import org.junit.Test

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 1/18/11 - 12:28 AM
 */
class TimesheetEntryRowProcessorTest
{
  @Test
  void processRow() {
    def rows = [["ENTRY_DATE":"12"], ["ENTRY_DATE":"13","UPDATE_DATE":"14"]]

    BackupEntityType.TIMESHEET_ENTRY.processor.processRows rows

    assert rows[0].UPDATE_DATE == "12"
    assert rows[1].UPDATE_DATE == "14"
  }
}
