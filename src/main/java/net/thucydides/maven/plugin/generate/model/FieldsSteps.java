package net.thucydides.maven.plugin.generate.model;

public class FieldsSteps{

    private String className;
    private String fieldName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldsSteps that = (FieldsSteps) o;

        if (!className.equals(that.className)) return false;
        if (!fieldName.equals(that.fieldName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = className.hashCode();
        result = 31 * result + fieldName.hashCode();
        return result;
    }
}
