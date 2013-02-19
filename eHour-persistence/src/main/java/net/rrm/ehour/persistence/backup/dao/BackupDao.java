package net.rrm.ehour.persistence.backup.dao;

import java.util.List;
import java.util.Map;

public interface BackupDao {
    List<Map<String, Object>> findForType(BackupEntityType type);
}
