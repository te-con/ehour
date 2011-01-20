package net.rrm.ehour.export.service.importer;

import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.springframework.util.Assert;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 12/6/10 11:59 PM
 */
public class XmlImporterBuilder
{
    private XMLEventReader xmlReader;
    private ConfigurationDao configurationDao;
    private ConfigurationParserDao configurationParserDao;
    private DomainObjectParserDao domainObjectParserDao;
    private UserRoleParserDao userRoleParserDao;
    private boolean skipValidation = false;

    public XmlImporter build() throws XMLStreamException
    {
        Assert.notNull(xmlReader);
        Assert.notNull(configurationDao);
        Assert.notNull(domainObjectParserDao);
        Assert.notNull(userRoleParserDao);

        PrimaryKeyCache keyCache = new PrimaryKeyCache();

        DomainObjectParser parser = new DomainObjectParser(xmlReader, domainObjectParserDao, keyCache);
        ConfigurationParser configurationParser = new ConfigurationParser(configurationParserDao);
        UserRoleParser userRoleParser = new UserRoleParser(userRoleParserDao, keyCache);

        return new XmlImporter(configurationDao, parser, configurationParser, userRoleParser, skipValidation);
    }

    public XmlImporterBuilder setSkipValidation(boolean skipValidation)
    {
        this.skipValidation = skipValidation;
        return this;
    }

    public XmlImporterBuilder setXmlReader(XMLEventReader xmlReader)
    {
        this.xmlReader = xmlReader;
        return this;
    }

    public XmlImporterBuilder setConfigurationDao(ConfigurationDao configurationDao)
    {
        this.configurationDao = configurationDao;
        return this;
    }

    public XmlImporterBuilder setConfigurationParserDao(ConfigurationParserDao configurationParserDao)
    {
        this.configurationParserDao = configurationParserDao;
        return this;
    }

    public XmlImporterBuilder setDomainObjectParserDao(DomainObjectParserDao domainObjectParserDao)
    {
        this.domainObjectParserDao = domainObjectParserDao;
        return this;
    }

    public XmlImporterBuilder setUserRoleParserDao(UserRoleParserDao userRoleParserDao)
    {
        this.userRoleParserDao = userRoleParserDao;
        return this;
    }
}
