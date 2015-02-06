package net.rrm.ehour.persistence.timesheet.dao

import net.rrm.ehour.domain.{TimesheetEntrySegment, TimesheetEntryId}
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl
import org.springframework.stereotype.Repository

@Repository("timesheetEntrySegmentDao")
class TimesheetEntrySegmentDaoHibernateImpl
  extends AbstractGenericDaoHibernateImpl[TimesheetEntryId, TimesheetEntrySegment](classOf[TimesheetEntrySegment])
  with TimesheetEntrySegmentDao {

}
