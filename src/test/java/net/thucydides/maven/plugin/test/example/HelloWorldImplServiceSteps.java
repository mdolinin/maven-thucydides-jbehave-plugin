
package net.thucydides.maven.plugin.test.example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import net.thucydides.core.Thucydides;
import org.hamcrest.Matchers;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;

public class HelloWorldImplServiceSteps {

    public HelloWorld helloWorldField = new HelloWorldImplService().getHelloWorldImplPort();

    @When("get hello world as string '$arg0Key' and save response to $stringKey")
    public void whenGetHelloWorldAsString(String arg0Key, String stringKey) {
        String arg0 = null;
        if (!arg0Key.isEmpty()) {
            arg0 = getVariableAsString(arg0Key);
        }
        String string = helloWorldField.getHelloWorldAsString(arg0);
        save(stringKey, string);
    }

    @Then("$actualStringKey string is equal to $expectedStringKey")
    public void thenString(String actualStringKey, String expectedStringKey) {
        String actualString = getVariableAsString(actualStringKey);
        String expectedString = getVariableAsString(expectedStringKey);
        Assert.assertEquals(actualString, expectedString);
    }

    @When("generate person instance '$parameterKey' and save response to $voidValueKey")
    public void whenGeneratePersonInstance(String parameterKey, String voidValueKey) {
        Person parameter = null;
        if (!parameterKey.isEmpty()) {
            parameter = getVariableValue(parameterKey);
        }
        helloWorldField.generatePersonInstance(parameter);
    }

    @Given("person saved to '$parameterKey - hello world impl service' with parameters: $parameters")
    public void givenPerson(String parameterKey, ExamplesTable parameters)
            throws DatatypeConfigurationException
    {
        Person parameter = new Person();
        if (parameters.getRows().size()!= 1) {
            throw new AssertionError("Wrong number of parameters.");
        }
        for (Map<String, String> row: parameters.getRows()) {
            if (!row.get("attribute1").isEmpty()) {
                String attribute1Value = row.get("attribute1");
                List<String> attribute1 = getVariableValue(attribute1Value);
                parameter.getAttribute1().addAll(attribute1);
            }
            if (!row.get("attribute2").isEmpty()) {
                String attribute2Value = row.get("attribute2");
                String attribute2 = getVariableAsString(attribute2Value);
                parameter.setAttribute2(attribute2);
            }
            if (!row.get("address").isEmpty()) {
                String addressValue = row.get("address");
                List<Address> address = getVariableValue(addressValue);
                parameter.getAddress().addAll(address);
            }
            if (!row.get("attribute4").isEmpty()) {
                String attribute4Value = row.get("attribute4");
                Address attribute4 = getVariableValue(attribute4Value);
                parameter.setAttribute4(attribute4);
            }
            if (!row.get("attribute5").isEmpty()) {
                String attribute5Value = row.get("attribute5");
                AccountingMethod attribute5 = getVariableAsAccountingMethod(attribute5Value);
                parameter.setAttribute5(attribute5);
            }
            if (!row.get("attribute6").isEmpty()) {
                String attribute6Value = row.get("attribute6");
                BigDecimal attribute6 = getVariableAsBigDecimal(attribute6Value);
                parameter.setAttribute6(attribute6);
            }
            if (!row.get("attribute7").isEmpty()) {
                String attribute7Value = row.get("attribute7");
                Boolean attribute7 = getVariableAsBoolean(attribute7Value);
                parameter.setAttribute7(attribute7);
            }
            if (!row.get("attribute8").isEmpty()) {
                String attribute8Value = row.get("attribute8");
                Double attribute8 = getVariableAsDouble(attribute8Value);
                parameter.setAttribute8(attribute8);
            }
            if (!row.get("attribute9").isEmpty()) {
                String attribute9Value = row.get("attribute9");
                Integer attribute9 = getVariableAsInteger(attribute9Value);
                parameter.setAttribute9(attribute9);
            }
            if (!row.get("attribute10").isEmpty()) {
                String attribute10Value = row.get("attribute10");
                Long attribute10 = getVariableAsLong(attribute10Value);
                parameter.setAttribute10(attribute10);
            }
            if (!row.get("attribute11").isEmpty()) {
                String attribute11Value = row.get("attribute11");
                BigInteger attribute11 = getVariableAsBigInteger(attribute11Value);
                parameter.setAttribute11(attribute11);
            }
            if (!row.get("attribute12").isEmpty()) {
                String attribute12Value = row.get("attribute12");
                XMLGregorianCalendar attribute12 = getVariableAsXMLGregorianCalendar(attribute12Value);
                parameter.setAttribute12(attribute12);
            }
            if (!row.get("attribute13").isEmpty()) {
                String attribute13Value = row.get("attribute13");
                Person attribute13 = getVariableValue(attribute13Value);
                parameter.setAttribute13(attribute13);
            }
            if (!row.get("attribute14").isEmpty()) {
                String attribute14Value = row.get("attribute14");
                List<Person> attribute14 = getVariableValue(attribute14Value);
                parameter.getAttribute14().addAll(attribute14);
            }
            if (!row.get("personParentId").isEmpty()) {
                String personParentIdValue = row.get("personParentId");
                Integer personParentId = getVariableAsInteger(personParentIdValue);
                parameter.setPersonParentId(personParentId);
            }
        }
        save(parameterKey, parameter);
    }

    @Given("list string $stringKeys and save to $attribute1Value - hello world impl service")
    public void givenListString(List<String> stringKeys, String attribute1Value) {
        List<String> attribute1List = new ArrayList<String>();
        for (String stringKey: stringKeys) {
            String string = getVariableAsString(stringKey);
            attribute1List.add(string);
        }
        save(attribute1Value, attribute1List);
    }

    @Given("address '$stringAttribute1Key' '$stringAttribute2Key' '$stringAttribute3Key' and save to $address - hello world impl service")
    public void givenAddress(String stringAttribute1Key, String stringAttribute2Key, String stringAttribute3Key, String address) {
        Address addressValue = new Address();
        if (!stringAttribute1Key.isEmpty()) {
            String stringAttribute1 = getVariableAsString(stringAttribute1Key);
            addressValue.setAttribute1(stringAttribute1);
        }
        if (!stringAttribute2Key.isEmpty()) {
            String stringAttribute2 = getVariableAsString(stringAttribute2Key);
            addressValue.setAttribute2(stringAttribute2);
        }
        if (!stringAttribute3Key.isEmpty()) {
            String stringAttribute3 = getVariableAsString(stringAttribute3Key);
            addressValue.setAttribute3(stringAttribute3);
        }
        save(address, addressValue);
    }

    @Given("list address $addressKeys and save to $addressValue - hello world impl service")
    public void givenListAddress(List<String> addressKeys, String addressValue) {
        List<Address> addressList = new ArrayList<Address>();
        for (String addressKey: addressKeys) {
            Address address = getVariableValue(addressKey);
            addressList.add(address);
        }
        save(addressValue, addressList);
    }

    private AccountingMethod getVariableAsAccountingMethod(String key) {
        try {
            return ((AccountingMethod) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return AccountingMethod.fromValue(key);
        }
    }

    @Given("x m l gregorian calendar and save to $attribute12 - hello world impl service")
    public void givenXMLGregorianCalendar(String attribute12)
            throws DatatypeConfigurationException
    {
        XMLGregorianCalendar attribute12Value = (DatatypeFactory.newInstance().newXMLGregorianCalendar());
        save(attribute12, attribute12Value);
    }

    @Given("list person $personKeys and save to $attribute14Value - hello world impl service")
    public void givenListPerson(List<String> personKeys, String attribute14Value) {
        List<Person> attribute14List = new ArrayList<Person>();
        for (String personKey: personKeys) {
            Person person = getVariableValue(personKey);
            attribute14List.add(person);
        }
        save(attribute14Value, attribute14List);
    }

    @When("generate big integer and save response to $bigIntegerKey")
    public void whenGenerateBigInteger(String bigIntegerKey) {
        BigInteger bigInteger = helloWorldField.generateBigInteger();
        save(bigIntegerKey, bigInteger);
    }

    @Then("$actualBigIntegerKey big integer is equal to $expectedBigIntegerKey")
    public void thenBigInteger(String actualBigIntegerKey, String expectedBigIntegerKey) {
        BigInteger actualBigInteger = getVariableAsBigInteger(actualBigIntegerKey);
        BigInteger expectedBigInteger = getVariableAsBigInteger(expectedBigIntegerKey);
        Assert.assertEquals(actualBigInteger, expectedBigInteger);
    }

    @When("generate big decimal '$valueKey' and save response to $bigDecimalKey")
    public void whenGenerateBigDecimal(String valueKey, String bigDecimalKey) {
        String value = null;
        if (!valueKey.isEmpty()) {
            value = getVariableAsString(valueKey);
        }
        BigDecimal bigDecimal = helloWorldField.generateBigDecimal(value);
        save(bigDecimalKey, bigDecimal);
    }

    @Then("$actualBigDecimalKey big decimal is equal to $expectedBigDecimalKey")
    public void thenBigDecimal(String actualBigDecimalKey, String expectedBigDecimalKey) {
        BigDecimal actualBigDecimal = getVariableAsBigDecimal(actualBigDecimalKey);
        BigDecimal expectedBigDecimal = getVariableAsBigDecimal(expectedBigDecimalKey);
        Assert.assertThat(actualBigDecimal, Matchers.comparesEqualTo
                (expectedBigDecimal));
    }

    @When("generate x m l gregorian calendar '$valueKey' and save response to $xMLGregorianCalendarKey")
    public void whenGenerateXMLGregorianCalendar(String valueKey, String xMLGregorianCalendarKey) {
        String value = null;
        if (!valueKey.isEmpty()) {
            value = getVariableAsString(valueKey);
        }
        XMLGregorianCalendar xMLGregorianCalendar = helloWorldField.generateXMLGregorianCalendar(value);
        save(xMLGregorianCalendarKey, xMLGregorianCalendar);
    }

    @Then("$actualXMLGregorianCalendarKey x m l gregorian calendar is equal to $expectedXMLGregorianCalendarKey")
    public void thenXMLGregorianCalendar(String actualXMLGregorianCalendarKey, String expectedXMLGregorianCalendarKey)
            throws DatatypeConfigurationException
    {
        XMLGregorianCalendar actualXMLGregorianCalendar = getVariableAsXMLGregorianCalendar(actualXMLGregorianCalendarKey);
        XMLGregorianCalendar expectedXMLGregorianCalendar = getVariableAsXMLGregorianCalendar(expectedXMLGregorianCalendarKey);
        Assert.assertEquals(actualXMLGregorianCalendar, expectedXMLGregorianCalendar);
    }

    @When("generate integer1 and save response to $integerKey")
    public void whenGenerateInteger1(String integerKey) {
        Integer integer = helloWorldField.generateInteger1();
        save(integerKey, integer);
    }

    @Then("$actualIntegerKey integer is equal to $expectedIntegerKey")
    public void thenInteger(String actualIntegerKey, String expectedIntegerKey) {
        Integer actualInteger = getVariableAsInteger(actualIntegerKey);
        Integer expectedInteger = getVariableAsInteger(expectedIntegerKey);
        Assert.assertEquals(actualInteger, expectedInteger);
    }

    @When("generate integer2 and save response to $integerKey")
    public void whenGenerateInteger2(String integerKey) {
        Integer integer = helloWorldField.generateInteger2();
        save(integerKey, integer);
    }

    private void save(String toPutTypeNameKey, Object toPutTypeName) {
        Thucydides.getCurrentSession().put(toPutTypeNameKey, toPutTypeName);
    }

    private<T>T getVariableValue(String key) {
        return ((T) Thucydides.getCurrentSession().get(key));
    }

    private String getVariableAsString(String key) {
        try {
            return ((String) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return key;
        }
    }

    private Boolean getVariableAsBoolean(String key) {
        try {
            return ((Boolean) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return Boolean.parseBoolean(key);
        }
    }

    private Integer getVariableAsInteger(String key) {
        try {
            return ((Integer) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return Integer.parseInt(key);
        }
    }

    private Long getVariableAsLong(String key) {
        try {
            return ((Long) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return Long.parseLong(key);
        }
    }

    private Double getVariableAsDouble(String key) {
        try {
            return ((Double) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return Double.parseDouble(key);
        }
    }

    private BigInteger getVariableAsBigInteger(String key) {
        try {
            return ((BigInteger) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return new BigInteger(key);
        }
    }

    private BigDecimal getVariableAsBigDecimal(String key) {
        try {
            return ((BigDecimal) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return new BigDecimal(key);
        }
    }

    private XMLGregorianCalendar getVariableAsXMLGregorianCalendar(String key)
            throws DatatypeConfigurationException
    {
        try {
            return ((XMLGregorianCalendar) Thucydides.getCurrentSession().get(key));
        } catch (AssertionError _x) {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(key);
        }
    }

}
