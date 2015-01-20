package net.thucydides.maven.plugin.test.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
    private List<String> attribute1;
    private String attribute2;
    private List<Address> attribute3;
    private Address attribute4;
    private String attribute5;
    private BigDecimal attribute6;
    private Boolean attribute7;
    private Double attribute8;
    private Integer attribute9;
    private Long attribute10;
    private BigInteger attribute11;
    private XMLGregorianCalendar attribute12;

    public List<String> getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(List<String> attribute1) {
        this.attribute1 = attribute1;
    }

    public XMLGregorianCalendar getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(XMLGregorianCalendar attribute12) {
        this.attribute12 = attribute12;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public List<Address> getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(List<Address> attribute3) {
        this.attribute3 = attribute3;
    }

    public Address getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(Address attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public BigDecimal getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(BigDecimal attribute6) {
        this.attribute6 = attribute6;
    }

    public Boolean getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(Boolean attribute7) {
        this.attribute7 = attribute7;
    }

    public Double getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(Double attribute8) {
        this.attribute8 = attribute8;
    }

    public Integer getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(Integer attribute9) {
        this.attribute9 = attribute9;
    }

    public Long getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(Long attribute10) {
        this.attribute10 = attribute10;
    }

    public BigInteger getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(BigInteger attribute11) {
        this.attribute11 = attribute11;
    }
}
