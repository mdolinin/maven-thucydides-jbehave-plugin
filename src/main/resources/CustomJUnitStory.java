package com.engagepoint.acceptancetest.utils;

import com.google.common.collect.Lists;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.Inflector;
import net.thucydides.jbehave.ThucydidesJBehave;
import net.thucydides.jbehave.ThucydidesJUnitStories;
import org.codehaus.plexus.util.StringUtils;
import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.StepCollector;
import org.jbehave.core.steps.StepFinder;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.join;
import static org.jbehave.core.reporters.Format.*;
import static org.jbehave.core.reporters.Format.CONSOLE;

public class CustomJUnitStory extends ThucydidesJUnitStories {
    static StepCollector stepCollector = new MarkUnmatchedStepsAsPending(new StepFinder(new StepFinder.ByLevenshteinDistance()));

    public CustomJUnitStory() {
        findStoriesCalled(storynamesDerivedFromClassName());
    }

    public CustomJUnitStory(EnvironmentVariables environmentVariables) {
        super(environmentVariables);
        findStoriesCalled(storynamesDerivedFromClassName());
    }

    protected CustomJUnitStory(net.thucydides.core.webdriver.Configuration configuration) {
        super(configuration);
        findStoriesCalled(storynamesDerivedFromClassName());
    }

    protected String storynamesDerivedFromClassName() {

        List<String> storyNames = getStoryNameCandidatesFrom(startingWithUpperCase(simpleClassName()),
                startingWithLowerCase(simpleClassName()),
                underscoredTestName());
        return join(storyNames, ";");
    }

    @Override
    public void run() throws Throwable {
        super.run();
    }

    @Override
    public Configuration configuration() {
        Configuration configuration = super.configuration().useStepCollector(stepCollector);
        return configuration;
    }

    private List<String> getStoryNameCandidatesFrom(String... storyNameCandidates) {
        List<String> storyNames = Lists.newArrayList();
        for (String storyName : storyNameCandidates) {
            if (storyNames.isEmpty()) {
                addIfPresent(storyNames, "/" + storyName + ".story");
                addIfPresent(storyNames, "stories/" + storyName + ".story");
            }
        }
        if (storyNames.isEmpty()) {
            for (String storyName : storyNameCandidates) {
                storyNames.add("**/" + storyName + ".story");
            }
        }
        return storyNames;
    }

    private String startingWithUpperCase(String storyName) {
        return StringUtils.capitalise(storyName);
    }

    private String startingWithLowerCase(String storyName) {
        return StringUtils.lowercaseFirstLetter(storyName);
    }

    private void addIfPresent(List<String> storyNames, String storyNameCandidate) {
        if (Thread.currentThread().getContextClassLoader().getResource(storyNameCandidate) != null) {
            storyNames.add(storyNameCandidate);
        }
    }

    private String simpleClassName() {
        return this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().lastIndexOf("IT"));
    }

    private String underscoredTestName() {
        return Inflector.getInstance().of(simpleClassName()).withUnderscores().toString();
    }

}
