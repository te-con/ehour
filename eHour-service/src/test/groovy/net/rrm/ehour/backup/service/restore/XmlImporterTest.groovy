package net.rrm.ehour.backup.service.restore

import net.rrm.ehour.backup.domain.ImportException
import net.rrm.ehour.backup.domain.ParseSession
import net.rrm.ehour.config.ConfigurationItem
import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.config.dao.ConfigurationDao
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory

import static org.mockito.Mockito.when

/**
 * Created by IntelliJ IDEA.
 * User: thies
 * Date: 1/16/11
 * Time: 2:14 AM
 * To change this template use File | Settings | File Templates.
 */
class XmlImporterTest {
  UserRoleParserDaoValidatorImpl daoValidator
  XMLEventReader eventReader
  UserRoleParser parser
  ParseSession status

  @Mock
  ConfigurationDao configurationDao

  @Test
  void shouldMatch084On083() {
    MockitoAnnotations.initMocks this

    def config = new Configuration("version", "0.8.4")

    when(configurationDao.findById(ConfigurationItem.VERSION.getDbField())).thenReturn(config)

    String xmlData = """<?xml version="1.0" ?>
<EHOUR DB_VERSION="0.8.3"></EHOUR>"""

    XMLInputFactory inputFactory = XMLInputFactory.newInstance()
    eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData))

    def importer = new XmlImporter(configurationDao, null, null, null, false)
    importer.importXml(null, eventReader)
  }

  @Test(expected = ImportException.class)
  void shouldNotMatch084On081() {
    MockitoAnnotations.initMocks this

    def config = new Configuration("version", "0.8.4")

    when(configurationDao.findById(ConfigurationItem.VERSION.getDbField())).thenReturn(config)

    String xmlData = """<?xml version="1.0" ?>
<EHOUR DB_VERSION="0.8.1"></EHOUR>"""

    XMLInputFactory inputFactory = XMLInputFactory.newInstance()
    eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData))

    def importer = new XmlImporter(configurationDao, null, null, null, false)
    importer.importXml(null, eventReader)
  }

}
