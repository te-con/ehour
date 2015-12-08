package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 5:33:50 PM
 */
public interface RestoreService {
    /**
     * Import database
     */
    void importDatabase(ParseSession session) throws ImportException;
}
