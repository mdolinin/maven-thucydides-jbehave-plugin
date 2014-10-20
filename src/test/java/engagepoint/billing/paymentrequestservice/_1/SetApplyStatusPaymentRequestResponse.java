
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setApplyStatusPaymentRequestResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setApplyStatusPaymentRequestResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paymentsAffected" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setApplyStatusPaymentRequestResponse", propOrder = {
    "paymentsAffected"
})
public class SetApplyStatusPaymentRequestResponse {

    protected Integer paymentsAffected;

    /**
     * Gets the value of the paymentsAffected property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPaymentsAffected() {
        return paymentsAffected;
    }

    /**
     * Sets the value of the paymentsAffected property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPaymentsAffected(Integer value) {
        this.paymentsAffected = value;
    }

}
