package net.rrm.ehour.persistence.backup.dao

import java.util

import net.rrm.ehour.persistence.retry.ExponentialBackoffRetryPolicy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository("exportDao")
class BackupDaoJdbcImpl extends BackupDao {
  @Autowired
  var jdbcTemplate: JdbcTemplate = _

  override def findForType(entityType: BackupEntityType): util.List[util.Map[String, Object]] =
    ExponentialBackoffRetryPolicy retry jdbcTemplate.queryForList("SELECT * FROM " + entityType.name)
}



