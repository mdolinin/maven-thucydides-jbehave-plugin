
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for searchPaymentRequests complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="searchPaymentRequests">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subscriberExternalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{urn:engagepoint:billing:PaymentRequestService:1.0}paymentRequestStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchPaymentRequests", propOrder = {
    "subscriberExternalId",
    "status"
})
public class SearchPaymentRequests {

    protected String subscriberExternalId;
    protected PaymentRequestStatus status;

    /**
     * Gets the value of the subscriberExternalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubscriberExternalId() {
        return subscriberExternalId;
    }

    /**
     * Sets the value of the subscriberExternalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubscriberExternalId(String value) {
        this.subscriberExternalId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentRequestStatus }
     *     
     */
    public PaymentRequestStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentRequestStatus }
     *     
     */
    public void setStatus(PaymentRequestStatus value) {
        this.status = value;
    }

}
