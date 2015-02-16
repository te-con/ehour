package net.rrm.ehour.backup.service.restore.structure;

import java.lang.reflect.Field;

public class IgnorableFieldDefinition extends FieldDefinition {
    public IgnorableFieldDefinition(Field field) {
        super(field);
    }

    @Override
    public boolean isIgnorable() {
        return true;
    }
}
