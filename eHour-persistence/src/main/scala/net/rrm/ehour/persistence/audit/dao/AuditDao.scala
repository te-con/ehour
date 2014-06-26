package net.rrm.ehour.persistence.audit.dao

import java.util

import net.rrm.ehour.data.AuditReportRequest
import net.rrm.ehour.domain.Audit
import net.rrm.ehour.persistence.dao.GenericDao

trait AuditDao extends GenericDao[Number, Audit] {
  /**
   * Find audit for request
   */
  def findAudits(request: AuditReportRequest): util.List[Audit]

  /**
   * Find audits for a request
   */
  def findAudits(request: AuditReportRequest, offset: Int, max: Int): util.List[Audit]

  /**
   * Count audits for request
   */
  def count(request: AuditReportRequest): Number
}
