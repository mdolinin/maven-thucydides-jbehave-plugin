package net.thucydides.maven.plugin.saop2bdd;

import com.google.common.base.CaseFormat;
import com.sun.codemodel.*;
import net.thucydides.core.Thucydides;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.jbehave.core.annotations.Then;
import org.junit.Assert;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.hamcrest.Matchers;

import static net.thucydides.maven.plugin.saop2bdd.SoapStepsGenerator.addGetValueFromVariable;
import static net.thucydides.maven.plugin.saop2bdd.SoapStepsGenerator.getVariableName;

public class GenerateThenSteps {
    private JCodeModel codeModel;
    private JDefinedClass serviceStepsRawClass;

    public GenerateThenSteps(JCodeModel codeModel, JDefinedClass serviceStepsRawClass) {
        this.codeModel = codeModel;
        this.serviceStepsRawClass = serviceStepsRawClass;
    }

    private JClass generateSimilarToClazz(JCodeModel codeModel, Class<?> type, String nameClazz, String packageName) {
        GenerateSimilarToClass similarToClass = new GenerateSimilarToClass();
        return similarToClass.createGenerateSimilarToClazz(codeModel, type, nameClazz, packageName);
    }

    public void generateFor(Class<?> type, String name, String key) {
        //create model of our web service class
        JClass rawTypeClass = codeModel.ref(ClassUtils.primitiveToWrapper(type));
        JType rawType = codeModel._ref(ClassUtils.primitiveToWrapper(type));

        //create model of Thucydides class
        JClass rawThucydidesClass = codeModel.ref(Thucydides.class);

        //create keys for actual and expected values
        String actualValueKey = "actual" + StringUtils.capitalize(key);
        String actualValue = "actual" + StringUtils.capitalize(name);
        String expectedValueKey = "expected" + StringUtils.capitalize(key);
        String expectedValue = "expected" + StringUtils.capitalize(name);

        //create then method for type
        String stepMethodName = type.getSimpleName();
        JMethod thenMethod = serviceStepsRawClass.method(JMod.PUBLIC, Void.TYPE, getVariableName(Then.class) +
                StringUtils.capitalize(stepMethodName));

        //add actual key to method params
        thenMethod.param(String.class, actualValueKey);

        //create jbehave annotation
        String stepPattern = "$" + actualValueKey + " " + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, stepMethodName).replaceAll("_", " ");
        JClass rawArrayListClass = codeModel.ref(ArrayList.class);
        JClass arrayListClazz = rawArrayListClass.narrow(String.class);

        //get values from test session
        //create get from test session map method
        //get request from test session map
        //resole primitive types if parameter is not key
        addGetValueFromVariable(codeModel, serviceStepsRawClass, thenMethod, thenMethod.body(), type, rawTypeClass, actualValue, actualValueKey);

        //create get from test session map method
        //get request from test session map
        //resole primitive types if parameter is not key
        addGetValueFromVariable(codeModel, serviceStepsRawClass, thenMethod, thenMethod.body(), type, rawTypeClass, expectedValue, expectedValueKey);

        if (isSimple(type)) {
            //add assertion of actual and expected
            JClass refAssert = codeModel.ref(Assert.class);
            JClass matchers = codeModel.ref(Matchers.class);
            if (type.equals(BigDecimal.class)) {
                thenMethod.body().add(refAssert.staticInvoke("assertThat").
                        arg(JExpr.ref(actualValue)).
                        arg(matchers.staticInvoke("comparesEqualTo").arg(JExpr.ref(expectedValue))));
            } else {
                thenMethod.body().add(refAssert.staticInvoke("assertEquals").arg(JExpr.ref(actualValue)).arg(JExpr.ref(expectedValue)));
            }

            //add save to part to annotation
            stepPattern += " is equal to $" + expectedValueKey;

            //add expected key to method params
            thenMethod.param(String.class, expectedValueKey);
        } else {
            JClass refCustomMatcherSimilarTo = generateSimilarToClazz(codeModel, type, key, serviceStepsRawClass.getPackage().name() + ".matcher");
            //add assertion of actual and expected
            JClass refAssert = codeModel.ref(Assert.class);
            JClass refCoreMatchers = codeModel.ref(CoreMatchers.class);
            thenMethod.body().add(refAssert.staticInvoke("assertThat").arg(JExpr.ref(actualValue)).arg(refCustomMatcherSimilarTo.staticInvoke("similarTo").arg(JExpr.ref(expectedValue)).invoke("exclude").arg(JExpr.ref("fields"))));

            //add save to part to annotation
            stepPattern += " is equal to $" + expectedValueKey + " and ignore $fields";

            //add expected key to method params
            thenMethod.param(String.class, expectedValueKey);

            //add ignore fileds key to method params
            thenMethod.param(arrayListClazz, "fields");
        }

        //add jbehave annotation
        thenMethod.annotate(Then.class).param("value", StringEscapeUtils.escapeJava(stepPattern));
    }

    private boolean isSimple(Class<?> type) {
        return ClassUtils.isPrimitiveOrWrapper(type)
                || type.equals(String.class)
                || type.isEnum()
                || type.isInterface()
                || type.equals(XMLGregorianCalendar.class)
                || type.equals(BigInteger.class)
                || type.equals(BigDecimal.class);
    }

}
