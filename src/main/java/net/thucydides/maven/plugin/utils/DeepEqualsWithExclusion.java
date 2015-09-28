package net.thucydides.maven.plugin.utils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class DeepEqualsWithExclusion {
    private int row = 0;
    private String fieldName = null;
    private List<String> listToExclude;
    private Map<String, String> expectedData;
    private Map<String, String> actualData;
    private Class<?> genericReturnType;

    public DeepEqualsWithExclusion(List<String> listToExclude, Map<String, String> expectedData, Map<String, String> actualData) {
        this.listToExclude = listToExclude;
        this.expectedData = expectedData;
        this.actualData = actualData;
    }

    public <T> void deepEqual(Method currentMethod, T expectedResponse, T actualResponse) {
        Class<?> classType = null;
        try {
            classType = initCurrentClassType(currentMethod, expectedResponse);

            if (filterBySimple(classType)) {
                checkSimpleType(currentMethod, expectedResponse, actualResponse);
            }
            if (filterByList(classType)) {
                checkListType(currentMethod, expectedResponse, actualResponse);
            }
            if (filterByComplex(classType)) {
                checkComplexType(expectedResponse, actualResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> Class<?> initCurrentClassType(Method currentMethod, T expectedResponse) {
        Class<?> clazz = currentMethod != null ? currentMethod.getReturnType() : expectedResponse.getClass();
        return clazz;
    }

    private <T> void checkSimpleType(Method currentMethod, T expectedResponse, T actualResponse) {
        Class<?> type = currentMethod.getReturnType();

        if (isBoolean(type)) {
            compareObjects(currentMethod, expectedResponse, actualResponse);
        }
        if (isEnumType(type)) {
            compareObjects(currentMethod, expectedResponse, actualResponse);
        }
        if (isXMLGregorianCalendarType(type)) {
            compareObjects(currentMethod, expectedResponse, actualResponse);
        }
        if (isPrimitiveType(type) || isStringType(type)) {
            compareObjects(currentMethod, expectedResponse, actualResponse);
        }
        if (isBigDecimalType(type) || isBigIntegerType(type) || isDouble(type)) {
            compareNumber(currentMethod, expectedResponse, actualResponse);
        }
    }

    private <T> void compareNumber(Method currentMethod, T expectedResponse, T actualResponse) {
        fieldName = getFieldName(currentMethod);
        if (!listToExclude.contains(fieldName)) {
            try {
                Comparable execActual = (Comparable) currentMethod.invoke(actualResponse);
                Comparable execExpected = (Comparable) currentMethod.invoke(expectedResponse);

                if (execActual != null && execExpected != null) {
                    if (execActual.compareTo(execExpected) != 0) {
                        writeAssertMessage(currentMethod, row, execActual, execExpected);
                    }
                }
                if (execActual == null ^ execExpected == null) {
                    writeAssertMessage(currentMethod, row, execActual, execExpected);
                }
            } catch (Exception e) {
                System.err.println("Exception while running comparePrimitive, " + e.getMessage());
            }
        }
    }

    private <T> void compareObjects(Method currentMethod, T expectedResponse, T actualResponse) {
        fieldName = getFieldName(currentMethod);
        if (!listToExclude.contains(fieldName)) {
            try {
                Object execActual = currentMethod.invoke(actualResponse);
                Object execExpected = currentMethod.invoke(expectedResponse);

                if (execActual != null && execExpected != null) {
                    if (!execActual.equals(execExpected)) {
                        writeAssertMessage(currentMethod, row, execActual, execExpected);
                    }
                }
                if (execActual == null ^ execExpected == null) {
                    writeAssertMessage(currentMethod, row, execActual, execExpected);
                }
            } catch (Exception e) {
                System.err.println("Exception while running comparePrimitive, " + e.getMessage());
            }
        }
    }

    private <T> void checkListType(Method currentMethod, T expectedResponse, T actualResponse) {
        fieldName = getFieldName(currentMethod);
        if (!listToExclude.contains(fieldName)) {
            try {
                List actList = (List) currentMethod.invoke(actualResponse);
                List expList = (List) currentMethod.invoke(expectedResponse);

                Comparator c = new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return CompareToBuilder.reflectionCompare(o1, o2);
                    }
                };

                Collections.sort(actList, c);
                Collections.sort(expList, c);

                if (actList.isEmpty() && expList.isEmpty()) {
                    return;
                }
                if (actList.size() != expList.size()) {
                    expectedData.put(getFieldName(currentMethod) + " size ", String.valueOf(expList.size()));
                    actualData.put(getFieldName(currentMethod) + " size ", String.valueOf(actList.size()));
                    return;
                }

                for (int i = 0; i < actList.size(); i++) {
                    genericReturnType = getClassGenericReturnType(currentMethod);
                    if (isSimpleType(genericReturnType)) {
                        if (!actList.get(i).equals(expList.get(i))) {
                            String explain = " in List row ";
                            expectedData.put((getFieldName(currentMethod) + explain + row) + " ", String.valueOf(expList.get(i)));
                            actualData.put((getFieldName(currentMethod) + explain + row) + " ", String.valueOf(actList.get(i)));
                        }
                    } else {
                        Object expected = expList.get(i);
                        Object actual = actList.get(i);
                        deepEqual(null, expected, actual);
                        row++;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Exception while running checkListType " + ex.getMessage());
            }
        }
    }

    private <T> void checkComplexType(T expectedResponse, T actualResponse) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = expectedResponse.getClass().getMethods();
        for (Method currentMethod : methods) {
            if (isGetter(currentMethod) || isBoolean(currentMethod)) {
                fieldName = getFieldName(currentMethod);
                if (!listToExclude.contains(fieldName)) {
                    Class<?> methodReturnType = currentMethod.getReturnType();
                    if (isSimpleType(methodReturnType) || filterByList(methodReturnType)) {
                        deepEqual(currentMethod, expectedResponse, actualResponse);
                    }
                    if (filterByComplex(methodReturnType)) {
                        if (isGetter(currentMethod) || isBoolean(currentMethod)) {
                            Object expectedInvokeObject = currentMethod.invoke(expectedResponse);
                            Object actualInvokeObject = currentMethod.invoke(actualResponse);
                            deepEqual(null, expectedInvokeObject, actualInvokeObject);
                        }
                    }
                }
            }
        }
    }

    private String getFieldName(Method method) {
        if (method == null) return "Null";
        if (method.getName().startsWith("is")) return makeFirstLetterToLowerCase(method.getName().substring(2));
        return makeFirstLetterToLowerCase(method.getName().substring(3));
    }

    private void writeAssertMessage(Method method, int row, Object actualValue, Object expectValue) {
        String simpleName = method.getReturnType().getSimpleName();
        String msg = getFieldName(method) + " in " + simpleName + " row " + row;
        expectedData.put(msg, String.valueOf(expectValue));
        actualData.put(msg, String.valueOf(actualValue));
    }

    private Class<?> getClassGenericReturnType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        Class<?> fieldGenericClass = null;
        if (genericReturnType instanceof ParameterizedType) {
            fieldGenericClass = (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        }
        return fieldGenericClass;
    }

    private String makeFirstLetterToLowerCase(String letter) {
        char c[] = letter.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    private boolean filterByComplex(Class<?> classType) {
        return !(filterByList(classType) || filterBySimple(classType));
    }

    private boolean filterByList(Class<?> methodReturnType) {
        return methodReturnType == List.class;
    }

    private boolean filterBySimple(Class<?> classType) {
        return isSimpleType(classType);
    }

    private boolean filterByVoid(Class<?> classType) {
        return isVoidType(classType);
    }

    private boolean isSimpleType(Class<?> type) {
        return ClassUtils.isPrimitiveOrWrapper(type)
                || type.equals(String.class)
                || type.isEnum()
                || type.equals(XMLGregorianCalendar.class)
                || type.equals(BigInteger.class)
                || type.equals(BigDecimal.class)
                || type.equals(Double.class);
    }

    private boolean isEnumType(Class<?> returnType) {
        return returnType.isEnum();
    }

    private boolean isXMLGregorianCalendarType(Class<?> returnType) {
        return returnType.equals(XMLGregorianCalendar.class);
    }

    private boolean isPrimitiveType(Class<?> returnType) {
        return returnType.isPrimitive();
    }

    private boolean isStringType(Class<?> returnType) {
        return returnType == String.class;
    }

    private boolean isBigDecimalType(Class<?> returnType) {
        return returnType == BigDecimal.class;
    }

    private boolean isBigIntegerType(Class<?> returnType) {
        return returnType == BigInteger.class;
    }

    private boolean isVoidType(Class<?> returnType) {
        return returnType.getName().equals(void.class.getName());
    }

    private boolean isDouble(Class<?> returnType) {
        return returnType == Double.class;
    }

    private boolean isBoolean(Class<?> returnType) {
        return returnType == Boolean.class;
    }

    private boolean isGetter(Method method) {
        if (!method.getName().startsWith("get")) return false;
        if (method.getParameterTypes().length != 0) return false;
        if (void.class.equals(method.getReturnType())) return false;
        if (Class.class.equals(method.getReturnType())) return false;
        return true;
    }

    private boolean isSetter(Method method) {
        if (!method.getName().startsWith("set")) return false;
        if (method.getParameterTypes().length != 1) return false;
        return true;
    }

    private boolean isBoolean(Method method) {
        return method.getName().startsWith("is")
                && (method.getParameterTypes()).length == 0
                && (!(method.getName().equals("getClass")));
    }
}
