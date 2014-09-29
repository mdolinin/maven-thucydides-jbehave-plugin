package net.thucydides.maven.plugin.saop2bdd;

import com.google.common.base.CaseFormat;
import com.sun.codemodel.*;
import net.thucydides.core.Thucydides;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.annotations.Given;

import javax.xml.bind.annotation.XmlType;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static net.thucydides.maven.plugin.saop2bdd.GenerateStepsApp.*;

/**
 * Created by mdolinin on 8/11/14.
 */
public class GenerateGivenStepsForList {
    private JCodeModel codeModel;
    private JDefinedClass serviceStepsRawClass;
    private Set<Class<?>> types = new HashSet<Class<?>>();

    public GenerateGivenStepsForList(JCodeModel codeModel, JDefinedClass serviceStepsRawClass) {
        this.codeModel = codeModel;
        this.serviceStepsRawClass = serviceStepsRawClass;
    }

    public void generateFor(Class<?> rawClass, Class<?> genericClass, String name, String key) {
        if (types.contains(genericClass)) {
            return;
        }
        //create model of our classes
        JClass modelRawClass = codeModel.ref(rawClass);
        JClass modelGenericClass = codeModel.ref(genericClass);
        JClass modelNarrowedRawClass = modelRawClass.narrow(modelGenericClass);

        //create model of Thucydides class
        JClass rawThucydidesClass = codeModel.ref(Thucydides.class);

        //create given method for type
        String stepMethodName = modelNarrowedRawClass.erasure().name() + modelGenericClass.name();
        JMethod givenMethod = serviceStepsRawClass.method(JMod.PUBLIC, Void.TYPE,
                getVariableName(Given.class) +
                        StringUtils.capitalize(stepMethodName));

        //create jbehave annotation
        String stepPattern = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, stepMethodName).replaceAll("_", " ");

        //add model of array list
        JClass rawArrayListClass = codeModel.ref(ArrayList.class);
        JClass arrayListClazz = rawArrayListClass.narrow(genericClass);

        //add parameter
        String listOfKeysParameterName = getXmlTypeName(genericClass) + "Keys";
        //create model for list with keys
        JClass modelNarrowedListOfKeys = modelRawClass.narrow(String.class);
        //add list with keys to parameters
        JVar listOfKeysParam = givenMethod.param(modelNarrowedListOfKeys, listOfKeysParameterName);
        stepPattern += " $" + listOfKeysParameterName;

        //create name for one key and local variable names
        String localVarGenericTypeName = getXmlTypeName(genericClass);
        String oneKey = localVarGenericTypeName + "Key";

        //initialize type local variable
        JVar typeLocalVar = givenMethod.body().decl(modelNarrowedRawClass, name, JExpr._new(arrayListClazz));

        //create loop
        JForEach jForEach = givenMethod.body().forEach(codeModel.ref(String.class), oneKey, listOfKeysParam);

        //get request from test session map
        //resole primitive types if parameter is not key
        addGetValueFromVariable(codeModel, serviceStepsRawClass, givenMethod, jForEach.body(), genericClass, modelGenericClass, localVarGenericTypeName, oneKey);

        jForEach.body().add(typeLocalVar.invoke("add").arg(JExpr.ref(localVarGenericTypeName)));

        //add save to part to annotation
        stepPattern += " and save to $" + key;
        //add response key to method
        givenMethod.param(String.class, key);

        //save response to test session
        givenMethod.body().add(JExpr.invoke(SAVE).arg(JExpr.ref(key)).arg(JExpr.ref(name)));

        //add jbehave annotation
        givenMethod.annotate(Given.class).param("value", StringEscapeUtils.escapeJava(stepPattern));

        types.add(genericClass);
    }

    private String getXmlTypeName(Class<?> clazz) {
        String xmlTypeName = "";
        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (annotation instanceof XmlType) {
                xmlTypeName = ((XmlType) annotation).name();
            }
        }
        return xmlTypeName.isEmpty() ? getVariableName(clazz) : xmlTypeName;
    }

}
