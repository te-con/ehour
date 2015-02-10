package net.rrm.ehour.backup.service.backup;

import java.util.List;

public interface BackupEntityLocator {
    List<BackupEntity> backupEntities();

    BackupEntity entityForClass(Class clazz);

    BackupEntity entityForName(String name);

    List<BackupJoinTable> joinTables();

    BackupJoinTable joinTableForName(String name);


    List<BackupEntity> reverseOrderedValues();

    BackupEntity userRoleBackupEntity();
}
