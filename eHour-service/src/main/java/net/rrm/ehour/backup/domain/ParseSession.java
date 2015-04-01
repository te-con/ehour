package net.rrm.ehour.backup.domain;

import net.rrm.ehour.backup.common.BackupEntityType;

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
    private Map<BackupEntityType, Integer> insertions = new HashMap<>();
    private Map<BackupEntityType, List<String>> errors = new HashMap<>();

    private String filename;
    private boolean globalError;
    private String globalErrorMessage;
    private boolean imported = false;

    private double progress;
    private int eventCount;
    private int atEvent;

    public ParseSession() {
    }

    public ParseSession(int eventCount) {
        this.eventCount = eventCount;
    }

    public void start() {
        atEvent = 0;
        progress = 0;
        insertions.clear();
        errors.clear();
    }

    public void eventProgressed() {
        atEvent++;

        if (eventCount > 0) {
            progress = atEvent / (double) eventCount;
        }
    }

    public void finish() {
        progress = 100;

        if (eventCount == 0) {
            eventCount = atEvent;
        }
    }

    public void deleteFile() {
        if (filename != null) {
            File file = new File(filename);
            file.delete();
            imported = true;
        }
    }

    public boolean isImportable() {
        return !(imported || hasErrors());
    }

    public void addError(BackupEntityType entity, String error) {
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

    public void addInsertion(BackupEntityType type) {
        Integer insertionCount;

        if (insertions.containsKey(type)) {
            insertionCount = insertions.get(type);
        } else {
            insertionCount = 0;
        }

        insertions.put(type, ++insertionCount);
    }

    public Map<BackupEntityType, Integer> getInsertions() {
        return insertions;
    }

    public Map<BackupEntityType, List<String>> getErrors() {
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

    public double getProgress() {
        return progress;
    }

    public int getEventCount() {
        return eventCount;
    }
}
