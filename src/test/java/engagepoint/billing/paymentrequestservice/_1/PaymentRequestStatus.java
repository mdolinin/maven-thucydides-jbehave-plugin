
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for paymentRequestStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="paymentRequestStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CREATED"/>
 *     &lt;enumeration value="INITIAL"/>
 *     &lt;enumeration value="APPROVED"/>
 *     &lt;enumeration value="APPLIED"/>
 *     &lt;enumeration value="VOID"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "paymentRequestStatus")
@XmlEnum
public enum PaymentRequestStatus {

    CREATED,
    INITIAL,
    APPROVED,
    APPLIED,
    VOID;

    public String value() {
        return name();
    }

    public static PaymentRequestStatus fromValue(String v) {
        return valueOf(v);
    }

}
