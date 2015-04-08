package net.thucydides.maven.plugin.saop2bdd;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.codemodel.*;
import com.sun.codemodel.writer.FileCodeWriter;
import net.thucydides.core.Thucydides;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.maven.plugin.logging.Log;
import org.jbehave.core.annotations.When;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceClient;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.sun.codemodel.JJavaName.isJavaIdentifier;

public class SoapStepsGenerator {

    public static final String GET_VARIABLE_VALUE = "getVariableValue";
    public static final String GET_VARIABLE_AS_BOOLEAN = "getVariableAsBoolean";
    public static final String GET_VARIABLE_AS_INTEGER = "getVariableAsInteger";
    public static final String GET_VARIABLE_AS_LONG = "getVariableAsLong";
    public static final String GET_VARIABLE_AS_DOUBLE = "getVariableAsDouble";
    public static final String GET_VARIABLE_AS_BIG_INTEGER = "getVariableAsBigInteger";
    public static final String GET_VARIABLE_AS_BIG_DECIMAL = "getVariableAsBigDecimal";
    public static final String GET_VARIABLE_AS_XML_GREGORIAN_CALENDAR = "getVariableAsXMLGregorianCalendar";
    public static final String GET_VARIABLE_AS_STRING = "getVariableAsString";
    public static final String SAVE = "save";
    private static JClass rawThucydidesClass;
    private static JClass modelAssertionErrorClass;

    public void generateFor(String packageForScenarioSteps, ClassLoader classLoader, File outputDir, Log log, String soapStepsPackage) throws ClassNotFoundException, JClassAlreadyExistsException, IOException {

        Reflections reflections = new Reflections(packageForScenarioSteps, classLoader);

        Set<Class<?>> soapServicesClients = reflections.getTypesAnnotatedWith(WebServiceClient.class);
        JCodeModel codeModel = new JCodeModel();

        for (Class<?> serviceClient : soapServicesClients) {
            JDefinedClass serviceStepsRawClass = codeModel._class(soapStepsPackage + serviceClient.getName() + "Steps");

            //create then steps generator
            GenerateThenSteps thenStepsGenerator = new GenerateThenSteps(codeModel, serviceStepsRawClass);

            //create given steps generator for lists
            GenerateGivenStepsForList generateGivenStepsForList = new GenerateGivenStepsForList(codeModel, serviceStepsRawClass);

            //create given steps generator
            GenerateGivenSteps givenStepsGenerator = new GenerateGivenSteps(codeModel, serviceStepsRawClass, generateGivenStepsForList);

            //create model of Thucydides class
            rawThucydidesClass = codeModel.ref(Thucydides.class);

            //create model of AssertionError class
            modelAssertionErrorClass = codeModel.ref(AssertionError.class);

            //create model of our web service class
            JClass rawServiceClientClass = codeModel.ref(serviceClient);

            Set<Method> getWebEndpointMethods = new HashSet<Method>();
            Method[] declaredMethods = serviceClient.getDeclaredMethods();

            //remove methods with parameters
            for (Method method : declaredMethods) {
                if (method.getParameterTypes().length == 0) {
                    getWebEndpointMethods.add(method);
                }
            }
            for (Method getWebEndpointMethod : getWebEndpointMethods) {
                //get class with webservice interface
                Class<?> webServiceInterface = getWebEndpointMethod.getReturnType();

                //add field with endpoint and initialize
                JFieldVar proxyField = addFieldWithUniqueName(serviceStepsRawClass, webServiceInterface);
                proxyField.init(JExpr._new(rawServiceClientClass).invoke(getWebEndpointMethod.getName()));

                //get list off all webservice interface method
                Method[] webServiceMethods = webServiceInterface.getDeclaredMethods();
                //process all webservice methods
                Set<Class<?>> repeatedTypes = new HashSet<Class<?>>();
                for (Method webServiceMethod : webServiceMethods) {
                    //create code model for webservice response
                    Class<?> webServiceResponseClass = webServiceMethod.getReturnType();
                    JClass modelWebServiceResponseClass = null;
                    if (webServiceResponseClass.getName().equals(void.class.getName())) {
                        modelWebServiceResponseClass = codeModel.ref(Void.class);
                    } else {
                        modelWebServiceResponseClass = codeModel.ref(ClassUtils.primitiveToWrapper(webServiceResponseClass));
                    }
                    //resolve generic types
                    Type genericReturnType = webServiceMethod.getGenericReturnType();
                    Class genericReturnTypeClass = null;
                    if (genericReturnType instanceof ParameterizedType) {
                        genericReturnTypeClass = (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];//
                        JClass modelActualReturnTypeClass = codeModel.ref(genericReturnTypeClass);
                        //narrow class by its generic type
                        modelWebServiceResponseClass = modelWebServiceResponseClass.narrow(modelActualReturnTypeClass);
                    }
                    //create name for response
                    String webServiceResponseTypeName = getWebResultName(webServiceMethod);


                    //create when method for webservice method
                    String stepMethodName = webServiceMethod.getName();
                    JMethod whenMethod = serviceStepsRawClass.method(JMod.PUBLIC, Void.TYPE,
                            getVariableName(When.class) +
                                    StringUtils.capitalize(stepMethodName)
                    );

                    //create jbehave annotation
                    String stepPattern = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, stepMethodName).replaceAll("_", " ");
                    //create call of webservice
                    JInvocation callWebservice = proxyField.invoke(stepMethodName);
                    //find method parameters and add
                    Class<?>[] parameterTypes = webServiceMethod.getParameterTypes();
                    Type[] genericParameterTypes = webServiceMethod.getGenericParameterTypes();
                    Annotation[][] parameterAnnotations = webServiceMethod.getParameterAnnotations();
                    for (int i = 0; i < parameterTypes.length; i++) {
                        //create model of parameter type
                        Class<?> parameterClass = parameterTypes[i];
                        JClass modelParameterClass = codeModel.ref(ClassUtils.primitiveToWrapper(parameterClass));
                        Type genericParameterType = genericParameterTypes[i];
                        Class<?> genericParameterTypeClass = null;

                        try {
                            if (genericParameterType instanceof ParameterizedType) {
                                genericParameterTypeClass = (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
                                JClass modelGenericParameterTypeClass = codeModel.ref(genericReturnTypeClass);
                                //narrow class by its generic type
                                modelParameterClass = modelParameterClass.narrow(modelGenericParameterTypeClass);
                            }
                        } catch (ClassCastException ex) {
                            int beginName = genericParameterType.toString().indexOf("<") + 1;
                            int endName = genericParameterType.toString().length() - 1;
                            String parameterType = genericParameterType.toString().substring(beginName, endName);
                            modelParameterClass = modelParameterClass.narrow(codeModel.ref(parameterType));
                            log.debug(ex.getMessage());
                        }

                        String parameterName = getWebParamName(parameterAnnotations[i]);
                        String parameterKeyName = parameterName + "Key";
                        whenMethod.param(String.class, parameterKeyName);
                        JVar declaration = addDeclaration(codeModel, serviceStepsRawClass, whenMethod, whenMethod.body(), parameterClass, modelParameterClass, parameterName, parameterKeyName);
                        JConditional jConditional = whenMethod.body()._if(JExpr.ref(parameterKeyName).invoke("isEmpty").not());
                        //get request from test session map
                        //resolve primitive types if parameter is not key
//                        addGetValueFromVariable(codeModel, serviceStepsRawClass, whenMethod, whenMethod.body(), parameterClass, modelParameterClass, parameterName, parameterKeyName);
                        addInitializationLocalVariable(codeModel, serviceStepsRawClass, whenMethod, jConditional._then(), parameterClass, modelParameterClass, parameterName, parameterKeyName, declaration);

                        callWebservice.arg(JExpr.ref(parameterName));
                        stepPattern += " '$" + parameterKeyName + "'";
                        if (genericParameterTypeClass == null) {
                            givenStepsGenerator.generateFor(parameterClass, parameterName, parameterKeyName);
                        } else {
                            //generate given steps for Lists
                            if (parameterClass.equals(List.class)) {
                                generateGivenStepsForList.generateFor(parameterClass, genericParameterTypeClass, parameterName, parameterKeyName, serviceStepsRawClass.name());
                                givenStepsGenerator.generateFor(genericParameterTypeClass, getVariableName(genericParameterTypeClass), getVariableName(genericParameterTypeClass) + "Key");
                            }
                        }
                    }

                    //add save to part to annotation
                    String webServiceResponseTypeNameKey = webServiceResponseTypeName + "Key";
                    //webServiceResponseIgnoreFields


                    stepPattern += " and save response to $" + webServiceResponseTypeNameKey;
                    //add response key to method
                    whenMethod.param(String.class, webServiceResponseTypeNameKey);

                    //add webservice call to when method body
                    if (!webServiceResponseClass.getName().equals(void.class.getName())) {
                        whenMethod.body().decl(modelWebServiceResponseClass, webServiceResponseTypeName, callWebservice);
                        //save response to test session
                        whenMethod.body().add(JExpr.invoke(SAVE).arg(JExpr.ref(webServiceResponseTypeNameKey)).arg(JExpr.ref(webServiceResponseTypeName)));
                        givenStepsGenerator.generateFor(webServiceResponseClass, webServiceResponseTypeName, webServiceResponseTypeNameKey);
                        //avoid repeated generation of then steps with the same repeatedTypes
                        if (!repeatedTypes.contains(ClassUtils.primitiveToWrapper(webServiceResponseClass))) {
                            thenStepsGenerator.generateFor(webServiceResponseClass, webServiceResponseTypeName, webServiceResponseTypeNameKey);
                            repeatedTypes.add(ClassUtils.primitiveToWrapper(webServiceResponseClass));
                        }
                    } else {
                        whenMethod.body().add(callWebservice);
                    }
                    //generate given steps for Lists
                    if (webServiceResponseClass.equals(List.class)) {
                        generateGivenStepsForList.generateFor(webServiceResponseClass, genericReturnTypeClass, webServiceResponseTypeName, webServiceResponseTypeNameKey, serviceStepsRawClass.name());
                        givenStepsGenerator.generateFor(genericReturnTypeClass, getVariableName(genericReturnTypeClass), getVariableName(genericReturnTypeClass) + "Key");
                    }

                    //add jbehave annotation
                    whenMethod.annotate(When.class).param("value", StringEscapeUtils.escapeJava(stepPattern));

                    //add throws exceptions
                    Class<?>[] exceptionTypes = webServiceMethod.getExceptionTypes();
                    for (Class exceptionType : exceptionTypes) {
                        whenMethod._throws(exceptionType);
                    }
                }
            }

            //create method save
            JClass modelObjectClass = codeModel.ref(Object.class);
            JMethod save = serviceStepsRawClass.method(JMod.PRIVATE, Void.TYPE, SAVE);
            JVar toPutTypeNameKey = save.param(String.class, "toPutTypeNameKey");
            JVar toPutTypeName = save.param(modelObjectClass, "toPutTypeName");
            //create put to test session map method
            save.body().add(rawThucydidesClass.staticInvoke("getCurrentSession").invoke("put").arg(toPutTypeNameKey).arg(toPutTypeName));

            //create method getVariableValue
            String genericTypeName = "T";
            String genericGenerifiedTypeName = "<T>T";
            JClass genericT = codeModel.ref(genericTypeName);
            JClass genericGenerifiedT = codeModel.ref(genericGenerifiedTypeName);
            JMethod getVariableValue = serviceStepsRawClass.method(JMod.PRIVATE, genericGenerifiedT, GET_VARIABLE_VALUE);
            JVar key = getVariableValue.param(String.class, "key");
            //create get from test session map method
            JInvocation getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            getVariableValue.body()._return(JExpr.cast(genericT, getCurrentSession.arg(key)));

            //create method getVariableAsString
            JClass refStringClass = codeModel.ref(String.class);
            JMethod getVariableAsString = serviceStepsRawClass.method(JMod.PRIVATE, refStringClass, GET_VARIABLE_AS_STRING);
            key = getVariableAsString.param(String.class, "key");
            //create try block
            JTryBlock jTryBlock = getVariableAsString.body()._try();
            //create get from test session map method
            getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            jTryBlock.body()._return(JExpr.cast(refStringClass, getCurrentSession.arg(key)));
            jTryBlock._catch(modelAssertionErrorClass).body()._return(key);

            //create method getVariableAsBoolean
            JClass refBooleanClass = codeModel.ref(Boolean.class);
            JMethod getVariableAsBoolean = serviceStepsRawClass.method(JMod.PRIVATE, refBooleanClass, GET_VARIABLE_AS_BOOLEAN);
            key = getVariableAsBoolean.param(String.class, "key");
            //create try block
            jTryBlock = getVariableAsBoolean.body()._try();
            //create get from test session map method
            getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            jTryBlock.body()._return(JExpr.cast(refBooleanClass, getCurrentSession.arg(key)));
            jTryBlock._catch(modelAssertionErrorClass).body()._return(refBooleanClass.staticInvoke("parseBoolean").arg(key));

            //create method getVariableAsInteger
            JClass refIntegerClass = codeModel.ref(Integer.class);
            JMethod getVariableAsInteger = serviceStepsRawClass.method(JMod.PRIVATE, refIntegerClass, GET_VARIABLE_AS_INTEGER);
            key = getVariableAsInteger.param(String.class, "key");
            //create try block
            jTryBlock = getVariableAsInteger.body()._try();
            //create get from test session map method
            getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            jTryBlock.body()._return(JExpr.cast(refIntegerClass, getCurrentSession.arg(key)));
            jTryBlock._catch(modelAssertionErrorClass).body()._return(refIntegerClass.staticInvoke("parseInt").arg(key));

            //create method getVariableAsLong
            JClass refLongClass = codeModel.ref(Long.class);
            JMethod getVariableAsLong = serviceStepsRawClass.method(JMod.PRIVATE, refLongClass, GET_VARIABLE_AS_LONG);
            key = getVariableAsLong.param(String.class, "key");
            //create try block
            jTryBlock = getVariableAsLong.body()._try();
            //create get from test session map method
            getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            jTryBlock.body()._return(JExpr.cast(refLongClass, getCurrentSession.arg(key)));
            jTryBlock._catch(modelAssertionErrorClass).body()._return(refLongClass.staticInvoke("parseLong").arg(key));

            //create method getVariableAsDouble
            JClass refDoubleClass = codeModel.ref(Double.class);
            JMethod getVariableAsDouble = serviceStepsRawClass.method(JMod.PRIVATE, refDoubleClass, GET_VARIABLE_AS_DOUBLE);
            key = getVariableAsDouble.param(String.class, "key");
            //create try block
            jTryBlock = getVariableAsDouble.body()._try();
            //create get from test session map method
            getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            jTryBlock.body()._return(JExpr.cast(refDoubleClass, getCurrentSession.arg(key)));
            jTryBlock._catch(modelAssertionErrorClass).body()._return(refDoubleClass.staticInvoke("parseDouble").arg(key));

            //create method getVariableAsBigInteger
            JClass refBigIntegerClass = codeModel.ref(BigInteger.class);
            JMethod getVariableAsBigInteger = serviceStepsRawClass.method(JMod.PRIVATE, refBigIntegerClass, GET_VARIABLE_AS_BIG_INTEGER);
            key = getVariableAsBigInteger.param(String.class, "key");
            //create try block
            jTryBlock = getVariableAsBigInteger.body()._try();
            //create get from test session map method
            getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            jTryBlock.body()._return(JExpr.cast(refBigIntegerClass, getCurrentSession.arg(key)));
            jTryBlock._catch(modelAssertionErrorClass).body()._return(JExpr._new(refBigIntegerClass).arg(key));

            //create method getVariableAsBigDecimal
            JClass refBigDecimalClass = codeModel.ref(BigDecimal.class);
            JMethod getVariableAsBigDecimal = serviceStepsRawClass.method(JMod.PRIVATE, refBigDecimalClass, GET_VARIABLE_AS_BIG_DECIMAL);
            key = getVariableAsBigDecimal.param(String.class, "key");
            //create try block
            jTryBlock = getVariableAsBigDecimal.body()._try();
            //create get from test session map method
            getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            jTryBlock.body()._return(JExpr.cast(refBigDecimalClass, getCurrentSession.arg(key)));
            jTryBlock._catch(modelAssertionErrorClass).body()._return(JExpr._new(refBigDecimalClass).arg(key));

            //create method getVariableAsXMLGregorianCalendar
            JClass refXMLGregorianCalendarClass = codeModel.ref(XMLGregorianCalendar.class);
            JClass refDataTypeFactoryClass = codeModel.ref(DatatypeFactory.class);
            JMethod getVariableAsXMLGregorianCalendar = serviceStepsRawClass.method(JMod.PRIVATE, refXMLGregorianCalendarClass, GET_VARIABLE_AS_XML_GREGORIAN_CALENDAR);
            key = getVariableAsXMLGregorianCalendar.param(String.class, "key");
            //create try block
            jTryBlock = getVariableAsXMLGregorianCalendar.body()._try();
            //create get from test session map method
            getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
            //get request from test session map
            jTryBlock.body()._return(JExpr.cast(refXMLGregorianCalendarClass, getCurrentSession.arg(key)));
            jTryBlock._catch(modelAssertionErrorClass).body()._return(refDataTypeFactoryClass.staticInvoke("newInstance").invoke("newXMLGregorianCalendar").arg(key));
            getVariableAsXMLGregorianCalendar._throws(DatatypeConfigurationException.class);
            log.info("Class " + serviceStepsRawClass.fullName() + " created");
        }

        //generate java files from model
        codeModel.build(new FileCodeWriter(outputDir));
    }

    //TODO join  addDeclaration + addInitializationLocalVariable methods in one
    public static JVar addDeclaration(JCodeModel codeModel, JDefinedClass serviceStepsRawClass, JMethod method, JBlock jBlock, Class<?> parameterClass, JClass rawParameterType, String parameterName, String parameterKeyName) {
        String typeName = rawParameterType.name();
        if (typeName.equals(Boolean.class.getSimpleName())) {
            return method.body().decl(codeModel._ref(Boolean.class), parameterName, JExpr.ref("null"));
        } else if (typeName.equals(Integer.class.getSimpleName())) {
            return method.body().decl(codeModel._ref(Integer.class), parameterName, JExpr.ref("null"));
        } else if (typeName.equals(Long.class.getSimpleName())) {
            return method.body().decl(codeModel._ref(Long.class), parameterName, JExpr.ref("null"));
        } else if (typeName.equals(Double.class.getSimpleName())) {
            return method.body().decl(codeModel._ref(Double.class), parameterName, JExpr.ref("null"));
        } else if (typeName.equals(BigInteger.class.getSimpleName())) {
            return method.body().decl(codeModel._ref(BigInteger.class), parameterName, JExpr.ref("null"));
        } else if (typeName.equals(BigDecimal.class.getSimpleName())) {
            return method.body().decl(codeModel._ref(BigDecimal.class), parameterName, JExpr.ref("null"));
        } else if (typeName.equals(XMLGregorianCalendar.class.getSimpleName())) {
            method._throws(DatatypeConfigurationException.class);
            return method.body().decl(codeModel._ref(XMLGregorianCalendar.class), parameterName, JExpr.ref("null"));
        } else if (parameterClass.isEnum()) {
            JClass refEnumClass = codeModel.ref(parameterClass);
            String getVariableAsEnumMethodName = createMethodGetVariableAsEnum(serviceStepsRawClass, parameterClass, rawParameterType, refEnumClass);
            return jBlock.decl(refEnumClass, parameterName, JExpr.invoke(getVariableAsEnumMethodName).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(String.class.getSimpleName())) {
            return jBlock.decl(codeModel._ref(String.class), parameterName, JExpr.ref("null"));
        } else {
            return jBlock.decl(rawParameterType, parameterName, JExpr.ref("null"));
        }
    }

    public static void addInitializationLocalVariable(JCodeModel codeModel, JDefinedClass serviceStepsRawClass, JMethod method, JBlock jBlock, Class<?> parameterClass, JClass rawParameterType, String parameterName, String parameterKeyName, JVar declaration) {
        String typeName = rawParameterType.name();
        if (typeName.equals(Boolean.class.getSimpleName())) {
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_AS_BOOLEAN).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(Integer.class.getSimpleName())) {
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_AS_INTEGER).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(Long.class.getSimpleName())) {
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_AS_LONG).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(Double.class.getSimpleName())) {
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_AS_DOUBLE).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(BigInteger.class.getSimpleName())) {
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_AS_BIG_INTEGER).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(BigDecimal.class.getSimpleName())) {
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_AS_BIG_DECIMAL).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(XMLGregorianCalendar.class.getSimpleName())) {
            method._throws(DatatypeConfigurationException.class);
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_AS_XML_GREGORIAN_CALENDAR).arg((JExpr.ref(parameterKeyName))));
        } else if (parameterClass.isEnum()) {
            JClass refEnumClass = codeModel.ref(parameterClass);
            String getVariableAsEnumMethodName = createMethodGetVariableAsEnum(serviceStepsRawClass, parameterClass, rawParameterType, refEnumClass);
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(getVariableAsEnumMethodName).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(String.class.getSimpleName())) {
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_AS_STRING).arg((JExpr.ref(parameterKeyName))));
        } else {
            jBlock.assign(JExpr.ref(parameterName), JExpr.invoke(GET_VARIABLE_VALUE).arg((JExpr.ref(parameterKeyName))));
        }
    }

    public static void addGetValueFromVariable(JCodeModel codeModel, JDefinedClass serviceStepsRawClass, JMethod method, JBlock jBlock, Class<?> parameterClass, JClass rawParameterType, String parameterName, String parameterKeyName) {
        String typeName = rawParameterType.name();
        if (typeName.equals(Boolean.class.getSimpleName())) {
            jBlock.decl(codeModel._ref(Boolean.class), parameterName, JExpr.invoke(GET_VARIABLE_AS_BOOLEAN).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(Integer.class.getSimpleName())) {
            jBlock.decl(codeModel._ref(Integer.class), parameterName, JExpr.invoke(GET_VARIABLE_AS_INTEGER).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(Long.class.getSimpleName())) {
            jBlock.decl(codeModel._ref(Long.class), parameterName, JExpr.invoke(GET_VARIABLE_AS_LONG).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(Double.class.getSimpleName())) {
            jBlock.decl(codeModel._ref(Double.class), parameterName, JExpr.invoke(GET_VARIABLE_AS_DOUBLE).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(BigInteger.class.getSimpleName())) {
            jBlock.decl(codeModel._ref(BigInteger.class), parameterName, JExpr.invoke(GET_VARIABLE_AS_BIG_INTEGER).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(BigDecimal.class.getSimpleName())) {
            jBlock.decl(codeModel._ref(BigDecimal.class), parameterName, JExpr.invoke(GET_VARIABLE_AS_BIG_DECIMAL).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(XMLGregorianCalendar.class.getSimpleName())) {
            method._throws(DatatypeConfigurationException.class);
            jBlock.decl(codeModel._ref(XMLGregorianCalendar.class), parameterName, JExpr.invoke(GET_VARIABLE_AS_XML_GREGORIAN_CALENDAR).arg((JExpr.ref(parameterKeyName))));
        } else if (parameterClass.isEnum()) {
            //create method getVariableAsEnum
            JClass refEnumClass = codeModel.ref(parameterClass);
            String getVariableAsEnumMethodName = createMethodGetVariableAsEnum(serviceStepsRawClass, parameterClass, rawParameterType, refEnumClass);
            jBlock.decl(refEnumClass, parameterName, JExpr.invoke(getVariableAsEnumMethodName).arg((JExpr.ref(parameterKeyName))));
        } else if (typeName.equals(String.class.getSimpleName())) {
            jBlock.decl(codeModel._ref(String.class), parameterName, JExpr.invoke(GET_VARIABLE_AS_STRING).arg((JExpr.ref(parameterKeyName))));
        } else {
            jBlock.decl(rawParameterType, parameterName, JExpr.invoke(GET_VARIABLE_VALUE).arg((JExpr.ref(parameterKeyName))));
        }
    }

    private static String createMethodGetVariableAsEnum(JDefinedClass serviceStepsRawClass, Class<?> parameterClass, JClass rawParameterType, JClass refEnumClass) {
        String getVariableAsEnumMethodName = "getVariableAs" + parameterClass.getSimpleName();

        for (JMethod jMethod : serviceStepsRawClass.methods()) {
            if (jMethod.name().equals(getVariableAsEnumMethodName)) {
                return getVariableAsEnumMethodName;
            }
        }

        JMethod getVariableAsEnum = serviceStepsRawClass.method(JMod.PRIVATE, refEnumClass, getVariableAsEnumMethodName);
        JVar key = getVariableAsEnum.param(String.class, "key");
        //create try block
        JTryBlock jTryBlock = getVariableAsEnum.body()._try();
        //create get from test session map method
        JInvocation getCurrentSession = getFromCurrentSessionMethod(rawThucydidesClass);
        //get request from test session map
        jTryBlock.body()._return(JExpr.cast(refEnumClass, getCurrentSession.arg(key)));
        jTryBlock._catch(modelAssertionErrorClass).body()._return(rawParameterType.staticInvoke("fromValue").arg(key));
        return getVariableAsEnumMethodName;
    }

    public static JInvocation getFromCurrentSessionMethod(JClass rawThucydidesClass) {
        return rawThucydidesClass.staticInvoke("getCurrentSession").invoke("get");
    }

    public static JInvocation getPutToCurrentSessionMethod(JClass rawThucydidesClass, String toPutTypeNameKey, String toPutTypeName) {
        return rawThucydidesClass.staticInvoke("getCurrentSession").invoke("put").arg(JExpr.ref(toPutTypeNameKey)).arg(JExpr.ref(toPutTypeName));
    }

    private static String getWebResultName(Method method) {
        String webResultName = "";
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation instanceof WebResult) {
                webResultName = ((WebResult) annotation).name();
            }
        }
        return webResultName.isEmpty() ? getVariableName(method.getReturnType()) : webResultName;
    }

    private static String getWebParamName(Annotation[] parameterAnnotations) {
        for (Annotation annotation : parameterAnnotations) {
            if (annotation instanceof WebParam) {
                return ((WebParam) annotation).name();
            }
        }
        return "";
    }

    public static JFieldVar addFieldWithUniqueName(JDefinedClass jDefinedClass, Class<?> type) {
        int i = 1;
        boolean nameIsNotResolved = true;
        String oldName = getVariableName(type) + "Field";
        String newName = oldName;
        JFieldVar field = null;
        while (nameIsNotResolved) {
            try {
                field = jDefinedClass.field(JMod.PUBLIC, type, newName);
                nameIsNotResolved = false;
            } catch (IllegalArgumentException e) {
                i++;
                newName = oldName + i;
            }
        }
        return field;
    }

    public static String addParamWithUniqueName(JMethod jMethod, Class<?> parameterType, String parameterName) {
        int i = 1;
        List<String> parametersNames = Lists.transform(jMethod.params(), new Function<JVar, String>() {
            @Override
            public String apply(@Nullable JVar input) {
                return input != null ? input.name() : null;
            }
        });
        String newName = parameterName;
        while (parametersNames.contains(newName)) {
            i++;
            newName = parameterName + i;
        }
        jMethod.param(parameterType, newName);
        return newName;
    }

    private static String getUniqueNameSuffixFor(Method method) {
        StringBuilder suffix = new StringBuilder();
        for (Class<?> parameterTypes : method.getParameterTypes()) {
            suffix.append(parameterTypes.getSimpleName());
        }
        return suffix.toString();
    }

    /**
     * Gets name of parameterType class, makes first char toLowerCase
     * and add 'Value' suffix if result is java identifier
     * @param parameterType type class
     * @return name for variable
     */
    public static String getVariableName(Class<?> parameterType) {
        String rawVarName = StringUtils.uncapitalize(parameterType.getSimpleName());
        if (!isJavaIdentifier(rawVarName)) {
            rawVarName += "Value";
        }
        return rawVarName;
    }
}

