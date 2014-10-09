package net.thucydides.maven.plugin.saop2bdd;

import com.sun.codemodel.*;
import org.apache.commons.lang3.ClassUtils;
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


public class GenerateSimilarToClass {
    public static final String CUSTOM_MATCHER_SIMILAR_TO_CLASS_NAME = "SimilarTo";
    private JCodeModel codeModel;
    private JDefinedClass definedClass;
    private JClass rawArrayListClass;
    private JClass arrayListClazz;
    private JFieldVar excludeListField;
    private JClass expectedResponseClazz;
    private JFieldVar expectedResponseField;
    private String expecterResponse = "expectedResponse";
    private JMethod matchesSafelyMethod;
    private String[] counterSymbol = new String[]{"o", "i", "j", "k", "m", "n"};
    private int countArraySymbol = 0;
    int c = 0;


    public JClass createGenerateSimilarToClazz(JCodeModel codeModel, Class<?> type, String nameClazz, String packageName) {
        try {
            String originalNameClass = getOrifinalName(nameClazz);
            initModel(type, codeModel, packageName, originalNameClass);
        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }
        createConstructorSimilarToClazz(type);
        createExcludeListField();
        createDataField("actualData");
        createDataField("expectedData");
        createExpectedResponseFiled(type);
        createExcludeMethod(arrayListClazz, excludeListField);
        createSimilarToMethod(type);
        createMatchesSafelyMethod(type);
        createDescribeMismatchSafely(type);
        createDescribeToMethod();
        return definedClass;

    }

    private String getOrifinalName(String nameClazz) {
        int beginOriginName = nameClazz.indexOf("get");
        int endOriginName = nameClazz.lastIndexOf("ResponseKey") > nameClazz.lastIndexOf("Key") ? nameClazz.lastIndexOf("ResponseKey") : nameClazz.lastIndexOf("Key");
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
        expectedResponseField = definedClass.field(JMod.PRIVATE, type, "expectedResponse");
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

    public void createDeclarationDataField(String fieldName, Class<?> dataTypeClass) {
        JClass dataDeclarationClazz = codeModel.ref(dataTypeClass);
        JFieldVar dataField = definedClass.field(JMod.PRIVATE, dataDeclarationClazz, fieldName);
//        dataField.init(newInstance);
    }

    public void createInstantiationDataField(String name) {
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
        //Create  JClass Model List
        //Parametrize class to string
        //Create excludeListField List<String> listToExclude
        rawArrayListClass = codeModel.ref(ArrayList.class);
        arrayListClazz = rawArrayListClass.narrow(String.class);
        excludeListField = definedClass.field(JMod.PRIVATE, arrayListClazz, "listToExclude");

    }

    public void initModel(Class<?> type, JCodeModel model, String packageName, String originalNameClass) throws JClassAlreadyExistsException {
        this.codeModel = model;
        JClass refTypeSafeMatcher = codeModel.ref(TypeSafeMatcher.class);
        JClass similarToListClazzWithGeneric = refTypeSafeMatcher.narrow(type);
        System.out.println("serviceStepsRawClass = " + packageName);
        String responseClassName = type.getName();
        System.out.println("responseClassName = " + responseClassName);
        definedClass = codeModel._class(packageName + "." + originalNameClass + CUSTOM_MATCHER_SIMILAR_TO_CLASS_NAME)._extends(similarToListClazzWithGeneric);

    }

    public void createExcludeMethod(JClass arrayListClazz, JFieldVar fieldExcludeList) {
        JMethod excludeMethod = definedClass.method(JMod.PUBLIC, definedClass, "exclude");
        String paramEcludeMethod = "listToExclude";
        excludeMethod.param(arrayListClazz, paramEcludeMethod);
        JBlock blockEclude = excludeMethod.body();
        excludeMethod.body().assign(JExpr._this().ref(fieldExcludeList), JExpr.ref(paramEcludeMethod));
        blockEclude._return(JExpr._this());
    }

    public void createSimilarToMethod(Class<?> type) {
        JMethod similarToMethod = definedClass.method(JMod.PUBLIC | JMod.STATIC, definedClass, "similarTo");
        String paramSimilarToMethod = "expectedResponse";
        similarToMethod.param(type, paramSimilarToMethod);
        similarToMethod.annotate(Factory.class);
        JBlock blockSimilarTo = similarToMethod.body();
        blockSimilarTo._return(JExpr._new(definedClass).arg(JExpr.ref(paramSimilarToMethod)));
    }

    public void createConstructorSimilarToClazz(Class<?> type) {
        JMethod constructorMethod = definedClass.constructor(JMod.PUBLIC);
        String expectedResponse = "expectedResponse";
        constructorMethod.param(type, expectedResponse);
        JBlock block = constructorMethod.body();
        block.assign(JExpr._this().ref("expectedResponse"), JExpr.ref(expectedResponse));
    }

    public void createMatchesSafelyMethod(Class<?> type) {
        matchesSafelyMethod = definedClass.method(JMod.PUBLIC, Boolean.TYPE, "matchesSafely");
        String paramMatchesSafely = "actualResponse";
        matchesSafelyMethod.param(type, paramMatchesSafely);
        matchesSafelyMethod.annotate(Override.class);
        JBlock blockMatchesSafely = matchesSafelyMethod.body();
        mainBody(type, paramMatchesSafely, blockMatchesSafely, null);
        blockMatchesSafely._return(JExpr.ref("expectedData").invoke("isEmpty").cand(JExpr.ref("actualData").invoke("isEmpty")));
    }


    private void mainBody(Class<?> type, String paramMatchesSafely, JBlock block, Method superMethod) {
        Class<?> classNameGenericReturnType = null;
        Method[] methods = type.getMethods();
        System.out.println("methods.length = " + methods.length + " = " + Arrays.toString(methods));
        for (Method method : methods) {
            String fieldName = null;
            System.out.println("method name = " + method.getName());
            if (isGoodMethod(method)) {
                Class<?> methodReturnType = method.getReturnType();
                if (isSimpleType(methodReturnType)) {
                    if (methodReturnType.isEnum()) {
                        String expectedName = "expectedRow";
                        JConditional condition = block._if(JExpr.ref(paramMatchesSafely).invoke(method.getName()).invoke("equals").arg(
                                JExpr.ref(expectedName).invoke(superMethod.getName()).invoke(method.getName())));
                        String explain = fieldName + " in row ";
                        condition._then().add(
                                JExpr.ref("expectedData").invoke("put").arg(JExpr.lit(explain).plus(JExpr.ref(counterSymbol[countArraySymbol]))).arg(JExpr.ref("expectedRow").invoke(superMethod.getName()).invoke(method.getName()).invoke("toString"))).add(
                                JExpr.ref("actualData").invoke("put").arg(JExpr.lit(explain).plus(JExpr.ref(counterSymbol[countArraySymbol]))).arg(JExpr.ref("actualRow").invoke(superMethod.getName()).invoke(method.getName()).invoke("toString"))
                        );
                        continue;
                    }
                    System.out.println(" begin isSimpleType methodReturnType = " + methodReturnType + " method name = " + method.getName());
//                    String fieldName = method.getName().substring(3);
                    fieldName = method.getName().substring(3);
                    String equalMethod = "compareTo";
                    System.out.println("TYPE = " + type);
                    if (isXMLGregorianCalendar(methodReturnType)) {
                        equalMethod = "compare";
                    }
                    JConditional condition = block._if(JExpr.ref("listToExclude").invoke("contains").arg(fieldName).not()
                            .cand(JExpr.ref("actualRow").invoke(method.getName()).invoke(equalMethod).arg(JExpr.ref("expectedRow").invoke(method.getName())).ne(JExpr.lit(0))));
                    String explain = fieldName + " in row ";
                    condition._then().add(
                            JExpr.ref("expectedData").invoke("put").arg(JExpr.lit(explain).plus(JExpr.ref(counterSymbol[countArraySymbol]))).arg(JExpr.ref("expectedRow").invoke(method.getName()).invoke("toString"))).add(
                            JExpr.ref("actualData").invoke("put").arg(JExpr.lit(explain).plus(JExpr.ref(counterSymbol[countArraySymbol]))).arg(JExpr.ref("actualRow").invoke(method.getName()).invoke("toString"))
                    );
                    System.out.println("End isSimpleType");
                }
                if (methodReturnType == List.class) {
                    countArraySymbol++;
                    System.out.println("Start inside List ");
                    classNameGenericReturnType = getClassGenericReturnType(method);
                    System.out.println("if(List.class) methodReturnType =" + methodReturnType);
                    JType genericType = codeModel.ref(classNameGenericReturnType);

                    if (isSimpleType(classNameGenericReturnType)) {
                        System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                        System.out.println("classNameGenericReturnType = " + classNameGenericReturnType);
                        genericType = codeModel.ref(superMethod.getReturnType());
//---------------------------------------------------------------------------------------------------------------------------------------------------------
                        JForLoop jForLoop = block._for();
                        JVar ivar = jForLoop.init(codeModel.INT, counterSymbol[countArraySymbol], JExpr.lit(0));
                        jForLoop.test(ivar.lt(JExpr.ref(paramMatchesSafely).invoke(method.getName()).invoke("size")));
                        jForLoop.update(ivar.incr());
                        String expectedName = "expectedRow" + genericType.name();
                        jForLoop.body().decl(genericType, expectedName, JExpr.ref("expectedRow").invoke(superMethod.getName()));
                        JConditional condition = jForLoop.body()._if(JExpr.ref(paramMatchesSafely).invoke("getValue").invoke("get").arg(JExpr.ref(counterSymbol[countArraySymbol])).invoke("equals").arg(
                                JExpr.ref(expectedName).invoke("getValue").invoke("get").arg(JExpr.ref(counterSymbol[countArraySymbol]))
                        ).not());
                        String explain = fieldName + " in row ";
                        condition._then().add(
                                JExpr.ref("expectedData").invoke("put").arg(JExpr.lit(explain).plus(JExpr.ref(counterSymbol[countArraySymbol])).plus(JExpr.ref(counterSymbol[countArraySymbol - 1]))).arg(JExpr.ref("expectedRow").invoke(superMethod.getName()).invoke(method.getName()).invoke("toString"))).add(
                                JExpr.ref("actualData").invoke("put").arg(JExpr.lit(explain).plus(JExpr.ref(counterSymbol[countArraySymbol])).plus(JExpr.ref(counterSymbol[countArraySymbol - 1]))).arg(JExpr.ref("actualRow").invoke(superMethod.getName()).invoke(method.getName()).invoke("toString"))
                        );
//---------------------------------------------------------------------------------------------------------------------------------------------------------
                    } else {
                        JForLoop jForLoop = block._for();
                        JVar ivar = jForLoop.init(codeModel.INT, counterSymbol[countArraySymbol], JExpr.lit(0));
                        jForLoop.test(ivar.lt(JExpr.ref(paramMatchesSafely).invoke(method.getName()).invoke("size")));
                        jForLoop.update(ivar.incr());
                        if (c == 0) {
                            c++;
                            jForLoop.body().decl(genericType, "actualRow", JExpr.ref(paramMatchesSafely).invoke(method.getName()).invoke("get").arg(JExpr.ref(counterSymbol[countArraySymbol])));
                            System.out.println("NEW METHOD TYPE  = " + method.getName());
                            jForLoop.body().decl(genericType, "expectedRow", JExpr.ref(expecterResponse).invoke(method.getName()).invoke("get").arg(JExpr.ref(counterSymbol[countArraySymbol])));
                        }
                        System.out.println("before mainbody");
                        mainBody(classNameGenericReturnType, "sd", jForLoop.body(), null);
                        System.out.println("after mainbody");
                        System.out.println("End inside List");

                    }
                    countArraySymbol--;
                } else if (methodReturnType != List.class && !isSimpleType(methodReturnType)) {
                    System.out.println("Inside Else");
                    JType genericType = codeModel.ref(methodReturnType);
                    System.out.println("----------------------------------");
                    System.out.println("Inside Else method.getName = " + method.getName());
                    System.out.println("Inside Else genericType = " + genericType);
                    System.out.println("Inside Else methodReturnType = " + methodReturnType);
                    System.out.println("----------------------------------");
                    System.out.println("methodReturnType = " + methodReturnType);
                    System.out.println(" begin isSimpleType methodReturnType = " + methodReturnType + "method name = " + method.getName());
                    fieldName = method.getName().substring(3);
                    JConditional condition = block._if(JExpr.ref("listToExclude").invoke("contains").arg(fieldName).not());
                    String localVar = "actualRow";
                    if (c == 0) {
                        localVar = "actualResponse";
                    }
                    condition._then().decl(genericType, localVar + fieldName, JExpr.ref(localVar).invoke(method.getName()));
                    condition._then().decl(genericType, localVar + fieldName, JExpr.ref(localVar).invoke(method.getName()));
                    System.out.println("End isSimpleType");
                    mainBody(methodReturnType, localVar + fieldName, condition._then(), method);
                }
            }
        }
    }

    private boolean isGoodMethod(Method method) {
        return method.getName().startsWith("get")
                && (method.getParameterTypes()).length == 0
                && (!(method.getName().equals("getClass")));
    }

   /* public static void writeIfElse(JCodeModel codeModel, JDefinedClass c) {
        JMethod method = c.method(JMod.PUBLIC, codeModel.VOID, "testIf");
        JVar input = method.param(codeModel.INT, "input");
        JBlock body = method.body();
        JConditional condition = body._if(input.lt(JExpr.lit(42)));
        condition._then().add(
                codeModel.ref(System.class).staticRef("out").invoke("println").arg(JExpr.lit("hello")));
        condition._else().add(
                codeModel.ref(System.class).staticRef("out").invoke("println").arg(JExpr.lit("world")));
    }*/

   /* public static void writeForLoop(JCodeModel codeModel, JDefinedClass c) {
        JMethod method = c.method(JMod.PUBLIC, codeModel.VOID, "testFor");
        JVar input = method.param(int.class, "input");
        JBlock body = method.body();
        JForLoop forLoop = body._for();
        JVar ivar = forLoop.init(codeModel.INT, "i", JExpr.lit(0));
        forLoop.test(ivar.lt(JExpr.lit(42)));
        forLoop.update(ivar.assignPlus(JExpr.lit(1)));
        forLoop.body().add(
                codeModel.ref(System.class).staticRef("out").invoke("println").arg(ivar));
    }
*/
   /* public void getMethod(){
        Method[] methods1 = classNameGenericReturnType.getMethods();
        for (Method method2 : methods1) {
            if (method2.getName().startsWith("get")
                    && (method2.getParameterTypes()).length == 0
                    && (!(method2.getName().equals("getClass")))) {
                System.out.println("method2 = " + method2);
            }
        }
    }*/

    private Class<?> getClassGenericReturnType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        Class<?> fieldGenericClass = null;
        if (genericReturnType instanceof ParameterizedType) {
            fieldGenericClass = (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        }
        return fieldGenericClass;
    }

    /*private JForLoop createLoopFor(String paramMatchesSafely, JBlock blockMatchesSafely, Method method) {
        JForLoop forLoop = blockMatchesSafely._for();
        JVar ivar = forLoop.init(codeModel.INT, "i", JExpr.lit(0));
        forLoop.test(ivar.lt(JExpr.ref(paramMatchesSafely).invoke(method.getName()).invoke("size")));
        forLoop.update(ivar.incr());
        return forLoop;
    }*/

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

    private void bodyMatcherSafe(Class<?> type) {
        Class clazz = type;
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (isGoodMethod(method)) {
            }
        }
    }

    public void createDescribeToMethod() {
        JMethod describeToMethod = definedClass.method(JMod.PUBLIC, Void.TYPE, "describeTo");
        String description = "description";
        JClass refDescription = codeModel.ref(Description.class);
        describeToMethod.param(refDescription, description);
        describeToMethod.annotate(Override.class);
        JBlock blockDescribeTo = describeToMethod.body();
        blockDescribeTo.add(JExpr.ref(description).invoke("appendText").arg("get bill history response with parameters"));
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

