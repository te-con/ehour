package net.rrm.ehour.persistence.backup.dao;

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 16, 2010 - 4:47:00 PM
 */
public class BackupDaoJdbcImplTest extends AbstractAnnotationDaoTest {
    public BackupDaoJdbcImplTest() {
        super("dataset-timesheet.xml");
    }

    @Test
    public void shouldFindAllTimesheetEntries() {
        List<Map<String, Object>> list = exportDao.findAll("TIMESHEET_ENTRY");

        assertEquals(12, list.size());
    }

    @Autowired
    private BackupDao exportDao;
}
