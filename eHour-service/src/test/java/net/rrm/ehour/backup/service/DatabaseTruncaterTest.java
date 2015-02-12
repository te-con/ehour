package net.rrm.ehour.backup.service;

import com.google.common.collect.Lists;
import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.backup.common.BackupEntityType;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.persistence.backup.dao.RestoreDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/19/11 - 8:54 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class DatabaseTruncaterTest {
    @Mock
    private RestoreDao importDao;

    @Mock
    private BackupConfig backupConfig;

    private DatabaseTruncater truncater;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        truncater = new DatabaseTruncater(importDao, backupConfig);
        truncater.setRestoreDao(importDao);
    }

    @Test
    public void shouldTruncate() {
        BackupEntityType entity = new BackupEntityType(TimesheetEntry.class, "timesheet_entry", 0);
        when(backupConfig.reverseOrderedValues()).thenReturn(Lists.newArrayList(entity));

        truncater.truncateDatabase();

        int wantedNumberOfInvocations = 1 + 3;
        verify(importDao, times(wantedNumberOfInvocations)).delete(Matchers.<Class<Object>>anyObject());
    }
}
