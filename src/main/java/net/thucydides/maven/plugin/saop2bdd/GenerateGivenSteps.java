package net.thucydides.maven.plugin.saop2bdd;

import com.google.common.base.CaseFormat;
import com.sun.codemodel.*;
import net.thucydides.core.Thucydides;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.model.ExamplesTable;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.thucydides.maven.plugin.saop2bdd.SoapStepsGenerator.*;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

public class GenerateGivenSteps {
    private JCodeModel codeModel;
    private JDefinedClass serviceStepsRawClass;
    private GenerateGivenStepsForList generateGivenStepsForList;
    private Set<Class<?>> types = new HashSet<Class<?>>();
    private static final int PARAMETER_COUNT=10;

    public GenerateGivenSteps(JCodeModel codeModel, JDefinedClass serviceStepsRawClass, GenerateGivenStepsForList generateGivenStepsForList) {
        this.codeModel = codeModel;
        this.serviceStepsRawClass = serviceStepsRawClass;
        this.generateGivenStepsForList = generateGivenStepsForList;
    }

    public void generateFor(Class<?> type, String name, String key) {
        if (isSimple(type)) {
            return;
        }
        //create model of our web service class
        JClass rawTypeClass = codeModel.ref(type);

        //create model of Thucydides class
        JClass rawThucydidesClass = codeModel.ref(Thucydides.class);

        //create given method for type
        String stepMethodName = type.getSimpleName();
        JMethod givenMethod = serviceStepsRawClass.method(JMod.PUBLIC, Void.TYPE,
                getVariableName(Given.class) +
                        StringUtils.capitalize(stepMethodName)
        );
        //create jbehave annotation
        String stepPattern = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, stepMethodName).replaceAll("_", " ");

        //initialize type local variable
        JVar typeLocalVar = givenMethod.body().decl(rawTypeClass, name, JExpr._new(rawTypeClass));

        //get list off all fields
        Field[] declaredFields = type.getDeclaredFields();

        if (declaredFields.length > PARAMETER_COUNT) {
            //add response key to method
            givenMethod.param(String.class, key);
            //create example table method parameter
            JVar exampleTable = givenMethod.param(ExamplesTable.class, "parameters");
            //if block for checking row size
            JConditional checkRowSizeBlock = givenMethod.body()._if(exampleTable.invoke("getRows").invoke("size").ne(JExpr.lit(1)));
            JType exceptionType = codeModel._ref(AssertionError.class);
            checkRowSizeBlock._then()._throw(JExpr._new(exceptionType).arg("Wrong number of parameters."));
            String forEachIterator = "row";
            //create for each for map<string,string>
            JClass parametarizedMap = codeModel.ref(Map.class).narrow(codeModel.ref(String.class), codeModel.ref(String.class));
            JForEach forEach = givenMethod.body().forEach(parametarizedMap, forEachIterator, exampleTable.invoke("getRows"));
            //process all setters
            for (Field field : declaredFields) {
                //get name and type
                String fieldName = field.getName();
                Class<?> fieldClass = field.getType();
                Type fieldGenericType = field.getGenericType();
                Class<?> fieldGenericClass = null;
                JClass modelFieldClass = codeModel.ref(ClassUtils.primitiveToWrapper(fieldClass));
                //resolve generic types
                if (fieldGenericType instanceof ParameterizedType) {
                    fieldGenericClass = (Class) ((ParameterizedType) fieldGenericType).getActualTypeArguments()[0];
                    JClass modelGenericParameterTypeClass = codeModel.ref(fieldGenericClass);
                    //narrow class by its generic type
                    modelFieldClass = modelFieldClass.narrow(modelGenericParameterTypeClass);
                }
                JInvocation getValueFromRow = JExpr.ref(forEachIterator).invoke("get").arg(fieldName);
                //create if block
                JConditional ifBlock = forEach.body()._if(getValueFromRow.invoke("isEmpty").not());
                String fieldNameValue = fieldName + "Value";
                //create local variable
                ifBlock._then().decl(codeModel.ref(String.class), fieldNameValue, getValueFromRow);
                //get object from thucydides session
                addGetValueFromVariable(codeModel, serviceStepsRawClass, givenMethod, ifBlock._then(), field.getClass(), modelFieldClass, fieldName, fieldNameValue);
                JInvocation callSetter;
                if (fieldGenericClass == null) {
                    Method[] methods = type.getMethods();
                    String goodMethodName = "";
                    for (Method method : methods) {
                        if (method.getName().equalsIgnoreCase("set" + field.getName())) {
                            goodMethodName = method.getName();
                        }
                    }
                    callSetter = typeLocalVar.invoke(goodMethodName);
                } else {
                    String trimmedFieldName = field.getName().replaceFirst("_", "");
                    callSetter = typeLocalVar.invoke("get" + capitalize(trimmedFieldName)).invoke("addAll");
                }
                callSetter.arg(JExpr.ref(fieldName));
                if (fieldGenericClass == null) {
                    generateFor(fieldClass, fieldNameValue, fieldName);
                } else {
                    generateFor(fieldGenericClass, fieldNameValue, fieldName);
                    //generate given steps for Lists
                    if (fieldClass.equals(List.class)) {
                        generateGivenStepsForList.generateFor(fieldClass, fieldGenericClass, fieldNameValue, fieldName);
                    }
                }
                //add setter call to when method body
                ifBlock._then().add(callSetter);
            }
            //add save to part to annotation
            stepPattern += " saved to '$" + key + getClassNameLikeString(serviceStepsRawClass.name()) + "' with parameters: $parameters";
        } else {
            //process all setters
            for (Field field : declaredFields) {
                //get name and type
                String fieldName = field.getName();
                Class<?> fieldClass = field.getType();
                Type fieldGenericType = field.getGenericType();
                Class<?> fieldGenericClass = null;
                JClass modelFieldClass = codeModel.ref(ClassUtils.primitiveToWrapper(fieldClass));

                //resolve generic types
                if (fieldGenericType instanceof ParameterizedType) {
                    fieldGenericClass = (Class) ((ParameterizedType) fieldGenericType).getActualTypeArguments()[0];
                    JClass modelGenericParameterTypeClass = codeModel.ref(fieldGenericClass);
                    //narrow class by its generic type
                    modelFieldClass = modelFieldClass.narrow(modelGenericParameterTypeClass);
                }
                fieldName = uncapitalize(fieldClass.getSimpleName()) + capitalize(fieldName);
                String localVariableParameterName = fieldName;
                fieldName = fieldName + "Key";

                //create if for optional parameter
                JConditional jConditional = givenMethod.body()._if(JExpr.ref(fieldName).invoke("isEmpty").not());
                //create model of parameter type
                //create get from test session map method
                //get request from test session map
                //resole primitive types if parameter is not key
                addGetValueFromVariable(codeModel, serviceStepsRawClass, givenMethod, jConditional._then(), fieldClass, modelFieldClass, localVariableParameterName, fieldName);
                JInvocation callSetter;
                if (fieldGenericClass == null) {
                    Method[] methods = type.getMethods();
                    String goodMethodName = "";
                    for (Method method : methods) {
                        if (method.getName().equalsIgnoreCase("set" + field.getName())) {
                            goodMethodName = method.getName();
                        }
                    }
                    callSetter = typeLocalVar.invoke(goodMethodName);
                } else {
                    String trimmedFieldName = field.getName().replaceFirst("_", "");
                    callSetter = typeLocalVar.invoke("get" + capitalize(trimmedFieldName)).invoke("addAll");
                }
                callSetter.arg(JExpr.ref(localVariableParameterName));
                fieldName = addParamWithUniqueName(givenMethod, String.class, fieldName);
                if (fieldGenericClass == null) {
                    generateFor(fieldClass, localVariableParameterName, fieldName);
                } else {
                    generateFor(fieldGenericClass, localVariableParameterName, fieldName);
                    //generate given steps for Lists
                    if (fieldClass.equals(List.class)) {
                        generateGivenStepsForList.generateFor(fieldClass, fieldGenericClass, localVariableParameterName, fieldName);
                    }
                }
                stepPattern += " '$" + fieldName + "'";
                //add setter call to when method body
                jConditional._then().add(callSetter);
            }
            //add save to part to annotation
            stepPattern += " and save to $" + key + getClassNameLikeString(serviceStepsRawClass.name());
            //add response key to method
            givenMethod.param(String.class, key);
        }
        //save response to test session
        givenMethod.body().add(JExpr.invoke(SAVE).arg(JExpr.ref(key)).arg(JExpr.ref(name)));
        //add jbehave annotation
        givenMethod.annotate(Given.class).param("value", StringEscapeUtils.escapeJava(stepPattern));
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

    private String getClassNameLikeString(String className) {
        String[] strings = StringUtils.splitByCharacterTypeCamelCase(className);
        String result = " -";
        for (int i = 0; i < strings.length - 1; i++) {
            result += " " + strings[i].toLowerCase();
        }
        return result;
    }
}
