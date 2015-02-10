package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.service.backup.BackupConfig;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.springframework.util.Assert;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 12/6/10 11:59 PM
 */
public class XmlParserBuilder {
    private XMLEventReader xmlReader;
    private ConfigurationDao configurationDao;
    private ConfigurationParserDao configurationParserDao;
    private DomainObjectParserDao domainObjectParserDao;
    private boolean skipValidation = false;
    private BackupConfig backupConfig;

    public XmlParser build() throws XMLStreamException {
        Assert.notNull(xmlReader);
        Assert.notNull(configurationDao);
        Assert.notNull(domainObjectParserDao);
        Assert.notNull(backupConfig);

        PrimaryKeyCache keyCache = new PrimaryKeyCache();

        JoinTableParser joinTableParser = new JoinTableParser(xmlReader, backupConfig);
        DomainObjectParser parser = new DomainObjectParser(xmlReader, domainObjectParserDao, keyCache, backupConfig);

        EntityTableParser entityTableParser = new EntityTableParser(xmlReader, parser);

        ConfigurationParser configurationParser = new ConfigurationParser(configurationParserDao);

        ParseContext ctx = new ParseContext(configurationDao, parser, configurationParser, joinTableParser, entityTableParser, skipValidation);
        return new XmlParser(ctx);
    }

    public XmlParserBuilder setSkipValidation(boolean skipValidation) {
        this.skipValidation = skipValidation;
        return this;
    }

    public XmlParserBuilder setXmlReader(XMLEventReader xmlReader) {
        this.xmlReader = xmlReader;
        return this;
    }

    public XmlParserBuilder setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
        return this;
    }

    public XmlParserBuilder setConfigurationParserDao(ConfigurationParserDao configurationParserDao) {
        this.configurationParserDao = configurationParserDao;
        return this;
    }

    public XmlParserBuilder setDomainObjectParserDao(DomainObjectParserDao domainObjectParserDao) {
        this.domainObjectParserDao = domainObjectParserDao;
        return this;
    }

    public XmlParserBuilder setBackupConfig(BackupConfig backupConfig) {
        this.backupConfig = backupConfig;
        return this;
    }
}
