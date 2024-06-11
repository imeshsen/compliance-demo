/**
 * 
 */
package lk.sampath.oc.Transfers.Service;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lk.sampath.oc.Transfers.Pojo.mobile.*;
import lk.sampath.oc.Transfers.request.App.DoTransferReqForCard;
import lk.sampath.oc.Transfers.request.App.GetCardSerialRequestForAppAPIs;
import lk.sampath.oc.Transfers.request.App.PayCreditCardRequestForApp;
import lk.sampath.oc.Transfers.request.App.SampathCCPaymentRequestforAppAPIs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import lk.sampath.oc.Transfers.Enums.ErrorCode;
import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Pojo.CCpaymentCardResponse;
import lk.sampath.oc.Transfers.Pojo.GetCardNumberRequest;
import lk.sampath.oc.Transfers.Pojo.GetCardNumberResponse;
import lk.sampath.oc.Transfers.Pojo.GetCardSerailResponse;
import lk.sampath.oc.Transfers.Pojo.GetCardSerialRequest;
import lk.sampath.oc.Transfers.Pojo.PaymentCardResponse;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lk.sampath.oc.Transfers.request.PayCreditCardRequest;
import lk.sampath.oc.Transfers.request.SampathCCPaymentRequest;
import lk.sampath.oc.Transfers.utils.ErrorCodeMessage;

/**
 * @author hrsupun
 *
 */
@Service
public class CreditCardRestService {

	private static Logger logger = LogManager.getLogger(CreditCardRestService.class);

	@Autowired
	@Qualifier("payCardRestTemplate")
	private RestTemplate payCardRestTemplate;

	@Autowired
	@Qualifier("getCardNumberRestTemplate")
	private RestTemplate getCardNumberRestTemplate;

	@Autowired
	@Qualifier("getCardSerailRestTemplate")
	private RestTemplate getCardSerailRestTemplate;

	@Autowired
	@Qualifier("payCardAppRestTemplate")
	private RestTemplate payCardAppRestTemplate;

	@Autowired
	@Qualifier("getCardNumberAppRestTemplate")
	private RestTemplate getCardNumberAppRestTemplate;

	@Autowired
	@Qualifier("getCardSerialAppRestTemplate")
	private RestTemplate getCardSerialAppRestTemplate;


	@Value("${sampath.cc.service.uri}")
	private String cCServiceUrl;

	@Value("${sampath.cc.service.getcardnumber.uri}")
	private String getcardNumberUri;

	@Value("${sampath.cc.service.getcardnumber.servicename}")
	private String getcardNumberServicename;

	@Value("${sampath.cc.service.getcardserial.uri}")
	private String getcardSerialUri;

	@Value("${sampath.cc.service.getcardserial.servicename}")
	private String getcardSerialServicename;

	@Value("${config.finacle.timeout}")
	private int timeout;

	@Value("${sampath.cc.service.cardpayment.app.servicename}")
	private String cCServiceAppServicename;

	@Value("${sampath.cc.service.app.uri}")
	private String cCServiceAppUrl;

	@Value("${sampath.cc.service.getcardnumber.app.servicename}")
	private String getcardNumberServicenameApp;

	@Value("${sampath.cc.service.getcardnumber.app.uri}")
	private String getcardNumberUriApp;

	@Value("${sampath.cc.service.getcardserial.app.servicename}")
	private String getcardSerialServicenameApp;

	@Value("${sampath.cc.service.getcardserial.app.uri}")
	private String getcardSerialUriApp;
	
	public PaymentCardResponse paymentCard(PayCreditCardRequest request) throws Throwable {
		// set time
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String requestTime = sdf.format(new Date());

		SampathCCPaymentRequest requestRest = new SampathCCPaymentRequest(request.getCardSerialNumber(),
				request.getDebitAccountNumber(), request.getAmount(), requestTime);
		// add request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("ServiceName", "ProcessCardPaymentforOmni");
		headers.set("TokenID", "0");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<SampathCCPaymentRequest> requestEntity = new HttpEntity<SampathCCPaymentRequest>(requestRest,
				headers);

		payCardRestTemplate.setRequestFactory(getClientHttpRequestFactory());
		
		PaymentCardResponse response = new PaymentCardResponse();
		try {
			
			try {
				logger.info("Requesting from " + cCServiceUrl + "/processcardpayment ==> " + requestEntity);
				response = payCardRestTemplate.postForObject(cCServiceUrl + "/processcardpayment", requestEntity,
						PaymentCardResponse.class);
				logger.info("Response from " + cCServiceUrl + "/processcardpayment ==> " + response);
				return response;

			} catch (ResourceAccessException e) {
				logger.error("Processcardpayment error occured - ResourceAccessException", e);
				throw e.getCause();
			}
			
		}catch(SocketTimeoutException e) {
			logger.error("Service call to ==> {} failed due to : {}", cCServiceUrl + "/processcardpayment", e);
			logger.error("Error occured",e);
			response.setCode((long) 9000);
			response.setSubCode((long) ErrorCodeMessage.FAIL_ERROR_CODE);
			response.setMessage("FAILED_IN_PROCESS_DO_CC_SETTLEMENT");
			response.setMessageCustomer(e.getLocalizedMessage());
			return response;
		} catch (Exception e) {
			logger.error("Service call to ==> {} failed due to : {}", cCServiceUrl + "/processcardpayment", e);
			logger.error("Error occured",e);
			response.setCode((long) ErrorCodeMessage.FAIL_ERROR_CODE);
			response.setSubCode((long) ErrorCodeMessage.FAIL_ERROR_CODE);
			response.setMessage("System error");
			response.setMessageCustomer(e.getLocalizedMessage());
			return response;
		}
	}

	public PaymentCardResponseForAppAPIs paymentCardForApp(PayCreditCardRequestForApp request) throws Throwable {
		// set time
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String requestTime = sdf.format(new Date());

		SampathCCPaymentRequestforAppAPIs requestRest = new SampathCCPaymentRequestforAppAPIs(request.getCardSerialNumber(),
				request.getDebitAccountNumber(), request.getAmount(), requestTime, request.getInitiatedSerno(), request.getInitiatedKey(),
				request.getChainSerno(), request.getChainAuth(),request.getDeviceId(), request.getPaymentCurrency());

		// add request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("ServiceName", cCServiceAppServicename);
		headers.set("IdentityType", "Nic");
		headers.set("IdentityValue", request.getNIC());
		headers.set("TokenID", "0");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<SampathCCPaymentRequestforAppAPIs> requestEntity = new HttpEntity<SampathCCPaymentRequestforAppAPIs>(requestRest,
				headers);

		payCardAppRestTemplate.setRequestFactory(getClientHttpRequestFactory());

		PaymentCardResponseForAppAPIs response = new PaymentCardResponseForAppAPIs();
		try {

			try {
				logger.info("Requesting from " + cCServiceAppUrl + " ==> " + requestEntity);
				response = payCardAppRestTemplate.postForObject(cCServiceAppUrl, requestEntity, PaymentCardResponseForAppAPIs.class);
				logger.info("Response from " + cCServiceAppUrl + " ==> " + response);
				return response;

			} catch (ResourceAccessException e) {
				logger.error("Processcardpayment error occured - ResourceAccessException", e);
				throw e.getCause();
			}

		}catch(SocketTimeoutException e) {
			logger.error("Service call to ==> {} failed due to : {}", cCServiceAppUrl, e);
			logger.error("Error occured",e);
			response.setCode((long) 9000);
			response.setSubCode((long) ErrorCodeMessage.FAIL_ERROR_CODE);
			response.setMessage("FAILED_IN_PROCESS_DO_CC_SETTLEMENT");
			response.setMessageCustomer(e.getLocalizedMessage());
			return response;
		} catch (Exception e) {
			logger.error("Service call to ==> {} failed due to : {}", cCServiceAppUrl, e);
			logger.error("Error occured",e);
			response.setCode((long) ErrorCodeMessage.FAIL_ERROR_CODE);
			response.setSubCode((long) ErrorCodeMessage.FAIL_ERROR_CODE);
			response.setMessage("System error");
			response.setMessageCustomer(e.getLocalizedMessage());
			return response;
		}
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {

	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
	      = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setReadTimeout(timeout);
	    
	    return clientHttpRequestFactory;
	}


	public String getCardNumberFromSerial(Long cardSerial) {
		// set time
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				String requestTime = sdf.format(new Date());

				GetCardNumberRequest requestRest = new GetCardNumberRequest( cardSerial,requestTime);
				// add request headers
				HttpHeaders headers = new HttpHeaders();
				headers.set("ServiceName", getcardNumberServicename);
				headers.set("TokenID", "0");
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<GetCardNumberRequest> requestEntity = new HttpEntity<GetCardNumberRequest>(requestRest,
						headers);

				GetCardNumberResponse response = null;
				try {
					logger.info("Requesting from " + getcardNumberUri  +" ==> "+ requestEntity);
					response = getCardNumberRestTemplate.postForObject(getcardNumberUri , requestEntity,
							GetCardNumberResponse.class);
					
					//response log is disabled due to the credit card numbers are captured in the log
					logger.info("Response from " + getcardNumberUri +" ==> disableLogs");
					return response.getCardNumber();
					
				} catch (Exception e) {
					logger.error("Service call to ==> {} failed due to : {}", getcardNumberUri, e);
					
					return null;
				}
	}

	public String  getCardNumberFromSerialForNewAPIs(PayCreditCardRequestForApp request) {
		// set time
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String requestTime = sdf.format(new Date());

		GetCardNumberRequestForAppAPIs requestRest = new GetCardNumberRequestForAppAPIs(requestTime,request.getCardSerialNumber(),
				request.getInitiatedSerno(),request.getInitiatedKey(),request.getChainSerno(),request.getChainAuth(),request.getDeviceId());

		// add request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("ServiceName", getcardNumberServicenameApp);
		headers.set("IdentityType", "Nic");
		headers.set("IdentityValue", request.getNIC());
		headers.set("TokenID", "0");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<GetCardNumberRequestForAppAPIs> requestEntity = new HttpEntity<GetCardNumberRequestForAppAPIs>(requestRest,
				headers);

		GetCardNumberResponseForAppAPIs response = null;
		try {
			logger.info("Requesting from " + getcardNumberUriApp +" ==> "+ requestEntity);
			response = getCardNumberAppRestTemplate.postForObject(getcardNumberUriApp, requestEntity,
					GetCardNumberResponseForAppAPIs.class);
			logger.info("Response from " + getcardNumberUriApp + " ==> " + response);
			//response log is disabled due to the credit card numbers are captured in the log
//			logger.info("Response from " + getcardNumberUriNew +" ==> disableLogs");
			return response.getCardNumber();

		} catch (Exception e) {
			logger.error("Service call to ==> {} failed due to : {}", getcardNumberUriApp, e);

			return null;
		}
	}

	public GetCardSerailResponse getSerialFromCardNumber(String cardNumber) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String requestTime = sdf.format(new Date());

		GetCardSerialRequest requestRest = new GetCardSerialRequest(cardNumber, requestTime);

		HttpHeaders headers = new HttpHeaders();
		headers.set("ServiceName", getcardSerialServicename);
		headers.set("TokenID", "0");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<GetCardSerialRequest> requestEntity = new HttpEntity<GetCardSerialRequest>(requestRest, headers);

		GetCardSerailResponse response = null;
		try {
			//log is disabled due to the credit card numbers are captured in the log
			logger.info("Requesting from " + getcardSerialUri + " ==> disableLogs ");
			response = getCardSerailRestTemplate.postForObject(getcardSerialUri, requestEntity,
					GetCardSerailResponse.class);
			logger.info("Response from " + getcardSerialUri + " ==> " + response);
			return response;
		} catch (Exception e) {
			logger.error("Service call to ==> {} failed due to : {}", getcardSerialUri, e);
			ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_019.toString(),
					InvokeMessage.EXCEPTION_OCCURED_WHEN_RETRIVE_CARD_SERIAL.toString());
			response.setResponseHeader(resHeader);
			return response;
		}
	}

	public GetCardSerialResponseForAppAPIs getSerialFromCardNumberForAppAPIs(String cardNumber, DoTransferReqForCard dtrfc) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String requestTime = sdf.format(new Date());

		GetCardSerialRequestForAppAPIs requestRest = new GetCardSerialRequestForAppAPIs(requestTime, "XXXXXXXXXXXXXX", dtrfc.getEncryptionType(),
				dtrfc.getInitiatedSerno(), dtrfc.getInitiatedKey(), dtrfc.getChainSerno(), dtrfc.getChainAuth(), dtrfc.getDeviceId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("ServiceName", getcardSerialServicenameApp);
		headers.set("IdentityType", dtrfc.getIdentityType());
		headers.set("IdentityValue", dtrfc.getNIC());
		headers.set("TokenID", "0");
		headers.setContentType(MediaType.APPLICATION_JSON);

		logger.info("Requesting from " + getcardSerialUriApp + " ==> " + "IdentityType:" + dtrfc.getIdentityType() +
				",IdentityValue:" + dtrfc.getNIC() + ",RequestRest:" + requestRest.toString());
		requestRest.setCardNumber(cardNumber);

		HttpEntity<GetCardSerialRequestForAppAPIs> requestEntity = new HttpEntity<GetCardSerialRequestForAppAPIs>(requestRest, headers);

		GetCardSerialResponseForAppAPIs response = null;
		try {
			//log is disabled due to the credit card numbers are captured in the log

			response = getCardSerialAppRestTemplate.postForObject(getcardSerialUriApp, requestEntity,
					GetCardSerialResponseForAppAPIs.class);
			logger.info("Response from " + getcardSerialUriApp + " ==> " + response.toString());

			return response;
		} catch (Exception e) {
			logger.error("Service call to ==> {} failed due to : {}", getcardSerialUriApp, e);
			ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_019.toString(),
					InvokeMessage.EXCEPTION_OCCURED_WHEN_RETRIVE_CARD_SERIAL.toString());
			response.setResponseHeader(resHeader);
			return response;
		}
	}

	public CCpaymentCardResponse ccpaymentCard(PayCreditCardRequest request) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String requestTime = sdf.format(new Date());

		SampathCCPaymentRequest requestRest = new SampathCCPaymentRequest(request.getCardSerialNumber(),
				request.getDebitAccountNumber(), request.getAmount(), requestTime);

		HttpHeaders headers = new HttpHeaders();
		headers.set("ServiceName", "ProcessCardPaymentforOmni");
		headers.set("TokenID", "0");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<SampathCCPaymentRequest> requestEntity = new HttpEntity<SampathCCPaymentRequest>(requestRest,
				headers);

		CCpaymentCardResponse response = null;
		try {
			logger.info("Requesting from " + cCServiceUrl + "/processcardpayment ==> " + requestEntity);
			response = payCardRestTemplate.postForObject(cCServiceUrl + "/processcardpayment", requestEntity,
					CCpaymentCardResponse.class);
			logger.info("Response from " + cCServiceUrl + "/processcardpayment ==> " + response);
			return response;
		} catch (Exception e) {
			logger.error("Service call to ==> {} failed due to : {}", cCServiceUrl + "/processcardpayment", e);
			ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_020.toString(),
					InvokeMessage.EXCEPTION_OCCURED_WHEN_PROCESS_CARD_PAYMENT.toString());
			response.setResponseHeader(resHeader);
			return response;
		}
	}

	public CCpaymentCardResponseForAppAPIs ccpaymentCardForAppAPIs(PayCreditCardRequest request) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String requestTime = sdf.format(new Date());

		SampathCCPaymentRequestforAppAPIs requestRest = new SampathCCPaymentRequestforAppAPIs(request.getCardSerialNumber(),
				"reducted", request.getAmount(), requestTime, request.getInitiatedSerno(),
				request.getInitiatedKey(), request.getChainSerno(), request.getChainAuth(), request.getDeviceId(), request.getPaymentCurrency());

		HttpHeaders headers = new HttpHeaders();
		headers.set("ServiceName", cCServiceAppServicename);
		headers.set("IdentityType", request.getIdentityType());
		headers.set("IdentityValue", request.getNIC());
		headers.set("TokenID", "0");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<SampathCCPaymentRequestforAppAPIs> requestEntity = new HttpEntity<SampathCCPaymentRequestforAppAPIs>(requestRest,
				headers);
		logger.info("Requesting from " + cCServiceAppUrl + " ==> " + "IdentityType:" + request.getIdentityType() +
				",IdentityValue:" + request.getNIC() + ",RequestRest:" + requestEntity);

		requestRest.setDebitAccountNumber(request.getDebitAccountNumber());
		CCpaymentCardResponseForAppAPIs response = null;
		try {
			response = payCardAppRestTemplate.postForObject(cCServiceAppUrl, requestEntity, CCpaymentCardResponseForAppAPIs.class);
			logger.info("Response from " + cCServiceAppUrl + " ==> " + response);
			return response;
		} catch (Exception e) {
			logger.error("Service call to ==> {} failed due to : {}", cCServiceAppUrl, e);
			ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_020.toString(),
					InvokeMessage.EXCEPTION_OCCURED_WHEN_PROCESS_CARD_PAYMENT.toString());
			response.setResponseHeader(resHeader);
			return response;
		}
	}
}
