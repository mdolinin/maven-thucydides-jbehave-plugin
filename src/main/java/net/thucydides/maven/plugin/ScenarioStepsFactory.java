package net.thucydides.maven.plugin;

import net.thucydides.jbehave.ThucydidesStepFactory;
import net.thucydides.jbehave.reflection.Extract;
import net.thucydides.maven.plugin.generate.model.*;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.parsers.StepMatcher;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.StepCandidate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static net.thucydides.maven.plugin.utils.NameUtils.*;

public class ScenarioStepsFactory extends ThucydidesStepFactory {

    private String rootPackage;
    private List<StepCandidate> stepCandidates;
    private Map<String, Integer> argumentNames;

    public ScenarioStepsFactory(String rootPackage, ClassLoader classLoader) {
        super(new MostUsefulConfiguration(), rootPackage, classLoader);
        this.rootPackage = rootPackage;
    }

    private List<StepCandidate> findStepCandidates() {
        List<StepCandidate> stepCandidates = new ArrayList<StepCandidate>();
        for (CandidateSteps candidateSteps : createCandidateSteps()) {
            stepCandidates.addAll(candidateSteps.listCandidates());
        }
        return stepCandidates;
    }

    public List<StepCandidate> getStepCandidates() {
        if (stepCandidates == null) {
            stepCandidates = findStepCandidates();
        }
        return stepCandidates;
    }

    public ScenarioStepsClassModel createScenarioStepsClassModelFrom(Story story) {
        ScenarioStepsClassModel scenarioStepsClassModel = new ScenarioStepsClassModel();
        scenarioStepsClassModel.setPackageName(rootPackage);
        scenarioStepsClassModel.setClassNamePrefix(getClassNameFrom(story.getName()));
        Set<String> imports = new HashSet<String>();
        Set<FieldsSteps> fieldSteps = new HashSet<FieldsSteps>();
        List<ScenarioMethod> scenarios = new ArrayList<ScenarioMethod>();
        for (Scenario scenario : story.getScenarios()) {
            ScenarioMethod scenarioMethod = new ScenarioMethod();
            scenarioMethod.setScenarioName(scenario.getTitle());
            scenarioMethod.setMethodName(getMethodNameFrom(scenario.getTitle()));
            List<MethodArgument> scenarioMethodArguments = new ArrayList<MethodArgument>();
            Set<String> thrownExceptions = new HashSet<String>();
            argumentNames = new HashMap<String, Integer>();
            List<StepMethod> stepMethods = new ArrayList<StepMethod>();
            String previousNonAndStep = null;
            for (String step : scenario.getSteps()) {
                StepMethod matchedStepMethod = getMatchedStepMethodFor(step, previousNonAndStep, imports, scenarioMethodArguments, thrownExceptions);
                if (matchedStepMethod.getMethodName() == null) {
                    continue;
                }
                FieldsSteps fieldsSteps = new FieldsSteps();
                fieldsSteps.setClassName(matchedStepMethod.getMethodClass().getSimpleName());
                fieldsSteps.setFieldName(matchedStepMethod.getFieldName());
                fieldSteps.add(fieldsSteps);
                stepMethods.add(matchedStepMethod);
                if (!step.startsWith(Keywords.AND)) {
                    previousNonAndStep = step;
                }
            }
            scenarioMethod.setStepMethods(stepMethods);
            scenarioMethod.setArguments(scenarioMethodArguments);
            scenarioMethod.setThrownExceptions(thrownExceptions);
            scenarios.add(scenarioMethod);
        }
        scenarioStepsClassModel.setImports(imports);
        scenarioStepsClassModel.setFieldsSteps(fieldSteps);
        scenarioStepsClassModel.setScenarios(scenarios);
        return scenarioStepsClassModel;
    }

    public StepMethod getMatchedStepMethodFor(String step, String previousNonAndStep, Set<String> imports, List<MethodArgument> scenarioMethodArguments, Set<String> thrownExceptions) {
        StepMethod stepMethod = new StepMethod();
        for (StepCandidate candidate : getStepCandidates()) {
            if (candidate.matches(step, previousNonAndStep)) {
                stepMethod.setMethodName(candidate.getMethod().getName());
                Class<?>[] exceptionTypes = candidate.getMethod().getExceptionTypes();
                for (Class<?> exceptionType : exceptionTypes) {
                    thrownExceptions.add(exceptionType.getSimpleName());
                    imports.add(exceptionType.getCanonicalName());
                }
                Class<?> stepClass = candidate.getMethod().getDeclaringClass();
                stepMethod.setMethodClass(stepClass);
                imports.add(stepClass.getCanonicalName());
                String fieldClassName = stepClass.getSimpleName();
                String fieldName = replaceFirstCharacterToLowerCase(fieldClassName);
                stepMethod.setFieldName(fieldName);
                //get method arguments
                List<MethodArgument> methodArguments = new ArrayList<MethodArgument>();
                StepCandidate stepCandidate = (StepCandidate) Extract.field("stepCandidate").from(candidate);
                StepMatcher stepMatcher = (StepMatcher) Extract.field("stepMatcher").from(stepCandidate);
                String[] parameterNames = stepMatcher.parameterNames();
                for (int i = 0; i < parameterNames.length; i++) {
                    MethodArgument methodArgument = new MethodArgument();
                    String parameterName = getUnicParameterName(parameterNames[i]);
                    methodArgument.setArgumentName(parameterName);
                    Class<?> argumentClass = candidate.getMethod().getParameterTypes()[i];
                    methodArgument.setArgumentClass(argumentClass);
                    methodArgument.setArgumentType(argumentClass.getSimpleName());
                    addToImports(imports, argumentClass);
                    Type type = candidate.getMethod().getGenericParameterTypes()[i];
                    if(isParametrized(type)){
                        String parametrizedTypeClassCanonicalName = getParametrizedTypeClassCanonicalName(type);
                        Class<?> parametrizedTypeClass = null;
                        try {
                            parametrizedTypeClass = getClass().getClassLoader().loadClass(parametrizedTypeClassCanonicalName);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        methodArgument.setArgumentGenericType(parametrizedTypeClass.getSimpleName());
                        addToImports(imports, parametrizedTypeClass);
                    }
                    methodArguments.add(methodArgument);
                    scenarioMethodArguments.add(methodArgument);
                }
                stepMethod.setMethodArguments(methodArguments);
                return stepMethod;
            }
        }
        return stepMethod;
    }

    private void addToImports(Set<String> imports, Class<?> argumentClass) {
        if (!argumentClass.isPrimitive()) {
            imports.add(argumentClass.getCanonicalName());
        }
    }

    public String getParametrizedTypeClassCanonicalName(Type type) {
        Type argumentType = argumentType(type);
        return argumentType.toString().replaceFirst("class ", "");
    }

    private boolean isParametrized(Type type) {
        return type instanceof ParameterizedType;
    }

    private Type rawType(Type type) {
        return ((ParameterizedType) type).getRawType();
    }

    private Type argumentType(Type type) {
        return ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    private String getUnicParameterName(String parameterName) {
        int counter = 0;
        if (argumentNames.containsKey(parameterName)) {
            counter = argumentNames.get(parameterName);
            counter++;
            argumentNames.put(parameterName, counter);
            parameterName = parameterName + counter;
        } else {
            argumentNames.put(parameterName, counter);
        }
        return parameterName;
    }
}
