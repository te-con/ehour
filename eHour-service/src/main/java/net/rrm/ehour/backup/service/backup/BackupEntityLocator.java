package net.rrm.ehour.backup.service.backup;

import java.util.List;

public interface BackupEntityLocator {
    List<BackupEntity> backupEntities();

    List<BackupJoinTable> joinTables();

    BackupEntity forClass(Class clazz);

    BackupEntity forName(String name);


    List<BackupEntity> reverseOrderedValues();

    BackupEntity userRoleBackupEntity();
}
