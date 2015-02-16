package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.persistence.config.dao.ConfigurationDao;

public class ParseContext {
    final ConfigurationDao configurationDao;
    final EntityParser entityParser;
    final ConfigurationParser configurationParser;
    final JoinTableParser joinTableParser;
    final EntityTableParser entityTableParser;
    final boolean skipValidation;

    public ParseContext(ConfigurationDao configurationDao, EntityParser entityParser, ConfigurationParser configurationParser, JoinTableParser joinTableParser, EntityTableParser entityTableParser, boolean skipValidation) {
        this.configurationDao = configurationDao;
        this.entityParser = entityParser;
        this.configurationParser = configurationParser;
        this.joinTableParser = joinTableParser;
        this.entityTableParser = entityTableParser;
        this.skipValidation = skipValidation;
    }
}
