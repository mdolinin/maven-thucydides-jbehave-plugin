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
    <#assign scenarioParametersQty = method.scenarioParameters?size>
    @When("${method.scenarioName}<#list method.arguments as argument><#if scenarioParametersQty <= argument_index> $${argument.argumentName}</#if></#list>")
    public void ${method.methodName}(<#list method.arguments as argument>${argument.argumentType}${argument.argumentGenericType} ${argument.argumentName}<#if argument_has_next>, </#if></#list>)<#if 0 < method.thrownExceptions?size> throws </#if><#list method.thrownExceptions as exception>${exception}<#if exception_has_next>, </#if></#list> {
        <#list method.stepMethods as step>
        ${step.fieldName}.${step.methodName}(<#list step.methodArguments as argument>${argument.argumentName}<#if argument_has_next>, </#if></#list>);
        </#list>
    }

    <#if method.arguments?size != 0 && method.arguments?size != scenarioParametersQty>
    @When("${method.scenarioName}")
    public void ${method.methodName}(<#list method.arguments as argument><#if argument_index < scenarioParametersQty>${argument.argumentType}${argument.argumentGenericType} ${argument.argumentName}<#if argument_has_next && argument_index < scenarioParametersQty - 1>, </#if></#if></#list>)<#if 0 < method.thrownExceptions?size> throws </#if><#list method.thrownExceptions as exception>${exception}<#if exception_has_next>, </#if></#list> {
        <#list method.stepMethods as step>
        ${step.fieldName}.${step.methodName}(<#list step.methodArguments as argument>${argument.argumentDefaultValue}<#if argument_has_next>, </#if></#list>);
        </#list>
    }

    </#if>
    </#list>
}