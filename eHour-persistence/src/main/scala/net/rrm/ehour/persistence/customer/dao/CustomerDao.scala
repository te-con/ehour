package net.rrm.ehour.persistence.customer.dao

import java.util

import net.rrm.ehour.domain.Customer
import net.rrm.ehour.persistence.dao.GenericDao

trait CustomerDao extends GenericDao[Integer, Customer] {
  /**
   * Find all Customers, active and inactive
   **/
  def findAll(): util.List[Customer]

  /**
   * Find all active customers
   */
  def findAllActive(): util.List[Customer]

  /**
   * Find customer on name and code
   */
  def findOnNameAndCode(name: String, code: String): Customer
}