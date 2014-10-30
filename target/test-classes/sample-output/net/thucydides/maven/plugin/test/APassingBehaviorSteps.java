package net.thucydides.maven.plugin.test;

import net.thucydides.maven.plugin.test.SimpleSteps;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.When;

public class APassingBehaviorSteps extends ScenarioSteps {

    private static final long serialVersionUID = 1L;

    @Steps
    private SimpleSteps simpleSteps;

    public APassingBehaviorSteps(Pages pages) {
        super(pages);
    }

    @When("A scenario that works without parameters and not implemented steps")
    public void aScenarioThatWorksWithoutParametersAndNotImplementedSteps() {
        simpleSteps.givenIHaveAnImplementedJBehaveScenario();
    }

}