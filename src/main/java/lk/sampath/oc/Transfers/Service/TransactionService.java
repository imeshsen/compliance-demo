package lk.sampath.oc.Transfers.Service;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lk.sampath.oc.Transfers.Pojo.mobile.*;
import lk.sampath.oc.Transfers.request.App.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.sampath.oc.Transfers.Entity.CreditCardPaymentTransaction;
import lk.sampath.oc.Transfers.Entity.ReverseMCashRequest;
import lk.sampath.oc.Transfers.Entity.TransactionRequest;
import lk.sampath.oc.Transfers.Entity.TransactionRequestHistory;
import lk.sampath.oc.Transfers.Entity.WithdrawMCashRequest;
import lk.sampath.oc.Transfers.Enums.CommissionStatus;
import lk.sampath.oc.Transfers.Enums.ErrorCode;
import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Enums.TransactionType;
import lk.sampath.oc.Transfers.Pojo.CCpaymentCardResponse;
import lk.sampath.oc.Transfers.Pojo.CommonApplicationResponse;
import lk.sampath.oc.Transfers.Pojo.DailyTransactionDTO;
import lk.sampath.oc.Transfers.Pojo.FundTransferDTO;
import lk.sampath.oc.Transfers.Pojo.GetCEFTEnabledFlagResponse;
import lk.sampath.oc.Transfers.Pojo.GetCardSerailResponse;
import lk.sampath.oc.Transfers.Pojo.GetTransfers;
import lk.sampath.oc.Transfers.Pojo.PaymentCCResponse;
import lk.sampath.oc.Transfers.Pojo.PaymentCardOminiResponse;
import lk.sampath.oc.Transfers.Pojo.PaymentCardResponse;
import lk.sampath.oc.Transfers.Pojo.PushNotificationRequestDto;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lk.sampath.oc.Transfers.Pojo.TransferResponse;
import lk.sampath.oc.Transfers.Pojo.ValidateTransferResponse;
import lk.sampath.oc.Transfers.Pojo.getTransfersResponse;
import lk.sampath.oc.Transfers.Pojo.profileInfo.ProfileResponse;
import lk.sampath.oc.Transfers.Pojo.reports.FundTransferReportDTO;
import lk.sampath.oc.Transfers.Pojo.reports.FundTransferReportRequestDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.FundTransfersReportResponseDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.GetFundTransfersRequestDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.GetFundTransfersResponseDTO;
import lk.sampath.oc.Transfers.Repository.CCPaymentTrnRepository;
import lk.sampath.oc.Transfers.Repository.ReverseMCashRepository;
import lk.sampath.oc.Transfers.Repository.TransactionHistoryRepository;
import lk.sampath.oc.Transfers.Repository.TransactionRepository;
import lk.sampath.oc.Transfers.Repository.WithdrawMCashRepository;
import lk.sampath.oc.Transfers.Specifications.TransactionRequestSpecification;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoInterBankTransferRequestType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoInterBankTransferResponseType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoSlipTransactionRequestType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoSlipTransactionResponseType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoTransferRequestType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.DoTransferResponseType;
import lk.sampath.oc.Transfers.integration.finacleIntegration.ObjectFactory;
import lk.sampath.oc.Transfers.integration.mcash.CommissionData;
import lk.sampath.oc.Transfers.integration.mcash.Create;
import lk.sampath.oc.Transfers.integration.mcash.CreateDataInt;
import lk.sampath.oc.Transfers.integration.mcash.GetCommission;
import lk.sampath.oc.Transfers.integration.mcash.GetCommissionResponse;
import lk.sampath.oc.Transfers.integration.mcash.Reverse;
import lk.sampath.oc.Transfers.integration.mcash.ReverseDataInt;
import lk.sampath.oc.Transfers.integration.mcash.User;
import lk.sampath.oc.Transfers.integration.mcash.Withdraw;
import lk.sampath.oc.Transfers.integration.mcash.WithdrawDataInt;
import lk.sampath.oc.Transfers.request.PayCreditCardRequest;
import lk.sampath.oc.Transfers.response.CustomMcashResponse;
import lk.sampath.oc.Transfers.utils.JacksonConfig;

@Service
public class TransactionService {

	private static Logger logger = LogManager.getLogger(TransactionService.class);

	@Value("${config.finacle.transMemoModulePrefix.vqr}")
	private String transMemoModulePrefixVQR;
	@Value("${config.finacle.transMemoModulePrefix.tfr}")
	private String transMemoModulePrefixTFR;
	@Value("${config.finacle.transMemoAppCode}")
	private String transMemoAppCode;

//	@Value("${config.finacle.transactionReq.memo.prefix}")
//	private String memoPrefix;
//	@Value("${config.finacle.transactionReq.memo.purpose}")
//	private String memoPurpose;
	@Value("${config.finacle.appCode}")
	private String appCode;
	@Value("${config.finacle.cdCICode}")
	private String cdCICode;
	@Value("${config.finacle.controller}")
	private String controller;
	@Value("${config.finacle.interBnkReq.terminalID}")
	private String terminalID;
	@Value("${config.finacle.interBnkReq.slips.oth}")
	private String slipOthCode;
	@Value("${config.finacle.interBnkReq.slips.cc}")
	private String slipCCCode;
	@Value("${config.finacle.interBnkReq.fromAccountType}")
	private String fromAccountType;
	@Value("${config.finacle.interBnkReq.fromAccountBankCode}")
	private String fromAccountBankCode;
	@Value("${config.finacle.interBnkReq.toAccountType}")
	private String toAccountType;
	@Value("${config.finacle.interBnkReq.commAccount}")
	private String commAccount;
	@Value("${config.finacle.interBnkReq.channelType}")
	private String channelType;
	@Value("${config.bankcode.sampathbank}")
	private String sampathBankCode;
	@Value("${config.finacle.interBnkReq.creditCardPaymentAccount}")
	private String creditCardPaymentAccount;
	@Value("${service.url.notification}")
	private String pushNotificationUrl;
	@Value("${config.ccSettlement.purpose}")
	private String purpose;
	@Value("${config.mcash.appCode}")
	private String mCashAppCode;

	@Value("${service.url.iib.finacle.wsdl}")
	private String iibFinacleWsdl;

	@Autowired
	private ObjectMapper objectMapper;

	private boolean pushNotificationSuccess;

	@Autowired
	private JacksonConfig jacksonConfig;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TransactionRepository trRepo;

	@Autowired
	private TransactionHistoryRepository trHistoryRepo;

	@Autowired
	private WithdrawMCashRepository withdrawMCRepo;

	@Autowired
	private ReverseMCashRepository reverseMCRepo;

	@Autowired
	private RestCustomerManagementService restCustomerManagementService;

	@Autowired
	private CreditCardRestService cCRestService;



	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	@Autowired
	private FinacleSOAPConsumerService finacleConsumerService;

	@Autowired
	private MCashSOAPConsumerService mCashConsumerService;

	@Autowired
	private RestChargeProfileService restChargeProfileService;

	@Autowired
	private RestConsumerService restConsumerService;


	private static final String defaultFromAccName = "Sampath Vishwa";


	@Autowired
	private CCPaymentTrnRepository cCPaymentTrnRepository;

	@Value("${sampath.cc.service.encryptionType}")
	private String encryptionType;

	public ValidateTransferResponse validateTransferForAppNet(DoTransferRequestForAppNet dtr, String reqID, String userName, String IdentityType, String IdentityValue) {
		ValidateTransferResponse validateTransferOut = this.restConsumerService.validateTransactionDetailsForApp(dtr,reqID,userName,IdentityType, IdentityValue);
		logger.info(jacksonConfig.convertToJson(validateTransferOut));
		return validateTransferOut;
	}

	public List<ValidateTransferResponse> validateTransferMLForAppNet(MLTransferReq mlTransferReq, String reqID, String userName, String IdentityType, String IdentityValue) {
		List<ValidateTransferResponse> validateTransferOutList = this.restConsumerService.validateTransactionDetailsMLForApp(mlTransferReq, reqID, userName, IdentityType, IdentityValue);
		logger.info(jacksonConfig.convertToJson(validateTransferOutList));
		return validateTransferOutList;
	}

	public ValidateTransferResponse validateTransfer(TransactionRequest tr, String uuid) {
//		logger.info("inside validate transfer");
		ResponseHeader resHeader = new ResponseHeader();
		resHeader.setReturnStatus(InvokeStatus.SUCCESS);
		ValidateTransferResponse validateTransferOut = new ValidateTransferResponse();
		
		resHeader = this.finacleConsumerService.validateDebitAccount();

		if (resHeader.getReturnStatus().equals(InvokeStatus.SUCCESS)) {
			resHeader = this.finacleConsumerService.getAdditionalDetails();
			// TODO Validate
			validateTransferOut.setResponseHeader(resHeader);
			if (resHeader.getReturnStatus().equals(InvokeStatus.SUCCESS)) {
				validateTransferOut = this.restConsumerService.validateTransactionDetails(tr,uuid);

			} else {
				validateTransferOut.setResponseHeader(resHeader);
			}
		} else {
			validateTransferOut.setResponseHeader(resHeader);
		}

	//	logger.info(validateTransferOut.toString());
		logger.info(jacksonConfig.convertToJson(validateTransferOut));
		return validateTransferOut;
	}

	public List<MLTransferRes> doMultipleTransferForAppNet(MLTransferReq mlTransferReq, String reqID, String userName, String IdentityType, String IdentityValue) {
		logger.info("inside doMultipleTransfer");

		List<DoTransferRequestForAppNet> doTransferRequestForAppList = new ArrayList<>();
		List<MLTranCategorySingleReq> mlTranCategorySingleReqList = mlTransferReq.getTranList();
		List<MLTransferRes> mlTransferResList = new ArrayList<>();

		for (MLTranCategorySingleReq mlList : mlTranCategorySingleReqList) {
			DoTransferRequestForAppNet doTransferRequestForApp = new DoTransferRequestForAppNet();
			BeanUtils.copyProperties(mlList, doTransferRequestForApp);
			doTransferRequestForApp.setFromAccountNumber(mlTransferReq.getDebitAccount());
			doTransferRequestForApp.setCurrency(mlTransferReq.getCurrency());
			doTransferRequestForAppList.add(doTransferRequestForApp);
		}
		int index = 0;
		for (DoTransferRequestForAppNet dtrList : doTransferRequestForAppList) {
			TransferResponse transferResponse = doTransferForAppNet(dtrList, reqID, userName,IdentityType, IdentityValue);
			MLTransferRes mlTransferRes = new MLTransferRes();
			BeanUtils.copyProperties(transferResponse, mlTransferRes);
			index++;
			mlTransferRes.setTranIndex(index);
			mlTransferRes.setTransferType(String.valueOf(dtrList.getTransactionCategory()));
			mlTransferResList.add(mlTransferRes);
		}
		return mlTransferResList;
	}

	@SuppressWarnings("unused")
	public TransferResponse doTransferForAppNet(DoTransferRequestForAppNet dtr, String reqID, String userName, String IdentityType, String IdentityValue) {
		logger.info("inside doTransferForApp");

		TransferResponse transactionRes = new TransferResponse();
		ResponseHeader resHeader = new ResponseHeader();
		TransactionRequest trq = new TransactionRequest();
		DoTransferReqForCard dtrfc = new DoTransferReqForCard();

		BeanUtils.copyProperties(dtr, trq);
		trq.setUserName(userName);
		trq.setChannel("SVR APP");
		BeanUtils.copyProperties(dtr, dtrfc);
		dtrfc.setEncryptionType(encryptionType);
		dtrfc.setIdentityType(IdentityType);
		dtrfc.setNIC(IdentityValue);
		try {
			trq.setPurpose(trq.getPurpose() != null ? trq.getPurpose().substring(0, Math.min(trq.getPurpose().length(), 255)) : "");
			saveTransactionRequest(trq);
			logger.info("transaction request saved");
		} catch (Exception e) {
			logger.info("tran request failed due to {}", e);
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_009.toString(),
					InvokeMessage.SAVE_TRANSACTION_REQUEST_FAILED.toString());
			transactionRes.setResponseHeader(resHeader);
		}

		DailyTransactionDTO dl = new DailyTransactionDTO(new Date(), trq.getUserName(), trq.getChannel(),
				trq.getTransactionCategory(), trq.getTransactionAmount(), trq.getCurrency());

		try {
			this.restConsumerService.saveDailyTransactionLimits(dl, reqID);
			logger.info("daily limits updated");
		} catch (URISyntaxException e) {
			logger.error("Error occured when daily limits updated", e);
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
					InvokeMessage.INVOKE_VALIDATE_TRANSFER_DETAIL_SERVICE_FAILED.toString());
		} catch (Exception e) {
			logger.error("Error occured when daily limits updated", e);
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_011.toString(),
					InvokeMessage.SAVE_TRANSACTION_FAILED.toString());
			transactionRes.setResponseHeader(resHeader);
		}

		try {
			if (resHeader.getReturnStatus() == null) {
				transactionRes = proceedTransferForAppNet(trq, reqID, dtrfc);
				if (InvokeStatus.FAIL.equals(transactionRes.getResponseHeader().getReturnStatus())) {
					logger.error("transfer proceed failed : {}", transactionRes.getResponseHeader().getReturnMessage());
					resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_014.toString(),
							transactionRes.getResponseHeader().getReturnMessage());
					transactionRes.setResponseHeader(resHeader);
				} else if (InvokeStatus.FAILED_IN_PROCESS.equals(transactionRes.getResponseHeader().getReturnStatus())) {
					logger.error("transfer proceed failed : {}", transactionRes.getResponseHeader().getReturnMessage());
					resHeader = new ResponseHeader(InvokeStatus.FAILED_IN_PROCESS, ErrorCode.TR_017.toString(),
							InvokeMessage.FAILED_IN_PROCESS_DO_TRANSFER.toString());
					transactionRes.setResponseHeader(resHeader);
				} else {
					logger.info("transfer proceeded");
				}
			}
			logger.info("transfer proceeded");
		} catch (Exception e) {
			logger.error("transfer proceed failed : {}", e.getLocalizedMessage());
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_010.toString(),
					InvokeMessage.PROCEED_TRANSFER_FAILED.toString());
			transactionRes.setResponseHeader(resHeader);
		}

		try {
			if (resHeader.getReturnStatus() != null) {
				this.restConsumerService.revertDailyTransactionLimits(dl, reqID);
				logger.info("daily limits reverted");
			}
		} catch (URISyntaxException e) {
			logger.error("Error occured when daily limits reverted", e);
//			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
//					InvokeMessage.INVOKE_VALIDATE_TRANSFER_DETAIL_SERVICE_FAILED.toString());
		} catch (Exception e) {
			logger.error("Error occured when daily limits reverted", e);
//			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_011.toString(),
//					InvokeMessage.SAVE_TRANSACTION_FAILED.toString());
//			transactionRes.setResponseHeader(resHeader);
		}
		try {
			deleteTransactionRequest(trq);
			logger.info("deleted transaction from OC_TRN_TRANSACTION_REQUEST table");
		} catch (Exception e) {
			logger.error("Error occured when deleting transaction", e);
//			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_006.toString(),
//					InvokeMessage.DELETE_TRANSFER_RECORD_FAILED.toString());
//			transactionRes.setResponseHeader(resHeader);

		}
		try {
			if (resHeader.getReturnStatus() == null) {
				trq.setStatus(InvokeStatus.SUCCESS.toString());
			} else {
				if (InvokeStatus.FAILED_IN_PROCESS.equals(transactionRes.getResponseHeader().getReturnStatus())) {
					trq.setStatus(InvokeStatus.FAILED_IN_PROCESS.toString());
				} else {
					trq.setStatus(InvokeStatus.FAIL.toString());
				}
			}
			saveTransactionRequestHistory(trq, pushNotificationSuccess, transactionRes);
			logger.info("transaction request moved to OC_TRN_TRANSACTION_REQUEST_H table");
		} catch (Exception e) {
			logger.error("Error occured when saving transaction to history table", e);
//			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_007.toString(),
//					InvokeMessage.SAVE_TRANSACTION_TO_RECORD_HISTORY_FAILED.toString());
//			transactionRes.setResponseHeader(resHeader);
		}
		return transactionRes;
	}

	@SuppressWarnings("unused")
	public TransferResponse doTransfer(TransactionRequest tr, String uuid) {
		logger.info("inside do transfer");

		TransferResponse transactionRes = new TransferResponse();
		ResponseHeader resHeader = new ResponseHeader();

		try {
			tr.setPurpose(tr.getPurpose() != null ? tr.getPurpose().substring(0, Math.min(tr.getPurpose().length(), 255)) : "");
			saveTransactionRequest(tr);
			logger.info("transaction request saved");
		} catch (Exception e) {
			logger.info("tran request failed due to {}", e);
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_009.toString(),
					InvokeMessage.SAVE_TRANSACTION_REQUEST_FAILED.toString());
			transactionRes.setResponseHeader(resHeader);
		}

		DailyTransactionDTO dl = new DailyTransactionDTO(new Date(), tr.getUserName(), tr.getChannel(),
				tr.getTransactionCategory(), tr.getTransactionAmount(),tr.getCurrency());

		try {
				this.restConsumerService.saveDailyTransactionLimits(dl,uuid);
				logger.info("daily limits updated");
		} catch (URISyntaxException e) {
			logger.error("Error occured when daily limits updated",e);
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
					InvokeMessage.INVOKE_VALIDATE_TRANSFER_DETAIL_SERVICE_FAILED.toString());
		} catch (Exception e) {
			logger.error("Error occured when daily limits updated",e);
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_011.toString(),
					InvokeMessage.SAVE_TRANSACTION_FAILED.toString());
			transactionRes.setResponseHeader(resHeader);
		}

		
		try {
			if (resHeader.getReturnStatus() == null) {
				transactionRes = proceedTransfer(tr,uuid);
				if (InvokeStatus.FAIL.equals(transactionRes.getResponseHeader().getReturnStatus())) {
					logger.error("transfer proceed failed : {}",transactionRes.getResponseHeader().getReturnMessage());
					resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_014.toString(),
							transactionRes.getResponseHeader().getReturnMessage());
					transactionRes.setResponseHeader(resHeader);
				}else if (InvokeStatus.FAILED_IN_PROCESS.equals(transactionRes.getResponseHeader().getReturnStatus())) {
						logger.error("transfer proceed failed : {}",transactionRes.getResponseHeader().getReturnMessage());
						resHeader = new ResponseHeader(InvokeStatus.FAILED_IN_PROCESS, ErrorCode.TR_017.toString(),
								InvokeMessage.FAILED_IN_PROCESS_DO_TRANSFER.toString());
						transactionRes.setResponseHeader(resHeader);
				}else {
					logger.info("transfer proceeded");
				}
			}
			logger.info("transfer proceeded");
		} catch (Exception e) {
			logger.error("transfer proceed failed : {}",e.getLocalizedMessage());
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_010.toString(),
					InvokeMessage.PROCEED_TRANSFER_FAILED.toString());
			transactionRes.setResponseHeader(resHeader);
		}

	
		try {
			if (resHeader.getReturnStatus() != null) {
				this.restConsumerService.revertDailyTransactionLimits(dl,uuid);
				logger.info("daily limits reverted");
			}
		} catch (URISyntaxException e) {
			logger.error("Error occured when daily limits reverted",e);
//			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_005.toString(),
//					InvokeMessage.INVOKE_VALIDATE_TRANSFER_DETAIL_SERVICE_FAILED.toString());
		} catch (Exception e) {
			logger.error("Error occured when daily limits reverted",e);
//			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_011.toString(),
//					InvokeMessage.SAVE_TRANSACTION_FAILED.toString());
//			transactionRes.setResponseHeader(resHeader);
		}

		try {
			deleteTransactionRequest(tr);
			logger.info("deleted transaction from OC_TRN_TRANSACTION_REQUEST table");
		} catch (Exception e) {
			logger.error("Error occured when deleting transaction",e);
//			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_006.toString(),
//					InvokeMessage.DELETE_TRANSFER_RECORD_FAILED.toString());
//			transactionRes.setResponseHeader(resHeader);

		}
		try {
			if (resHeader.getReturnStatus() == null) {
				tr.setStatus(InvokeStatus.SUCCESS.toString());
			} else {
				if(InvokeStatus.FAILED_IN_PROCESS.equals(transactionRes.getResponseHeader().getReturnStatus())){
					tr.setStatus(InvokeStatus.FAILED_IN_PROCESS.toString());
				}else {
					tr.setStatus(InvokeStatus.FAIL.toString());
				}
			}
			saveTransactionRequestHistory(tr, pushNotificationSuccess, transactionRes);
			logger.info("transaction request moved to OC_TRN_TRANSACTION_REQUEST_H table");
		} catch (Exception e) {
			logger.error("Error occured when saving transaction to history table",e);
//			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_007.toString(),
//					InvokeMessage.SAVE_TRANSACTION_TO_RECORD_HISTORY_FAILED.toString());
//			transactionRes.setResponseHeader(resHeader);
		}

		return transactionRes;
	}

	void saveTransactionRequest(TransactionRequest tr) {
		tr.setStatus("NEW");
		this.trRepo.save(tr);
	}

	void deleteTransactionRequest(TransactionRequest tr) {
		this.trRepo.delete(tr);
	}

	void saveTransactionRequestHistory(TransactionRequest tr, boolean pushNotificationSend, TransferResponse transactionRes) {
		TransactionRequestHistory trH = new TransactionRequestHistory();
		trH.setBackendRefNumber(tr.getBackendRefNumber());
		trH.setBackendResponse(tr.getBackendResponse());
		trH.setBankCode(tr.getBankCode());
		trH.setBankName(tr.getBankName());
		trH.setBranchCode(tr.getBranchCode());
		trH.setBenificiaryRemarks(tr.getBenificiaryRemarks());
		trH.setAccountNickName(tr.getAccountNickName());
		trH.setCardName(tr.getCardName());
		trH.setCdciStatus(tr.getCdciStatus());
		trH.setChannel(tr.getChannel());
		trH.setCurrency(tr.getCurrency());
		trH.setExternalId(tr.getExternalId());
		trH.setFromAccountNumber(tr.getFromAccountNumber());
		trH.setMobileCashSenderMobile(tr.getMobileCashSenderMobile());
		trH.setMobileRecipientNIC(tr.getMobileRecipientNIC());
		trH.setMobileCashSenderNIC(tr.getMobileCashSenderNIC());
		trH.setMobileRecipientMobile(tr.getMobileRecipientMobile());
		trH.setMobileRecipientName(tr.getMobileRecipientName());
		trH.setRequestedDate(tr.getRequestedDate());
		trH.setSenderRemarks(tr.getSenderRemarks());
		trH.setStatus(tr.getStatus());
		if(TransactionCategory.CC.equals(tr.getTransactionCategory())) {
			trH.setToAccountNumber(this.maskCreditCard(tr.getToAccountNumber()));
		}else {
			trH.setToAccountNumber(tr.getToAccountNumber());
		}
		trH.setTransactionAmount(tr.getTransactionAmount());
		trH.setTransactionCategory(tr.getTransactionCategory());
		trH.setTransactionId(tr.getTransactionId());
		trH.setTransactionType(tr.getTransactionType());
		trH.setScheduleId(tr.getScheduleId());
		trH.setTransferFrequency(tr.getTransferFrequency());
		trH.setNextSchedule(tr.getNextSchedule());
		trH.setNumOfTransfers(tr.getNumOfTransfers());
		trH.setFromAccountName(tr.getFromAccountName());
		trH.setToAccountName(tr.getToAccountName());
		trH.setNumOfTransfers(tr.getNumOfTransfers());
		trH.setUserName(tr.getUserName());
		trH.setPushNotificationSaved(pushNotificationSend ? "YES" : "NO");
		// commissioning
		trH.setCommissionAmount(tr.getCommissionAmount());
		trH.setCommissionStatus(tr.getCommissionStatus());
		trH.setCommissionFromAccountNumber(tr.getCommissionFromAccountNumber());
		trH.setPurpose(tr.getPurpose());
		trH.setScheduleOccr(tr.getScheduleOccr());
		// success or error message
		trH.setMessage(transactionRes.getResponseHeader() == null ? null
				: transactionRes.getResponseHeader().getReturnCode() + "-"
						+ transactionRes.getResponseHeader().getReturnStatus() + "-"
						+ transactionRes.getResponseHeader().getReturnMessage());
		this.trHistoryRepo.save(trH);
	}

	private String maskCreditCard(String toAccountNumber) {
		String maskedCard = toAccountNumber.replaceAll("\\w(?=\\w{4})", "X");
		return maskedCard;
	}

	boolean validateBranch() {
		// TODO
		return false;
	}

	boolean validateLimits() {
		// TODO
		return false;
	}

	TransferResponse proceedTransfer(TransactionRequest tr, String uuid) {
		TransactionType transferMode = tr.getTransactionType();
		TransactionCategory transferCategory = tr.getTransactionCategory();
		String bankCode = tr.getBankCode();

		if ((transferCategory.equals(TransactionCategory.OWN) || transferCategory.equals(TransactionCategory.TPS))
				|| (transferCategory.equals(TransactionCategory.OTH) && (bankCode.equals(sampathBankCode)))) {
			logger.info("initiating fiancle transfer");
			return processFinacleTransfer(tr);
		}
		if ((transferCategory.equals(TransactionCategory.CC) && (bankCode.equals(sampathBankCode)))) {
			logger.info("initiating inter bank credit card transfer");
			return processCCTransfer(tr);
		}
		if (transferCategory.equals(TransactionCategory.OTH) && (!bankCode.equals(sampathBankCode))
				|| transferCategory.equals(TransactionCategory.CC) && (!bankCode.equals(sampathBankCode))) {
			logger.info("initiating inter bank transfer");
			return processInterBankTransfer(tr,uuid);
		} else if (transferCategory.equals(TransactionCategory.SMC)||transferCategory.equals(TransactionCategory.MC)) {
			logger.info("initiating sampath mobile cash transfer");
			return processMobileCashTransfer(tr,uuid);
		} else {
			throw new RuntimeException("Not a fincale transfer, TransactionType : " + transferMode
					+ " TransactionCategory : " + transferCategory + " BankCode : " + bankCode);
		}
	}

	TransferResponse proceedTransferForAppNet(TransactionRequest trq, String reqID, DoTransferReqForCard dtrfc) {
		TransactionType transferMode = trq.getTransactionType();
		TransactionRequest tr = new TransactionRequest();
		TransactionCategory transferCategory = trq.getTransactionCategory();
		String bankCode = trq.getBankCode();

		if ((transferCategory.equals(TransactionCategory.OWN) || transferCategory.equals(TransactionCategory.TPS))
				|| (transferCategory.equals(TransactionCategory.OTH) && (bankCode.equals(sampathBankCode)))) {
			logger.info("initiating fiancle transfer");
			return processFinacleTransfer(trq);
		}
		if ((transferCategory.equals(TransactionCategory.CC) && (bankCode.equals(sampathBankCode)))) {
			logger.info("initiating inter bank credit card transfer");
			return processCCTransferForAppNet(trq, dtrfc);
		}
		if (transferCategory.equals(TransactionCategory.OTH) && (!bankCode.equals(sampathBankCode))
				|| transferCategory.equals(TransactionCategory.CC) && (!bankCode.equals(sampathBankCode))) {
			logger.info("initiating inter bank transfer");
			return processInterBankTransfer(trq, reqID);

		} else if (transferCategory.equals(TransactionCategory.SMC) || transferCategory.equals(TransactionCategory.MC)) {
			logger.info("initiating sampath mobile cash transfer");
			return processMobileCashTransfer(trq,reqID);
		} else {
			throw new RuntimeException("Not a fincale transfer, TransactionType : " + transferMode
					+ " TransactionCategory : " + transferCategory + " BankCode : " + bankCode);
		}
	}

	public TransferResponse processFinacleTransfer(TransactionRequest tr) {
		TransferResponse transferRes = new TransferResponse();
		ObjectFactory objectFactory = new ObjectFactory();
		DoTransferRequestType transReq = new DoTransferRequestType();
		
		transReq.setAPPCode(appCode);
		transReq.setCDCICode(cdCICode);
		transReq.setController(controller);
		transReq.setDTxnAmount(tr.getTransactionAmount().doubleValue());
		transReq.setFromAccountNo(tr.getFromAccountNumber());
		
		if(tr.getTransactionCategory().equals(TransactionCategory.CC)){
			transReq.setTransMemo(tr.getToAccountNumber());
			transReq.setToAccountNo(creditCardPaymentAccount);
		}else {
			transReq.setToAccountNo(tr.getToAccountNumber());
//			String transMemo = "CMNBLK-01-" + memoPrefix + "-02-" + beneRemarks + "-03--04-" + senderRemarks + "-05-"
//					+ memoPurpose;
//			transReq.setTransMemo(transMemo);
			if(null != this.constructTransMemo(tr)) {
			transReq.setTransMemo(this.constructTransMemo(tr));
			}else {
				ResponseHeader resHeader = new ResponseHeader();
				resHeader.setReturnStatus(InvokeStatus.FAIL);
				resHeader.setReturnMessage(InvokeMessage.FAILED_CONSTRUCT_TRANSMEMO.toString());
				transferRes.setResponseHeader(resHeader);
				return transferRes;
			}
			
		}
		
		transReq.setValueDate(sdf2.format(new Date()));

		// TODO below mapping needed or not?
		transReq.setFromCurrCode(tr.getCurrency());
		//logger.info("finacle transfer request : " + transReq.toString());
		logger.info("Requesting from " + iibFinacleWsdl);
		String toAccount = transReq.getToAccountNo();
		String fromAccount = transReq.getFromAccountNo();
		transReq.setToAccountNo("reducted");
		transReq.setFromAccountNo("reducted");
		logger.info("finacle transfer request : " + jacksonConfig.convertToJson(transReq));
		transReq.setToAccountNo(toAccount);
		transReq.setFromAccountNo(fromAccount);
		// TODO Call active doFinacleTransfer
		DoTransferResponseType transRes = null;
		try {
			transRes = finacleConsumerService.doFinacleTransfer(iibFinacleWsdl,objectFactory.createDoTransferRequest(transReq));
		} catch (Throwable e) {
			logger.error("Finacle do transfer failed : {}", e);
		}
		
		try {
			logger.info("Response from " + iibFinacleWsdl);
			logger.info("finacle transfer response : " + objectMapper.writeValueAsString(transRes));
		} catch (JsonProcessingException e) {
			logger.error("json mapping issue : {}", e.getLocalizedMessage());
		}

		transferRes.setTransferReference(tr.getTransactionId());
		transferRes.setAuthenticationType("");

		ResponseHeader resHeader = new ResponseHeader();
		resHeader.setReturnCode(transRes.getActionCode());
		tr.setBackendResponse(transRes.getActionCode());
		if (transRes.getActionCode().equals("000")) {
			resHeader.setReturnStatus(InvokeStatus.SUCCESS);
			resHeader.setReturnMessage(InvokeMessage.DO_FINACLE_TRANSFER_SUCCESS.toString());
			pushNotificationSuccess = this.pushNotification(tr.getUserName(), InvokeStatus.SUCCESS.toString(), tr.getTransactionId(), tr.getTransactionAmount().doubleValue());
		}else if (transRes.getActionCode().equals("906")
				||transRes.getActionCode().equals("909")
				||transRes.getActionCode().equals("911")
				||transRes.getActionCode().equals("913")
				||transRes.getActionCode().equals("9000")) {
			resHeader.setReturnStatus(InvokeStatus.FAILED_IN_PROCESS);
			resHeader.setReturnMessage(transRes.getProcessedDesc());
		} else {
			resHeader.setReturnStatus(InvokeStatus.FAIL);
			resHeader.setReturnMessage(InvokeMessage.FAILED_TO_DO_FINACLE_TRANSFER.toString());
		}

		transferRes.setResponseHeader(resHeader);
		return transferRes;
	}
	
	public String constructTransMemo(TransactionRequest tr) {
		StringBuilder transMemo = null;
		try {
			String beneRemarks = null != tr.getBenificiaryRemarks() ? tr.getBenificiaryRemarks() : "";
			String debitAccount = null != tr.getFromAccountNumber() ? tr.getFromAccountNumber() : "";
			String transferReference = String.valueOf(tr.getTransactionId());
			String senderRemarks = null != tr.getSenderRemarks() ? tr.getSenderRemarks() : "";

			transMemo = new StringBuilder();
			transMemo.append("CMNBLK-01-");
			transMemo.append(transMemoAppCode);
			if (tr.getTransactionCategory().equals("SQR")) {
				transMemo.append(transMemoModulePrefixVQR);
			} else {
				transMemo.append(transMemoModulePrefixTFR);
			}

			transMemo.append("-02-");

			if (beneRemarks.length() <= 15) {
				transMemo.append(beneRemarks);
			} else {
				transMemo.append(beneRemarks ,0 ,15);
			}

			transMemo.append("-03-");
			transMemo.append("0000");
			transMemo.append(debitAccount);
			transMemo.append("-04-");
			
			if (transferReference.length() <= 12) {
				String transref = "";
				for (int i = transferReference.length(); i < 12; i++) {
					transref = transref.concat("0");
				}
				transMemo.append(transref);
				transMemo.append(transferReference);
			}else {
				transMemo.append(transferReference ,0,12);
			}

			if (senderRemarks.length() <= 15) {
				transMemo.append(senderRemarks);
			} else {
				transMemo.append(senderRemarks,0,15);
			}

			transMemo.append("-05-");
			transMemo.append("0000");
			transMemo.append(debitAccount);
			return transMemo.toString();
		} catch (Exception e) {
			logger.info("construct transMemo failed" + e);
			return null;
		}
		

	}


	public TransferResponse processInterBankTransfer(TransactionRequest tr, String uuid) {

		TransferResponse transferRes = new TransferResponse();
		CapexPayLoad capexPayLoad = new CapexPayLoad();


		ObjectFactory objectFactory = new ObjectFactory();
		ResponseHeader resHeader = new ResponseHeader();
		double chargeAmount;
		
		if (!tr.getCurrency().equals("LKR")) {
			logger.info("tran request failed due to foreign currency transfers");
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_022.toString(),
					InvokeMessage.NOT_ALLOWED_FOREIGN_CURRENCY_TRANSFERS.toString());
			transferRes.setResponseHeader(resHeader);
			return transferRes;
		}
		
		try {
			chargeAmount = this.getChargeCommissionAmount(tr,uuid);
			tr.setCommissionAmount(BigDecimal.valueOf(chargeAmount));
			tr.setCommissionStatus(CommissionStatus.NOT_SPECIFIED);
			trRepo.save(tr);
		} catch (Exception ex) {
			logger.error("Process Inter Bank Transfer", ex);
			resHeader.setReturnStatus(InvokeStatus.FAIL);
			resHeader.setReturnMessage(ex.getMessage());
			transferRes.setResponseHeader(resHeader);
			return transferRes;
		}

		boolean isCommissioningSuccess = false;
		if (null != tr.getCommissionFromAccountNumber() && !tr.getCommissionFromAccountNumber().isEmpty() && chargeAmount > 0.0) {
			try {
				TransferResponse transResponse = this.doTransferWithCommissionAccount(tr, chargeAmount, true);
				if (null != transResponse && transResponse.getResponseHeader().getReturnStatus().toString()
						.equals(InvokeStatus.SUCCESS.toString())) {
					tr.setCommissionStatus(CommissionStatus.COMMISSIONING_SUCCESS);
					trRepo.save(tr);
					isCommissioningSuccess = true;
					chargeAmount = 0;
				} else {
					tr.setCommissionStatus(CommissionStatus.COMMISSIONING_FAILED);
					trRepo.save(tr);
					return transResponse;
				}
			} catch (Exception e) {
				logger.error("Error occured when doing finacle do transfer for commission",e);
				resHeader.setReturnStatus(InvokeStatus.FAIL);
				resHeader.setReturnMessage(InvokeMessage.FAILED_TO_DO_FINACLE_TRANSFER_FOR_COMMISSION.toString());
				transferRes.setResponseHeader(resHeader);
				tr.setCommissionStatus(CommissionStatus.COMMISSIONING_FAILED);
				trRepo.save(tr);
				return transferRes;
			}
		}
		TransactionCategory transferCategory = tr.getTransactionCategory();

		GetCEFTEnabledFlagResponse getCEFTEnabledFlagResponse;
		try {
			getCEFTEnabledFlagResponse = restConsumerService.getCEFTEnabledFlag(tr.getBankCode());
		} catch (Exception e) {
			resHeader.setReturnStatus(InvokeStatus.FAIL);
			resHeader.setReturnMessage(InvokeMessage.ERROR_ON_CEFT_FLAG_INQUIRY.toString());
			transferRes.setResponseHeader(resHeader);
			trRepo.save(tr);
			return transferRes;
		}
		
		String ceftFlag=getCEFTEnabledFlagResponse.getCeftEnableFlag();

		if (null != getCEFTEnabledFlagResponse && getCEFTEnabledFlagResponse.getStatus().equals("Success") && ceftFlag.equals("Y")) {
			
			DoInterBankTransferRequestType interBnkReq = new DoInterBankTransferRequestType();

			interBnkReq.setAPPCode(appCode);
			interBnkReq.setController(controller);
			interBnkReq.setCDCICode(cdCICode);
			interBnkReq.setTerminalID(terminalID);
			interBnkReq.setFromAccNo(tr.getFromAccountNumber());
			interBnkReq.setFromAccType(fromAccountType);
			interBnkReq.setFromAccBankCode(fromAccountBankCode);
			interBnkReq.setFromAccBranchCode("");
			if(tr.getTransactionCategory().equals(TransactionCategory.CC)){
				interBnkReq.setCardNo(tr.getToAccountNumber());
				interBnkReq.setToAccNo("");
			}else {
				interBnkReq.setToAccNo(tr.getToAccountNumber());
			}
			interBnkReq.setToAccName(null != tr.getToAccountName() ? tr.getToAccountName():"");
			interBnkReq.setToAccType(toAccountType);
			interBnkReq.setToAccBankCode(tr.getBankCode());
			interBnkReq.setToAccBranchCode(tr.getBranchCode());
			interBnkReq.setTxnAmount(tr.getTransactionAmount().doubleValue());
			interBnkReq.setCommAmount(chargeAmount);
			interBnkReq.setCommAccount(commAccount);
			interBnkReq.setTranRemarks(tr.getBenificiaryRemarks());
			interBnkReq.setValueDate(sdf2.format(new Date()));
			if (transferCategory.equals(TransactionCategory.OTH))
				interBnkReq.setSlipsCode(slipOthCode);
			else if (transferCategory.equals(TransactionCategory.CC))
				interBnkReq.setSlipsCode(slipCCCode);

			interBnkReq.setCEFTFlag(ceftFlag);

			interBnkReq.setChannelType(channelType);
			interBnkReq.setDrCurrencyCode(tr.getCurrency());
			interBnkReq.setFromAccName(null != tr.getFromAccountName() && !tr.getFromAccountName().isEmpty() ? tr.getFromAccountName():defaultFromAccName);
			interBnkReq.setReference(tr.getBackendRefNumber());

//			logger.info("inter bank request : " + jacksonConfig.convertToJson(interBnkReq));
			DoInterBankTransferResponseType interBnkRes = null;
			String toAccount = interBnkReq.getToAccNo();
			String fromAccount = interBnkReq.getFromAccNo();
			String cardNumber = interBnkReq.getCardNo();
			interBnkReq.setToAccNo("reducted");
			interBnkReq.setFromAccNo("reducted");
			interBnkReq.setCardNo("xxxxxxxxxxxxxxxx");
			logger.info("Requesting from " + iibFinacleWsdl);
			logger.info("interbank transfer request : " + jacksonConfig.convertToJson(interBnkReq));
			interBnkReq.setToAccNo(toAccount);
			interBnkReq.setFromAccNo(fromAccount);
			interBnkReq.setCardNo(cardNumber);
			try {
				interBnkRes = finacleConsumerService.doInterbankTransfer(iibFinacleWsdl,objectFactory.createDoInterBankTransferRequest(interBnkReq));
			} catch (Throwable e) {
				logger.error("Finacle interbank transfer failed : {}", e);
			}
			logger.info("Response from " + iibFinacleWsdl);
			logger.info("interbank transfer response : " + jacksonConfig.convertToJson(interBnkRes));

			transferRes.setTransferReference(tr.getTransactionId());
			transferRes.setAuthenticationType("");

			resHeader.setReturnCode("000");
			tr.setBackendResponse(interBnkRes.getTxnStatus());
			if (interBnkRes.getTxnStatus().equals("000")
				|| interBnkRes.getTxnStatus().equals("008")
				|| interBnkRes.getTxnStatus().equals("068")) {
				// add backend reference
				tr.setBackendRefNumber(interBnkRes.getTraceNo());
				resHeader.setReturnStatus(InvokeStatus.SUCCESS);
				resHeader.setReturnMessage(InvokeMessage.DO_INTERBANK_TRANSFER_SUCCESS.toString());
				pushNotificationSuccess = this.pushNotification(tr.getUserName(), InvokeStatus.SUCCESS.toString(), tr.getTransactionId(), tr.getTransactionAmount().doubleValue() + chargeAmount);
			}else if (interBnkRes.getActionCode().equals("906")
					||interBnkRes.getActionCode().equals("909")
					||interBnkRes.getActionCode().equals("911")
					||interBnkRes.getActionCode().equals("913")
					||interBnkRes.getActionCode().equals("9000")) {
				resHeader.setReturnStatus(InvokeStatus.FAILED_IN_PROCESS);
				resHeader.setReturnMessage(interBnkRes.getProcessedDesc());
			} else {
				resHeader.setReturnStatus(InvokeStatus.FAIL);
				resHeader.setReturnMessage(interBnkRes.getTxnStatusDesc());
			}
			transferRes.setResponseHeader(resHeader);

		}else if (null != getCEFTEnabledFlagResponse && getCEFTEnabledFlagResponse.getStatus().equals("Success") && ceftFlag.equals("N")){
			
			DoSlipTransactionRequestType doSlipTransactionRequestType = new DoSlipTransactionRequestType();
			doSlipTransactionRequestType.setAPPCode(appCode);
			doSlipTransactionRequestType.setCDCICode(cdCICode);
			doSlipTransactionRequestType.setController(controller);
			doSlipTransactionRequestType.setDCommAmount(chargeAmount);
			doSlipTransactionRequestType.setDTxnAmount(tr.getTransactionAmount().doubleValue());
			doSlipTransactionRequestType.setFromAccountNo(tr.getFromAccountNumber());
			doSlipTransactionRequestType.setRegAcctName(null != tr.getToAccountName() ? tr.getToAccountName() : "");
			if (tr.getTransactionCategory().equals(TransactionCategory.CC)) {
				doSlipTransactionRequestType.setRegAcctNo(tr.getToAccountNumber());
			} else {
				doSlipTransactionRequestType.setRegAcctNo(tr.getToAccountNumber());
			}
			doSlipTransactionRequestType.setRegBankCode(tr.getBankCode());
			doSlipTransactionRequestType.setRegBranchCode(tr.getBranchCode());
			if (transferCategory.equals(TransactionCategory.OTH))
				doSlipTransactionRequestType.setSlipsCode(slipOthCode);
			else if (transferCategory.equals(TransactionCategory.CC))
				doSlipTransactionRequestType.setSlipsCode(slipCCCode);
			doSlipTransactionRequestType.setTranIndex(tr.getBackendRefNumber());
			doSlipTransactionRequestType.setValueDate(sdf2.format(new Date()));
//			logger.info("doSlipTransaction request : " + jacksonConfig.convertToJson(doSlipTransactionRequestType));
			
			DoSlipTransactionResponseType slipTranRes = null;
			try {
				slipTranRes = finacleConsumerService.doSlipTransaction(iibFinacleWsdl,objectFactory.createDoSlipTransactionRequest(doSlipTransactionRequestType));
			} catch (Throwable e) {
				logger.error("Finacle interbank transfer failed : {}", e);
			}
			
			logger.info("doSlipTransaction response : " + jacksonConfig.convertToJson(slipTranRes));
			transferRes.setTransferReference(tr.getTransactionId());
			transferRes.setAuthenticationType("");
			resHeader.setReturnCode(slipTranRes.getActionCode());
			tr.setBackendResponse(slipTranRes.getTxnStatus());
			if (slipTranRes.getActionCode().equals("000")) {
				// add backend reference
				tr.setBackendRefNumber("");
				resHeader.setReturnStatus(InvokeStatus.SUCCESS);
				resHeader.setReturnMessage(InvokeMessage.DO_INTERBANK_TRANSFER_SUCCESS.toString());
				pushNotificationSuccess = this.pushNotification(tr.getUserName(), InvokeStatus.SUCCESS.toString(),
						tr.getTransactionId(), tr.getTransactionAmount().doubleValue() + chargeAmount);
			} else if (slipTranRes.getActionCode().equals("906") || slipTranRes.getActionCode().equals("909")
					|| slipTranRes.getActionCode().equals("911") || slipTranRes.getActionCode().equals("913")
					||slipTranRes.getActionCode().equals("9000")) {
				resHeader.setReturnStatus(InvokeStatus.FAILED_IN_PROCESS);
				resHeader.setReturnMessage(slipTranRes.getProcessedDesc());
			} else {
				resHeader.setReturnStatus(InvokeStatus.FAIL);
				resHeader.setReturnMessage(slipTranRes.getTxnStatus());
			}
			transferRes.setResponseHeader(resHeader);
		}else {
				logger.error("Error occured when doing finacle do transfer");
				resHeader.setReturnStatus(InvokeStatus.FAIL);
				resHeader.setReturnMessage(InvokeMessage.ERROR_ON_CEFT_FLAG_INQUIRY.toString());
				transferRes.setResponseHeader(resHeader);
				trRepo.save(tr);
				return transferRes;
		}
		
		if(isCommissioningSuccess && !resHeader.getReturnStatus().equals(InvokeStatus.SUCCESS)) {
			logger.info("Start rollback commission transaction");
			doTransferRollbackWithCommissionAccount(tr);
			logger.info("End rollback commission transaction");
		}
		capexPayLoad.setMessage("");
		transferRes.setCapexPayLoad(capexPayLoad);
		return transferRes;
	}

	// Rollback transfer commission
	private void doTransferRollbackWithCommissionAccount(TransactionRequest tr)  {
		TransferResponse transferRes = new TransferResponse();
		ObjectFactory objectFactory = new ObjectFactory();
		DoTransferRequestType transReq = new DoTransferRequestType();

//		String beneRemarks = "ROLLBACK_COMMISSION_AMOUNT_TO_USER";
//		String senderRemarks = "ROLLBACK_COMMISSION_AMOUNT_FROM_COMMON_ACCOUNT";
//		String transMemo = "CMNBLK-01-" + memoPrefix + "-02-" + beneRemarks + "-03--04-" + senderRemarks + "-05-"
//				+ memoPurpose;

		transReq.setAPPCode(appCode);
		transReq.setCDCICode(cdCICode);
		transReq.setController(controller);
		transReq.setDTxnAmount(tr.getCommissionAmount().doubleValue());
		transReq.setFromAccountNo(commAccount);
		
		transReq.setToAccountNo(tr.getCommissionFromAccountNumber());
//		transReq.setTransMemo(transMemo);
		if(null != this.constructTransMemo(tr)) {
			transReq.setTransMemo(this.constructTransMemo(tr));
			}else {
				ResponseHeader resHeader = new ResponseHeader();
				resHeader.setReturnStatus(InvokeStatus.FAIL);
				resHeader.setReturnMessage(InvokeMessage.FAILED_CONSTRUCT_TRANSMEMO.toString());
				transferRes.setResponseHeader(resHeader);
				logger.info("Failed construct TransMemo" +transferRes);	
			}
		transReq.setValueDate(sdf2.format(new Date()));

		// TODO below mapping needed or not?
		transReq.setFromCurrCode("LKR");
		logger.info("finacle transfer request : " + jacksonConfig.convertToJson(transReq));
		// TODO Call active doFinacleTransfer
		try {
			DoTransferResponseType transRes = finacleConsumerService.doFinacleTransfer(iibFinacleWsdl,objectFactory.createDoTransferRequest(transReq));
			try {
				logger.info("finacle transfer response : " + objectMapper.writeValueAsString(transRes));
			} catch (JsonProcessingException e) {
				logger.error("json mapping issue : {}", e.getLocalizedMessage());
			}
			
			if (transRes.getActionCode().equals("000")) {
				logger.info("Successfully rollback the commission amount");
				tr.setCommissionStatus(CommissionStatus.COMMISSIONING_REVERSED);
			}else {
				logger.error("Commission rollback failed due to : {}", transRes.getProcessedDesc());
				tr.setCommissionStatus(CommissionStatus.COMMISSIONING_REVERSE_FAILED);		
			}
		} catch (Exception e) {
			logger.error("Commission rollback failed due to : {}", e.getLocalizedMessage());
			tr.setCommissionStatus(CommissionStatus.COMMISSIONING_REVERSE_FAILED);
		} catch (Throwable e1) {
			logger.error("Commission rollback failed due to : {}", e1.getLocalizedMessage());
			tr.setCommissionStatus(CommissionStatus.COMMISSIONING_REVERSE_FAILED);
		}
		trRepo.save(tr);
	}

	public TransferResponse processMobileCashTransfer(TransactionRequest tr, String uuid) {

		TransferResponse transferRes = new TransferResponse();
		double chargeAmount = 0;
		ProfileResponse profileResponse = restCustomerManagementService.getCustomerProfileInfo(tr.getUserName(),uuid);
		boolean isCommissioning = false;
		if (profileResponse.getPayLoad().getChargeProfile().equals("STF")) {
		 isCommissioning = true;
		} 
		boolean isCommissioningSuccess = false;
		
		if (!profileResponse.getPayLoad().getChargeProfile().equals("STF")) {
		if ((tr.getCommissionFromAccountNumber() != null && !tr.getCommissionFromAccountNumber().isEmpty())
				&& !tr.getFromAccountNumber().equals(tr.getCommissionFromAccountNumber())) {

			try {
				isCommissioning = true;
				chargeAmount = this.getChargeCommissionMcashAmount(tr);
				// chargeAmount = this.getChargeCommissionAmount(tr);
				tr.setCommissionAmount(BigDecimal.valueOf(chargeAmount));
				tr.setCommissionStatus(CommissionStatus.NOT_SPECIFIED);
				trRepo.save(tr);
			} catch (Exception ex) {
				ResponseHeader resHeader = new ResponseHeader();
				resHeader.setReturnStatus(InvokeStatus.FAIL);
				resHeader.setReturnMessage(InvokeMessage.GET_MOBILE_CASH_COMMISION_FAILED.toString());
				transferRes.setResponseHeader(resHeader);
				return transferRes;
			}

			try {
				TransferResponse transResponse = this.doTransferWithCommissionAccount(tr, chargeAmount, false);
				if (null != transResponse && transResponse.getResponseHeader().getReturnStatus().toString()
						.equalsIgnoreCase(InvokeStatus.SUCCESS.toString())) {
					tr.setCommissionStatus(CommissionStatus.COMMISSIONING_SUCCESS);
					trRepo.save(tr);
					isCommissioningSuccess = true;
				}
			} catch (Exception e) {
				tr.setCommissionStatus(CommissionStatus.COMMISSIONING_FAILED);
				trRepo.save(tr);
				ResponseHeader resHeader = new ResponseHeader();
				resHeader.setReturnStatus(InvokeStatus.FAIL);
				resHeader.setReturnMessage(InvokeMessage.FAILED_TO_DO_FINACLE_TRANSFER_FOR_COMMISSION.toString());
				transferRes.setResponseHeader(resHeader);
				return transferRes;
			}
		}
		}
		
		logger.info("chargeAmount: {}", chargeAmount);
		
		Create createReq = new Create();

		User u = new User();
		u.setAppUser(tr.getUserName());
		u.setAppCode(mCashAppCode);

		CreateDataInt dataInt = new CreateDataInt();
		// dataInt.setCompanyID(303030);
		dataInt.setTxnType("POI");
		dataInt.setTxnStatus("U");
		dataInt.setTrWithdrawType("POI");
		dataInt.setAccount(tr.getFromAccountNumber());
		dataInt.setBeneID(tr.getMobileRecipientNIC());
		dataInt.setBeneName(tr.getMobileRecipientName());
		dataInt.setBeneTel(tr.getMobileRecipientMobile());
		dataInt.setBeneTitle("");
		dataInt.setSenderTitle("");
		dataInt.setCurrCode("LKR");
		dataInt.setDeductCommission(isCommissioning == true ? "N":"Y");
//				(tr.getCommissionFromAccountNumber() != null && !tr.getCommissionFromAccountNumber().isEmpty()) == true
//						? "N"
//						: "Y");
		dataInt.setSenderID(tr.getMobileCashSenderNIC());
		dataInt.setSenderName(tr.getUserName());
//TODO		dataInt.setSenderTitle(tr.get);
		dataInt.setSenderTel(tr.getMobileCashSenderMobile());
		dataInt.setSenderTel2(tr.getMobileRecipientMobile());
		dataInt.setTxnAmount(tr.getTransactionAmount().doubleValue());
		createReq.setCreateData(dataInt);
		createReq.setUser(u);
		logger.info("create mcash Request : {}", jacksonConfig.convertToJson(createReq));
		CustomMcashResponse resp = mCashConsumerService.createMCash(createReq);
		logger.info("create mcash Response : {}", jacksonConfig.convertToJson(resp));
		ResponseHeader resHeader = new ResponseHeader();
		if (resp.isFlag()) {
			// TODO mapping
			transferRes.setTransferReference(tr.getTransactionId());
			transferRes.setAuthenticationType("");
			// Transfer request update
			tr.setBackendRefNumber(resp.getSmcTranId());
			tr.setBackendResponse(Integer.toString(resp.getStatus()));

			resHeader.setReturnCode(Integer.toString(resp.getStatus()));
			resHeader.setReturnStatus(InvokeStatus.SUCCESS);
			resHeader.setReturnMessage(InvokeMessage.DO_MOBILE_CASH_TRANSFER_SUCCESS.toString());
			transferRes.setResponseHeader(resHeader);
			pushNotificationSuccess = this.pushNotification(tr.getUserName(), InvokeStatus.SUCCESS.toString(), tr.getTransactionId(), tr.getTransactionAmount().doubleValue() + chargeAmount);
		} else {

			resHeader.setReturnCode("-99");
			resHeader.setReturnStatus(InvokeStatus.FAIL);
			resHeader.setReturnMessage(resp.getDescription());
			transferRes.setResponseHeader(resHeader);
		}
		
		// if failed rollback commissioning
		if(isCommissioningSuccess && !resHeader.getReturnStatus().equals(InvokeStatus.SUCCESS)) {
			logger.info("Start rollback commission transaction");
			doTransferRollbackWithCommissionAccount(tr);
			logger.info("End rollback commission transaction");
		}

		return transferRes;
	}

	private double getChargeCommissionMcashAmount(TransactionRequest tr) {

		CommissionData commissionData = new CommissionData();
		commissionData.setAccount(tr.getCommissionFromAccountNumber());
		commissionData.setAction(0);
		commissionData.setTxnAmount(tr.getTransactionAmount().doubleValue());
		commissionData.setSenderID(tr.getMobileCashSenderNIC());
		
		User user = new User();
		user.setAppCode(mCashAppCode);
		user.setAppUser(tr.getUserName());

		GetCommission getCommission = new GetCommission();
		getCommission.setUser(user);
		getCommission.setCommData(commissionData);
		
		GetCommissionResponse resp = mCashConsumerService.getCommisionsMCash(getCommission);
		
		return resp.getReturn();

	}

	public GetFundTransfersResponseDTO getFundTransfers(GetFundTransfersRequestDTO getTransObj) {

		ResponseHeader resHeader;
		GetFundTransfersResponseDTO transRes = new GetFundTransfersResponseDTO();

		String startDateStr = getTransObj.getTransferRequestedDateFrom();
		String endDateStr = getTransObj.getTransferRequestedDateto();

		logger.info("start date >> {}" , startDateStr);
		logger.info("end date >> {}" , endDateStr);

		Date sDate = null;
		Date eDate = null;
		try {
			if (startDateStr != null && !startDateStr.isEmpty())
				sDate = Date.from(sdf.parse(startDateStr).toInstant()
						.atZone(ZoneId.systemDefault()).toLocalDate()
						.atTime(LocalTime.MIN)
						.atZone(ZoneId.systemDefault()).toInstant());
			if (endDateStr != null && !endDateStr.isEmpty())
				eDate = Date.from(sdf.parse(endDateStr).toInstant()
						.atZone(ZoneId.systemDefault()).toLocalDate()
						.atTime(LocalTime.MAX)
						.atZone(ZoneId.systemDefault()).toInstant());
		} catch (ParseException e) {
			logger.info("date parsing excepiton");
			logger.error(e.getStackTrace());
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_003.toString(),
					InvokeMessage.DATE_PARSE_EXCEPTION.toString());
			transRes.setResponseHeader(resHeader);
			return transRes;
		}

		logger.info("start date >> {}" , sDate);
		logger.info("end date >> {}" , eDate);

		logger.info("inside get transfers service");

		int pageNumber = (1 / 200);
		int pageSize = 200;

		logger.info("page number > {}" , pageNumber);
		logger.info("page size > {}" , pageSize);
		Pageable pageable = new PageRequest(pageNumber, pageSize);

		TransactionCategory transferType = null;
		if (getTransObj.getTransferType() != null && !getTransObj.getTransferType().isEmpty()) {
			transferType = TransactionCategory.valueOf(getTransObj.getTransferType());
		}
		List<CreditCardPaymentTransaction> settlementList = new ArrayList<>();
		if(transferType == null || transferType == TransactionCategory.CC) {
			try {
				settlementList = getCCSettlementTransfers(sDate,eDate,getTransObj);
			} catch (Exception e) {
				logger.error("Error getting credit card settlement list..");
			}
		}

		Iterator<TransactionRequestHistory> trHistoryItr = this.trHistoryRepo
				.findAll(generateFundTransferQuery(sDate, eDate, transferType, getTransObj), pageable).iterator();
		transRes.setListOfTransactions(new ArrayList<FundTransferDTO>());
		logger.info("transfers history records retrived...");
		while (trHistoryItr.hasNext()) {
			TransactionRequestHistory txnHistoryObj = trHistoryItr.next();
			FundTransferDTO obj = new FundTransferDTO();
			obj.setTransferRequestId(txnHistoryObj.getTransactionId());
			obj.setTransferMode(txnHistoryObj.getTransactionType());
			obj.setTransferType(txnHistoryObj.getTransactionCategory());
//			obj.setModeOfCC("CC_OTH");
			obj.setTransferRequestDate(txnHistoryObj.getRequestedDate());
			obj.setAmount(txnHistoryObj.getTransactionAmount());
			obj.setCurrency(txnHistoryObj.getCurrency());
			obj.setFromAccount(txnHistoryObj.getFromAccountNumber());
			obj.setToAccount(txnHistoryObj.getToAccountNumber());
			obj.setToBank(txnHistoryObj.getBankCode());
			obj.setUserId(txnHistoryObj.getUserName());
			obj.setTransferDate(txnHistoryObj.getRequestedDate());
			obj.setTransferFrequency(txnHistoryObj.getTransferFrequency());
			obj.setNextSchedule(txnHistoryObj.getNextSchedule());
			obj.setNumOfTransfers(txnHistoryObj.getNumOfTransfers());
			obj.setStatus(txnHistoryObj.getStatus());
			obj.setFailureReason(txnHistoryObj.getMessage());
			obj.setBranchCode(txnHistoryObj.getBranchCode());
			obj.setCardName(txnHistoryObj.getCardName());
			obj.setSenderRemark(txnHistoryObj.getSenderRemarks());
			obj.setBenificiaryRemarks(txnHistoryObj.getBenificiaryRemarks());
			obj.setAccountName(txnHistoryObj.getToAccountName());
			obj.setPurpose(txnHistoryObj.getPurpose());
			obj.setScheduleOccr(txnHistoryObj.getScheduleOccr());
			transRes.getListOfTransactions().add(obj);
		};
		
		for (CreditCardPaymentTransaction ccSettlemnt : settlementList) {
			FundTransferDTO obj = new FundTransferDTO();
			obj.setTransferRequestId(ccSettlemnt.getCcPaymentId());
			obj.setTransferMode(ccSettlemnt.getTransferMethod());
			obj.setTransferType(TransactionCategory.CC);
	//		obj.setModeOfCC("CC_OWN");
			obj.setTransferRequestDate(ccSettlemnt.getAddedDate());
			obj.setAmount(ccSettlemnt.getAmount());
			obj.setCurrency(ccSettlemnt.getCurrency());
			obj.setFromAccount(ccSettlemnt.getDebitAccountNumber());
			obj.setToAccount(ccSettlemnt.getCardNumber());
			obj.setToBank(sampathBankCode);
			obj.setUserId(ccSettlemnt.getVishwaUserId());
			obj.setTransferDate(ccSettlemnt.getAddedDate());
			obj.setStatus(ccSettlemnt.getStatus().name());
			obj.setFailureReason(ccSettlemnt.getRemark());
			obj.setPurpose(purpose);
			transRes.getListOfTransactions().add(obj);
		}
		
		resHeader = new ResponseHeader(InvokeStatus.SUCCESS, InvokeMessage.GET_TRANSFERS_SUCCESS.toString());
		transRes.setResponseHeader(resHeader);

		return transRes;
	}
	
	
	public List<CreditCardPaymentTransaction> getCCSettlementTransfers(Date fromDate, Date toDate,
			GetFundTransfersRequestDTO transferObj) {
		List<CreditCardPaymentTransaction> settlemntList;

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CreditCardPaymentTransaction> criteriaQuery = criteriaBuilder
				.createQuery(CreditCardPaymentTransaction.class);

		Root<CreditCardPaymentTransaction> root = criteriaQuery.from(CreditCardPaymentTransaction.class);
		List<Predicate> predicateList = new ArrayList<Predicate>();

		
		if (!StringUtils.isEmpty(transferObj.getUserId()) && null != transferObj.getUserId()) {
			predicateList.add(criteriaBuilder.equal(root.get("vishwaUserId"), transferObj.getUserId()));

		}

		if (!StringUtils.isEmpty(fromDate) && null != fromDate) {
			predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("addedDate"), fromDate));

		}

		if (!StringUtils.isEmpty(toDate) && null != toDate) {
			predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("addedDate"), toDate));

		}

		if (!StringUtils.isEmpty(transferObj.getAmountFrom()) && null != transferObj.getAmountFrom()) {
			predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), transferObj.getAmountFrom()));

		}

		if (!StringUtils.isEmpty(transferObj.getAmountTo()) && null != transferObj.getAmountTo()) {
			predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), transferObj.getAmountTo()));

		}

		if (!StringUtils.isEmpty(transferObj.getTxnRequestedId()) && null != transferObj.getTxnRequestedId()) {
			predicateList.add(criteriaBuilder.equal(root.get("ccPaymentId"), transferObj.getTxnRequestedId()));

		}

		if (!StringUtils.isEmpty(transferObj.getStatus()) && null != transferObj.getStatus()) {
			predicateList.add(criteriaBuilder.equal(root.get("status"), InvokeStatus.valueOf(transferObj.getStatus())));

		}

		if (!StringUtils.isEmpty(transferObj.getScheduleId()) && null != transferObj.getScheduleId()) {
			predicateList.add(criteriaBuilder.equal(root.get("scheduleId"), transferObj.getScheduleId()));
		}
		criteriaQuery.where(predicateList.toArray(new Predicate[0]));
		settlemntList = entityManager.createQuery(criteriaQuery).getResultList();

		return settlemntList;

	}	
	
	
	
	
	
	
	
	
	
	
	

	public FundTransfersReportResponseDTO getFundTransferReport(FundTransferReportRequestDTO getTransObj) {

		ResponseHeader resHeader = new ResponseHeader();
		FundTransfersReportResponseDTO transRes = new FundTransfersReportResponseDTO();

		String startDateStr = getTransObj.getFromDate();
		String endDateStr = getTransObj.getToDate();

		logger.info("start date >> " + startDateStr);
		logger.info("end date >> " + endDateStr);

		Date sDate = null;
		Date eDate = null;
		try {
			if (startDateStr != null && !endDateStr.isEmpty())
				sDate = sdf.parse(startDateStr);
			if (endDateStr != null && !endDateStr.isEmpty())
				eDate = sdf.parse(endDateStr);

		} catch (ParseException e) {
			logger.info("date parsing excepiton");
			logger.error(e.getStackTrace());
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_003.toString(),
					InvokeMessage.DATE_PARSE_EXCEPTION.toString());
			transRes.setResponseHeader(resHeader);
			return transRes;
		}

		logger.info("start date >> " + sDate);
		logger.info("end date >> " + eDate);

		logger.info("inside get transfers service");

		Iterator<Object[]> itrList;
		if (sDate != null && eDate != null && getTransObj.getUserId() != null && !getTransObj.getUserId().isEmpty()) {
			itrList = this.trHistoryRepo.getFundTransferReports(sDate, eDate, getTransObj.getUserId()).iterator();
		} else if (sDate != null && eDate != null) {
			itrList = this.trHistoryRepo.getFundTransferReports(sDate, eDate).iterator();
		} else if (getTransObj.getUserId() != null && !getTransObj.getUserId().isEmpty()) {
			itrList = this.trHistoryRepo.getFundTransferReports(getTransObj.getUserId()).iterator();
		} else {
			itrList = this.trHistoryRepo.getFundTransferReports().iterator();
		}
		transRes.setListOfTransactions(new ArrayList<FundTransferReportDTO>());
		if (itrList != null) {
			while (itrList.hasNext()) {
				Object[] objNext = itrList.next();
				FundTransferReportDTO obj = new FundTransferReportDTO();
				obj.setTransactionType(objNext[0].toString());
				obj.setCurrency(objNext[1].toString());
				obj.setTransactionCount(Integer.parseInt(objNext[2].toString()));
				obj.setAmount(Double.parseDouble(objNext[3].toString()));
				transRes.getListOfTransactions().add(obj);
			}
		}
		resHeader = new ResponseHeader(InvokeStatus.SUCCESS, InvokeMessage.GET_TRANSFERS_SUCCESS.toString());
		transRes.setResponseHeader(resHeader);

		return transRes;
	}

	private Specification<TransactionRequestHistory> generateFundTransferQuery(Date sDate, Date eDate,
			TransactionCategory transferType, GetFundTransfersRequestDTO getTransObj) {

		Specification<TransactionRequestHistory> specification;
		specification = TransactionRequestSpecification.filterFromMinAndMaxAmount(getTransObj.getAmountFrom(),
				getTransObj.getAmountTo());

		if (specification == null)
			specification = TransactionRequestSpecification.filterFromUserName(getTransObj.getUserId());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromUserName(getTransObj.getUserId()));

		if (specification == null)
			specification = TransactionRequestSpecification.filterFromTxnStartDateAndEndDate(sDate, eDate);
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromTxnStartDateAndEndDate(sDate, eDate));

		if (specification == null)
			specification = TransactionRequestSpecification.filterTransactionCategory(transferType);
		else
			specification = specification.and(TransactionRequestSpecification.filterTransactionCategory(transferType));

//		if (specification == null)
//			specification = TransactionRequestSpecification.filterFromExternelId(getTransObj.getTxnRequestedId());
//		else
//			specification = specification
//					.and(TransactionRequestSpecification.filterFromExternelId(getTransObj.getTxnRequestedId()));

		if (specification == null)
			specification = TransactionRequestSpecification.filterFromStatus(getTransObj.getStatus());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromStatus(getTransObj.getStatus()));

		if (specification == null)
			specification = TransactionRequestSpecification.filterFromTxnRequestedId(getTransObj.getTxnRequestedId());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromTxnRequestedId(getTransObj.getTxnRequestedId()));


		if (specification == null)
			specification = TransactionRequestSpecification.filterFromScheduleId(getTransObj.getScheduleId());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromScheduleId(getTransObj.getScheduleId()));

		return specification;
	}
	private Specification<TransactionRequestHistory> generateTransferQuery(Date sDate, Date eDate,GetTransfers getTransObj) {
		

		Specification<TransactionRequestHistory> specification;
		specification = TransactionRequestSpecification
				.filterFromFromAccountNumber(getTransObj.getFromAccountNumber());
		
		if (specification == null)
			specification = TransactionRequestSpecification.filterFromTxnRequestedId(getTransObj.getTransactionId());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromTxnRequestedId(getTransObj.getTransactionId()));
		
		if (specification == null)
			specification = TransactionRequestSpecification.filterFromSenderRemarks(getTransObj.getSenderRemarks());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromSenderRemarks(getTransObj.getSenderRemarks()));
		
		if (specification == null)
			specification = TransactionRequestSpecification.filterFromMaxAmount(getTransObj.getAmountMax());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromMaxAmount(getTransObj.getAmountMax()));
		
		if (specification == null)
			specification = TransactionRequestSpecification.filterFromMinAmount(getTransObj.getAmountMin());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromMinAmount(getTransObj.getAmountMin()));
		
		if (specification == null)
			specification = TransactionRequestSpecification.filterFromUserNameLowerCaseMatch(getTransObj.getUserName());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromUserNameLowerCaseMatch(getTransObj.getUserName()));
		
		if (sDate != null && eDate == null) {
			if (specification == null)
				specification = TransactionRequestSpecification.filterFromTxnStartDate(sDate);
			else
				specification = specification.and(TransactionRequestSpecification.filterFromTxnStartDate(sDate));
		} else if (sDate == null && eDate != null) {
			if (specification == null)
				specification = TransactionRequestSpecification.filterFromTxnEndDate(eDate);
			else
				specification = specification.and(TransactionRequestSpecification.filterFromTxnEndDate(eDate));
		} else if (sDate != null && eDate != null) {
			if (specification == null)
				specification = TransactionRequestSpecification.filterFromTxnStartDateAndEndDate(sDate, eDate);
			else
				specification = specification
						.and(TransactionRequestSpecification.filterFromTxnStartDateAndEndDate(sDate, eDate));
		}
		
		
		if (specification == null)
			specification = TransactionRequestSpecification.filterFromTransactionCategory(getTransObj.getTransferCategory());
		else
			specification = specification
					.and(TransactionRequestSpecification.filterFromTransactionCategory(getTransObj.getTransferCategory()));

		return specification;
	}

	public getTransfersResponse getTransfers(GetTransfers getTransObj) {

		ResponseHeader resHeader = new ResponseHeader();
		getTransfersResponse transRes = new getTransfersResponse();

		String startDateStr = getTransObj.getTxnRequestedStartDate() == null || getTransObj.getTxnRequestedStartDate().isEmpty() ? null : getTransObj.getTxnRequestedStartDate();
		String endDateStr = getTransObj.getTxnRequestedEndDate() == null || getTransObj.getTxnRequestedEndDate().isEmpty() ? null : getTransObj.getTxnRequestedEndDate();

		logger.info("start date >> " + startDateStr);
		logger.info("end date >> " + endDateStr);

		Date sDate = null;
		Date eDate = null;
		try {
			if (startDateStr != null)
				sDate = sdf.parse(startDateStr);
			if (endDateStr != null)
				eDate = sdf.parse(endDateStr);

		} catch (ParseException e) {
			logger.info("date parsing excepiton");
			logger.error(e.getStackTrace());
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_003.toString(),
					InvokeMessage.DATE_PARSE_EXCEPTION.toString());
			transRes.setResponseHeader(resHeader);
			return transRes;
		}

		logger.info("start date >> " + sDate);
		logger.info("end date >> " + eDate);

		logger.info("inside get transfers service");

		int pageNumber = (getTransObj.getStartRecord() / getTransObj.getNumberOfRecords());
		int pageSize = getTransObj.getNumberOfRecords();

		logger.info("page number > " + pageNumber);
		logger.info("page size > " + pageSize);
		Pageable pageable = PageRequest.of(pageNumber, pageSize,Sort.by("requestedDate").descending());

//		Page<TransactionRequestHistory> trHPage= this.trHistoryRepo.findAll(pageable);

		Page<TransactionRequestHistory> trHPage = this.trHistoryRepo.findAll(generateTransferQuery(sDate, eDate, getTransObj),
				pageable);

		transRes.setTrList(trHPage.getContent());
		resHeader = new ResponseHeader(InvokeStatus.SUCCESS, InvokeMessage.GET_TRANSFERS_SUCCESS.toString());
		transRes.setResponseHeader(resHeader);

		return transRes;
	}

	public CustomMcashResponse withdrawMCash(WithdrawMCashRequest mcashObj) {
		this.withdrawMCRepo.save(mcashObj);

		User u = new User();
		u.setAppCode(mCashAppCode);
		u.setAppUser(mcashObj.getUserName());

		WithdrawDataInt withdrawIntObj = new WithdrawDataInt();
		withdrawIntObj.setAccount(mcashObj.getAccountNumber());
		withdrawIntObj.setBeneID(mcashObj.getBeneficiaryId());
//		withdrawIntObj.setBeneMessage(mcashObj.getBeneficiaryMessage());
		withdrawIntObj.setBenePin(mcashObj.getBeneficiaryPin());
		withdrawIntObj.setCompanyName(mcashObj.getCompanyName());
//		withdrawIntObj.setPaidBy(Integer.parseInt(mcashObj.getPaidBy()));
//		withdrawIntObj.setPaidDate(sdf.format(mcashObj.getPaidDate()));
//		withdrawIntObj.setSendMessage(mcashObj.getSendMessage());
		withdrawIntObj.setTxnAmount(mcashObj.getTxnAmount().doubleValue());
//		withdrawIntObj.setTxnPin(mcashObj.getTxnPin());

		Withdraw withdrawObj = new Withdraw();
		withdrawObj.setUser(u);
		withdrawObj.setWithdrawData(withdrawIntObj);
		return this.mCashConsumerService.withdrawMCash(withdrawObj);
	}

	public CustomMcashResponse reverseMCash(ReverseMCashRequest mcashObj) {
		this.reverseMCRepo.save(mcashObj);

		User u = new User();
		u.setAppCode(mCashAppCode);
		u.setAppUser(mcashObj.getUserName());

		ReverseDataInt reverseIntObj = new ReverseDataInt();
//		reverseIntObj.setBeneMessage("");
//		reverseIntObj.setCompanyName(mcashObj.getCompanyName());
		reverseIntObj.setSenderId(mcashObj.getSenderId());
		reverseIntObj.setSenderPin(mcashObj.getSenderPin());
//		reverseIntObj.setSendMessage("");
//		reverseIntObj.setSuspendBy("");
//		reverseIntObj.setSuspendSol("");

		Reverse reverseObj = new Reverse();
		reverseObj.setUser(u);
		reverseObj.setReverseData(reverseIntObj);

		return this.mCashConsumerService.reverseMobileCash(reverseObj);
	}
	
	//Commission deduction
	private TransferResponse doTransferWithCommissionAccount(TransactionRequest tr, double chargeAmount, boolean isInterbanking){
			TransferResponse transferRes = new TransferResponse();	
			DoTransferRequestType transReq = new DoTransferRequestType();
			ObjectFactory objectFactory = new ObjectFactory();
//			String beneRemarks = tr.getBenificiaryRemarks();
//			String senderRemarks = tr.getSenderRemarks();
//			String transMemo = "CMNBLK-01-" + memoPrefix + "-02-" + beneRemarks + "-03--04-" + senderRemarks + "-05-"
//					+ memoPurpose;

			transReq.setAPPCode(appCode);
			transReq.setCDCICode(cdCICode);
			transReq.setController(controller);
			transReq.setDTxnAmount(chargeAmount);
			if(isInterbanking || (null != tr.getCommissionFromAccountNumber() && !tr.getCommissionFromAccountNumber().isEmpty())){
				transReq.setFromAccountNo(tr.getCommissionFromAccountNumber());
			}else{
				transReq.setFromAccountNo(tr.getFromAccountNumber());
				tr.setCommissionFromAccountNumber(tr.getFromAccountNumber());
			}
			transReq.setToAccountNo(commAccount);
//			transReq.setTransMemo(transMemo);
			if(null != this.constructTransMemo(tr)) {
				transReq.setTransMemo(this.constructTransMemo(tr));
				}else {
					ResponseHeader resHeader = new ResponseHeader();
					resHeader.setReturnStatus(InvokeStatus.FAIL);
					resHeader.setReturnMessage(InvokeMessage.FAILED_CONSTRUCT_TRANSMEMO.toString());
					transferRes.setResponseHeader(resHeader);
					return transferRes;
				}
			transReq.setValueDate(sdf2.format(new Date()));

			// TODO below mapping needed or not?
			transReq.setFromCurrCode("LKR");
			String toAccount = transReq.getToAccountNo();
			String fromAccount = transReq.getFromAccountNo();
			transReq.setToAccountNo("reducted");
			transReq.setFromAccountNo("reducted");
			logger.info("Requesting from " + iibFinacleWsdl);
			logger.info("finacle transfer request : " + jacksonConfig.convertToJson(transReq));
			transReq.setToAccountNo(toAccount);
			transReq.setFromAccountNo(fromAccount);
			// TODO Call active doFinacleTransfer
			try {
				DoTransferResponseType transRes = finacleConsumerService.doFinacleTransfer(iibFinacleWsdl,objectFactory.createDoTransferRequest(transReq));
				try {
					logger.info("Response from " + iibFinacleWsdl);
					logger.info("finacle transfer response : " + objectMapper.writeValueAsString(transRes));
				} catch (JsonProcessingException e) {
					logger.error("json mapping issue : {}", e.getLocalizedMessage());
				}
				
				transferRes.setTransferReference(tr.getTransactionId());
				transferRes.setAuthenticationType("");

				ResponseHeader respHeader = new ResponseHeader();
				// tr.setBackendResponse(transRes.getActionCode());
				respHeader.setReturnCode(transRes.getActionCode());
				if (transRes.getActionCode().equals("000")) {
					respHeader.setReturnStatus(InvokeStatus.SUCCESS);
					respHeader.setReturnMessage(InvokeMessage.DO_INTERBANK_TRANSFER_SUCCESS.toString());
				}else if (transRes.getActionCode().equals("906")
							||transRes.getActionCode().equals("909")
							||transRes.getActionCode().equals("911")
							||transRes.getActionCode().equals("913")
							||transRes.getActionCode().equals("9000")) {
					respHeader.setReturnStatus(InvokeStatus.FAILED_IN_PROCESS);
					respHeader.setReturnMessage(transRes.getProcessedDesc());
				} else {
					respHeader.setReturnStatus(InvokeStatus.FAIL);
					respHeader.setReturnMessage(InvokeMessage.FAILED_TO_DO_FINACLE_TRANSFER_FOR_COMMISSION.toString());		
				}
				transferRes.setResponseHeader(respHeader);
				return transferRes;
			} catch (Exception e) {
				ResponseHeader respHeader = new ResponseHeader();
				respHeader.setReturnStatus(InvokeStatus.FAIL);
				respHeader.setReturnMessage(InvokeMessage.FAILED_TO_DO_FINACLE_TRANSFER_FOR_COMMISSION.toString());
				transferRes.setResponseHeader(respHeader);
				return transferRes;
			} catch (Throwable e1) {
				logger.error("Failed to do finacle transfer : {}", e1.getLocalizedMessage());
				ResponseHeader respHeader = new ResponseHeader();
				respHeader.setReturnStatus(InvokeStatus.FAIL);
				respHeader.setReturnMessage(InvokeMessage.FAILED_TO_DO_FINACLE_TRANSFER_FOR_COMMISSION.toString());
				transferRes.setResponseHeader(respHeader);
				return transferRes;
			}
	}
	
	private double getChargeCommissionAmount(TransactionRequest tr, String uuid) throws Exception{
		String  chargeProfileId;
		try {
			ProfileResponse profileResponse = restCustomerManagementService.getCustomerProfileInfo(tr.getUserName(),uuid);
			if(profileResponse != null && profileResponse.getPayLoad() !=  null && profileResponse.getPayLoad() != null && profileResponse.getPayLoad() != null ) {
				chargeProfileId=  profileResponse.getPayLoad().getChargeProfile();

				tr.setFromAccountName(profileResponse.getPayLoad().getCustomerName() != null ? profileResponse.getPayLoad().getCustomerName() : tr.getFromAccountName());
			}else {
				throw new Exception(InvokeMessage.PROFILE_NOT_FOUND.toString());
			}
		}catch (Exception ex) {
			throw new Exception(InvokeMessage.ISSUE_ON_RETRIEVING_PROFILE.toString());
		}
		try {
			// Added this code block because finserve only adding OTH type charge profile
			TransactionCategory chargeType = tr.getTransactionCategory();
			if (TransactionCategory.MC.equals(chargeType)
					|| (TransactionCategory.OTH.equals(chargeType) && !sampathBankCode.equals(tr.getBankCode()))
					|| (TransactionCategory.CC.equals(chargeType) && !sampathBankCode.equals(tr.getBankCode()))) {
				chargeType = TransactionCategory.OTH;
			}
			// end
			return restChargeProfileService.getChargeProfile(tr.getUserName(), chargeProfileId, chargeType.toString());
		}catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	public boolean pushNotification(String username, String status,
			long reference, double amount) {
		RestTemplate restTemplate = new RestTemplate();

		String dateFormat = "dd-MM-yyyy HH:mm:ss";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);

		boolean isSuccess = false;

		PushNotificationRequestDto pushNotificationReq = new PushNotificationRequestDto();
		pushNotificationReq
				.setNotificationDate(LocalDateTime.now().format(formatter));
		pushNotificationReq.setUsername(username);
		pushNotificationReq
		.setContent("Transaction value of LKR " + amount + " was made with reference : " + reference);
		pushNotificationReq.setStatus(status);
		pushNotificationReq.setTopic("Sampath TFR ");// No userId and device Id

		HttpEntity<PushNotificationRequestDto> pushNotificationReqEntity = new HttpEntity<PushNotificationRequestDto>(
				pushNotificationReq, header);

		try {
			logger.info(
					"PushNotification request : username {} content {} topic Sampath SR",
					username, pushNotificationReq.getContent());
			ResponseEntity<CommonApplicationResponse> response = restTemplate
					.exchange(pushNotificationUrl, HttpMethod.POST,
							pushNotificationReqEntity,
							CommonApplicationResponse.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				isSuccess = true;
			}
		}catch (HttpClientErrorException e) {
			logger.error("Error sending pushNotification : {}",e.getResponseBodyAsString());
			return false;
		}catch (RestClientException e) {
			logger.error(
					"Error occured when consuming pushNotification service", e);
			return false;
		}
		return isSuccess;

	}

	public CommonApplicationResponse doCcPaymentTransfer(PayCreditCardRequest request) {

		CommonApplicationResponse resp = null;
		CreditCardPaymentTransaction txn = null;
		
		// Added this code block if request comes from shedule ops service.(If already integrated remove this part)
		if(null == request.getTransactionType()) {
			request.setTransactionType(TransactionType.SCHEDULE);
		}
		
		PaymentCardResponse responsePay = null;
		try {
			responsePay = cCRestService.paymentCard(request);
		} catch (Throwable e) {
			logger.error("Card payment failed : {}", e);
			resp = new CommonApplicationResponse(400, "Internel error occured");
			return resp;
		}
	
		PaymentCardOminiResponse response = new PaymentCardOminiResponse(responsePay);
		PaymentCCResponse ccresponse = new PaymentCCResponse();
		
		String cardnumber="";
		if(response.getCode() == 9000) {
			 cardnumber="";
		}else {
			 cardnumber = cCRestService.getCardNumberFromSerial(request.getCardSerialNumber());
		}
		
		
		if (response.getCode() == 0 && response.getSubCode() == 0) {
			
			txn = new CreditCardPaymentTransaction(request.getVishwaUserId(), request.getCardSerialNumber(),
					request.getDebitAccountNumber(), request.getAmount(),
					response.getCode() + "-"+ InvokeStatus.SUCCESS.name() +"-"+ response.getMessage(), InvokeStatus.SUCCESS, request.getTransactionType(),"LKR",cardnumber);
			CreditCardPaymentTransaction paymentResponse = cCPaymentTrnRepository.save(txn);
			
			ccresponse.setCode(response.getCode());
			ccresponse.setMessage(response.getMessage());
			ccresponse.setMessageCustomer(response.getMessageCustomer());
			ccresponse.setSubCode(response.getSubCode());
			ccresponse.setRequestId(response.getRequestId());
			ccresponse.setReference(paymentResponse.getCcPaymentId());
			
			
			resp = new CommonApplicationResponse(response.getCode().intValue(), response.getMessage());
			resp.setPayload(ccresponse);
			
			return resp;
		}else if(response.getCode() == 9000) {
			txn = new CreditCardPaymentTransaction(request.getVishwaUserId(), request.getCardSerialNumber(),
					request.getDebitAccountNumber(), request.getAmount(), 
					response.getCode() + "-"+ InvokeStatus.FAILED_IN_PROCESS.name() +"-"+ response.getMessage(), InvokeStatus.FAILED_IN_PROCESS, request.getTransactionType(),"LKR",cardnumber);
			 cCPaymentTrnRepository.save(txn);	
			 resp = new CommonApplicationResponse(response.getCode().intValue(), response.getMessage());
				resp.setPayload(response);
				return resp;
		} else {
			txn = new CreditCardPaymentTransaction(request.getVishwaUserId(), request.getCardSerialNumber(),
					request.getDebitAccountNumber(), request.getAmount(), 
					response.getCode() + "-"+ InvokeStatus.FAIL.name() +"-"+ response.getMessage(), InvokeStatus.FAIL, request.getTransactionType(),"LKR",cardnumber);
			 cCPaymentTrnRepository.save(txn);			
			resp = new CommonApplicationResponse(response.getCode().intValue(), response.getMessage());
			resp.setPayload(response);
			return resp;
		}
	}

	public CommonApplicationResponse doCcPaymentTransferForApp(String userName, PayCreditCardRequestForApp request) {

		CommonApplicationResponse resp = null;
		CreditCardPaymentTransaction txn = null;

		// Added this code block if request comes from shedule ops service.(If already integrated remove this part)
		if (null == request.getTransactionType()) {
			request.setTransactionType(TransactionType.SCHEDULE);
		}

		PaymentCardResponseForAppAPIs responsePay = null;
		try {
			responsePay = cCRestService.paymentCardForApp(request);
		} catch (Throwable e) {
			logger.error("Card payment failed : {}", e);
			resp = new CommonApplicationResponse(400, "Internel error occured");
			return resp;
		}

		PaymentCardOminiResponseForAppAPIs response = new PaymentCardOminiResponseForAppAPIs(responsePay);
		PaymentCCResponseForAppAPIs ccresponse = new PaymentCCResponseForAppAPIs();

		String cardnumber = "";
		if (response.getCode() == 9000) {
			cardnumber = "";
		} else {
			cardnumber = cCRestService.getCardNumberFromSerialForNewAPIs(request);
		}

		if (response.getCode() == 0 && response.getSubCode() == 0) {

			txn = new CreditCardPaymentTransaction(userName, request.getCardSerialNumber(),
					request.getDebitAccountNumber(), request.getAmount(),
					response.getCode() + "-" + InvokeStatus.SUCCESS.name() + "-" + response.getMessage(), InvokeStatus.SUCCESS, request.getTransactionType(), "LKR", cardnumber);
			CreditCardPaymentTransaction paymentResponse = cCPaymentTrnRepository.save(txn);

			ccresponse.setCode(response.getCode());
			ccresponse.setMessage(response.getMessage());
			ccresponse.setMessageCustomer(response.getMessageCustomer());
			ccresponse.setSubCode(response.getSubCode());
			ccresponse.setRequestId(response.getRequestId());
			ccresponse.setCcReferenceId(paymentResponse.getCcPaymentId());
			ccresponse.setRequestReference(response.getRequestReference());
			ccresponse.setChainSerno(response.getChainSerno());
			ccresponse.setChainAuth(response.getChainAuth());

			resp = new CommonApplicationResponse(response.getCode().intValue(), response.getMessage());
			resp.setPayload(ccresponse);

			return resp;
		} else if (response.getCode() == 9000) {
			txn = new CreditCardPaymentTransaction(userName, request.getCardSerialNumber(),
					request.getDebitAccountNumber(), request.getAmount(),
					response.getCode() + "-" + InvokeStatus.FAILED_IN_PROCESS.name() + "-" + response.getMessage(), InvokeStatus.FAILED_IN_PROCESS, request.getTransactionType(), "LKR", cardnumber);
			CreditCardPaymentTransaction paymentResponse = cCPaymentTrnRepository.save(txn);

			ccresponse.setCode(response.getCode());
			ccresponse.setMessage(response.getMessage());
			ccresponse.setMessageCustomer(response.getMessageCustomer());
			ccresponse.setSubCode(response.getSubCode());
			ccresponse.setRequestId(response.getRequestId());
			ccresponse.setCcReferenceId(paymentResponse.getCcPaymentId());
			ccresponse.setRequestReference(response.getRequestReference());
			ccresponse.setChainSerno(response.getChainSerno());
			ccresponse.setChainAuth(response.getChainAuth());

			resp = new CommonApplicationResponse(response.getCode().intValue(), response.getMessage());
			resp.setPayload(ccresponse);
			return resp;
		} else {
			txn = new CreditCardPaymentTransaction(userName, request.getCardSerialNumber(),
					request.getDebitAccountNumber(), request.getAmount(),
					response.getCode() + "-" + InvokeStatus.FAIL.name() + "-" + response.getMessage(), InvokeStatus.FAIL, request.getTransactionType(), "LKR", cardnumber);
			CreditCardPaymentTransaction paymentResponse = cCPaymentTrnRepository.save(txn);

			ccresponse.setCode(response.getCode());
			ccresponse.setMessage(response.getMessage());
			ccresponse.setMessageCustomer(response.getMessageCustomer());
			ccresponse.setSubCode(response.getSubCode());
			ccresponse.setRequestId(response.getRequestId());
			ccresponse.setCcReferenceId(paymentResponse.getCcPaymentId());
			ccresponse.setRequestReference(response.getRequestReference());
			ccresponse.setChainSerno(response.getChainSerno());
			ccresponse.setChainAuth(response.getChainAuth());

			resp = new CommonApplicationResponse(response.getCode().intValue(), response.getMessage());
			resp.setPayload(ccresponse);
			return resp;
		}
	}

	public TransferResponse processCCTransfer(TransactionRequest tr) {

		TransferResponse transferRes = new TransferResponse();
		ResponseHeader resHeader = new ResponseHeader();

		try {

			logger.info("Start call to get serial from card number");
			GetCardSerailResponse getCardSerailResponse = cCRestService
					.getSerialFromCardNumber(tr.getToAccountNumber());
			logger.info("End call to get serial from card number");

			if (getCardSerailResponse == null || getCardSerailResponse.getSerno() == null) {

				logger.info("Could not retrieve card serial");
				resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_O18.toString(),
						InvokeMessage.COULD_NOT_RETRIVE_CARD_SERIAL.toString());

			} else {

				PayCreditCardRequest request = new PayCreditCardRequest();
				request.setAmount(tr.getTransactionAmount());
				request.setCardSerialNumber(getCardSerailResponse.getSerno());
				request.setDebitAccountNumber(tr.getFromAccountNumber());
				request.setTransactionType(tr.getTransactionType());
				request.setVishwaUserId(tr.getUserName());

				logger.info("Start process credit card payment");
				CCpaymentCardResponse responsePay = cCRestService.ccpaymentCard(request);
				logger.info("End process credit card payment", jacksonConfig.convertToJson(responsePay));

				if (responsePay.getCode() == 0 && responsePay.getSubCode() == 0) {
					resHeader.setReturnCode(InvokeStatus.SUCCESS.toString());
					resHeader.setReturnStatus(InvokeStatus.SUCCESS);
					resHeader.setReturnMessage(InvokeMessage.DO_CC_TRANSFER_SUCCESS.toString());
				} else {
					resHeader.setReturnCode(InvokeStatus.FAIL.toString());
					resHeader.setReturnStatus(InvokeStatus.FAIL);
					resHeader.setReturnMessage(InvokeMessage.DO_CC_TRANSFER_FAILED.toString());
				}
			}
		} catch (Exception e) {
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_O18.toString(),
					InvokeMessage.EXCEPTION_OCCURED_WHEN_DO_CC_TRANSFER.toString());
		}
		transferRes.setAuthenticationType("");
		transferRes.setTransferReference(tr.getTransactionId());
		transferRes.setResponseHeader(resHeader);

		return transferRes;
	}

	public TransferResponse processCCTransferForAppNet(TransactionRequest trq, DoTransferReqForCard dtrfc) {

		TransferResponse transferRes = new TransferResponse();
		ResponseHeader resHeader = new ResponseHeader();
		CapexPayLoad capexPayLoad = new CapexPayLoad();

		try {

			logger.info("Start call to get serial from card number");
			GetCardSerialResponseForAppAPIs getCardSerailResponse = cCRestService
					.getSerialFromCardNumberForAppAPIs(trq.getToAccountNumber(), dtrfc);
			logger.info("End call to get serial from card number");

			if (getCardSerailResponse == null || getCardSerailResponse.getSerno() == null) {

				logger.info("Could not retrieve card serial");
				resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_O18.toString(),
						InvokeMessage.COULD_NOT_RETRIVE_CARD_SERIAL.toString());

			} else {

				PayCreditCardRequest request = new PayCreditCardRequest();
				request.setAmount(trq.getTransactionAmount());
				request.setCardSerialNumber(getCardSerailResponse.getSerno());
				request.setDebitAccountNumber(trq.getFromAccountNumber());
				request.setPaymentCurrency(trq.getCurrency());
				request.setInitiatedSerno(dtrfc.getInitiatedSerno());
				request.setInitiatedKey(dtrfc.getInitiatedKey());
				request.setChainSerno(getCardSerailResponse.getChainSerno());
				request.setChainAuth(getCardSerailResponse.getChainAuth());
				request.setDeviceId(dtrfc.getDeviceId());
				request.setIdentityType(dtrfc.getIdentityType());
				request.setNIC(dtrfc.getNIC());

				logger.info("Start process credit card payment");
				CCpaymentCardResponseForAppAPIs responsePay = cCRestService.ccpaymentCardForAppAPIs(request);
				logger.info("End process credit card payment", jacksonConfig.convertToJson(responsePay.toString()));

				if (responsePay.getCode() == 0 && responsePay.getSubCode() == 0) {
					resHeader.setReturnCode("000");
					resHeader.setReturnStatus(InvokeStatus.SUCCESS);
					resHeader.setReturnMessage(InvokeMessage.DO_CC_TRANSFER_SUCCESS.toString());

					capexPayLoad.setCode(responsePay.getCode());
					capexPayLoad.setSubCode(responsePay.getSubCode());
					capexPayLoad.setMessage(responsePay.getMessage());
					capexPayLoad.setMessageCustomer(responsePay.getMessageCustomer());
					capexPayLoad.setRequestId(request.getDeviceId());
					capexPayLoad.setChainSerno(responsePay.getChainSerno());
					capexPayLoad.setChainAuth(responsePay.getChainAuth());
				} else {
					resHeader.setReturnCode(InvokeStatus.FAIL.toString());
					resHeader.setReturnStatus(InvokeStatus.FAIL);
					resHeader.setReturnMessage(InvokeMessage.DO_CC_TRANSFER_FAILED.toString());

					capexPayLoad.setCode(responsePay.getCode());
					capexPayLoad.setSubCode(responsePay.getSubCode());
					capexPayLoad.setMessage(responsePay.getMessage());
					capexPayLoad.setMessageCustomer(responsePay.getMessageCustomer());
					capexPayLoad.setRequestId(request.getDeviceId());
					capexPayLoad.setChainSerno(responsePay.getChainSerno());
					capexPayLoad.setChainAuth(responsePay.getChainAuth());
				}
			}
		} catch (Exception e) {
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_O18.toString(),
					InvokeMessage.EXCEPTION_OCCURED_WHEN_DO_CC_TRANSFER.toString());
		}
		transferRes.setAuthenticationType("");
		transferRes.setTransferReference(trq.getTransactionId());
		transferRes.setResponseHeader(resHeader);
		transferRes.setCapexPayLoad(capexPayLoad);
		return transferRes;
	}
}
