package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.Configuration;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:25 AM
 */
public interface ConfigurationParserDao {
    void persist(Configuration config);
}
