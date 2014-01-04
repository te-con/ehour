package net.rrm.ehour.sysinfo

import org.springframework.beans.factory.annotation.{Value, Autowired}
import org.springframework.stereotype.Service

trait SystemInfoService {
  def info: SystemInfo
}

@Service
class SystemInfoServiceImpl @Autowired()(@Value("${ehour.database}") databaseName: String, @Value("${ehour.database.url:none}") databaseUrl: String, @Value("${ehour.database.driver:none}") jdbcDriver: String) extends SystemInfoService {
  def info: SystemInfo = SystemInfo(databaseName, databaseUrl, jdbcDriver)
}

case class SystemInfo(databaseName: String, databaseUrl: String, jdbcDriver: String)
