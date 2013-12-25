package net.thucydides.maven.plugin.test;

import org.jbehave.core.annotations.Given;

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
}
