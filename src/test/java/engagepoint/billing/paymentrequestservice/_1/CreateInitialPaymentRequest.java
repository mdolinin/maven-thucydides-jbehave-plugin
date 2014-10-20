
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createInitialPaymentRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createInitialPaymentRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paymentRequest" type="{urn:engagepoint:billing:PaymentRequestService:1.0}paymentRequestDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createInitialPaymentRequest", propOrder = {
    "paymentRequest"
})
public class CreateInitialPaymentRequest {

    protected PaymentRequestDTO paymentRequest;

    /**
     * Gets the value of the paymentRequest property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentRequestDTO }
     *     
     */
    public PaymentRequestDTO getPaymentRequest() {
        return paymentRequest;
    }

    /**
     * Sets the value of the paymentRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentRequestDTO }
     *     
     */
    public void setPaymentRequest(PaymentRequestDTO value) {
        this.paymentRequest = value;
    }

}
