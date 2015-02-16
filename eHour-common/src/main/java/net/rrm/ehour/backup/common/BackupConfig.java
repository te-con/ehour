package net.rrm.ehour.backup.common;

import java.util.List;

public interface BackupConfig {
    List<BackupEntityType> backupEntities();

    BackupEntityType entityForClass(Class clazz);

    List<BackupJoinTable> joinTables();

    BackupJoinTable joinTableForName(String name);

    List<BackupEntityType> reverseOrderedValues();
}
