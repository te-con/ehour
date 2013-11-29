package net.rrm.ehour.sysinfo

import org.springframework.beans.factory.annotation.{Value, Autowired}
import org.springframework.stereotype.Service

trait SysInfoService {
  def info: SysInfo
}

@Service("sysInfoService")
class SysInfoServiceImpl @Autowired()(@Value("${ehour.database}") databaseName: String, @Value("${ehour.database.url:none}") databaseUrl: String, @Value("${ehour.database.driver:none}") jdbcDriver: String) extends SysInfoService {
  def info: SysInfo = SysInfo(databaseName, databaseUrl, jdbcDriver)
}

case class SysInfo(databaseName: String, databaseUrl: String, jdbcDriver: String)
