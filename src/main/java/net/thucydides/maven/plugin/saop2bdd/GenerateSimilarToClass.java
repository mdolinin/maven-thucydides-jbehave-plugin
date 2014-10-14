package net.thucydides.maven.plugin.saop2bdd;

import com.sun.codemodel.*;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;


public class GenerateSimilarToClass {
    public static final Logger logger = Logger.getLogger(GenerateSimilarToClass.class.getName());
    public static final String CUSTOM_MATCHER_SIMILAR_TO_CLASS_NAME = "SimilarTo";
    public static final String ACTUAL_ROW = "actualRow";
    public static final String KEY = "key";
    public static final String EXPECTED_ROW = "expectedRow";
    public static final String EXPECTED_DATA = "expectedData";
    public static final String ACTUAL_DATA = "actualData";
    public static final String RESPONSE_KEY = "ResponseKey";
    public static final String EXPECTED_RESPONSE = "expectedResponse";
    public static final String ACTUAL_RESPONSE = "actualResponse";
    public static final String LIST_TO_EXCLUDE = "listToExclude";
    private JCodeModel codeModel;
    private JDefinedClass definedClass;
    private JClass rawArrayListClass;
    private JClass arrayListClazz;
    private JFieldVar excludeListField;
    private JClass expectedResponseClazz;
    private JFieldVar expectedResponseField;
    private JMethod matchesSafelyMethod;
    private String[] counterSymbol = new String[]{"o", "i", "j", "k", "m", "n"};
    private int countArraySymbol = 0;
    int c = 0;


    public JClass createGenerateSimilarToClazz(JCodeModel codeModel, Class<?> type, String nameClazz, String packageName) {
        try {
            JClass ref = codeModel.ref(Void.class);
            String originalNameClassWithLowerCase = getOrifinalName(nameClazz);
            originalNameClassWithLowerCase = makeFirstLetterUpperCase(originalNameClassWithLowerCase);
            initModel(type, codeModel, packageName, originalNameClassWithLowerCase);
        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }
        createConstructorSimilarToClazz(type);
        createExcludeListField();
        createDataField(ACTUAL_DATA);
        createDataField(EXPECTED_DATA);
        createExpectedResponseFiled(type);
        createExcludeMethod(arrayListClazz, excludeListField);
        createSimilarToMethod(type);
        createMatchesSafelyMethod(type);
        createDescribeMismatchSafely(type);
        createDescribeToMethod(type);
        return definedClass;

    }

    private String getOrifinalName(String nameClazz) {
        int beginOriginName = nameClazz.indexOf("get");
        int endOriginName = nameClazz.lastIndexOf(RESPONSE_KEY) > nameClazz.lastIndexOf(KEY) ? nameClazz.lastIndexOf(RESPONSE_KEY) : nameClazz.lastIndexOf(KEY);
        if (beginOriginName == -1 && endOriginName == -1) {
            return nameClazz;
        }
        if (beginOriginName != -1 && endOriginName != -1) {
            return nameClazz.substring(beginOriginName, endOriginName);
        } else if (beginOriginName != -1) {
            return nameClazz.substring(beginOriginName);
        } else {
            return nameClazz.substring(0, endOriginName);
        }
    }

    public void createExpectedResponseFiled(Class<?> type) {
        expectedResponseClazz = codeModel.ref(type);
        expectedResponseField = definedClass.field(JMod.PRIVATE, type, EXPECTED_RESPONSE);
    }

    public void createDataField(String name) {
        JClass dataDeclarationClazz = codeModel.ref(Map.class);
        JClass narrowDataDeclarationClazz = dataDeclarationClazz.narrow(String.class, String.class);
        JClass dataInstantiationClazz = codeModel.ref(LinkedHashMap.class);
        JClass narrowDataInstantiationClazz = dataInstantiationClazz.narrow(String.class, String.class);
        JExpression newInstance = JExpr._new(narrowDataInstantiationClazz);
        String fieldName = name;
        JFieldVar dataField = definedClass.field(JMod.PRIVATE, narrowDataDeclarationClazz, fieldName);
        dataField.init(newInstance);
    }

    public void createExcludeListField() {
        rawArrayListClass = codeModel.ref(List.class);
        arrayListClazz = rawArrayListClass.narrow(String.class);
        excludeListField = definedClass.field(JMod.PRIVATE, arrayListClazz, "listToExclude");

    }

    public void initModel(Class<?> type, JCodeModel model, String packageName, String originalNameClass) throws JClassAlreadyExistsException {
        this.codeModel = model;
        JClass refTypeSafeMatcher = codeModel.ref(TypeSafeMatcher.class);
        JClass similarToListClazzWithGeneric = refTypeSafeMatcher.narrow(type);
        String responseClassName = type.getName();
        definedClass = codeModel._class(packageName + "." + originalNameClass + CUSTOM_MATCHER_SIMILAR_TO_CLASS_NAME)._extends(similarToListClazzWithGeneric);

    }

    public void createExcludeMethod(JClass arrayListClazz, JFieldVar fieldExcludeList) {
        JMethod excludeMethod = definedClass.method(JMod.PUBLIC, definedClass, "exclude");
        String paramExcludeMethod = "listToExclude";
        excludeMethod.param(arrayListClazz, paramExcludeMethod);
        JBlock blockEclude = excludeMethod.body();
        excludeMethod.body().assign(JExpr._this().ref(fieldExcludeList), JExpr.ref(paramExcludeMethod));
        blockEclude._return(JExpr._this());
    }

    public void createSimilarToMethod(Class<?> type) {
        JMethod similarToMethod = definedClass.method(JMod.PUBLIC | JMod.STATIC, definedClass, "similarTo");
        String paramSimilarToMethod = EXPECTED_RESPONSE;
        similarToMethod.param(type, paramSimilarToMethod);
        similarToMethod.annotate(Factory.class);
        JBlock blockSimilarTo = similarToMethod.body();
        blockSimilarTo._return(JExpr._new(definedClass).arg(JExpr.ref(paramSimilarToMethod)));
    }

    public void createConstructorSimilarToClazz(Class<?> type) {
        JMethod constructorMethod = definedClass.constructor(JMod.PUBLIC);
        constructorMethod.param(type, EXPECTED_RESPONSE);
        JBlock block = constructorMethod.body();
        block.assign(JExpr._this().ref(EXPECTED_RESPONSE), JExpr.ref(EXPECTED_RESPONSE));
    }

    public void createMatchesSafelyMethod(Class<?> type) {
        matchesSafelyMethod = definedClass.method(JMod.PUBLIC, Boolean.TYPE, "matchesSafely");
        matchesSafelyMethod.param(type, ACTUAL_RESPONSE);
        matchesSafelyMethod.annotate(Override.class);
        JBlock blockMatchesSafely = matchesSafelyMethod.body();
        mainBody(type, ACTUAL_RESPONSE, EXPECTED_RESPONSE, blockMatchesSafely, null);
        blockMatchesSafely._return(JExpr.ref(EXPECTED_DATA).invoke("isEmpty").cand(JExpr.ref(ACTUAL_DATA).invoke("isEmpty")));
    }

    private JExpression createMsgForEqualsCondition(String name) {
        JExpression exp = null;
        name = name.substring(3);
        if (countArraySymbol == 0) {
            exp = JExpr.lit(name + " ");
        }
        if (countArraySymbol == 1) {
            exp = JExpr.lit(name + " in row ").plus(JExpr.ref(counterSymbol[countArraySymbol]));
        }
        if (countArraySymbol == 2) {
            exp = JExpr.lit(name + " in row ").plus(JExpr.ref(counterSymbol[1])).plus(JExpr.ref(counterSymbol[2]));
        }
        if (countArraySymbol == 3) {
            exp = JExpr.lit(name + " in row ").plus(JExpr.ref(counterSymbol[1])).plus(JExpr.ref(counterSymbol[2])).plus(JExpr.ref(counterSymbol[3]));
        }
        return exp;
    }


    private void mainBody(Class<?> type, String paramActual, String paramExpected, JBlock block, Method superMethod) {
        Class<?> classNameGenericReturnType = null;
        if (type.getName().equals(void.class.getName())) {
            return;
        }
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            String fieldName = null;
            if (isGetMethod(method)) {
                Class<?> methodReturnType = method.getReturnType();
                if (isSimpleType(methodReturnType)) {
                    if (methodReturnType.isEnum()) {
                        JConditional condition = block._if(JExpr.ref(paramActual).invoke(method.getName()).invoke("equals").arg(JExpr.ref(paramExpected).invoke(method.getName())).not());
                        if (null != superMethod) {
                            condition._then()
                                    .add(JExpr.ref(EXPECTED_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(JExpr.ref(paramExpected).invoke(method.getName()).invoke("toString")))
                                    .add(JExpr.ref(ACTUAL_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(JExpr.ref(paramActual).invoke(method.getName()).invoke("toString")));
                        } else {
                            condition._then()
                                    .add(JExpr.ref(EXPECTED_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(JExpr.ref(paramExpected).invoke(method.getName()).invoke("toString")))
                                    .add(JExpr.ref(ACTUAL_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(JExpr.ref(paramActual).invoke(method.getName()).invoke("toString")));
                        }
                        continue;
                    }

                    fieldName = method.getName().substring(3);
                    fieldName = makeFirstLetterLowerCase(fieldName);
                    String equalMethod = "compareTo";
                    if (isXMLGregorianCalendar(methodReturnType)) {
                        equalMethod = "compare";
                        createConditionBlockForExcludeList(block, paramActual, paramExpected, method, fieldName, equalMethod);
                    }
                    if (methodReturnType.isPrimitive()) {
                        JConditional condition = block._if(JExpr.ref(LIST_TO_EXCLUDE).invoke("contains").arg(fieldName)
                                .not()
                                .cand(JExpr.ref(paramActual).invoke(method.getName()).ne(JExpr.ref(paramExpected).invoke(method.getName()))));
                        condition._then()
                                .add(JExpr.ref(EXPECTED_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(codeModel.ref(String.class).staticInvoke("valueOf").arg(JExpr.ref(paramExpected).invoke(method.getName()))))
                                .add(JExpr.ref(ACTUAL_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(codeModel.ref(String.class).staticInvoke("valueOf").arg(JExpr.ref(paramActual).invoke(method.getName()))));
                    } else {
                        createConditionBlockForExcludeList(block, paramActual, paramExpected, method, fieldName, equalMethod);
                    }
                }
                if (methodReturnType == List.class) {
                    countArraySymbol++;
                    classNameGenericReturnType = getClassGenericReturnType(method);
                    JType genericType = codeModel.ref(classNameGenericReturnType);
                    JForLoop jForLoop = block._for();
                    JVar ivar = jForLoop.init(codeModel.INT, counterSymbol[countArraySymbol], JExpr.lit(0));
                    jForLoop.test(ivar.lt(JExpr.ref(paramActual).invoke(method.getName()).invoke("size")));
                    jForLoop.update(ivar.incr());

                    if (isSimpleType(classNameGenericReturnType)) {
                        if (superMethod != null) {
                            genericType = codeModel.ref(superMethod.getReturnType());
                        } else {
                            genericType = codeModel.ref(method.getReturnType());
                        }
                        JConditional condition = jForLoop.body()._if(JExpr.ref(paramActual).invoke(method.getName()).invoke("get").arg(JExpr.ref(counterSymbol[countArraySymbol])).invoke("equals").arg(
                                JExpr.ref(paramExpected).invoke(method.getName()).invoke("get").arg(JExpr.ref(counterSymbol[countArraySymbol]))
                        ).not());
                        if (superMethod != null) {
                            String explain = " in row ";
                            condition._then().add(
                                    JExpr.ref(EXPECTED_DATA).invoke("put").arg(JExpr.lit(explain).plus(JExpr.ref(counterSymbol[countArraySymbol])).plus(JExpr.ref(counterSymbol[countArraySymbol - 1]))).arg(JExpr.ref(EXPECTED_ROW).invoke(superMethod.getName()).invoke(method.getName()).invoke("toString"))).add(
                                    JExpr.ref(ACTUAL_DATA).invoke("put").arg(JExpr.lit(explain).plus(JExpr.ref(counterSymbol[countArraySymbol])).plus(JExpr.ref(counterSymbol[countArraySymbol - 1]))).arg(JExpr.ref(ACTUAL_ROW).invoke(superMethod.getName()).invoke(method.getName()).invoke("toString"))
                            );
                        }

                    } else {
                        String actualParamLocal = null;
                        String expectedParamLocal = null;
                        String varWithoutActual = paramActual.substring(6);
                        String expectedResp = "expected" + varWithoutActual;
                        if (c == 0) {
                            actualParamLocal = ACTUAL_ROW;
                            expectedParamLocal = EXPECTED_ROW;
                            c++;
                        } else {
                            actualParamLocal = paramActual + c;
                            expectedParamLocal = paramExpected + c;
                        }
                        jForLoop.body().decl(genericType, actualParamLocal, JExpr.ref(paramActual).invoke(method.getName()).invoke("get").arg(JExpr.ref(counterSymbol[countArraySymbol])));
                        jForLoop.body().decl(genericType, expectedParamLocal, JExpr.ref(expectedResp).invoke(method.getName()).invoke("get").arg(JExpr.ref(counterSymbol[countArraySymbol])));
                        mainBody(classNameGenericReturnType, actualParamLocal, expectedParamLocal, jForLoop.body(), null);
                    }
                    countArraySymbol--;
                } else if (methodReturnType != List.class && !isSimpleType(methodReturnType)) {
                    JType genericType = codeModel.ref(methodReturnType);
                    fieldName = method.getName().substring(3);
                    fieldName = makeFirstLetterLowerCase(fieldName);
                    JConditional condition = block._if(JExpr.ref(LIST_TO_EXCLUDE).invoke("contains").arg(fieldName).not());
                    condition._then().decl(genericType, paramActual + fieldName, JExpr.ref(paramActual).invoke(method.getName()));
                    condition._then().decl(genericType, paramExpected + fieldName, JExpr.ref(paramExpected).invoke(method.getName()));
                    mainBody(methodReturnType, paramActual + fieldName, paramExpected + fieldName, condition._then(), method);
                }
            }
        }
    }

    private void createConditionBlockForExcludeList(JBlock block, String paramActual, String paramExpected, Method method, String fieldName, String equalMethod) {
        JConditional firstConditional = block._if(JExpr.ref(LIST_TO_EXCLUDE).invoke("contains").arg(fieldName).not());
        JConditional secondConditional = firstConditional._then()._if(JExpr.ref(paramActual).invoke(method.getName()).ne(JExpr.ref("null")));
        JConditional thirdConditional = secondConditional._then()._if(JExpr.ref(paramExpected).invoke(method.getName()).ne(JExpr.ref("null")));
        JConditional fourthConditional = thirdConditional._then()._if(JExpr.ref(paramActual).invoke(method.getName()).invoke(equalMethod).arg(JExpr.ref(paramExpected).invoke(method.getName())).ne(JExpr.ref("0")));
        fourthConditional
                ._then()
                .add(JExpr.ref(EXPECTED_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(JExpr.ref(paramExpected).invoke(method.getName()).invoke("toString")))
                .add(JExpr.ref(ACTUAL_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(JExpr.ref(paramActual).invoke(method.getName()).invoke("toString")));
        thirdConditional._else()
                .add(JExpr.ref(EXPECTED_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg("Null"))
                .add(JExpr.ref(ACTUAL_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(JExpr.ref(paramActual).invoke(method.getName()).invoke("toString")));
        secondConditional._else().block()._if(JExpr.ref(paramExpected).invoke(method.getName()).ne(JExpr.ref("null")))
                ._then()
                .add(JExpr.ref(EXPECTED_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg(JExpr.ref(paramExpected).invoke(method.getName()).invoke("toString")))
                .add(JExpr.ref(ACTUAL_DATA).invoke("put").arg(createMsgForEqualsCondition(method.getName())).arg("Null"));
    }

    private boolean isGetMethod(Method method) {
        return method.getName().startsWith("get")
                && (method.getParameterTypes()).length == 0
                && (!(method.getName().equals("getClass")));
    }

    private String makeFirstLetterLowerCase(String letter) {
        char c[] = letter.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    private String makeFirstLetterUpperCase(String letter) {
        char c[] = letter.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    private Class<?> getClassGenericReturnType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        Class<?> fieldGenericClass = null;
        if (genericReturnType instanceof ParameterizedType) {
            fieldGenericClass = (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        }
        return fieldGenericClass;
    }

    private boolean isSimpleType(Class<?> type) {
        return ClassUtils.isPrimitiveOrWrapper(type)
                || type.equals(String.class)
                || type.isEnum()
                || type.equals(XMLGregorianCalendar.class)
                || type.equals(BigInteger.class)
                || type.equals(BigDecimal.class);
    }

    private boolean isXMLGregorianCalendar(Class<?> type) {
        return type.equals(XMLGregorianCalendar.class);
    }

    public void createDescribeToMethod(Class<?> type) {
        JMethod describeToMethod = definedClass.method(JMod.PUBLIC, Void.TYPE, "describeTo");
        String description = "description";
        JClass refDescription = codeModel.ref(Description.class);
        describeToMethod.param(refDescription, description);
        describeToMethod.annotate(Override.class);
        JBlock blockDescribeTo = describeToMethod.body();
        String[] strings = StringUtils.splitByCharacterTypeCamelCase(type.getSimpleName());
        String name = new String();
        for (String msg : strings) {
            name += msg + " ";
        }
        blockDescribeTo.add(JExpr.ref(description).invoke("appendText").arg(name));
    }


    public void createDescribeMismatchSafely(Class<?> type) {
        JMethod describeToMethod = definedClass.method(JMod.PUBLIC, Void.TYPE, "describeMismatchSafely");
        String paramMatchesSafely = "item";
        String description = "mismatchDescription";
        JClass refDescription = codeModel.ref(Description.class);
        describeToMethod.param(type, paramMatchesSafely);
        describeToMethod.param(refDescription, description);
        describeToMethod.annotate(Override.class);
        JBlock blockDescribeTo = describeToMethod.body();
        blockDescribeTo.add(JExpr.ref(description).invoke("appendText").arg("\n"));
        JClass modelRawClass = codeModel.ref(Set.class);
        JClass modelNarrowedSet = modelRawClass.narrow(String.class);
        JVar decl = blockDescribeTo.decl(modelNarrowedSet, "expectedDataSet", JExpr.ref("expectedData").invoke("keySet"));
        JForEach jForEach = blockDescribeTo.forEach(codeModel.ref(String.class), "expectedParameter", decl);
        jForEach.body().add(JExpr.ref(description)
                        .invoke("appendText").arg("\t\t")
                        .invoke("appendText").arg("for parameter ")
                        .invoke("appendValue").arg(JExpr.ref("expectedParameter"))
                        .invoke("appendText").arg(" - expected ")
                        .invoke("appendValue").arg(JExpr.ref("expectedData").invoke("get").arg(JExpr.ref("expectedParameter")))
                        .invoke("appendText").arg(", but was ")
                        .invoke("appendValue").arg(JExpr.ref("actualData").invoke("get").arg(JExpr.ref("expectedParameter")))
                        .invoke("appendText").arg("\n")
        );
    }
}

