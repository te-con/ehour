package net.rrm.ehour.backup.domain;

import net.rrm.ehour.backup.service.backup.BackupEntity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 11/30/10 12:57 AM
 */
public class ParseSession implements Serializable {
    private Map<BackupEntity, Integer> insertions = new HashMap<>();
    private Map<BackupEntity, List<String>> errors = new HashMap<>();

    private String filename;
    private boolean globalError;
    private String globalErrorMessage;
    private boolean imported = false;

    public void deleteFile() {
        if (filename != null) {
            File file = new File(filename);
            file.delete();
            imported = true;
        }
    }

    public void clearSession() {
        insertions.clear();
        errors.clear();
    }

    public boolean isImportable() {
        return !(imported || hasErrors());
    }

    public void addError(BackupEntity entity, String error) {
        if (entity == null) {
            return;
        }

        List<String> errorsForType;

        if (errors.containsKey(entity)) {
            errorsForType = errors.get(entity);
        } else {
            errorsForType = new ArrayList<>();
        }

        errorsForType.add(error);

        errors.put(entity, errorsForType);
    }

    public void addInsertion(BackupEntity type) {
        Integer insertionCount;

        if (insertions.containsKey(type)) {
            insertionCount = insertions.get(type);
        } else {
            insertionCount = 0;
        }

        insertions.put(type, ++insertionCount);
    }

    public Map<BackupEntity, Integer> getInsertions() {
        return insertions;
    }

    public Map<BackupEntity, List<String>> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty() || globalError;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isGlobalError() {
        return globalError;
    }

    public void setGlobalError(boolean globalError) {
        this.globalError = globalError;
    }

    public String getGlobalErrorMessage() {
        return globalErrorMessage;
    }

    public void setGlobalErrorMessage(String globalErrorMessage) {
        this.globalErrorMessage = globalErrorMessage;
    }

    public void setImported(boolean i) {
        this.imported = i;
    }
}
