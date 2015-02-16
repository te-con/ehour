package net.rrm.ehour.persistence.backup.dao

import java.util

trait BackupDao {
  def findAll(table: String): util.List[util.Map[String, Object]]
}


