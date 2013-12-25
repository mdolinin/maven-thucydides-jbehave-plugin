package net.thucydides.maven.plugin.generate.model;

public class MethodArgument {

    private Class<?> argumentClass;
    private String argumentType;
    private String argumentName;

    public Class<?> getArgumentClass() {
        return argumentClass;
    }

    public void setArgumentClass(Class<?> argumentClass) {
        this.argumentClass = argumentClass;
    }

    public String getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(String argumentType) {
        this.argumentType = argumentType;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }
}
