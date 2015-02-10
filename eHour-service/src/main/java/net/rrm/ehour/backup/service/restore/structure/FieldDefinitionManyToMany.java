package net.rrm.ehour.backup.service.restore.structure;

import java.lang.reflect.Field;

public class FieldDefinitionManyToMany extends FieldDefinition {

    public FieldDefinitionManyToMany(Field field) {
        super(field, new FieldProcessorManyToManyImpl());
    }

    @Override
    public boolean isPartOfXML() {
        return false;
    }
}
