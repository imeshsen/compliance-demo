///**
// * 
// */
//package lk.sampath.oc.Transfers.Service;
//
//import java.net.URL;
//
//import javax.xml.ws.BindingProvider;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import lk.sampath.oc.Transfers.Pojo.PaymentCardResponse;
//import lk.sampath.oc.Transfers.integration.creditcard.CreditCardServices;
//import lk.sampath.oc.Transfers.integration.creditcard.ICreditCards;
//import lk.sampath.oc.Transfers.integration.creditcard.ResponseMessageClassPaymentCardResponse;
//import lk.sampath.oc.Transfers.request.PayCreditCardRequest;
//import lk.sampath.oc.Transfers.utils.ErrorCodeMessage;
//
///**
// * @author hrsupun
// *
// */
//@Service
//public class CreditCardSOAPConsumerService {
//	
//	private static Logger logger = LogManager.getLogger(CreditCardSOAPConsumerService.class);
//	
//	@Value("${service.url.creditcard.endpoint}")
//	private String creditCardServiceUrl;
//	
//	@Value("${service.url.creditcard.paymentcard.authID}")
//	private String paymentcardAuthId;
//
//	public PaymentCardResponse paymentCard(PayCreditCardRequest request) {
//
//		URL url = ClassLoader.getSystemResource("wsdl/creditcard/CreditCardServices_1_1.wsdl");
//		CreditCardServices service = new CreditCardServices(url);
//		ICreditCards proxy = service.getBasicHttpBindingICreditCards();
//		((BindingProvider) proxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
//				creditCardServiceUrl);
//		ResponseMessageClassPaymentCardResponse resp = null;
//		PaymentCardResponse response = new PaymentCardResponse();
//		try {
//			resp = proxy.paymentCard(paymentcardAuthId, request.getCardSerialNumber(), request.getDebitAccountNumber(),
//					request.getAmount().doubleValue());
//			response.setCode(resp.getCode());
//			response.setSubCode(resp.getSubCode());
//			response.setMessage(resp.getMessage().getValue());
//			response.setMessageCustomer(resp.getMessageCustomer().getValue());
//			return response;
//		} catch (Exception e) {
//			logger.error("Service call to ==> {} failed due to : {}", creditCardServiceUrl, e);
//			response.setCode((long)ErrorCodeMessage.FAIL_ERROR_CODE);
//			response.setSubCode((long)ErrorCodeMessage.FAIL_ERROR_CODE);
//			response.setMessage("System error");
//			response.setMessageCustomer(e.getLocalizedMessage());
//			return response;
//		}
//
//	}
//
//}
