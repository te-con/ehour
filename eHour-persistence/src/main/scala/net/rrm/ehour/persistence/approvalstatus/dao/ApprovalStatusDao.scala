package net.rrm.ehour.persistence.approvalstatus.dao

import java.util

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain.{ApprovalStatus, Customer, User}
import net.rrm.ehour.persistence.dao.GenericDao

trait ApprovalStatusDao extends GenericDao[Integer, ApprovalStatus] {
  /**
   * Returns all {@link ApprovalStatus}s corresponding to {@link User} and {@link Customer} within passed {@link DateRange}.
   */
  def findApprovalStatusesForUserWorkingForCustomer(user: User, customer: Customer, dateRange: DateRange): util.List[ApprovalStatus]

  /**
   * Returns all {@link ApprovalStatus}s corresponding to a {@link Customer}s within passed {@link DateRange}.
   */
  def findApprovalStatusesForCustomers(customers: util.List[Customer], dateRange: DateRange): util.List[ApprovalStatus]
}
