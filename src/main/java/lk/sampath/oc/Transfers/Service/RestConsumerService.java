package lk.sampath.oc.Transfers.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import lk.sampath.oc.Transfers.request.App.DoTransferRequestAppForValidation;
import lk.sampath.oc.Transfers.request.App.DoTransferRequestForAppNet;
import lk.sampath.oc.Transfers.request.App.MLTranCategorySingleReq;
import lk.sampath.oc.Transfers.request.App.MLTransferReq;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.sampath.oc.Transfers.Entity.TransactionRequest;
import lk.sampath.oc.Transfers.Enums.ErrorCode;
import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Pojo.DailyTransactionDTO;
import lk.sampath.oc.Transfers.Pojo.GetCEFTEnabledFlagRequest;
import lk.sampath.oc.Transfers.Pojo.GetCEFTEnabledFlagResponse;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lk.sampath.oc.Transfers.Pojo.UpdateDailyLimitsOutput;
import lk.sampath.oc.Transfers.Pojo.ValidateTransferOutput;
import lk.sampath.oc.Transfers.Pojo.ValidateTransferResponse;
import lk.sampath.oc.Transfers.utils.JacksonConfig;

@Service
public class RestConsumerService {

	private static Logger logger = LogManager.getLogger(RestConsumerService.class);

	@Autowired
	private JacksonConfig jacksonConfig;
	
	RestTemplate restTemplate = new RestTemplate();

	@Value("${service.url.transferDetail.endpoint}")
	private String transferDetailServiceBaseUrl;

	@Value("${service.url.approvals.endpoint}")
	private String approvalsBaseUrl;

	@Value("${service.url.getCEFTEnabled.endpoint}")
	private String getCEFTEnabledFlagFancleURL;
	
	@Autowired
	@Qualifier("TRDOMNIChannelAPI")
	private RestTemplate tRDOMNIChannelAPI;
	
	@Autowired
	@Qualifier("poolTRD")
	private PoolingHttpClientConnectionManager poolConnectorForTRD;
	
	public ValidateTransferResponse validateTransactionDetails(TransactionRequest requestBody, String uuid) {

		String fromAccount = requestBody.getFromAccountNumber();
		requestBody.setFromAccountNumber("reducted");
		String toAccount = requestBody.getToAccountNumber();
		requestBody.setToAccountNumber("reducted");
		logger.info("calling validate transfer details rest api Request"+ jacksonConfig.convertToJson(requestBody));
		requestBody.setFromAccountNumber(fromAccount);
		requestBody.setToAccountNumber(toAccount);
		
		ValidateTransferResponse validateTransferDetailResponse = new ValidateTransferResponse();
//		String transferDetailServiceUrl ="http://192.125.125.111:9081/trd/validateTransferDetail";
		String transferDetailServiceUrl = transferDetailServiceBaseUrl + "/validateTransferDetail";
		URI transferDetailServiceUri;
		HttpHeaders header = new HttpHeaders();
		header.set("X-Request-ID", uuid);
		
		HttpEntity<TransactionRequest> request = new HttpEntity<TransactionRequest>(requestBody,header);
		try {
			logger.info("validateTransferDetail api pool {}",poolConnectorForTRD.getTotalStats());
			transferDetailServiceUri = new URI(transferDetailServiceUrl);
			ResponseEntity<ValidateTransferOutput> validateTrnferDetailOut = tRDOMNIChannelAPI.exchange(transferDetailServiceUri,HttpMethod.POST,
					request, ValidateTransferOutput.class);

			ValidateTransferOutput validateTransferDetailOut = validateTrnferDetailOut.getBody();
			
			logger.info("calling validate transfer details rest api response"+ jacksonConfig.convertToJson(validateTransferDetailOut));
			
			ResponseHeader resHeader;
			if (validateTransferDetailOut.getInvokeStatus().equals(InvokeStatus.SUCCESS)) {
				resHeader = new ResponseHeader(
						InvokeStatus.valueOf(validateTransferDetailOut.getInvokeStatus().toString()),
						InvokeMessage.VALIDATE_TRANSFER_DETAILS_SUCCESS.toString());
			} else {
				resHeader = new ResponseHeader(
						InvokeStatus.valueOf(validateTransferDetailOut.getInvokeStatus().toString()),
						ErrorCode.TR_008.toString(), validateTransferDetailOut.getInvokeMessage().toString());
			}
			validateTransferDetailResponse.setResponseHeader(resHeader);

		}catch (HttpClientErrorException e) {
			String responseString = e.getResponseBodyAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				logger.error("Validate transfer failed due to : {}", e.getLocalizedMessage());
				ValidateTransferOutput validateTransferDetailResp = mapper.readValue(responseString,ValidateTransferOutput.class);
				ResponseHeader resHeader = new ResponseHeader(validateTransferDetailResp.getInvokeStatus(),
						validateTransferDetailResp.getInvokeCode(),
						validateTransferDetailResp.getInvokeMessage());
				validateTransferDetailResponse.setResponseHeader(resHeader);
			} catch (Exception ex) {
				logger.error("Validate transfer failed due to : {}", ex.getLocalizedMessage());
				ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
						ex.getMessage());
				validateTransferDetailResponse.setResponseHeader(resHeader);
			}
		} catch (RestClientException e) {
			logger.error("invoke failed. cannot access url : {}", e.getLocalizedMessage());
			ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
					e.getMessage());
			validateTransferDetailResponse.setResponseHeader(resHeader);
		} catch (Exception e) {
			logger.info("invoke failed. cannot access url");
			logger.error("Consumer Connection error",e);
			ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
					InvokeMessage.INVOKE_VALIDATE_TRANSFER_DETAIL_SERVICE_FAILED.toString());
			validateTransferDetailResponse.setResponseHeader(resHeader);
		}
		return validateTransferDetailResponse;
	}

	public ValidateTransferResponse validateTransactionDetailsForApp(DoTransferRequestForAppNet requestBody, String reqID, String userName, String IdentityType, String IdentityValue) {
		DoTransferRequestAppForValidation doTrnRequestAppForValidation = new DoTransferRequestAppForValidation();
		BeanUtils.copyProperties(requestBody, doTrnRequestAppForValidation);
		doTrnRequestAppForValidation.setUserName(userName);
		doTrnRequestAppForValidation.setIdentityType(IdentityType);
		doTrnRequestAppForValidation.setNIC(IdentityValue);

		String fromAccount = doTrnRequestAppForValidation.getFromAccountNumber();
		doTrnRequestAppForValidation.setFromAccountNumber("reducted");
		String toAccount = doTrnRequestAppForValidation.getToAccountNumber();
		doTrnRequestAppForValidation.setToAccountNumber("reducted");
		logger.info("calling validate transfer details rest api Request"+ jacksonConfig.convertToJson(doTrnRequestAppForValidation));
		doTrnRequestAppForValidation.setFromAccountNumber(fromAccount);
		doTrnRequestAppForValidation.setToAccountNumber(toAccount);

		ValidateTransferResponse validateTransferDetailResponse = new ValidateTransferResponse();
//		String transferDetailServiceUrl ="http://192.125.125.111:9081/trd/validateTransferDetail";
		String transferDetailServiceUrl = transferDetailServiceBaseUrl + "/validateTransferDetail";
		URI transferDetailServiceUri;
		HttpHeaders header = new HttpHeaders();
		header.set("X-Request-ID", reqID);

		HttpEntity<DoTransferRequestAppForValidation> request = new HttpEntity<DoTransferRequestAppForValidation>(doTrnRequestAppForValidation,header);
		try {
			logger.info("Requesting from " + transferDetailServiceUrl);
			logger.info("validateTransferDetail api pool {}",poolConnectorForTRD.getTotalStats());
			transferDetailServiceUri = new URI(transferDetailServiceUrl);
			ResponseEntity<ValidateTransferOutput> validateTrnferDetailOut = tRDOMNIChannelAPI.exchange(transferDetailServiceUri,HttpMethod.POST,
					request, ValidateTransferOutput.class);

			ValidateTransferOutput validateTransferDetailOut = validateTrnferDetailOut.getBody();
			logger.info("Response from " + transferDetailServiceUrl);
			logger.info("calling validate transfer details rest api response"+ jacksonConfig.convertToJson(validateTransferDetailOut));

			ResponseHeader resHeader;
			if (validateTransferDetailOut.getInvokeStatus().equals(InvokeStatus.SUCCESS)) {
				resHeader = new ResponseHeader(
						InvokeStatus.valueOf(validateTransferDetailOut.getInvokeStatus().toString()), "000",
						InvokeMessage.VALIDATE_TRANSFER_DETAILS_SUCCESS.toString());
			} else {
				resHeader = new ResponseHeader(
						InvokeStatus.valueOf(validateTransferDetailOut.getInvokeStatus().toString()),
						ErrorCode.TR_008.toString(), validateTransferDetailOut.getInvokeMessage().toString());
			}
			validateTransferDetailResponse.setResponseHeader(resHeader);

		}catch (HttpClientErrorException e) {
			String responseString = e.getResponseBodyAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				logger.error("Validate transfer failed due to : {}", e.getLocalizedMessage());
				ValidateTransferOutput validateTransferDetailResp = mapper.readValue(responseString,ValidateTransferOutput.class);
				ResponseHeader resHeader = new ResponseHeader(validateTransferDetailResp.getInvokeStatus(),
						validateTransferDetailResp.getInvokeCode(),
						validateTransferDetailResp.getInvokeMessage());
				validateTransferDetailResponse.setResponseHeader(resHeader);
			} catch (Exception ex) {
				logger.error("Validate transfer failed due to : {}", ex.getLocalizedMessage());
				ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
						ex.getMessage());
				validateTransferDetailResponse.setResponseHeader(resHeader);
			}
		} catch (RestClientException e) {
			logger.error("invoke failed. cannot access url : {}", e.getLocalizedMessage());
			ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
					e.getMessage());
			validateTransferDetailResponse.setResponseHeader(resHeader);
		} catch (Exception e) {
			logger.info("invoke failed. cannot access url");
			logger.error("Consumer Connection error",e);
			ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
					InvokeMessage.INVOKE_VALIDATE_TRANSFER_DETAIL_SERVICE_FAILED.toString());
			validateTransferDetailResponse.setResponseHeader(resHeader);
		}
		return validateTransferDetailResponse;
	}

	public List<ValidateTransferResponse> validateTransactionDetailsMLForApp(MLTransferReq mlTransferReq, String reqID, String userName, String IdentityType, String IdentityValue) {

		List<DoTransferRequestAppForValidation> doTransferValidationMLList = new ArrayList<>();
		List<ValidateTransferResponse> validateTransferResponseList = new ArrayList<>();
		List<MLTranCategorySingleReq> tranList = mlTransferReq.getTranList();

		for(MLTranCategorySingleReq validationList : tranList){
			DoTransferRequestAppForValidation doTransferRequestAppForValidation = new DoTransferRequestAppForValidation();
			BeanUtils.copyProperties(validationList, doTransferRequestAppForValidation);
			doTransferRequestAppForValidation.setUserName(userName);
			doTransferRequestAppForValidation.setFromAccountNumber(mlTransferReq.getDebitAccount());
			doTransferRequestAppForValidation.setCurrency(mlTransferReq.getCurrency());
			doTransferRequestAppForValidation.setIdentityType(IdentityType);
			doTransferRequestAppForValidation.setNIC(IdentityValue);
			doTransferValidationMLList.add(doTransferRequestAppForValidation);
		}

//		String transferDetailServiceUrl ="http://192.125.125.111:9081/trd/validateTransferDetail";
		String transferDetailServiceUrl = transferDetailServiceBaseUrl + "/validateTransferDetail";
		URI transferDetailServiceUri;
		HttpHeaders header = new HttpHeaders();
		header.set("X-Request-ID", reqID);

		int index = 0;
		for(DoTransferRequestAppForValidation dtReq : doTransferValidationMLList) {
			ValidateTransferResponse validateTransferDetailResponse = new ValidateTransferResponse();
			HttpEntity<DoTransferRequestAppForValidation> request = new HttpEntity<DoTransferRequestAppForValidation>(dtReq, header);
			try {
				logger.info("Requesting from " + transferDetailServiceUrl);
				logger.info("validateTransferDetail api pool {}", poolConnectorForTRD.getTotalStats());
				transferDetailServiceUri = new URI(transferDetailServiceUrl);
				ResponseEntity<ValidateTransferOutput> validateTrnferDetailOut = tRDOMNIChannelAPI.exchange(transferDetailServiceUri, HttpMethod.POST,
						request, ValidateTransferOutput.class);

				ValidateTransferOutput validateTransferDetailOut = validateTrnferDetailOut.getBody();
				logger.info("Response from " + transferDetailServiceUrl);
				logger.info("calling validate transfer details rest api response" + jacksonConfig.convertToJson(validateTransferDetailOut));

				ResponseHeader resHeader;
				if (validateTransferDetailOut.getInvokeStatus().equals(InvokeStatus.SUCCESS)) {
					resHeader = new ResponseHeader(
							InvokeStatus.valueOf(validateTransferDetailOut.getInvokeStatus().toString()), "000",
							InvokeMessage.VALIDATE_TRANSFER_DETAILS_SUCCESS.toString());
				} else {
					resHeader = new ResponseHeader(
							InvokeStatus.valueOf(validateTransferDetailOut.getInvokeStatus().toString()),
							ErrorCode.TR_008.toString(), validateTransferDetailOut.getInvokeMessage().toString());
				}
				validateTransferDetailResponse.setResponseHeader(resHeader);

			} catch (HttpClientErrorException e) {
				String responseString = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				try {
					logger.error("Validate transfer failed due to : {}", e.getLocalizedMessage());
					ValidateTransferOutput validateTransferDetailResp = mapper.readValue(responseString, ValidateTransferOutput.class);
					ResponseHeader resHeader = new ResponseHeader(validateTransferDetailResp.getInvokeStatus(),
							validateTransferDetailResp.getInvokeCode(),
							validateTransferDetailResp.getInvokeMessage());
					validateTransferDetailResponse.setResponseHeader(resHeader);
				} catch (Exception ex) {
					logger.error("Validate transfer failed due to : {}", ex.getLocalizedMessage());
					ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
							ex.getMessage());
					validateTransferDetailResponse.setResponseHeader(resHeader);
				}
			} catch (RestClientException e) {
				logger.error("invoke failed. cannot access url : {}", e.getLocalizedMessage());
				ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
						e.getMessage());
				validateTransferDetailResponse.setResponseHeader(resHeader);
			} catch (Exception e) {
				System.out.println("invoke failed. cannot access url");
				logger.error("Consumer Connection error", e);
				ResponseHeader resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
						InvokeMessage.INVOKE_VALIDATE_TRANSFER_DETAIL_SERVICE_FAILED.toString());
				validateTransferDetailResponse.setResponseHeader(resHeader);
			}
			index++;
			validateTransferDetailResponse.setTranIndex(String.valueOf(index));
			validateTransferDetailResponse.setTransferType(String.valueOf(dtReq.getTransactionCategory()));
			validateTransferResponseList.add(validateTransferDetailResponse);
		}
		return validateTransferResponseList;
	}

	public void saveDailyTransactionLimits(DailyTransactionDTO dailyTransaction, String uuid) throws URISyntaxException {
		logger.info("calling update daily limits rest api" + jacksonConfig.convertToJson(dailyTransaction));
		// String transferDetailServiceUrl
		HttpHeaders header = new HttpHeaders();
		header.set("X-Request-ID", uuid);
		HttpEntity<DailyTransactionDTO> request = new HttpEntity<DailyTransactionDTO>(dailyTransaction,header);
		// ="http://192.125.125.111:9081/trd/updateDailyLimits";
		String transferDetailServiceUrl = transferDetailServiceBaseUrl + "/updateDailyLimits";
		URI transferDetailServiceUri = new URI(transferDetailServiceUrl);
		UpdateDailyLimitsOutput saveDailyTransactionResponse = null;
		
		logger.info("updateDailyLimits api pool {}",poolConnectorForTRD.getTotalStats());
		saveDailyTransactionResponse = tRDOMNIChannelAPI.postForObject(transferDetailServiceUri, request,
				UpdateDailyLimitsOutput.class);
	
		logger.info(jacksonConfig.convertToJson(saveDailyTransactionResponse));

	}
	
	public void revertDailyTransactionLimits(DailyTransactionDTO dailyTransaction, String uuid) throws URISyntaxException {
		logger.info("calling update daily limits rest api" + jacksonConfig.convertToJson(dailyTransaction));
		// String transferDetailServiceUrl
		
		HttpHeaders header = new HttpHeaders();
		header.set("X-Request-ID", uuid);
		HttpEntity<DailyTransactionDTO> request = new HttpEntity<DailyTransactionDTO>(dailyTransaction,header);
		// ="http://192.125.125.111:9081/trd/updateDailyLimits";
		String transferDetailServiceUrl = transferDetailServiceBaseUrl + "/revertLimits";
		URI transferDetailServiceUri = new URI(transferDetailServiceUrl);
		UpdateDailyLimitsOutput saveDailyTransactionResponse = null;
		
		logger.info("revertLimits api pool {}",poolConnectorForTRD.getTotalStats());
		saveDailyTransactionResponse = tRDOMNIChannelAPI.postForObject(transferDetailServiceUri, request,
				UpdateDailyLimitsOutput.class);
		logger.info(jacksonConfig.convertToJson(saveDailyTransactionResponse));

	}
	
	public GetCEFTEnabledFlagResponse getCEFTEnabledFlag(String bankCode) throws Exception {

		GetCEFTEnabledFlagResponse getCEFTEnabledFlagResponse = null;
		try {

			logger.info("Calling getCEFTEnabledFlag rest finacle api" + bankCode);
			String getCEFTEnabledFlagUrl = getCEFTEnabledFlagFancleURL;
			URI getCEFTEnabledFlagUri = new URI(getCEFTEnabledFlagUrl);
			GetCEFTEnabledFlagRequest getCEFTEnabledFlagRequest = new GetCEFTEnabledFlagRequest();
			getCEFTEnabledFlagRequest.setBnk_code(bankCode);
			getCEFTEnabledFlagResponse = restTemplate.postForObject(getCEFTEnabledFlagUri, getCEFTEnabledFlagRequest,
					GetCEFTEnabledFlagResponse.class);
			logger.info("Response getCEFTEnabledFlag " + jacksonConfig.convertToJson(getCEFTEnabledFlagResponse));

		} catch (Exception e) {
			logger.info("Error occured getCEFTEnabledFlag " ,e);
			throw new Exception(InvokeMessage.ERROR_ON_CEFT_FLAG_INQUIRY.toString());
		}

		return getCEFTEnabledFlagResponse;

	}

}
