package net.thucydides.maven.plugin.generate.model;

import java.util.List;

public class StepMethod {

    private Class<?> methodClass;
    private String fieldName;
    private String methodName;
    private List<MethodArgument> methodArguments;

    public Class<?> getMethodClass() {
        return methodClass;
    }

    public void setMethodClass(Class<?> methodClass) {
        this.methodClass = methodClass;
    }

    public List<MethodArgument> getMethodArguments() {
        return methodArguments;
    }

    public void setMethodArguments(List<MethodArgument> methodArguments) {
        this.methodArguments = methodArguments;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
