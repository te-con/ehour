package net.rrm.ehour.export.service;

import net.rrm.ehour.persistence.export.dao.ExportType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 11/30/10 12:57 AM
 */
public class ParseStatus
{
    public Map<ExportType, Integer> insertions = new HashMap<ExportType, Integer>();
    public Map<ExportType, List<String>> errors = new HashMap<ExportType, List<String>>();

    public void addError(ExportType type, String error)
    {
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

        insertionCount++;

        insertions.put(type, insertionCount);
    }

    public Map<ExportType, Integer> getInsertions()
    {
        return insertions;
    }

    public Map<ExportType, List<String>> getErrors()
    {
        return errors;
    }
}
