package net.rrm.ehour.backup.domain;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 14, 2010 - 12:40:40 AM
 */
public class ImportException extends Exception {

    private String[] parameters;

    public ImportException(String messageKey) {
        super(messageKey);
    }

    public ImportException(String messageKey, String... parameters) {
        super(messageKey);

        this.parameters = parameters;
    }


    public ImportException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }

    public String getMessageKey() {
        return getMessage();
    }

    public String[] getParameters() {
        return parameters;
    }
}
