package net.rrm.ehour.util

import org.junit.Test

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 1/23/11 - 12:57 AM
 */
class EnvFileSystemResourceTest
{

  @Test
  void shouldParseEnv()
  {
    System.properties.put "EHOUR_HOME", "ok";

    def resource = new EnvFileSystemResource("\${EHOUR_HOME}/test")

    assert resource.file.path == "ok/test"
  }

  @Test
  void shouldParseWithoutEnv()
  {
    def resource = new EnvFileSystemResource("ok/test")

    assert resource.file.path == "ok/test"
  }

}
