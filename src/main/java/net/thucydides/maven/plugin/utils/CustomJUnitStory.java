package net.thucydides.maven.plugin.utils;

import com.google.common.collect.Lists;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.Inflector;
import net.thucydides.jbehave.ThucydidesJUnitStories;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.steps.*;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.join;


public class CustomJUnitStory extends ThucydidesJUnitStories {

    static StepCollector stepCollector = new MarkUnmatchedStepsAsPending(new StepFinder1(new StepFinder1.ByPriorityField()));

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
        return org.apache.commons.lang.StringUtils.capitalise(storyName);
    }

    private String startingWithLowerCase(String storyName) {
        return org.codehaus.plexus.util.StringUtils.lowercaseFirstLetter(storyName);
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

class StepFinder1 extends StepFinder {
    private static List<StepCandidate> copyCandidates;
    static int counter = 0;

    private PrioritisingStrategy prioritisingStrategy;

    /**
     * Creates a StepFinder with a {@link ByPriorityField} strategy
     */
    public StepFinder1() {
        this(new ByPriorityField());
    }

    /**
     * Creates a StepFinder with a custom strategy
     *
     * @param prioritisingStrategy the PrioritisingStrategy
     */
    public StepFinder1(PrioritisingStrategy prioritisingStrategy) {
        this.prioritisingStrategy = prioritisingStrategy;
    }

    /**
     * Returns the stepdocs for the candidates collected from the given
     * {@link org.jbehave.core.steps.CandidateSteps}.
     *
     * @param candidateSteps the List of CandidateSteps
     * @return The List of Stepdocs, one for each {@link org.jbehave.core.steps.StepCandidate}.
     */
    public List<Stepdoc> stepdocs(List<CandidateSteps> candidateSteps) {
        List<Stepdoc> stepdocs = new LinkedList<Stepdoc>();
        for (StepCandidate candidate : collectCandidates(candidateSteps)) {
            stepdocs.add(new Stepdoc(candidate));
        }
        return stepdocs;
    }

    /**
     * Finds matching steps, represented as {@link Stepdoc}s, for a given
     * textual step and a list of {@link CandidateSteps}.
     *
     * @param stepAsText     the textual step
     * @param candidateSteps the List of CandidateSteps
     * @return The list of Stepdocs, one for each matched {@link StepCandidate}.
     */
    public List<Stepdoc> findMatching(String stepAsText, List<CandidateSteps> candidateSteps) {
        List<Stepdoc> matching = new ArrayList<Stepdoc>();
        for (StepCandidate candidate : collectCandidates(candidateSteps)) {
            if (candidate.matches(stepAsText)) {
                matching.add(new Stepdoc(candidate));
            }
        }
        return matching;
    }

    /**
     * Returns the steps instances associated to CandidateSteps
     *
     * @param candidateSteps the List of CandidateSteps
     * @return The List of steps instances
     */
    public List<Object> stepsInstances(List<CandidateSteps> candidateSteps) {
        List<Object> instances = new ArrayList<Object>();
        for (CandidateSteps steps : candidateSteps) {
            if (steps instanceof Steps) {
                instances.add(((Steps) steps).instance());
            }
        }
        return instances;
    }

    /**
     * Collects a list of step candidates from {@link CandidateSteps} instances.
     *
     * @param candidateSteps the list {@link CandidateSteps} instances
     * @return A List of {@link StepCandidate}
     */
    public List<StepCandidate> collectCandidates(List<CandidateSteps> candidateSteps) {
        List<StepCandidate> collected = new ArrayList<StepCandidate>();
        for (CandidateSteps steps : candidateSteps) {
            collected.addAll(steps.listCandidates());
        }
        return collected;
    }

    /**
     * Prioritises the list of step candidates that match a given step.
     *
     * @param stepAsText the textual step to match
     * @param candidates the List of StepCandidate
     * @return The prioritised list according to the
     * {@link PrioritisingStrategy}.
     */
    public List<StepCandidate> prioritise(String stepAsText, List<StepCandidate> candidates) {
        return prioritisingStrategy.prioritise(stepAsText, candidates);
    }

    /**
     * Defines the priorising strategy of step candidates
     */
    public static interface PrioritisingStrategy {

        List<StepCandidate> prioritise(String stepAsString, List<StepCandidate> candidates);

    }


    public static class ByPriorityField implements PrioritisingStrategy {

        public List<StepCandidate> prioritise(String stepAsText, List<StepCandidate> candidates) {
            if (copyCandidates != null) return copyCandidates;
            Collections.sort(candidates, new Comparator<StepCandidate>() {
                public int compare(StepCandidate o1, StepCandidate o2) {
                    int paramsO1 = o1.getMethod().getParameterTypes().length;
                    int paramsO2 = o2.getMethod().getParameterTypes().length;
                    return compareInt(paramsO2, paramsO1);
                }
            });
            copyCandidates = candidates;
            return copyCandidates;
        }
    }

    public static int compareInt(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }


    public static class ByLevenshteinDistance implements PrioritisingStrategy {

        private LevenshteinDistance ld = new LevenshteinDistance();

        public List<StepCandidate> prioritise(final String stepAsText, List<StepCandidate> candidates) {
            Collections.sort(candidates, new Comparator<StepCandidate>() {
                public int compare(StepCandidate o1, StepCandidate o2) {
                    String scoringPattern1 = scoringPattern(o1);
                    String scoringPattern2 = scoringPattern(o2);
                    String stepWithoutStartingWord = trimStartingWord(stepAsText);
                    Integer score1 = 0 - ld.calculate(scoringPattern1, stepWithoutStartingWord);
                    Integer score2 = 0 - ld.calculate(scoringPattern2, stepWithoutStartingWord);
                    int result = score2.compareTo(score1);
                    // default to strategy by priority if no score result
                    return result != 0 ? result : o2.getPriority().compareTo(o1.getPriority());
                }

                private String scoringPattern(StepCandidate candidate) {
                    return candidate.getPatternAsString().replaceAll("\\s\\$\\w+\\s", " ").replaceAll("\\$\\w+", "");
                }

                private String trimStartingWord(String stepAsString) {
                    return org.apache.commons.lang.StringUtils.substringAfter(stepAsString, " ");
                }

            });
            return candidates;
        }

        private class LevenshteinDistance {

            public int calculate(String s, String t) {
                int d[][]; // matrix
                int n; // length of s
                int m; // length of t
                int i; // iterates through s
                int j; // iterates through t
                char s_i; // ith character of s
                char t_j; // jth character of t
                int cost; // cost

                // Step 1
                n = s.length();
                m = t.length();
                if (n == 0) {
                    return m;
                }
                if (m == 0) {
                    return n;
                }
                d = new int[n + 1][m + 1];
                // Step 2
                for (i = 0; i <= n; i++) {
                    d[i][0] = i;
                }
                for (j = 0; j <= m; j++) {
                    d[0][j] = j;
                }
                // Step 3
                for (i = 1; i <= n; i++) {
                    s_i = s.charAt(i - 1);
                    // Step 4
                    for (j = 1; j <= m; j++) {
                        t_j = t.charAt(j - 1);
                        // Step 5
                        if (s_i == t_j) {
                            cost = 0;
                        } else {
                            cost = 1;
                        }
                        // Step 6
                        d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
                    }
                }
                // Step 7
                return d[n][m];
            }

            private int minimum(int a, int b, int c) {
                int mi = a;
                if (b < mi) {
                    mi = b;
                }
                if (c < mi) {
                    mi = c;
                }
                return mi;
            }

        }

    }

}


