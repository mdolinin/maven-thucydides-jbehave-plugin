
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;


/**
 * <p>Java class for paymentRequestDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="paymentRequestDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subscriberExternalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insertUserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externalPaymentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reasonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "paymentRequestDTO", propOrder = {
    "subscriberExternalId",
    "billNumber",
    "paymentDate",
    "amount",
    "description",
    "insertUserId",
    "externalPaymentId",
    "reasonCode",
    "status"
})
public class PaymentRequestDTO {

    protected String subscriberExternalId;
    protected String billNumber;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar paymentDate;
    protected BigDecimal amount;
    protected String description;
    protected String insertUserId;
    protected String externalPaymentId;
    protected String reasonCode;
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
     * Gets the value of the billNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillNumber() {
        return billNumber;
    }

    /**
     * Sets the value of the billNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillNumber(String value) {
        this.billNumber = value;
    }

    /**
     * Gets the value of the paymentDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getPaymentDate() {
        return paymentDate;
    }

    /**
     * Sets the value of the paymentDate property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public void setPaymentDate(XMLGregorianCalendar value) {
        this.paymentDate = value;
    }

    /**
     * Gets the value of the amount property.
     *
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     *
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the insertUserId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInsertUserId() {
        return insertUserId;
    }

    /**
     * Sets the value of the insertUserId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInsertUserId(String value) {
        this.insertUserId = value;
    }

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
     * Gets the value of the reasonCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReasonCode(String value) {
        this.reasonCode = value;
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
