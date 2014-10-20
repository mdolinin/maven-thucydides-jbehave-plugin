
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for canBeVoidedRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="canBeVoidedRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="externalPaymentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currentDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "canBeVoidedRequest", propOrder = {
    "externalPaymentId",
    "currentDate"
})
public class CanBeVoidedRequest {

    protected String externalPaymentId;
    @XmlSchemaType(name = "anySimpleType")
    protected Object currentDate;

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

    /**
     * Gets the value of the currentDate property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCurrentDate() {
        return currentDate;
    }

    /**
     * Sets the value of the currentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCurrentDate(Object value) {
        this.currentDate = value;
    }

}
