package net.rrm.ehour.export.service.importer;

import net.rrm.ehour.domain.Configuration;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:25 AM
 */
public interface ConfigurationParserDao
{
    public void persist(Configuration config);
}
