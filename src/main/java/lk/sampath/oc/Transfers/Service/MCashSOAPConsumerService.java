package lk.sampath.oc.Transfers.Service;

import java.net.URL;

import javax.xml.ws.BindingProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.sampath.oc.Transfers.integration.mcash.Create;
import lk.sampath.oc.Transfers.integration.mcash.GetCommission;
import lk.sampath.oc.Transfers.integration.mcash.GetCommissionResponse;
import lk.sampath.oc.Transfers.integration.mcash.MCashAuthenticationException_Exception;
import lk.sampath.oc.Transfers.integration.mcash.MCashException_Exception;
import lk.sampath.oc.Transfers.integration.mcash.MCashServices;
import lk.sampath.oc.Transfers.integration.mcash.MCashServices_Service;
import lk.sampath.oc.Transfers.integration.mcash.MCashSystemException_Exception;
import lk.sampath.oc.Transfers.integration.mcash.MCashValidationException_Exception;
import lk.sampath.oc.Transfers.integration.mcash.ResultData;
import lk.sampath.oc.Transfers.integration.mcash.Reverse;
import lk.sampath.oc.Transfers.integration.mcash.Withdraw;
import lk.sampath.oc.Transfers.response.CustomMcashResponse;
import lk.sampath.oc.Transfers.utils.JacksonConfig;

@Service
public class MCashSOAPConsumerService extends WebServiceGatewaySupport {

	private static Logger logger = LogManager.getLogger(MCashSOAPConsumerService.class);
	
	@Value("${service.url.mcash.endpoint}")
	private String mCashEndpoint;
	
	@Autowired
	private JacksonConfig jacksonConfig;
	@Autowired
	private ObjectMapper objectMapper;

//	WithdrawResponse withdrawMCash(Withdraw withdrawReq) {
//		logger.info("calling soap api for mcash withdraw");
//		JAXBElement<WithdrawResponse> response = (JAXBElement<WithdrawResponse>) getWebServiceTemplate()
//				.marshalSendAndReceive(mCashEndpoint, withdrawReq, new SoapActionCallback(""));
//		System.out.println(response.getValue().toString());
//		return response.getValue();
//	}

	public CustomMcashResponse withdrawMCash(Withdraw withdrawReq) {
		URL url = ClassLoader.getSystemResource("wsdl/MCashServices.wsdl");
		MCashServices_Service service = new MCashServices_Service(url);
		MCashServices proxy = service.getMCashServicesPort();
		((BindingProvider) proxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mCashEndpoint);
		logger.info("Using endpoint URL: "
				+ ((BindingProvider) proxy).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		try {
			String benePin = withdrawReq.getWithdrawData().getBenePin();
			withdrawReq.getWithdrawData().setBenePin("reducted");
			logger.info("SOAP Request : {}", objectMapper.writeValueAsString(withdrawReq));
			withdrawReq.getWithdrawData().setBenePin(benePin);
		} catch (JsonProcessingException e) {
			logger.error("Error while object mapping : {}", e.getLocalizedMessage());
		}
		ResultData resp = null;
		CustomMcashResponse customResp = new CustomMcashResponse();

		try {
			resp = proxy.withdraw(withdrawReq.getUser(), withdrawReq.getWithdrawData());
			customResp = objectMapper.convertValue(resp, CustomMcashResponse.class);
			customResp.setFlag(true);
		} catch (MCashSystemException_Exception e) {
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
		} catch (MCashException_Exception e) {
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
		} catch (MCashAuthenticationException_Exception e) {
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
		} catch (MCashValidationException_Exception e) {
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
		}

		return customResp;
	}
	
//	ReverseResponse reverseMobileCash(Reverse reverseReq) {
//		logger.info("calling soap api for mcash reverse");
//		JAXBElement<ReverseResponse> response = (JAXBElement<ReverseResponse>) getWebServiceTemplate()
//				.marshalSendAndReceive(mCashEndpoint, reverseReq, new SoapActionCallback(""));
//		logger.info(response.getValue().toString());
//		return response.getValue();
//
//	}
	
	public CustomMcashResponse reverseMobileCash(Reverse reverseReq) {
		URL url = ClassLoader.getSystemResource("wsdl/MCashServices.wsdl");
		MCashServices_Service service = new MCashServices_Service(url);
		MCashServices proxy = service.getMCashServicesPort();
		((BindingProvider) proxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mCashEndpoint);
		logger.info("Using endpoint URL: "
				+ ((BindingProvider) proxy).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		try {
			String senderPin = reverseReq.getReverseData().getSenderPin();
			reverseReq.getReverseData().setSenderPin("reducted");
			logger.info("SOAP Request : {}", objectMapper.writeValueAsString(reverseReq));
			reverseReq.getReverseData().setSenderPin(senderPin);
		} catch (JsonProcessingException e) {
			logger.error("Error while object mapping : {}", e.getLocalizedMessage());
		}
		ResultData resp = null;
		CustomMcashResponse customResp = new CustomMcashResponse();
		try {
			resp = proxy.reverse(reverseReq.getUser(), reverseReq.getReverseData());
			customResp = objectMapper.convertValue(resp, CustomMcashResponse.class);
			customResp.setFlag(true);
		} catch (MCashSystemException_Exception e) {
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
		} catch (MCashException_Exception e) {
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
		} catch (MCashAuthenticationException_Exception e) {
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
		} catch (MCashValidationException_Exception e) {
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
		}
		return customResp;
	}

	public CustomMcashResponse createMCash(Create createReq) {

		URL url = ClassLoader.getSystemResource("wsdl/MCashServices.wsdl");

		MCashServices_Service service = new MCashServices_Service(url);

		MCashServices proxy = service.getMCashServicesPort();
		((BindingProvider) proxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mCashEndpoint);

		logger.info("Using endpoint URL: "
				+ ((BindingProvider) proxy).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		logger.info("SOAP Request", createReq.toString());
		logger.info("SOAP Request", jacksonConfig.convertToJson(createReq));
//		ResultData response = proxy.create(createReq.getUser(), createReq.getCreateData());
		CustomMcashResponse customResp = new CustomMcashResponse();
		
		try {
			ResultData response = proxy.create(createReq.getUser(), createReq.getCreateData());
			if(response.getStatus()==0) {
				customResp.setFlag(true);
				customResp.setDescription(response.getDescription());
				customResp.setSmcTranId(response.getSmcTranId());
				customResp.setStatus(response.getStatus());
			}else {
				logger.error("Mobile Cash Transfer Error -"+response.getDescription().toString());
				customResp.setFlag(false);
				customResp.setDescription(response.getDescription());
				customResp.setSmcTranId(response.getSmcTranId());
				customResp.setStatus(response.getStatus());
			}
			return customResp;
			
		} catch (MCashSystemException_Exception e) {
			// TODO Auto-generated catch block
			logger.error("Mobile Cash Transfer Error",e.getLocalizedMessage());
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
			return customResp;
		} catch (MCashException_Exception e) {
			// TODO Auto-generated catch block
			logger.error("Mobile Cash Transfer Error",e.getLocalizedMessage());
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
			return customResp;
		} catch (MCashAuthenticationException_Exception e) {
			// TODO Auto-generated catch block
			logger.error("Mobile Cash Transfer Error",e.getLocalizedMessage());
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
			return customResp;
		} catch (MCashValidationException_Exception e) {
			// TODO Auto-generated catch block
			logger.error("Mobile Cash Transfer Error",e.getLocalizedMessage());
			customResp.setFlag(false);
			customResp.setDescription(e.getLocalizedMessage());
			customResp.setStatus(-99);
			return customResp;
		}

//		logger.info("calling soap api for mcash create");
//		JAXBElement<CreateResponse> response = (JAXBElement<CreateResponse>) getWebServiceTemplate()
//				// .marshalSendAndReceive("http://192.125.125.96:7003/MobileCashServices/MCashServices",
//				// createReq,
//				// .marshalSendAndReceive("http://192.168.80.94:8088/mockMCashServicesPortBinding",
//				// createReq,
//				// .marshalSendAndReceive("http://localhost:8088/mockMCashServicesPortBinding",
//				// createReq,
//				.marshalSendAndReceive(mCashEndpoint, createReq, new SoapActionCallback(""));
//		System.out.println(response.getValue().toString());

	}

	public GetCommissionResponse getCommisionsMCash(GetCommission getCommision) {

		URL url = ClassLoader.getSystemResource("wsdl/MCashServices.wsdl");
		MCashServices_Service service = new MCashServices_Service(url);
		MCashServices proxy = service.getMCashServicesPort();
		((BindingProvider) proxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mCashEndpoint);
		logger.info("Using endpoint URL: "
				+ ((BindingProvider) proxy).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		
		logger.info("GetCommission Request : {}", jacksonConfig.convertToJson(getCommision));
		
		double resp = 0;
		GetCommissionResponse customResp = new GetCommissionResponse();

		try {
			resp = proxy.getCommission(getCommision.getUser(), getCommision.getCommData());
			logger.info("GetCommission Response : {}",resp);
			customResp.setReturn(resp);
			logger.info("GetCommission Response : {}", jacksonConfig.convertToJson(customResp));
		} catch (MCashSystemException_Exception e) {
			customResp.setReturn(0);
			logger.error("Get Commission error occured", e);
		} catch (MCashException_Exception e) {
			customResp.setReturn(0);
			logger.error("Get Commission error occured", e);
		} catch (MCashAuthenticationException_Exception e) {
			customResp.setReturn(0);
			logger.error("Get Commission error occured", e);
		} catch (MCashValidationException_Exception e) {
			customResp.setReturn(0);
			logger.error("Get Commission error occured", e);
		}

		return customResp;
	}
}
