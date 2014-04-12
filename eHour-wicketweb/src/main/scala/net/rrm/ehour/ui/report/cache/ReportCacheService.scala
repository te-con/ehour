package net.rrm.ehour.ui.report.cache

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.report.reports.ReportData
import org.springframework.cache.ehcache.EhCacheCacheManager
import java.util.UUID

trait ReportCacheService {
  def storeReportData(reportData: ReportData): String

  def retrieveReportData(key: String): Option[ReportData]
}

@Service
class ReportCacheServiceEhCacheImpl @Autowired() (cacheManager: EhCacheCacheManager) extends ReportCacheService {

  val cache = cacheManager.getCache("reportCache")

  override def storeReportData(reportData: ReportData): String = {
    val key = UUID.randomUUID().toString

    cache.put(key, reportData)

    key
  }

  override def retrieveReportData(key: String): Option[ReportData] = {
    val value = cache.get(key)
    if (value == null) None else Some(value.get().asInstanceOf[ReportData])
  }
}
