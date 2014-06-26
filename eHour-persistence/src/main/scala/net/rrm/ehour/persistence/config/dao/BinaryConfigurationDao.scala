package net.rrm.ehour.persistence.config.dao

import net.rrm.ehour.domain.BinaryConfiguration
import net.rrm.ehour.persistence.dao.GenericDao

/**
 * Created on Apr 2, 2009, 4:28:03 PM
 * @author Thies Edeling (thies@te-con.nl)
 *
 */
trait BinaryConfigurationDao extends GenericDao[String, BinaryConfiguration]

