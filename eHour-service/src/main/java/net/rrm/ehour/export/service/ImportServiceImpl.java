package net.rrm.ehour.export.service;

import net.rrm.ehour.export.service.importer.*;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 5:34:24 PM
 */
@Service("importService")
public class ImportServiceImpl implements ImportService
{
    private static final Logger LOG = Logger.getLogger(ImportServiceImpl.class);

    @Autowired
    private ConfigurationDao configurationDao;

    @Autowired
    private ConfigurationParserDao configurationParserDao;

    @Autowired
    private DomainObjectParserDao domainObjectParserDao;

    @Override
    public void importDatabase(ParseSession session) throws ImportException
    {

    }

    @Override
    public ParseSession prepareImportDatabase(String xmlData) throws ImportException
    {
        try
        {
            String tempFilename = writeToTempFile(xmlData);
            ParseSession status = validateXml(xmlData);
            status.setFilename(tempFilename);
            return status;
        } catch (Exception e)
        {
            LOG.error(e);
            throw new ImportException("import.error.failedToParse", e);
        }
    }

    private String writeToTempFile(String xmlData) throws IOException
    {
        FileWriter writer = null;
        File file;

        try
        {
            file = File.createTempFile("import", "xml");
            file.deleteOnExit();

            writer = new FileWriter(file);
            writer.write(xmlData);
        } finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }

        return file.getAbsolutePath();
    }

    private ParseSession validateXml(String xmlData) throws XMLStreamException, ImportException, IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        ParseSession status = new ParseSession();

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));

        DomainObjectParserDaoValidatorImpl daoValidator = new DomainObjectParserDaoValidatorImpl();
        DomainObjectParser parser = new DomainObjectParser(eventReader, daoValidator, status);

        ConfigurationParserDaoValidatorImpl configValidator = new ConfigurationParserDaoValidatorImpl();
        ConfigurationParser configurationParser = new ConfigurationParser(configValidator);

        UserRoleParserDaoValidatorImpl userRoleValidator = new UserRoleParserDaoValidatorImpl();
        UserRoleParser userRoleParser = new UserRoleParser(userRoleValidator);

        XmlImporter importer = new XmlImporter(configurationDao, parser, configurationParser, userRoleParser);

        importer.importXml(status, eventReader);

        return status;
    }

    public void setConfigurationDao(ConfigurationDao configurationDao)
    {
        this.configurationDao = configurationDao;
    }
}
