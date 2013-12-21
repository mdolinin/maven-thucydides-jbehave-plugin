package net.thucydides.maven.plugin.test;

import org.jbehave.core.annotations.Given;

public class SimpleSteps {

    @Given("I have an implemented JBehave scenario")
    public void givenIHaveAnImplementedJBehaveScenario() {
        System.out.println("This is jbehave simple step execution.");
    }


}
