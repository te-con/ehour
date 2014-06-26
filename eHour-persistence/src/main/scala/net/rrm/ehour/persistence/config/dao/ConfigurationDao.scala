package net.rrm.ehour.persistence.config.dao

import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.dao.GenericDao

trait ConfigurationDao extends GenericDao[String, Configuration]