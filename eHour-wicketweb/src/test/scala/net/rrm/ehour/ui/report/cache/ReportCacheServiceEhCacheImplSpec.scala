package net.rrm.ehour.ui.report.cache

import net.rrm.ehour.AbstractSpec
import org.mockito.Mockito._
import org.springframework.cache.ehcache.{EhCacheCache, EhCacheCacheManager}
import net.rrm.ehour.report.reports.AggregateReportDataObjectMother
import net.sf.ehcache.CacheManager

class ReportCacheServiceEhCacheImplSpec extends AbstractSpec {
  val cacheManager = mock[EhCacheCacheManager]
  var cacheService:ReportCacheService = _

  override def beforeEach() {
    val instance = CacheManager.newInstance("src/main/resources/report-ehcache.xml")
    reset(cacheManager)

    when(cacheManager.getCache("reportCache")).thenReturn(new EhCacheCache(instance.getCache("reportCache")))

    cacheService = new ReportCacheServiceEhCacheImpl(cacheManager)
  }

  "Report Cache Service" should {
    "store report data" in {
      val reportData = AggregateReportDataObjectMother.generateReportData

      cacheService.storeReportData(reportData) should not be null
    }

    "retrieve previously stored data" in {
      val reportData = AggregateReportDataObjectMother.generateReportData

      val key = cacheService.storeReportData(reportData)

      val retrievedData = cacheService.retrieveReportData(key)

      reportData should equal(retrievedData.get)
    }
  }
}
