
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
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

public class HelloWorldImplServiceSteps {

    public HelloWorld helloWorldField = new HelloWorldImplService().getHelloWorldImplPort();

    @When("generate person instance '$personKey' and save response to $voidValueKey")
    public void whenGeneratePersonInstance(String personKey, String voidValueKey) {
        Person person = null;
        if (!personKey.isEmpty()) {
            person = getVariableValue(personKey);
        }
        helloWorldField.generatePersonInstance(person);
    }

    @Given("person saved to '$personKey - hello world impl service' with parameters: $parameters")
    public void givenPerson(String personKey, ExamplesTable parameters)
        throws DatatypeConfigurationException
    {
        Person person = new Person();
        if (parameters.getRows().size()!= 1) {
            throw new AssertionError("Wrong number of parameters.");
        }
        for (Map<String, String> row: parameters.getRows()) {
            if (!row.get("attribute1").isEmpty()) {
                String attribute1Value = row.get("attribute1");
                List<String> attribute1 = getVariableValue(attribute1Value);
                person.getAttribute1().addAll(attribute1);
            }
            if (!row.get("attribute2").isEmpty()) {
                String attribute2Value = row.get("attribute2");
                String attribute2 = getVariableAsString(attribute2Value);
                person.setAttribute2(attribute2);
            }
            if (!row.get("address").isEmpty()) {
                String addressValue = row.get("address");
                List<Address> address = getVariableValue(addressValue);
                person.getAddress().addAll(address);
            }
            if (!row.get("attribute4").isEmpty()) {
                String attribute4Value = row.get("attribute4");
                Address attribute4 = getVariableValue(attribute4Value);
                person.setAttribute4(attribute4);
            }
            if (!row.get("attribute5").isEmpty()) {
                String attribute5Value = row.get("attribute5");
                String attribute5 = getVariableAsString(attribute5Value);
                person.setAttribute5(attribute5);
            }
            if (!row.get("attribute6").isEmpty()) {
                String attribute6Value = row.get("attribute6");
                BigDecimal attribute6 = getVariableAsBigDecimal(attribute6Value);
                person.setAttribute6(attribute6);
            }
            if (!row.get("attribute7").isEmpty()) {
                String attribute7Value = row.get("attribute7");
                Boolean attribute7 = getVariableAsBoolean(attribute7Value);
                person.setAttribute7(attribute7);
            }
            if (!row.get("attribute8").isEmpty()) {
                String attribute8Value = row.get("attribute8");
                Double attribute8 = getVariableAsDouble(attribute8Value);
                person.setAttribute8(attribute8);
            }
            if (!row.get("attribute9").isEmpty()) {
                String attribute9Value = row.get("attribute9");
                Integer attribute9 = getVariableAsInteger(attribute9Value);
                person.setAttribute9(attribute9);
            }
            if (!row.get("attribute10").isEmpty()) {
                String attribute10Value = row.get("attribute10");
                Long attribute10 = getVariableAsLong(attribute10Value);
                person.setAttribute10(attribute10);
            }
            if (!row.get("attribute11").isEmpty()) {
                String attribute11Value = row.get("attribute11");
                BigInteger attribute11 = getVariableAsBigInteger(attribute11Value);
                person.setAttribute11(attribute11);
            }
            if (!row.get("attribute12").isEmpty()) {
                String attribute12Value = row.get("attribute12");
                XMLGregorianCalendar attribute12 = getVariableAsXMLGregorianCalendar(attribute12Value);
                person.setAttribute12(attribute12);
            }
        }
        save(personKey, person);
    }

    @Given("list string $stringKeys and save to $attribute1Value")
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

    @Given("list address $addressKeys and save to $addressValue")
    public void givenListAddress(List<String> addressKeys, String addressValue) {
        List<Address> addressList = new ArrayList<Address>();
        for (String addressKey: addressKeys) {
            Address address = getVariableValue(addressKey);
            addressList.add(address);
        }
        save(addressValue, addressList);
    }

    @When("get hello world as string '$arg0Key' and save response to $stringKey")
    public void whenGetHelloWorldAsString(String arg0Key, String stringKey) {
        String arg0 = null;
        if (!arg0Key.isEmpty()) {
            arg0 = getVariableAsString(arg0Key);
        }
        String string = helloWorldField.getHelloWorldAsString(arg0);
        save(stringKey, string);
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
