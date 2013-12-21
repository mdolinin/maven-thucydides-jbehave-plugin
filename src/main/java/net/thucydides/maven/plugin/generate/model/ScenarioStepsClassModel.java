package net.thucydides.maven.plugin.generate.model;

import java.util.List;
import java.util.Set;

public class ScenarioStepsClassModel {

    private String packageName;
    private Set<String> imports;
    private String classNamePrefix;
    private Set<FieldsSteps> fieldsSteps;
    private List<ScenarioMethod> scenarios;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public String getClassNamePrefix() {
        return classNamePrefix;
    }

    public void setClassNamePrefix(String classNamePrefix) {
        this.classNamePrefix = classNamePrefix;
    }

    public Set<FieldsSteps> getFieldsSteps() {
        return fieldsSteps;
    }

    public void setFieldsSteps(Set<FieldsSteps> fieldsSteps) {
        this.fieldsSteps = fieldsSteps;
    }

    public List<ScenarioMethod> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<ScenarioMethod> scenarios) {
        this.scenarios = scenarios;
    }

}
