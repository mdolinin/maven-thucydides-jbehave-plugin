
package engagepoint.billing.paymentrequestservice._1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the engagepoint.billing.paymentrequestservice._1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SetVoidStatusPaymentRequestResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "setVoidStatusPaymentRequestResponse");
    private final static QName _SetPaymentRequestStatus_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "setPaymentRequestStatus");
    private final static QName _SetPaymentRequestStatusResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "setPaymentRequestStatusResponse");
    private final static QName _SearchPaymentRequestsResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "searchPaymentRequestsResponse");
    private final static QName _ConstraintViolationException_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "constraintViolationException");
    private final static QName _GetPendingPaymentsToVoidResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "getPendingPaymentsToVoidResponse");
    private final static QName _VoidPaymentRequestByInternalId_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "voidPaymentRequestByInternalId");
    private final static QName _SetApplyStatusPaymentRequestResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "setApplyStatusPaymentRequestResponse");
    private final static QName _PaymentRequestDTO_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "paymentRequestDTO");
    private final static QName _SearchPaymentRequestsByPayment_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "searchPaymentRequestsByPayment");
    private final static QName _SetApplyStatusPaymentRequest_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "setApplyStatusPaymentRequest");
    private final static QName _CreateInitialPaymentRequestsResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "createInitialPaymentRequestsResponse");
    private final static QName _CanBeVoidedRequest_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "canBeVoidedRequest");
    private final static QName _VoidPaymentRequestByExternalId_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "voidPaymentRequestByExternalId");
    private final static QName _GetPendingPaymentsToVoid_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "getPendingPaymentsToVoid");
    private final static QName _SetVoidStatusPaymentRequest_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "setVoidStatusPaymentRequest");
    private final static QName _SearchPaymentRequests_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "searchPaymentRequests");
    private final static QName _GetPaymentRequestByExternalIdAndSubscriberResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "getPaymentRequestByExternalIdAndSubscriberResponse");
    private final static QName _GetInsertUserId_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "getInsertUserId");
    private final static QName _PaymentRequestByInternalIdRequest_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "paymentRequestByInternalIdRequest");
    private final static QName _GetPaymentRequestByExternalIdAndSubscriberRequest_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "getPaymentRequestByExternalIdAndSubscriberRequest");
    private final static QName _PaymentRequestNoFoundExceptionMsg_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "paymentRequestNoFoundExceptionMsg");
    private final static QName _CreateInitialPaymentRequests_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "createInitialPaymentRequests");
    private final static QName _VoidPaymentRequestByInternalIdResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "voidPaymentRequestByInternalIdResponse");
    private final static QName _VoidPaymentRequestByExternalIdResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "voidPaymentRequestByExternalIdResponse");
    private final static QName _GetInsertUserIdResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "getInsertUserIdResponse");
    private final static QName _CreateInitialPaymentRequest_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "createInitialPaymentRequest");
    private final static QName _PaymentRequestByInternalIdResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "paymentRequestByInternalIdResponse");
    private final static QName _CreateInitialPaymentRequestResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "createInitialPaymentRequestResponse");
    private final static QName _SearchPaymentRequestsByPaymentResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "searchPaymentRequestsByPaymentResponse");
    private final static QName _EntityNotFoundException_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "entityNotFoundException");
    private final static QName _CanBeVoidedRequestResponse_QNAME = new QName("urn:engagepoint:billing:PaymentRequestService:1.0", "canBeVoidedRequestResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: engagepoint.billing.paymentrequestservice._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateInitialPaymentRequests }
     * 
     */
    public CreateInitialPaymentRequests createCreateInitialPaymentRequests() {
        return new CreateInitialPaymentRequests();
    }

    /**
     * Create an instance of {@link SetPaymentRequestStatusResponse }
     *
     */
    public SetPaymentRequestStatusResponse createSetPaymentRequestStatusResponse() {
        return new SetPaymentRequestStatusResponse();
    }

    /**
     * Create an instance of {@link VoidPaymentRequestByExternalId }
     *
     */
    public VoidPaymentRequestByExternalId createVoidPaymentRequestByExternalId() {
        return new VoidPaymentRequestByExternalId();
    }

    /**
     * Create an instance of {@link SetApplyStatusPaymentRequest }
     *
     */
    public SetApplyStatusPaymentRequest createSetApplyStatusPaymentRequest() {
        return new SetApplyStatusPaymentRequest();
    }

    /**
     * Create an instance of {@link SearchPaymentRequests }
     *
     */
    public SearchPaymentRequests createSearchPaymentRequests() {
        return new SearchPaymentRequests();
    }

    /**
     * Create an instance of {@link CanBeVoidedRequest }
     *
     */
    public CanBeVoidedRequest createCanBeVoidedRequest() {
        return new CanBeVoidedRequest();
    }

    /**
     * Create an instance of {@link GetInsertUserIdResponse }
     *
     */
    public GetInsertUserIdResponse createGetInsertUserIdResponse() {
        return new GetInsertUserIdResponse();
    }

    /**
     * Create an instance of {@link VoidPaymentRequestByExternalIdResponse }
     *
     */
    public VoidPaymentRequestByExternalIdResponse createVoidPaymentRequestByExternalIdResponse() {
        return new VoidPaymentRequestByExternalIdResponse();
    }

    /**
     * Create an instance of {@link SetVoidStatusPaymentRequestResponse }
     *
     */
    public SetVoidStatusPaymentRequestResponse createSetVoidStatusPaymentRequestResponse() {
        return new SetVoidStatusPaymentRequestResponse();
    }

    /**
     * Create an instance of {@link GetPaymentRequestByExternalIdAndSubscriberRequest }
     *
     */
    public GetPaymentRequestByExternalIdAndSubscriberRequest createGetPaymentRequestByExternalIdAndSubscriberRequest() {
        return new GetPaymentRequestByExternalIdAndSubscriberRequest();
    }

    /**
     * Create an instance of {@link GetPendingPaymentsToVoidResponse }
     *
     */
    public GetPendingPaymentsToVoidResponse createGetPendingPaymentsToVoidResponse() {
        return new GetPendingPaymentsToVoidResponse();
    }

    /**
     * Create an instance of {@link GetPendingPaymentsToVoid }
     *
     */
    public GetPendingPaymentsToVoid createGetPendingPaymentsToVoid() {
        return new GetPendingPaymentsToVoid();
    }

    /**
     * Create an instance of {@link SearchPaymentRequestsByPaymentResponse }
     *
     */
    public SearchPaymentRequestsByPaymentResponse createSearchPaymentRequestsByPaymentResponse() {
        return new SearchPaymentRequestsByPaymentResponse();
    }

    /**
     * Create an instance of {@link CanBeVoidedRequestResponse }
     *
     */
    public CanBeVoidedRequestResponse createCanBeVoidedRequestResponse() {
        return new CanBeVoidedRequestResponse();
    }

    /**
     * Create an instance of {@link SearchPaymentRequestsByPayment }
     *
     */
    public SearchPaymentRequestsByPayment createSearchPaymentRequestsByPayment() {
        return new SearchPaymentRequestsByPayment();
    }

    /**
     * Create an instance of {@link SearchPaymentRequestsResponse }
     *
     */
    public SearchPaymentRequestsResponse createSearchPaymentRequestsResponse() {
        return new SearchPaymentRequestsResponse();
    }

    /**
     * Create an instance of {@link VoidPaymentRequestByInternalId }
     *
     */
    public VoidPaymentRequestByInternalId createVoidPaymentRequestByInternalId() {
        return new VoidPaymentRequestByInternalId();
    }

    /**
     * Create an instance of {@link SetApplyStatusPaymentRequestResponse }
     *
     */
    public SetApplyStatusPaymentRequestResponse createSetApplyStatusPaymentRequestResponse() {
        return new SetApplyStatusPaymentRequestResponse();
    }

    /**
     * Create an instance of {@link CreateInitialPaymentRequestResponse }
     *
     */
    public CreateInitialPaymentRequestResponse createCreateInitialPaymentRequestResponse() {
        return new CreateInitialPaymentRequestResponse();
    }

    /**
     * Create an instance of {@link GetInsertUserId }
     *
     */
    public GetInsertUserId createGetInsertUserId() {
        return new GetInsertUserId();
    }

    /**
     * Create an instance of {@link SetVoidStatusPaymentRequest }
     *
     */
    public SetVoidStatusPaymentRequest createSetVoidStatusPaymentRequest() {
        return new SetVoidStatusPaymentRequest();
    }

    /**
     * Create an instance of {@link PaymentRequestDTO }
     *
     */
    public PaymentRequestDTO createPaymentRequestDTO() {
        return new PaymentRequestDTO();
    }

    /**
     * Create an instance of {@link SetPaymentRequestStatus }
     *
     */
    public SetPaymentRequestStatus createSetPaymentRequestStatus() {
        return new SetPaymentRequestStatus();
    }

    /**
     * Create an instance of {@link VoidPaymentRequestByInternalIdResponse }
     *
     */
    public VoidPaymentRequestByInternalIdResponse createVoidPaymentRequestByInternalIdResponse() {
        return new VoidPaymentRequestByInternalIdResponse();
    }

    /**
     * Create an instance of {@link GetPaymentRequestByExternalIdAndSubscriberResponse }
     *
     */
    public GetPaymentRequestByExternalIdAndSubscriberResponse createGetPaymentRequestByExternalIdAndSubscriberResponse() {
        return new GetPaymentRequestByExternalIdAndSubscriberResponse();
    }

    /**
     * Create an instance of {@link CreateInitialPaymentRequest }
     *
     */
    public CreateInitialPaymentRequest createCreateInitialPaymentRequest() {
        return new CreateInitialPaymentRequest();
    }

    /**
     * Create an instance of {@link CreateInitialPaymentRequestsResponse }
     *
     */
    public CreateInitialPaymentRequestsResponse createCreateInitialPaymentRequestsResponse() {
        return new CreateInitialPaymentRequestsResponse();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SetVoidStatusPaymentRequestResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "setVoidStatusPaymentRequestResponse")
    public JAXBElement<SetVoidStatusPaymentRequestResponse> createSetVoidStatusPaymentRequestResponse(SetVoidStatusPaymentRequestResponse value) {
        return new JAXBElement<SetVoidStatusPaymentRequestResponse>(_SetVoidStatusPaymentRequestResponse_QNAME, SetVoidStatusPaymentRequestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SetPaymentRequestStatus }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "setPaymentRequestStatus")
    public JAXBElement<SetPaymentRequestStatus> createSetPaymentRequestStatus(SetPaymentRequestStatus value) {
        return new JAXBElement<SetPaymentRequestStatus>(_SetPaymentRequestStatus_QNAME, SetPaymentRequestStatus.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SetPaymentRequestStatusResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "setPaymentRequestStatusResponse")
    public JAXBElement<SetPaymentRequestStatusResponse> createSetPaymentRequestStatusResponse(SetPaymentRequestStatusResponse value) {
        return new JAXBElement<SetPaymentRequestStatusResponse>(_SetPaymentRequestStatusResponse_QNAME, SetPaymentRequestStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SearchPaymentRequestsResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "searchPaymentRequestsResponse")
    public JAXBElement<SearchPaymentRequestsResponse> createSearchPaymentRequestsResponse(SearchPaymentRequestsResponse value) {
        return new JAXBElement<SearchPaymentRequestsResponse>(_SearchPaymentRequestsResponse_QNAME, SearchPaymentRequestsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "constraintViolationException")
    public JAXBElement<String> createConstraintViolationException(String value) {
        return new JAXBElement<String>(_ConstraintViolationException_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetPendingPaymentsToVoidResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "getPendingPaymentsToVoidResponse")
    public JAXBElement<GetPendingPaymentsToVoidResponse> createGetPendingPaymentsToVoidResponse(GetPendingPaymentsToVoidResponse value) {
        return new JAXBElement<GetPendingPaymentsToVoidResponse>(_GetPendingPaymentsToVoidResponse_QNAME, GetPendingPaymentsToVoidResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link VoidPaymentRequestByInternalId }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "voidPaymentRequestByInternalId")
    public JAXBElement<VoidPaymentRequestByInternalId> createVoidPaymentRequestByInternalId(VoidPaymentRequestByInternalId value) {
        return new JAXBElement<VoidPaymentRequestByInternalId>(_VoidPaymentRequestByInternalId_QNAME, VoidPaymentRequestByInternalId.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SetApplyStatusPaymentRequestResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "setApplyStatusPaymentRequestResponse")
    public JAXBElement<SetApplyStatusPaymentRequestResponse> createSetApplyStatusPaymentRequestResponse(SetApplyStatusPaymentRequestResponse value) {
        return new JAXBElement<SetApplyStatusPaymentRequestResponse>(_SetApplyStatusPaymentRequestResponse_QNAME, SetApplyStatusPaymentRequestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link PaymentRequestDTO }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "paymentRequestDTO")
    public JAXBElement<PaymentRequestDTO> createPaymentRequestDTO(PaymentRequestDTO value) {
        return new JAXBElement<PaymentRequestDTO>(_PaymentRequestDTO_QNAME, PaymentRequestDTO.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SearchPaymentRequestsByPayment }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "searchPaymentRequestsByPayment")
    public JAXBElement<SearchPaymentRequestsByPayment> createSearchPaymentRequestsByPayment(SearchPaymentRequestsByPayment value) {
        return new JAXBElement<SearchPaymentRequestsByPayment>(_SearchPaymentRequestsByPayment_QNAME, SearchPaymentRequestsByPayment.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SetApplyStatusPaymentRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "setApplyStatusPaymentRequest")
    public JAXBElement<SetApplyStatusPaymentRequest> createSetApplyStatusPaymentRequest(SetApplyStatusPaymentRequest value) {
        return new JAXBElement<SetApplyStatusPaymentRequest>(_SetApplyStatusPaymentRequest_QNAME, SetApplyStatusPaymentRequest.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CreateInitialPaymentRequestsResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "createInitialPaymentRequestsResponse")
    public JAXBElement<CreateInitialPaymentRequestsResponse> createCreateInitialPaymentRequestsResponse(CreateInitialPaymentRequestsResponse value) {
        return new JAXBElement<CreateInitialPaymentRequestsResponse>(_CreateInitialPaymentRequestsResponse_QNAME, CreateInitialPaymentRequestsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CanBeVoidedRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "canBeVoidedRequest")
    public JAXBElement<CanBeVoidedRequest> createCanBeVoidedRequest(CanBeVoidedRequest value) {
        return new JAXBElement<CanBeVoidedRequest>(_CanBeVoidedRequest_QNAME, CanBeVoidedRequest.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link VoidPaymentRequestByExternalId }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "voidPaymentRequestByExternalId")
    public JAXBElement<VoidPaymentRequestByExternalId> createVoidPaymentRequestByExternalId(VoidPaymentRequestByExternalId value) {
        return new JAXBElement<VoidPaymentRequestByExternalId>(_VoidPaymentRequestByExternalId_QNAME, VoidPaymentRequestByExternalId.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetPendingPaymentsToVoid }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "getPendingPaymentsToVoid")
    public JAXBElement<GetPendingPaymentsToVoid> createGetPendingPaymentsToVoid(GetPendingPaymentsToVoid value) {
        return new JAXBElement<GetPendingPaymentsToVoid>(_GetPendingPaymentsToVoid_QNAME, GetPendingPaymentsToVoid.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SetVoidStatusPaymentRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "setVoidStatusPaymentRequest")
    public JAXBElement<SetVoidStatusPaymentRequest> createSetVoidStatusPaymentRequest(SetVoidStatusPaymentRequest value) {
        return new JAXBElement<SetVoidStatusPaymentRequest>(_SetVoidStatusPaymentRequest_QNAME, SetVoidStatusPaymentRequest.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SearchPaymentRequests }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "searchPaymentRequests")
    public JAXBElement<SearchPaymentRequests> createSearchPaymentRequests(SearchPaymentRequests value) {
        return new JAXBElement<SearchPaymentRequests>(_SearchPaymentRequests_QNAME, SearchPaymentRequests.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetPaymentRequestByExternalIdAndSubscriberResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "getPaymentRequestByExternalIdAndSubscriberResponse")
    public JAXBElement<GetPaymentRequestByExternalIdAndSubscriberResponse> createGetPaymentRequestByExternalIdAndSubscriberResponse(GetPaymentRequestByExternalIdAndSubscriberResponse value) {
        return new JAXBElement<GetPaymentRequestByExternalIdAndSubscriberResponse>(_GetPaymentRequestByExternalIdAndSubscriberResponse_QNAME, GetPaymentRequestByExternalIdAndSubscriberResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetInsertUserId }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "getInsertUserId")
    public JAXBElement<GetInsertUserId> createGetInsertUserId(GetInsertUserId value) {
        return new JAXBElement<GetInsertUserId>(_GetInsertUserId_QNAME, GetInsertUserId.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link Long }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "paymentRequestByInternalIdRequest")
    public JAXBElement<Long> createPaymentRequestByInternalIdRequest(Long value) {
        return new JAXBElement<Long>(_PaymentRequestByInternalIdRequest_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetPaymentRequestByExternalIdAndSubscriberRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "getPaymentRequestByExternalIdAndSubscriberRequest")
    public JAXBElement<GetPaymentRequestByExternalIdAndSubscriberRequest> createGetPaymentRequestByExternalIdAndSubscriberRequest(GetPaymentRequestByExternalIdAndSubscriberRequest value) {
        return new JAXBElement<GetPaymentRequestByExternalIdAndSubscriberRequest>(_GetPaymentRequestByExternalIdAndSubscriberRequest_QNAME, GetPaymentRequestByExternalIdAndSubscriberRequest.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "paymentRequestNoFoundExceptionMsg")
    public JAXBElement<String> createPaymentRequestNoFoundExceptionMsg(String value) {
        return new JAXBElement<String>(_PaymentRequestNoFoundExceptionMsg_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CreateInitialPaymentRequests }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "createInitialPaymentRequests")
    public JAXBElement<CreateInitialPaymentRequests> createCreateInitialPaymentRequests(CreateInitialPaymentRequests value) {
        return new JAXBElement<CreateInitialPaymentRequests>(_CreateInitialPaymentRequests_QNAME, CreateInitialPaymentRequests.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link VoidPaymentRequestByInternalIdResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "voidPaymentRequestByInternalIdResponse")
    public JAXBElement<VoidPaymentRequestByInternalIdResponse> createVoidPaymentRequestByInternalIdResponse(VoidPaymentRequestByInternalIdResponse value) {
        return new JAXBElement<VoidPaymentRequestByInternalIdResponse>(_VoidPaymentRequestByInternalIdResponse_QNAME, VoidPaymentRequestByInternalIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link VoidPaymentRequestByExternalIdResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "voidPaymentRequestByExternalIdResponse")
    public JAXBElement<VoidPaymentRequestByExternalIdResponse> createVoidPaymentRequestByExternalIdResponse(VoidPaymentRequestByExternalIdResponse value) {
        return new JAXBElement<VoidPaymentRequestByExternalIdResponse>(_VoidPaymentRequestByExternalIdResponse_QNAME, VoidPaymentRequestByExternalIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetInsertUserIdResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "getInsertUserIdResponse")
    public JAXBElement<GetInsertUserIdResponse> createGetInsertUserIdResponse(GetInsertUserIdResponse value) {
        return new JAXBElement<GetInsertUserIdResponse>(_GetInsertUserIdResponse_QNAME, GetInsertUserIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CreateInitialPaymentRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "createInitialPaymentRequest")
    public JAXBElement<CreateInitialPaymentRequest> createCreateInitialPaymentRequest(CreateInitialPaymentRequest value) {
        return new JAXBElement<CreateInitialPaymentRequest>(_CreateInitialPaymentRequest_QNAME, CreateInitialPaymentRequest.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link PaymentRequestDTO }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "paymentRequestByInternalIdResponse")
    public JAXBElement<PaymentRequestDTO> createPaymentRequestByInternalIdResponse(PaymentRequestDTO value) {
        return new JAXBElement<PaymentRequestDTO>(_PaymentRequestByInternalIdResponse_QNAME, PaymentRequestDTO.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CreateInitialPaymentRequestResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "createInitialPaymentRequestResponse")
    public JAXBElement<CreateInitialPaymentRequestResponse> createCreateInitialPaymentRequestResponse(CreateInitialPaymentRequestResponse value) {
        return new JAXBElement<CreateInitialPaymentRequestResponse>(_CreateInitialPaymentRequestResponse_QNAME, CreateInitialPaymentRequestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link SearchPaymentRequestsByPaymentResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "searchPaymentRequestsByPaymentResponse")
    public JAXBElement<SearchPaymentRequestsByPaymentResponse> createSearchPaymentRequestsByPaymentResponse(SearchPaymentRequestsByPaymentResponse value) {
        return new JAXBElement<SearchPaymentRequestsByPaymentResponse>(_SearchPaymentRequestsByPaymentResponse_QNAME, SearchPaymentRequestsByPaymentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "entityNotFoundException")
    public JAXBElement<String> createEntityNotFoundException(String value) {
        return new JAXBElement<String>(_EntityNotFoundException_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CanBeVoidedRequestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:engagepoint:billing:PaymentRequestService:1.0", name = "canBeVoidedRequestResponse")
    public JAXBElement<CanBeVoidedRequestResponse> createCanBeVoidedRequestResponse(CanBeVoidedRequestResponse value) {
        return new JAXBElement<CanBeVoidedRequestResponse>(_CanBeVoidedRequestResponse_QNAME, CanBeVoidedRequestResponse.class, null, value);
    }

}
