package net.thucydides.maven.plugin.generate.model;

import java.util.List;

public class ScenarioMethod {

    private String scenarioName;
    private String methodName;

    public List<StepMethod> getStepMethods() {
        return stepMethods;
    }

    public void setStepMethods(List<StepMethod> stepMethods) {
        this.stepMethods = stepMethods;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    private List<StepMethod> stepMethods;

}
