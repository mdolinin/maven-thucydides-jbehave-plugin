
package net.thucydides.maven.plugin.test.example;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import net.thucydides.core.Thucydides;
import org.jbehave.core.annotations.When;

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
