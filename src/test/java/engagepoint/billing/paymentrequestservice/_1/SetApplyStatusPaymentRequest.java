
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setApplyStatusPaymentRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setApplyStatusPaymentRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="externalPaymentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setApplyStatusPaymentRequest", propOrder = {
    "externalPaymentId"
})
public class SetApplyStatusPaymentRequest {

    protected String externalPaymentId;

    /**
     * Gets the value of the externalPaymentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalPaymentId() {
        return externalPaymentId;
    }

    /**
     * Sets the value of the externalPaymentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalPaymentId(String value) {
        this.externalPaymentId = value;
    }

}
