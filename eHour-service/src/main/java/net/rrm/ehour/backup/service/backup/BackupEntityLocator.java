package net.rrm.ehour.backup.service.backup;

import java.util.List;

public interface BackupEntityLocator {
    List<BackupEntity> findBackupEntities();

    BackupEntity forClass(Class clazz);

    List<BackupEntity> reverseOrderedValues();

    BackupEntity userRoleBackupEntity();
}
