package net.thucydides.maven.plugin.generate.model;

import java.util.List;
import java.util.Set;

public class ScenarioMethod {

    private String scenarioName;
    private String methodName;
    private List<StepMethod> stepMethods;
    private List<MethodArgument> arguments;
    private Set<String> thrownExceptions;
    private List<String> add;

    public List<String> getScenarioParameters() {
        return scenarioParameters;
    }

    public void setScenarioParameters(List<String> scenarioParameters) {
        this.scenarioParameters = scenarioParameters;
    }

    public Set<String> getThrownExceptions() {
        return thrownExceptions;
    }

    public void setThrownExceptions(Set<String> thrownExceptions) {
        this.thrownExceptions = thrownExceptions;
    }

    public List<MethodArgument> getArguments() {
        return arguments;
    }

    public void setArguments(List<MethodArgument> arguments) {
        this.arguments = arguments;
    }

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

}
