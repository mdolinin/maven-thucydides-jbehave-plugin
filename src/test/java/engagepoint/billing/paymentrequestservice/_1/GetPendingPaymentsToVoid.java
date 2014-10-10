
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getPendingPaymentsToVoid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getPendingPaymentsToVoid">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
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
@XmlType(name = "getPendingPaymentsToVoid", propOrder = {
    "currentDate"
})
public class GetPendingPaymentsToVoid {

    @XmlSchemaType(name = "anySimpleType")
    protected Object currentDate;

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
