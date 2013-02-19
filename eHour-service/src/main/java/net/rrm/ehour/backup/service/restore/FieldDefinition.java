package net.rrm.ehour.backup.service.restore;

import java.lang.reflect.Field;

public class FieldDefinition {
    private Field field;

    public FieldDefinition(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public boolean isIgnorable() {
        return false;
    }
}
