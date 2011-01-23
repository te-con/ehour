package net.rrm.ehour.util

import org.junit.Test

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 1/23/11 - 12:57 AM
 */
class EnvFileSystemResourceTest
{
	private static final String SEPARATOR = System.getProperty("file.separator")


  @Test
  void shouldParseEnv()
  {
    System.properties.put "EHOUR_HOME", "ok";

    def resource = new EnvFileSystemResource("\${EHOUR_HOME}${SEPARATOR}test")

    assert resource.file.path == "ok${SEPARATOR}test"
  }

  @Test
  void shouldParseWithoutEnv()
  {
    def resource = new EnvFileSystemResource("ok${SEPARATOR}test")

    assert resource.file.path == "ok${SEPARATOR}test"
  }

}
