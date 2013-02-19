package net.rrm.ehour.backup.service;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 5:33:50 PM
 */
public interface RestoreService {
    /**
     * Repare import XML database
     *
     * @param xmlData
     */
    ParseSession prepareImportDatabase(String xmlData);

    /**
     * Import database
     *
     * @param session
     * @throws ImportException
     */
    ParseSession importDatabase(ParseSession session);
}
