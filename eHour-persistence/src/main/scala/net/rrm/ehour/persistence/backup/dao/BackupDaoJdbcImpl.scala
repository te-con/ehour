package net.rrm.ehour.persistence.backup.dao

import java.util

import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoHibernate4Impl
import org.hibernate.transform.Transformers
import org.springframework.stereotype.Repository

@Repository("exportDao")
class BackupDaoJdbcImpl extends AbstractAnnotationDaoHibernate4Impl with BackupDao {
  override def findAll(table: String): util.List[util.Map[String, Object]] = {
    getSession
        .createSQLQuery("SELECT * FROM " + table)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .list()
        .asInstanceOf[util.List[util.Map[String, Object]]]
  }

}
