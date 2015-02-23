package com.engagepoint.acceptancetest.utils;

import org.apache.commons.lang3.ClassUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


public class DeepEqualsWithExclusion {

    private String[] counterSymbol = new String[]{"o", "i", "j", "k", "m", "n"};
    private int countArraySymbol = 0;
    private String fieldName = null;
    private List<String> listToExclude;
    private Map<String, String> expectedData;
    private Map<String, String> actualData;
    private Class<?> classNameGenericReturnType;
    private int counter = 0;

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
        Class<?> classType;
        if (currentMethod != null) {
            return classType = currentMethod.getReturnType();
        } else {
            return classType = expectedResponse.getClass();
        }
    }

    private <T> void checkSimpleType(Method currentMethod, T expectedResponse, T actualResponse) {
        Class<?> type = currentMethod.getReturnType();
        if (isEnumType(type)) {
            compareEnum(currentMethod, expectedResponse, actualResponse);
        }
        if (isXMLGregorianCalendarType(type)) {
            compareXMLGregorianCalendar(currentMethod, expectedResponse, actualResponse);
        }
        if (isPrimitiveType(type) || isStringType(type)) {
            comparePrimitive(currentMethod, expectedResponse, actualResponse);
        }
        if (isBigDecimalType(type)) {
            compareBigDecimal(currentMethod, expectedResponse, actualResponse);
        }
        if (isBigIntegerType(type)) {
            compareBigInteger(currentMethod, expectedResponse, actualResponse);
        }
    }

    private <T> void comparePrimitive(Method currentMethod, T expectedResponse, T actualResponse) {
        fieldName = makeFirstLetterToLowerCase(currentMethod.getName().substring(3));
        if (!listToExclude.contains(fieldName)) {
            try {
                Object execActual = currentMethod.invoke(actualResponse);
                Object execExpected = currentMethod.invoke(expectedResponse);
                if (execActual != null) {
                    if (execExpected != null) {
                        if (!execActual.equals(execExpected)) {
                            expectedData.put(getFieldName(currentMethod), String.valueOf(execExpected));
                            actualData.put(getFieldName(currentMethod), String.valueOf(execActual));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception while running comparePrimitive, " + e.getMessage());
            }
        }
    }

    private <T> void compareBigDecimal(Method currentMethod, T expectedResponse, T actualResponse) {
        fieldName = makeFirstLetterToLowerCase(currentMethod.getName().substring(3));
        if (!listToExclude.contains(fieldName)) {
            try {
                BigDecimal execActual = (BigDecimal) currentMethod.invoke(actualResponse);
                BigDecimal execExpected = (BigDecimal) currentMethod.invoke(expectedResponse);
                if (execActual != null) {
                    if (execExpected != null) {
                        if (execActual.compareTo(execExpected) != 0) {
                            expectedData.put((getFieldName(currentMethod) + " in BigDecimal row " + countArraySymbol), String.valueOf(execExpected));
                            actualData.put((getFieldName(currentMethod) + " in BigDecimal row " + countArraySymbol), String.valueOf(execActual));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception while running comparePrimitive, " + e.getMessage());
            }
        }
    }

    private <T> void compareBigInteger(Method currentMethod, T expectedResponse, T actualResponse) {
        fieldName = makeFirstLetterToLowerCase(currentMethod.getName().substring(3));
        if (!listToExclude.contains(fieldName)) {
            try {
                BigInteger execActual = (BigInteger) currentMethod.invoke(actualResponse);
                BigInteger execExpected = (BigInteger) currentMethod.invoke(expectedResponse);
                if (execActual != null) {
                    if (execExpected != null) {
                        if (execActual.compareTo(execExpected) != 0) {
                            expectedData.put((getFieldName(currentMethod) + " in BigInteger row " + countArraySymbol), String.valueOf(execExpected));
                            actualData.put((getFieldName(currentMethod) + " in BigInteger row " + countArraySymbol), String.valueOf(execActual));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception while running comparePrimitive, " + e.getMessage());
            }
        }
    }

    private <T> void compareEnum(Method currentMethod, T expectedResponse, T actualResponse) {
        fieldName = makeFirstLetterToLowerCase(currentMethod.getName().substring(3));
        if (!listToExclude.contains(fieldName)) {
            try {
                if (currentMethod.invoke(actualResponse) != null) {
                    if (currentMethod.invoke(expectedResponse) != null) {
                        if (!currentMethod.invoke(actualResponse).equals(currentMethod.invoke(expectedResponse))) {
                            expectedData.put((getFieldName(currentMethod) + " in Enum row " + countArraySymbol), currentMethod.invoke(expectedResponse).toString());
                            actualData.put((getFieldName(currentMethod) + " in Enum row " + countArraySymbol), currentMethod.invoke(actualResponse).toString());
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception while running compareEnum, " + e.getMessage());
            }
        }
    }

    private <T> void compareXMLGregorianCalendar(Method currentMethod, T expectedResponse, T actualResponse) {
        fieldName = makeFirstLetterToLowerCase(currentMethod.getName().substring(3));
        if (!listToExclude.contains(fieldName)) {
            try {
                XMLGregorianCalendar execActual = (XMLGregorianCalendar) currentMethod.invoke(actualResponse);
                XMLGregorianCalendar execExpected = (XMLGregorianCalendar) currentMethod.invoke(expectedResponse);
                if (execActual != null && execExpected != null) {
                    if (!execActual.equals(execExpected)) {
                        expectedData.put((getFieldName(currentMethod) + " in XML row " + countArraySymbol), String.valueOf(execExpected));
                        actualData.put((getFieldName(currentMethod) + " in XML row " + countArraySymbol), String.valueOf(execActual));
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception while running compareXMLGregorianCalendar, " + e.getMessage());
            }
        }
    }

    private <T> void checkListType(Method currentMethod, T expectedResponse, T actualResponse) {

        fieldName = makeFirstLetterToLowerCase(currentMethod.getName().substring(3));
        if (!listToExclude.contains(fieldName)) {
            try {
                List actList = (List) currentMethod.invoke(actualResponse);
                List expList = (List) currentMethod.invoke(expectedResponse);

                if (actList.isEmpty() && expList.isEmpty()) {
                    return;
                }
                if (actList.size() != expList.size()) {
                    expectedData.put(getFieldName(currentMethod) + " size ", String.valueOf(expList.size()));
                    actualData.put(getFieldName(currentMethod) + " size ", String.valueOf(actList.size()));
                    return;
                }

                for (int i = 0; i < actList.size(); i++) {

                    classNameGenericReturnType = getClassGenericReturnType(currentMethod);
                    if (isSimpleType(classNameGenericReturnType)) {

                        if (!actList.get(i).equals(expList.get(i))) {
                            String explain = " in List row ";
                            expectedData.put((getFieldName(currentMethod) + explain + countArraySymbol) + " ", String.valueOf(expList.get(i)));
                            actualData.put((getFieldName(currentMethod) + explain + countArraySymbol) + " ", String.valueOf(actList.get(i)));
                        }
                    } else {
                        Object expected = expList.get(i);
                        Object actual = actList.get(i);
                        deepEqual(null, expected, actual);
                        countArraySymbol++;
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
                fieldName = makeFirstLetterToLowerCase(currentMethod.getName().substring(3));
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

     private String getFieldName(Method method){
         return makeFirstLetterToLowerCase(method.getName().substring(3));
     }

    private String createMsgForEqualsCondition(String name) {
        String exp = "";
        name = name.substring(3);
        if (countArraySymbol == 0) {
            exp = name + " ";
        }
        if (countArraySymbol == 1) {
            exp = name + " in row " + counterSymbol[countArraySymbol];
        }
        if (countArraySymbol == 2) {
            exp = name + " in row " + counterSymbol[1] + " " + counterSymbol[2];
        }
        if (countArraySymbol == 3) {
            exp = name + " in row " + counterSymbol[1] + " " + counterSymbol[2] + " " + counterSymbol[3];
        }
        return exp;
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
                || type.equals(BigDecimal.class);
    }

    private boolean isEnumType(Class<?> returnType) {
        return returnType.isEnum();
    }

    private boolean isXMLGregorianCalendarType(Class<?> type) {
        return type.equals(XMLGregorianCalendar.class);
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

    private boolean isVoidType(Class<?> type) {
        return type.getName().equals(void.class.getName());
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
