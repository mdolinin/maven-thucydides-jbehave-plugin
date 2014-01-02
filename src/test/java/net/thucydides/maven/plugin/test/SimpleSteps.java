package net.thucydides.maven.plugin.test;

import net.thucydides.maven.plugin.test.example.SimpleException;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.List;

public class SimpleSteps {

    @Given("I have an implemented JBehave scenario")
    public void givenIHaveAnImplementedJBehaveScenario() {
        System.out.println("This is jbehave simple step execution.");
    }


    @Given("I have an implemented JBehave scenario with one $parameter")
    public void givenIHaveAnImplementedJBehaveScenarioWithOneParameter(String parameter) {
        System.out.println("I have an implemented JBehave scenario with one " + parameter);
    }

    @Given("the scenario with two $first and $second parameters also works")
    public void givenIHaveAnImplementedJBehaveScenarioWithTwoParameters(String first, String second) {
        System.out.println("I have an implemented JBehave scenario with two " + first + ", " + second + " parameters.");
    }

    @When("user open '$url'")
    public void whenUserOpenUrl(String url){
        System.out.println("User open url - " + url);
    }

    @When("user save $links for use")
    public void whenUserSaveLinksForUse(List<String> links){
        System.out.println("User save links - " + links);
    }

    @Then("user should see title with: $titleTable")
    public void thenUserShouldSeeTitleWithText(String titleText) throws SimpleException {
        System.out.println("User see page with title - " + titleText);
    }
}
