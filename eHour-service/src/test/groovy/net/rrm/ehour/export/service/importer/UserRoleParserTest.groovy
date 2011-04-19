package net.rrm.ehour.export.service.importer

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import net.rrm.ehour.domain.User
import net.rrm.ehour.export.service.ParseSession
import net.rrm.ehour.persistence.export.dao.ExportType
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * @author  thies (thies@te-con.nl)
 * Date: 11/30/10 12:31 AM
 */
class UserRoleParserTest {

  UserRoleParserDaoValidatorImpl daoValidator
  XMLEventReader eventReader
  UserRoleParser parser
  ParseSession status

  @Before
  void setUp()
  {
    String xmlData = """<USER_TO_USERROLES>
  <USER_TO_USERROLE>
   <ROLE>ROLE_ADMIN</ROLE>
   <USER_ID>1</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_REPORT</ROLE>
   <USER_ID>1</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_ADMIN</ROLE>
   <USER_ID>2</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_CONSULTANT</ROLE>
   <USER_ID>2</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_PROJECTMANAGER</ROLE>
   <USER_ID>2</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_REPORT</ROLE>
   <USER_ID>2</USER_ID>
  </USER_TO_USERROLE>
 </USER_TO_USERROLES>"""

    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData))
    eventReader.nextTag()

    daoValidator = new UserRoleParserDaoValidatorImpl()
    status = new ParseSession()

    def cache = new PrimaryKeyCache()
    cache.putKey(User.class, 1, 1)
    cache.putKey(User.class, 2, 2)

    parser = new UserRoleParser(daoValidator, cache)
  }

  @Test
  void shouldParseUserRoles()
  {
    parser.parseUserRoles eventReader, status
    assertEquals 6, daoValidator.persistCount
    assertEquals 6, status.getInsertions()[ExportType.USER_ROLE]
  }
}
