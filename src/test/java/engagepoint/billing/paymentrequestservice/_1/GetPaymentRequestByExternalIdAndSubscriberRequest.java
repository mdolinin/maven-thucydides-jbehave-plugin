
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getPaymentRequestByExternalIdAndSubscriberRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getPaymentRequestByExternalIdAndSubscriberRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paymentRequestExternalId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="subscriberId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPaymentRequestByExternalIdAndSubscriberRequest", propOrder = {
    "paymentRequestExternalId",
    "subscriberId"
})
public class GetPaymentRequestByExternalIdAndSubscriberRequest {

    @XmlElement(required = true)
    protected String paymentRequestExternalId;
    @XmlElement(required = true)
    protected String subscriberId;

    /**
     * Gets the value of the paymentRequestExternalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentRequestExternalId() {
        return paymentRequestExternalId;
    }

    /**
     * Sets the value of the paymentRequestExternalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentRequestExternalId(String value) {
        this.paymentRequestExternalId = value;
    }

    /**
     * Gets the value of the subscriberId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubscriberId() {
        return subscriberId;
    }

    /**
     * Sets the value of the subscriberId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubscriberId(String value) {
        this.subscriberId = value;
    }

}
