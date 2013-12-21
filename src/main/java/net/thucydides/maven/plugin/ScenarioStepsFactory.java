package net.thucydides.maven.plugin;

import net.thucydides.jbehave.ClassFinder;
import net.thucydides.jbehave.ThucydidesStepFactory;
import net.thucydides.maven.plugin.generate.model.FieldsSteps;
import net.thucydides.maven.plugin.generate.model.ScenarioMethod;
import net.thucydides.maven.plugin.generate.model.ScenarioStepsClassModel;
import net.thucydides.maven.plugin.generate.model.StepMethod;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.StepCandidate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.thucydides.maven.plugin.utils.NameUtils.*;

public class ScenarioStepsFactory {


    private final ThucydidesStepFactory thucydidesStepFactory;
    private String rootPackage;
    private List<StepCandidate> stepCandidates;

    public ScenarioStepsFactory(String rootPackage) {
        this.rootPackage = rootPackage;
        this.thucydidesStepFactory = new ThucydidesStepFactory(new MostUsefulConfiguration(), rootPackage, ClassFinder.loadClasses().getClassLoader());
    }

    private List<StepCandidate> findStepCandidates() {
        List<StepCandidate> stepCandidates = new ArrayList<StepCandidate>();
        for (CandidateSteps candidateSteps : thucydidesStepFactory.createCandidateSteps()) {
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
            List<StepMethod> stepMethods = new ArrayList<StepMethod>();
            for (String step : scenario.getSteps()) {
                StepCandidate matchedStep = getMatchedStepFor(step);
                if (matchedStep == null) {
                    continue;
                }
                Class<?> stepClass = matchedStep.getMethod().getDeclaringClass();
                imports.add(stepClass.getCanonicalName());
                FieldsSteps fieldsSteps = new FieldsSteps();
                String fieldClassName = stepClass.getSimpleName();
                fieldsSteps.setClassName(fieldClassName);
                String fieldName = replaceFirstCharacterToLowerCase(fieldClassName);
                fieldsSteps.setFieldName(fieldName);
                fieldSteps.add(fieldsSteps);
                StepMethod stepMethod = new StepMethod();
                stepMethod.setFieldName(fieldName);
                stepMethod.setMethodName(matchedStep.getMethod().getName());
                stepMethods.add(stepMethod);
            }
            scenarioMethod.setStepMethods(stepMethods);
            scenarios.add(scenarioMethod);
        }
        scenarioStepsClassModel.setImports(imports);
        scenarioStepsClassModel.setFieldsSteps(fieldSteps);
        scenarioStepsClassModel.setScenarios(scenarios);
        return scenarioStepsClassModel;
    }

    public StepCandidate getMatchedStepFor(String step) {
        for (StepCandidate candidate : getStepCandidates()) {
            if (candidate.matches(step)) {
                return candidate;
            }
        }
        return null;
    }
}
