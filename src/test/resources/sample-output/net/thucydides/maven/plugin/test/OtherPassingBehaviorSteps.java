package net.thucydides.maven.plugin.test;

import net.thucydides.maven.plugin.test.SimpleSteps;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import org.jbehave.core.steps.ParameterConverters.StringListConverter;
import net.thucydides.maven.plugin.test.example.SimpleException;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.When;

public class OtherPassingBehaviorSteps extends ScenarioSteps {

    private static final long serialVersionUID = 1L;

    @Steps
    private SimpleSteps simpleSteps;

    public OtherPassingBehaviorSteps(Pages pages) {
        super(pages);
    }

    @When("A scenario that works with parameters $parameter $first $second $url $links $first1 $second1 $first2 $second2 $titleTable")
    public void aScenarioThatWorksWithParameters(String parameter, String first, String second, String url, List<String> links, String first1, String second1, String first2, String second2, String titleTable) throws SimpleException {
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithOneParameter(parameter);
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithTwoParameters(first, second);
        simpleSteps.whenUserOpenUrl(url);
        simpleSteps.whenUserSaveLinksForUse(links);
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithTwoParameters(first1, second1);
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithTwoParameters(first2, second2);
        simpleSteps.thenUserShouldSeeTitleWithText(titleTable);
    }

    @When("A scenario that works with parameters")
    public void aScenarioThatWorksWithParameters() throws SimpleException {
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithOneParameter("param");
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithTwoParameters("uno", "dos");
        simpleSteps.whenUserOpenUrl("http:\\\\www.google.com");
        simpleSteps.whenUserSaveLinksForUse((ArrayList) new StringListConverter().convertValue("google,yandex,bing", new ArrayList().getClass()));
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithTwoParameters("uno", "dos");
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithTwoParameters("uno", "dos");
        simpleSteps.thenUserShouldSeeTitleWithText("'Google'");
    }

    @When("A scenario that has own $parameter inside")
    public void aScenarioThatHasOwnparameterInside(String parameter) {
        simpleSteps.givenIHaveAnImplementedJBehaveScenarioWithOneParameter(parameter);
    }

}