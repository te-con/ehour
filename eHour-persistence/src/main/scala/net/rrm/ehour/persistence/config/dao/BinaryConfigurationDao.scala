package net.rrm.ehour.persistence.config.dao

import net.rrm.ehour.domain.BinaryConfiguration
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository

/**
 * Created on Apr 2, 2009, 4:28:03 PM
 * @author Thies Edeling (thies@te-con.nl)
 *
 */
trait BinaryConfigurationDao extends GenericDao[String, BinaryConfiguration]

@Repository("binaryConfigurationDao")
class BinaryConfigurationDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[String, BinaryConfiguration](classOf[BinaryConfiguration]) with BinaryConfigurationDao
