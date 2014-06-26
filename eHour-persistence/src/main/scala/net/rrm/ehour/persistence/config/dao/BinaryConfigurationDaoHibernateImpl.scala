package net.rrm.ehour.persistence.config.dao

import net.rrm.ehour.domain.BinaryConfiguration
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateScalaImpl
import org.springframework.stereotype.Repository

/**
 * Created on Apr 2, 2009, 4:32:29 PM
 * Converted on Jun 27, 2014, 01:16 AM
 * @author Thies Edeling (thies@te-con.nl)
 *
 */
@Repository("binaryConfigurationDao")
class BinaryConfigurationDaoHibernateImpl extends AbstractGenericDaoHibernateScalaImpl[String, BinaryConfiguration](classOf[BinaryConfiguration]) with BinaryConfigurationDao
