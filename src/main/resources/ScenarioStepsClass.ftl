package ${scenarioStepsClass.packageName};

<#list scenarioStepsClass.imports as classToImport>
import ${classToImport};
</#list>
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.When;

public class ${scenarioStepsClass.classNamePrefix}Steps extends ScenarioSteps {

    private static final long serialVersionUID = 1L;

    <#list scenarioStepsClass.fieldsSteps as innerSteps>
    @Steps
    private ${innerSteps.className} ${innerSteps.fieldName};

    </#list>
    public ${scenarioStepsClass.classNamePrefix}Steps(Pages pages) {
        super(pages);
    }

    <#list scenarioStepsClass.scenarios as method>
    @When("${method.scenarioName}")
    public void ${method.methodName}() {
        <#list method.stepMethods as step>
        ${step.fieldName}.${step.methodName}();
        </#list>
    }

    </#list>
}