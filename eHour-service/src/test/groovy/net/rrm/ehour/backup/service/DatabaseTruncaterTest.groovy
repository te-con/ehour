package net.rrm.ehour.backup.service

import net.rrm.ehour.persistence.backup.dao.BackupEntityType
import net.rrm.ehour.persistence.backup.dao.RestoreDao
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Matchers.anyObject
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 1/19/11 - 8:54 PM
 */
class DatabaseTruncaterTest {
    @Mock
    RestoreDao importDao

    DatabaseTruncater truncater

    @Before
    void setUp() {
        MockitoAnnotations.initMocks this
        truncater = new DatabaseTruncater()
        truncater.restoreDao = importDao
    }

    @Test
    void shouldTruncate() {
        truncater.truncateDatabase()

        verify(importDao, times(BackupEntityType.reverseOrderedValues()[0].order + 3)).delete anyObject()
    }
}
