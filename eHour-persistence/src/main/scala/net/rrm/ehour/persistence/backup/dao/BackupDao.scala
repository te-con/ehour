package net.rrm.ehour.persistence.backup.dao

import java.util

trait BackupDao {
  def findForType(entityType: BackupEntityType): util.List[util.Map[String, Object]]
}


