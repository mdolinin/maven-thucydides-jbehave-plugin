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
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static net.thucydides.maven.plugin.saop2bdd.GenerateStepsApp.addGetValueFromVariable;
import static net.thucydides.maven.plugin.saop2bdd.GenerateStepsApp.getVariableName;

public class GenerateThenSteps {
    public static final Logger logger = Logger.getLogger(GenerateThenSteps.class.getName());
    private JCodeModel codeModel;
    private JDefinedClass serviceStepsRawClass;
    private Set<Class<?>> types = new HashSet<Class<?>>();

    public GenerateThenSteps(JCodeModel codeModel, JDefinedClass serviceStepsRawClass) {
        this.codeModel = codeModel;
        this.serviceStepsRawClass = serviceStepsRawClass;
    }

    private JClass genarateSimilarToClazz(JCodeModel codeModel,Class<?> type, String nameClazz, String packageName) {
        GenerateSimilarToClass similarToClass = new GenerateSimilarToClass();
         return similarToClass.createGenerateSimilarToClazz(codeModel, type, nameClazz,packageName );
    }


    public void generateFor(Class<?> type, String name, String key, File outputDir) {
        if (isSimple(type)) {
            return;
        }
        System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
        JClass refCustomMatcherSimilarTo = genarateSimilarToClazz(codeModel, type, key, serviceStepsRawClass.getPackage().name());

        //create model of our web service class
        JClass rawTypeClass = codeModel.ref(type);
        JType rawType = codeModel._ref(type);

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

        //add assertion of actual and expected
        JClass refAssert = codeModel.ref(Assert.class);
        JClass refCoreMatchers = codeModel.ref(CoreMatchers.class);

//        thenMethod.body().add(refAssert.staticInvoke("assertThat").arg(JExpr.ref(actualValue)).arg(refCoreMatchers.staticInvoke("is").arg(JExpr.ref(expectedValue))));

        thenMethod.body().add(refAssert.staticInvoke("assertThat").arg(JExpr.ref(actualValue)).arg(refCustomMatcherSimilarTo.staticInvoke("similarTo").arg(JExpr.ref(expectedValue))));

        //add save to part to annotation
        stepPattern += " is equal to $" + expectedValueKey + " and ignore $fields";
//        stepPattern += " is equal to $" + expectedValueKey;
        logger.info("stepPattern " + stepPattern);
        //add expected key to method params
        thenMethod.param(String.class, expectedValueKey);
        //add ignore fileds key to method params

        thenMethod.param(arrayListClazz, "fields");

        //add jbehave annotation
        thenMethod.annotate(Then.class).param("value", StringEscapeUtils.escapeJava(stepPattern));

        //add throws exceptions
//        Class<?>[] exceptionTypes = setter.getExceptionTypes();
//        for (Class exceptionType : exceptionTypes) {
//            thenMethod._throws(exceptionType);
//        }
        types.add(type);
    }

    private boolean isSimple(Class<?> type) {
        return types.contains(type)
                || ClassUtils.isPrimitiveOrWrapper(type)
                || type.equals(String.class)
                || type.isEnum()
                || type.isInterface()
                || type.equals(XMLGregorianCalendar.class)
                || type.equals(BigInteger.class)
                || type.equals(BigDecimal.class);
    }

}
