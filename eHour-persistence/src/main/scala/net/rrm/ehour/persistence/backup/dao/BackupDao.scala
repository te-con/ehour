package net.rrm.ehour.persistence.backup.dao

import java.util

trait BackupDao {
  def findForType(table: String): util.List[util.Map[String, Object]]
}


