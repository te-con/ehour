package net.rrm.ehour.export.service;

import net.rrm.ehour.persistence.export.dao.ExportType;

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
public class ParseSession implements Serializable
{
    private Map<ExportType, Integer> insertions = new HashMap<ExportType, Integer>();
    private Map<ExportType, List<String>> errors = new HashMap<ExportType, List<String>>();

    private String filename;
    private boolean globalError;
    private String globalErrorMessage;
    private boolean imported = false;

    public void deleteFile()
    {
        if (filename != null)
        {
            File file = new File(filename);
            file.delete();
            imported = true;
        }
    }

    public void clearSession()
    {
        insertions.clear();
        errors.clear();
    }

    public boolean isImportable() {
        return !(imported || hasErrors());
    }


    public void addError(ExportType type, String error)
    {
        if (type == null)
        {
            return;
        }

        List<String> errorsForType;

        if (errors.containsKey(type))
        {
            errorsForType = errors.get(type);
        } else
        {
            errorsForType = new ArrayList<String>();
        }

        errorsForType.add(error);

        errors.put(type, errorsForType);
    }

    public void addInsertion(ExportType type)
    {
        Integer insertionCount;

        if (insertions.containsKey(type))
        {
            insertionCount = insertions.get(type);
        } else
        {
            insertionCount = 0;
        }

        insertions.put(type, ++insertionCount);
    }

    public Map<ExportType, Integer> getInsertions()
    {
        return insertions;
    }

    public Map<ExportType, List<String>> getErrors()
    {
        return errors;
    }

    public boolean hasErrors()
    {
        return !errors.isEmpty() || globalError;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public boolean isGlobalError()
    {
        return globalError;
    }

    public void setGlobalError(boolean globalError)
    {
        this.globalError = globalError;
    }

    public String getGlobalErrorMessage()
    {
        return globalErrorMessage;
    }

    public void setGlobalErrorMessage(String globalErrorMessage)
    {
        this.globalErrorMessage = globalErrorMessage;
    }

    public void setImported(boolean i)
    {
        this.imported = i;
    }
}
