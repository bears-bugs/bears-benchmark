package net.bytebuddy.description.field;

import java.lang.reflect.Field;

public class FieldDescriptionForLoadedFieldsTest extends AbstractFieldDescriptionTest {

    @Override
    protected FieldDescription.InDefinedShape describe(Field field) {
        return new FieldDescription.ForLoadedField(field);
    }
}
