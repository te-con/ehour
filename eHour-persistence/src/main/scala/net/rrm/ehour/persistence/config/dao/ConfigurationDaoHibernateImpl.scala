package net.rrm.ehour.persistence.config.dao

import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateScalaImpl
import org.springframework.stereotype.Repository

@Repository("configurationDao")
class ConfigurationDaoHibernateImpl extends AbstractGenericDaoHibernateScalaImpl[String, Configuration](classOf[Configuration]) with ConfigurationDao


