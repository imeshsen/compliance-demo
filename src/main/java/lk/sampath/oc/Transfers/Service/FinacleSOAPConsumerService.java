package lk.sampath.oc.Transfers.Service;

import java.net.SocketTimeoutException;
import java.net.URL;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.soap.SoapFaultException;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoInterBankTransferRequestType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoInterBankTransferResponseType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoSlipTransactionFault;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoSlipTransactionRequestType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoSlipTransactionResponseType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoTransferRequestType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoTransferResponseType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.IIBFinacleIntegration;
import lk.sampath.oc.Transfers.integration.finacleIntegration.IIBFinacleIntegration_Service;

@Service
@SuppressWarnings("unchecked")
public class FinacleSOAPConsumerService extends WebServiceGatewaySupport{

	private static Logger logger = LogManager.getLogger(FinacleSOAPConsumerService.class);
	
	@Value("${config.finacle.timeout}")
	private int timeout;
	
	@Value("${service.url.iib.finacle.wsdl}")
	private String iibFinacleWsdl;

	@Autowired
	private ObjectMapper objectMapper;
	
	ResponseHeader validateDebitAccount() {
		// TODO
		ResponseHeader res = new ResponseHeader(InvokeStatus.SUCCESS,
				InvokeMessage.VALIDATE_DEBIT_ACCOUNT_SUCCESS.toString());
		return res;
	}

	ResponseHeader getAdditionalDetails() {
		ResponseHeader res = new ResponseHeader(InvokeStatus.SUCCESS,
				InvokeMessage.GET_ADDITIONAL_DETAILS_SUCCESS.toString());
		return res;
	}

	DoTransferResponseType doFinacleTransfer(String url, JAXBElement<DoTransferRequestType> jaxbElement)
			throws Throwable {

		logger.info("calling soap api for finacle transfer");

		try {
			try {

				WebServiceTemplate wst = getWebServiceTemplate();
				HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
				sender.setReadTimeout(timeout);
				wst.setMessageSender(sender);

				JAXBElement<?> marshalSendAndReceive = (JAXBElement<?>) wst.marshalSendAndReceive(url, jaxbElement);

				if (marshalSendAndReceive.getValue() instanceof DoTransferResponseType) {
					
					JAXBElement<DoTransferResponseType> response = (JAXBElement<DoTransferResponseType>) marshalSendAndReceive;
					
					logger.info("Response out:" + objectMapper.writeValueAsString(response.getValue()));
					
					return response.getValue();

				} else {
					return null;
				}

			} catch (WebServiceIOException e) {
				logger.error("DoTransfer error occured - WebServiceIOException",e);
				throw e.getCause();
			}

		} catch (SocketTimeoutException e) {
			logger.error("Fail to doFinacleTransfer", e);
			DoTransferResponseType response = new DoTransferResponseType();
			response.setActionCode("9000");
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode("9000");
			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;

		} catch (SoapFaultException e) {
			logger.error("Backend response :DoTransferResponseType error", e);
			DoTransferResponseType response = new DoTransferResponseType();
			response.setActionCode(e.getFaultCode());
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode(e.getFaultCode());

			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;

		} catch (Exception e) {
			logger.error("Backend response :DoTransferResponseType error", e);
			DoTransferResponseType response = new DoTransferResponseType();
			response.setActionCode("400");
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode("400");

			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;
		}

	}

	DoInterBankTransferResponseType doInterbankTransfer(String url, JAXBElement<DoInterBankTransferRequestType> jaxbElement) throws Throwable {
		logger.info("calling soap api for interbank transfer");
		
		try {
			try {

				WebServiceTemplate wst = getWebServiceTemplate();
				HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
				sender.setReadTimeout(timeout);
				wst.setMessageSender(sender);

				JAXBElement<?> marshalSendAndReceive = (JAXBElement<?>) wst.marshalSendAndReceive(url, jaxbElement);

				if (marshalSendAndReceive.getValue() instanceof DoInterBankTransferResponseType) {
					
					JAXBElement<DoInterBankTransferResponseType> response = (JAXBElement<DoInterBankTransferResponseType>) marshalSendAndReceive;
					
					logger.info("Response out:" + objectMapper.writeValueAsString(response.getValue()));
					
					return response.getValue();

				} else {
					return null;
				}

			} catch (WebServiceIOException e) {
				logger.error("DoTransfer error occured - WebServiceIOException",e);
				throw e.getCause();
			}

		} catch (SocketTimeoutException e) {
			logger.error("Fail to doFinacleTransfer", e);
			DoInterBankTransferResponseType response = new DoInterBankTransferResponseType();
			response.setTxnStatus("9000");
			response.setActionCode("9000");
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode("9000");
			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;

		} catch (SoapFaultException e) {
			logger.error("Backend response :DoTransferResponseType error", e);
			DoInterBankTransferResponseType response = new DoInterBankTransferResponseType();
			response.setActionCode(e.getFaultCode());
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode(e.getFaultCode());

			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;

		} catch (Exception e) {
			logger.error("Backend response :DoTransferResponseType error", e);
			DoInterBankTransferResponseType response = new DoInterBankTransferResponseType();
			response.setActionCode("400");
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode("400");

			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;
		}
		
	}
	
	DoSlipTransactionResponseType doSlipTransaction(String url,JAXBElement<DoSlipTransactionRequestType> jaxbElement) throws Throwable {
		logger.info("Calling soap api for do Slip Transaction");
		
		try {
			try {

				WebServiceTemplate wst = getWebServiceTemplate();
				HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
				sender.setReadTimeout(timeout);
				wst.setMessageSender(sender);

				JAXBElement<?> marshalSendAndReceive = (JAXBElement<?>) wst.marshalSendAndReceive(url, jaxbElement);

				if (marshalSendAndReceive.getValue() instanceof DoSlipTransactionResponseType) {
					
					JAXBElement<DoSlipTransactionResponseType> response = (JAXBElement<DoSlipTransactionResponseType>) marshalSendAndReceive;
					
					logger.info("Response out:" + objectMapper.writeValueAsString(response.getValue()));
					
					return response.getValue();

				} else {
					return null;
				}

			} catch (WebServiceIOException e) {
				logger.error("DoTransfer error occured - WebServiceIOException",e);
				throw e.getCause();
			}

		} catch (SocketTimeoutException e) {
			logger.error("Fail to doFinacleTransfer", e);
			DoSlipTransactionResponseType response = new DoSlipTransactionResponseType();
			response.setTxnStatus("9000");
			response.setActionCode("9000");
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode("9000");
			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;

		} catch (SoapFaultException e) {
			logger.error("Backend response :DoTransferResponseType error", e);
			DoSlipTransactionResponseType response = new DoSlipTransactionResponseType();
			response.setActionCode(e.getFaultCode());
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode(e.getFaultCode());

			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;

		} catch (Exception e) {
			logger.error("Backend response :DoTransferResponseType error", e);
			DoSlipTransactionResponseType response = new DoSlipTransactionResponseType();
			response.setActionCode("400");
			response.setProcessedDesc(e.getMessage());
			response.setProcessedCode("400");

			logger.info("Response out:" + objectMapper.writeValueAsString(response));
			return response;
		}
	}

}
