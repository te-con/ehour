package net.rrm.ehour.backup.service.backup;

import java.util.List;

public interface BackupConfig {
    List<BackupEntityType> backupEntities();

    BackupEntityType entityForClass(Class clazz);

    BackupEntityType entityForName(String name);

    List<BackupJoinTable> joinTables();

    BackupJoinTable joinTableForName(String name);

    List<BackupEntityType> reverseOrderedValues();
}
