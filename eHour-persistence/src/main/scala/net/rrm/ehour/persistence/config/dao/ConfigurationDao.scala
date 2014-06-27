package net.rrm.ehour.persistence.config.dao

import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository

trait ConfigurationDao extends GenericDao[String, Configuration]

@Repository("configurationDao")
class ConfigurationDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[String, Configuration](classOf[Configuration]) with ConfigurationDao


