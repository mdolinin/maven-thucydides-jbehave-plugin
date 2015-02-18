package net.thucydides.maven.plugin.test.example;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;

@WebService(name = "HelloWorld", targetNamespace = "http://example.test.plugin.maven.thucydides.net/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface HelloWorld {

    @WebMethod
    @WebResult(partName = "return")
    public String getHelloWorldAsString(@WebParam(name = "arg0", partName = "arg0") String arg0);

    @WebMethod
    public void generatePersonInstance(@WebParam(name = "parameter") Person person);

    @WebMethod
    public BigInteger generateBigInteger();

    @WebMethod
    public BigDecimal generateBigDecimal(@WebParam(name = "value") String value);

    @WebMethod
    public XMLGregorianCalendar generateXMLGregorianCalendar(@WebParam(name = "value") String value);

    @WebMethod
    public Integer generateInteger1();

    @WebMethod
    public Integer generateInteger2();
}
