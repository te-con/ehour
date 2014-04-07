package net.rrm.ehour.ui.report.cache

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.report.reports.ReportData
import org.springframework.cache.ehcache.EhCacheCacheManager
import java.util.UUID

@Service
class ReportCacheService @Autowired()(cacheManager: EhCacheCacheManager) {
  val cache = cacheManager.getCache("reportCache")

  def storeReportData(reportData: ReportData): String = {
    val key = UUID.randomUUID().toString

    cache.put(key, reportData)

    key
  }

  def retrieveReportData(key: String): Option[ReportData] = {
    val value = cache.get(key)
    if (value == null) None else Some(value.get().asInstanceOf[ReportData])
  }
}
