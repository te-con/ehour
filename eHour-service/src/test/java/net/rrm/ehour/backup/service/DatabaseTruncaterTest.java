package net.rrm.ehour.backup.service;

import net.rrm.ehour.persistence.backup.dao.BackupEntityType;
import net.rrm.ehour.persistence.backup.dao.RestoreDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/19/11 - 8:54 PM
 */
public class DatabaseTruncaterTest {
    @Mock
    private RestoreDao importDao;
    private DatabaseTruncater truncater;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        truncater = new DatabaseTruncater();
        truncater.setRestoreDao(importDao);
    }

    @Test
    public void shouldTruncate() {
        truncater.truncateDatabase();

        int wantedNumberOfInvocations = BackupEntityType.reverseOrderedValues().get(0).getOrder() + 3;
        verify(importDao, times(wantedNumberOfInvocations)).delete(Matchers.<Class<Object>>anyObject());
    }
}
