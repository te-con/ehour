package net.rrm.ehour.backup.config;

import net.rrm.ehour.backup.common.BackupEntityType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class EhourBackupConfigTest {
    @Test
    public void should_reverse_order() {
        EhourBackupConfig backupConfig = new EhourBackupConfig();
        List<BackupEntityType> types = backupConfig.reverseOrderedValues();

        assertTrue(types.get(0).getOrder() > types.get(1).getOrder());
    }

}