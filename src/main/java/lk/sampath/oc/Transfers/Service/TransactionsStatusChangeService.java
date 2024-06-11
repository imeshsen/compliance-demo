package lk.sampath.oc.Transfers.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import lk.sampath.oc.Transfers.Entity.CreditCardPaymentTransaction;
import lk.sampath.oc.Transfers.Entity.TransactionRequestHistory;
import lk.sampath.oc.Transfers.Entity.TransactionStatusChange;
import lk.sampath.oc.Transfers.Entity.TransactionStatusChangeHistory;
import lk.sampath.oc.Transfers.Enums.ErrorCode;
import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Exceptions.ServiceException;
import lk.sampath.oc.Transfers.Pojo.CountResponse;
import lk.sampath.oc.Transfers.Pojo.approval.ApprovalDTO;
import lk.sampath.oc.Transfers.Pojo.approval.ApprovalListResponseDTO;
import lk.sampath.oc.Transfers.Pojo.approval.ApprovalResponseDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.ApprovedTransferStatusUpdateResponse;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.SubmitTransferStatusChangeRequestDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.SubmitTransferStatusUpdateResponse;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.TransferStatusAuthorizationsOutputDto;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.TransferStatusAuthorizationsResponse;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.TransferStatusUpdateDTO;
import lk.sampath.oc.Transfers.Repository.CCPaymentTrnRepository;
import lk.sampath.oc.Transfers.Repository.TransactionHistoryRepository;
import lk.sampath.oc.Transfers.Repository.TransactionStatusChangeHistoryRepository;
import lk.sampath.oc.Transfers.Repository.TransactionStatusChangeRepository;

@Service
public class TransactionsStatusChangeService {
	private static Logger logger = LogManager.getLogger(TransactionsStatusChangeService.class);

	private static final String APPROVAL_TYPE = "TRANSACTION_STATUS";
	private static final String PENDING_STATUS = "PENDING";
	@Autowired
	private TransactionStatusChangeRepository transactionStatusChangeRepository;
	@Autowired
	private TransactionHistoryRepository transactionHistoryRepository;
	@Autowired
	private CCPaymentTrnRepository cCPaymentTrnRepository;
	@Autowired
	private TransactionStatusChangeHistoryRepository transactionStatusChangeHistoryRepository;
	@Autowired
	private RestConsumerApprovalService restConsumerApprovalService;

	@Value("${config.bankcode.sampathbank}")
	private String sampathBankCode;
	
	public SubmitTransferStatusUpdateResponse submitTransferStatusUpdate(

			SubmitTransferStatusChangeRequestDTO submitTransferStatusUpdate) {
		// TODO Create Call TXN Call
		SubmitTransferStatusUpdateResponse response = new SubmitTransferStatusUpdateResponse();
		try {
//			TransactionRequestHistory history = null;
//			CreditCardPaymentTransaction ccHistory =null;
//			if(submitTransferStatusUpdate.getModeOfCC().equals("CC_OTH")) {
//				history = transactionHistoryRepository.findByTransactionId(submitTransferStatusUpdate.getTransferRequestId());
//				if(history == null) {
//					logger.error("No such transaction history for given transfer id");
//					response.setReturnStatus(InvokeStatus.FAIL);
//					response.setReturnCode(ErrorCode.TR_013.toString());
//					response.setReturnMessage(InvokeMessage.INVALID_TRANSFER_REFERANCE.toString());
//					return response;
//				}
//			}else if(submitTransferStatusUpdate.getModeOfCC().equals("CC_OWN")) {
//				ccHistory = cCPaymentTrnRepository.findByCcPaymentId(submitTransferStatusUpdate.getTransferRequestId());
//				if(ccHistory == null) {
//					logger.error("No such transaction history for given transfer id");
//					response.setReturnStatus(InvokeStatus.FAIL);
//					response.setReturnCode(ErrorCode.TR_013.toString());
//					response.setReturnMessage(InvokeMessage.INVALID_TRANSFER_REFERANCE.toString());
//					return response;
//				}
//			}
//			
//			if(submitTransferStatusUpdate.getModeOfCC().equals("CC_OTH")) {
//				TransactionStatusChange statusChangeObj = transactionStatusChangeRepository.findbyTransactionRequestID(history);
//				if(statusChangeObj != null) {
//					logger.error("Already pending a status change request for transactionID : {}", submitTransferStatusUpdate.getTransferRequestId());
//					response.setReturnStatus(InvokeStatus.FAIL);
//					response.setReturnCode(ErrorCode.TR_002.toString());
//					response.setReturnMessage(InvokeMessage.TRANSACTION_STATUS_CHANGE_REQUEST_ALREADY_PENDING.toString());
//					return response;
//					
//				}
//			}else if(submitTransferStatusUpdate.getModeOfCC().equals("CC_OWN")) {
//				TransactionStatusChange statusChangeObj = transactionStatusChangeRepository.findbyCcPaymentId(ccHistory);
//				if(statusChangeObj != null) {
//					logger.error("Already pending a status change request for transactionID : {}", submitTransferStatusUpdate.getTransferRequestId());
//					response.setReturnStatus(InvokeStatus.FAIL);
//					response.setReturnCode(ErrorCode.TR_002.toString());
//					response.setReturnMessage(InvokeMessage.TRANSACTION_STATUS_CHANGE_REQUEST_ALREADY_PENDING.toString());
//					return response;
//					
//				}
//			}
			
			TransactionRequestHistory history = transactionHistoryRepository.findByTransactionId(submitTransferStatusUpdate.getTransferRequestId());
			if(history == null) {
				logger.error("No such transaction history for given transfer id");
				response.setReturnStatus(InvokeStatus.FAIL);
				response.setReturnCode(ErrorCode.TR_013.toString());
				response.setReturnMessage(InvokeMessage.INVALID_TRANSFER_REFERANCE.toString());
				return response;
			}

			TransactionStatusChange statusChangeObj = transactionStatusChangeRepository.findbyTransactionRequestID(history);
			if(statusChangeObj != null) {
				logger.error("Already pending a status change request for transactionID : {}", submitTransferStatusUpdate.getTransferRequestId());
				response.setReturnStatus(InvokeStatus.FAIL);
				response.setReturnCode(ErrorCode.TR_002.toString());
				response.setReturnMessage(InvokeMessage.TRANSACTION_STATUS_CHANGE_REQUEST_ALREADY_PENDING.toString());
				return response;

			}
			
			
			/**
			 * Create new request and sending to the api
			 */
			ApprovalDTO approvalDto = new ApprovalDTO();
			approvalDto.setReferenceId(String.valueOf(submitTransferStatusUpdate.getTransferRequestId()));
			approvalDto.setType(APPROVAL_TYPE);
			ApprovalResponseDTO approvalResponseDTO = restConsumerApprovalService.createApproval(approvalDto,
					submitTransferStatusUpdate.getAdminUserId(),submitTransferStatusUpdate.getUserGroup());

			TransactionStatusChange entity = new TransactionStatusChange();
			entity.setAdminUserId(submitTransferStatusUpdate.getAdminUserId());
			entity.setNewStatus(submitTransferStatusUpdate.getNewStatus().toString());
			entity.setUserGroup(submitTransferStatusUpdate.getUserGroup());
			entity.setTransactionRequestHistory(history);
//			entity.setCreditCardPaymentTransaction(ccHistory);
//			entity.setModeOfCC(submitTransferStatusUpdate.getModeOfCC());
			entity.setApprovalId(approvalResponseDTO.getPayLoad().getApprovalID());
			transactionStatusChangeRepository.save(entity);
			response.setReturnCode(ErrorCode.TR_001.toString());
			response.setApprovalId(entity.getApprovalId());
			response.setReturnStatus(InvokeStatus.SUCCESS);
			response.setReturnMessage(InvokeMessage.TRANSACTION_STATUS_CHANGE_REQUEST_SUBMMITED.toString());
		} catch (Exception ex) {
			logger.error(InvokeMessage.FAILED_TO_SUBMIT_TRANSACTION_STATUS_CHANGE_REQUEST,ex);
			response.setReturnStatus(InvokeStatus.FAIL);
			response.setReturnCode(ErrorCode.TR_002.toString());
			response.setReturnMessage(InvokeMessage.FAILED_TO_SUBMIT_TRANSACTION_STATUS_CHANGE_REQUEST.toString());
		}
		return response;
	}

	public TransferStatusAuthorizationsResponse getTransferStatusAuthorizations(@RequestBody String adminUserId) {

		TransferStatusAuthorizationsResponse responseObj = new TransferStatusAuthorizationsResponse();
		responseObj.setListOfTransactions(new ArrayList<TransferStatusAuthorizationsOutputDto>());
		int pageNumber = (1 / 200);
		int pageSize = 200;
		try {

			logger.debug("page number > " + pageNumber);
			logger.debug("page size > " + pageSize);
			Pageable pageable = new PageRequest(pageNumber, pageSize);
			
			Iterator<TransactionStatusChange> itrTxnObject = this.transactionStatusChangeRepository.findAll(pageable)
					.iterator();

			while (itrTxnObject.hasNext()) {
				TransactionStatusChange entity = itrTxnObject.next();
				TransferStatusAuthorizationsOutputDto obj = new TransferStatusAuthorizationsOutputDto();
				obj.setAmount(entity.getTransactionRequestHistory().getTransactionAmount());
				obj.setApprovalId(entity.getApprovalId());
				obj.setCurrency(entity.getTransactionRequestHistory().getCurrency());
				obj.setFromAccount(entity.getTransactionRequestHistory().getFromAccountNumber());
				obj.setNewStatus(entity.getNewStatus());
				obj.setStatus(entity.getTransactionRequestHistory().getStatus());
				obj.setToAccount(entity.getTransactionRequestHistory().getToAccountNumber());
				obj.setToBank(entity.getTransactionRequestHistory().getBankCode());
				obj.setTransferRequestDate(entity.getTransactionRequestHistory().getRequestedDate());
				obj.setTransferRequestId(entity.getTransactionRequestHistory().getTransactionId());
				obj.setTransferType(entity.getTransactionRequestHistory().getTransactionCategory().toString());
				obj.setUserId(entity.getTransactionRequestHistory().getUserName());
				responseObj.getListOfTransactions().add(obj);
			}
			
			responseObj.setReturnCode(ErrorCode.TR_001.toString());
			responseObj.setReturnMessage(InvokeMessage.GET_PENDING_TRANSFER_REQUEST_SUCCESS.toString());
		} catch (Exception ex) {
			responseObj.setReturnCode(ErrorCode.TR_002.toString());
			responseObj.setReturnMessage(InvokeMessage.GET_PENDING_TRANSFER_REQUEST_FAILED.toString());
		}

		return responseObj;
	}
	
	public TransferStatusAuthorizationsResponse getTransferStatusAuthorizationsByGroupID( String groupID,String userName) {
		
		
		TransferStatusAuthorizationsResponse responseObj = new TransferStatusAuthorizationsResponse();
		responseObj.setListOfTransactions(new ArrayList<TransferStatusAuthorizationsOutputDto>());
		try {
			
			ApprovalListResponseDTO approvalListResponseDTO=restConsumerApprovalService.retrieveApprovals(PENDING_STATUS, APPROVAL_TYPE, groupID,userName);
			
			Iterator<ApprovalDTO>  itrApprovalDTO = approvalListResponseDTO.getListOfApprovals().iterator();
			while (itrApprovalDTO.hasNext()) {
				ApprovalDTO approvalDTO =itrApprovalDTO.next();
				TransactionStatusChange entity = this.transactionStatusChangeRepository.findByApprovalId(approvalDTO.getApprovalID()).get();
				TransferStatusAuthorizationsOutputDto obj = new TransferStatusAuthorizationsOutputDto();
//				if(entity.getModeOfCC() == null || entity.getModeOfCC().isEmpty() || entity.getModeOfCC().equals("CC_OTH")) {
//					obj.setAmount(entity.getTransactionRequestHistory().getTransactionAmount());
//					obj.setApprovalId(entity.getApprovalId());
//					obj.setCurrency(entity.getTransactionRequestHistory().getCurrency());
//					obj.setFromAccount(entity.getTransactionRequestHistory().getFromAccountNumber());
//					obj.setNewStatus(entity.getNewStatus());
//					obj.setStatus(entity.getTransactionRequestHistory().getStatus());
//					obj.setToAccount(entity.getTransactionRequestHistory().getToAccountNumber());
//					obj.setToBank(entity.getTransactionRequestHistory().getBankCode());
//					obj.setTransferRequestDate(entity.getTransactionRequestHistory().getRequestedDate());
//					obj.setTransferRequestId(entity.getTransactionRequestHistory().getTransactionId());
//					obj.setTransferType(entity.getTransactionRequestHistory().getTransactionCategory().toString());
//					obj.setUserId(entity.getTransactionRequestHistory().getUserName());
//					responseObj.getListOfTransactions().add(obj);
//				}else if( entity.getModeOfCC().equals("CC_OWN")) {
//					obj.setAmount(entity.getCreditCardPaymentTransaction().getAmount());
//					obj.setApprovalId(entity.getApprovalId());
//					obj.setCurrency(entity.getCreditCardPaymentTransaction().getCurrency());
//					obj.setFromAccount(entity.getCreditCardPaymentTransaction().getDebitAccountNumber());
//					obj.setNewStatus(entity.getNewStatus());
//					obj.setStatus(entity.getCreditCardPaymentTransaction().getStatus().toString());
//					obj.setToAccount(entity.getCreditCardPaymentTransaction().getCardNumber());
//					obj.setToBank(sampathBankCode);
//					obj.setTransferRequestDate(entity.getCreditCardPaymentTransaction().getAddedDate());
//					obj.setTransferRequestId(entity.getCreditCardPaymentTransaction().getCcPaymentId());
//					obj.setTransferType(TransactionCategory.CC.toString());
//					obj.setUserId(entity.getCreditCardPaymentTransaction().getVishwaUserId());
//					responseObj.getListOfTransactions().add(obj);
//				}
				
				obj.setAmount(entity.getTransactionRequestHistory().getTransactionAmount());
				obj.setApprovalId(entity.getApprovalId());
				obj.setCurrency(entity.getTransactionRequestHistory().getCurrency());
				obj.setFromAccount(entity.getTransactionRequestHistory().getFromAccountNumber());
				obj.setNewStatus(entity.getNewStatus());
				obj.setStatus(entity.getTransactionRequestHistory().getStatus());
				obj.setToAccount(entity.getTransactionRequestHistory().getToAccountNumber());
				obj.setToBank(entity.getTransactionRequestHistory().getBankCode());
				obj.setTransferRequestDate(entity.getTransactionRequestHistory().getRequestedDate());
				obj.setTransferRequestId(entity.getTransactionRequestHistory().getTransactionId());
				obj.setTransferType(entity.getTransactionRequestHistory().getTransactionCategory().toString());
				obj.setUserId(entity.getTransactionRequestHistory().getUserName());
				responseObj.getListOfTransactions().add(obj);
				
			}
			
			responseObj.setReturnCode(ErrorCode.TR_001.toString());
			responseObj.setReturnMessage(InvokeMessage.GET_PENDING_TRANSFER_REQUEST_SUCCESS.toString());
		} catch (Exception ex) {
			logger.error(InvokeMessage.GET_PENDING_TRANSFER_REQUEST_FAILED,ex);
			responseObj.setReturnCode(ErrorCode.TR_002.toString());
			responseObj.setReturnMessage(InvokeMessage.GET_PENDING_TRANSFER_REQUEST_FAILED.toString());
		}

		return responseObj;
	}

	public ApprovedTransferStatusUpdateResponse approveTransferStatusUpdate(
			@RequestBody TransferStatusUpdateDTO approveTransferStatusUpdateDto) {
		// TODO Create Call TXN Call
		ApprovedTransferStatusUpdateResponse response = new ApprovedTransferStatusUpdateResponse();
		try {

			Optional<TransactionStatusChange> transactionStatusChange = transactionStatusChangeRepository
					.findByApprovalId(approveTransferStatusUpdateDto.getApprovalId());
			if (!transactionStatusChange.isPresent()) {
				response.setReturnStatus(InvokeStatus.SUCCESS);
				response.setReturnCode(ErrorCode.TR_002.toString());
				response.setReturnMessage(InvokeMessage.TRANSACTION_STATUS_CHANGE_REQUEST_NOT_EXISTS.toString());
				return response;
			}
			TransactionStatusChange obj = transactionStatusChange.get();

			ApprovalDTO approvalDto = new ApprovalDTO();
			approvalDto.setApprovalID(approveTransferStatusUpdateDto.getApprovalId());
			approvalDto.setApprover(approveTransferStatusUpdateDto.getAdminUserId());
			approvalDto.setApprovalStatus(approveTransferStatusUpdateDto.getAction().toString());
			approvalDto.setComment(approveTransferStatusUpdateDto.getComments());
			approvalDto.setReferenceId(String.valueOf(obj.getTransactionRequestHistory().getTransactionId()));
			
//			if(obj.getModeOfCC() == null || obj.getModeOfCC().isEmpty() || obj.getModeOfCC().equals("CC_OTH")) {
//				approvalDto.setReferenceId(String.valueOf(obj.getTransactionRequestHistory().getTransactionId()));
//			}else if(obj.getModeOfCC().equals("CC_OWN")){
//				approvalDto.setReferenceId(String.valueOf(obj.getCreditCardPaymentTransaction().getCcPaymentId()));
//			}
			
			ApprovalResponseDTO approvalResponseDTO = restConsumerApprovalService.approve(approvalDto,
					approveTransferStatusUpdateDto.getAdminUserId(),approveTransferStatusUpdateDto.getUserGroup());
			if (approvalResponseDTO.getReturnCode().equalsIgnoreCase("200") && approvalResponseDTO.getApprovalStatus().equals("VERIFIED")) {
				
				TransactionRequestHistory txnHistory = obj.getTransactionRequestHistory();
				txnHistory.setStatus(obj.getNewStatus());
				transactionHistoryRepository.save(txnHistory);
				
//				if(obj.getModeOfCC() == null || obj.getModeOfCC().isEmpty() || obj.getModeOfCC().equals("CC_OTH")) {
//					TransactionRequestHistory txnHistory = obj.getTransactionRequestHistory();
//					txnHistory.setStatus(obj.getNewStatus());
//					transactionHistoryRepository.save(txnHistory);
//				}else if(obj.getModeOfCC().equals("CC_OWN")) {
//					CreditCardPaymentTransaction txnHistory = obj.getCreditCardPaymentTransaction();
//					txnHistory.setStatus(obj.getNewStatus().equals("SUCCESS") ? InvokeStatus.SUCCESS :InvokeStatus.FAIL);
//					cCPaymentTrnRepository.save(txnHistory);
//				}
				
				
				TransactionStatusChangeHistory objHistory = new TransactionStatusChangeHistory();
				objHistory.setId(obj.getId());
				objHistory.setAdminUserId(obj.getAdminUserId());
				objHistory.setApprovalAction(approveTransferStatusUpdateDto.getAction());
				objHistory.setApprovalId(obj.getApprovalId());
				objHistory.setComment(approveTransferStatusUpdateDto.getComments());
				objHistory.setNewStatus(obj.getNewStatus());
				objHistory.setUserGroup(obj.getUserGroup());
				objHistory.setTransactionRequestHistory(obj.getTransactionRequestHistory());
//				objHistory.setCreditCardPaymentTransaction(obj.getCreditCardPaymentTransaction());
				transactionStatusChangeHistoryRepository.save(objHistory);
				transactionStatusChangeRepository.delete(obj);
				response.setReturnStatus(InvokeStatus.SUCCESS);
				response.setReturnCode(ErrorCode.TR_001.toString());
				response.setComment(approveTransferStatusUpdateDto.getComments());
				response.setReturnMessage(InvokeMessage.TRANSACTION_STATUS_CHANGE_APPRODVED_TO.toString() + " : " + obj.getNewStatus());
				
			} else if(approvalResponseDTO.getReturnCode().equalsIgnoreCase("200") && approvalResponseDTO.getApprovalStatus().equals("REJECTED")) {
			
			TransactionStatusChangeHistory objHistory = new TransactionStatusChangeHistory();
			objHistory.setId(obj.getId());
			objHistory.setAdminUserId(obj.getAdminUserId());
			objHistory.setApprovalAction(approveTransferStatusUpdateDto.getAction());
			objHistory.setApprovalId(obj.getApprovalId());
			objHistory.setComment(approveTransferStatusUpdateDto.getComments());
			objHistory.setNewStatus(obj.getNewStatus());
			objHistory.setUserGroup(obj.getUserGroup());
			objHistory.setTransactionRequestHistory(obj.getTransactionRequestHistory());
			transactionStatusChangeHistoryRepository.save(objHistory);
			transactionStatusChangeRepository.delete(obj);
			response.setReturnStatus(InvokeStatus.SUCCESS);
			response.setReturnCode(ErrorCode.TR_001.toString());
			response.setComment(approveTransferStatusUpdateDto.getComments());
			response.setReturnMessage(InvokeMessage.TRANSACTION_STATUS_CHANGE_REJECTED_TO.toString() + " : " + obj.getNewStatus());
			
			}else {
				response.setReturnStatus(InvokeStatus.FAIL);
				response.setReturnCode(ErrorCode.TR_003.toString());
				response.setReturnMessage(approvalResponseDTO.getReturnMessage());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			response.setReturnStatus(InvokeStatus.FAIL);
			response.setReturnCode(ErrorCode.TR_002.toString());
			response.setReturnMessage(InvokeMessage.FAILED_TO_SUBMIT_APPROVAL_REQUEST.toString());
		}
		return response;
	}

	public CountResponse getCounts() {
		logger.info("START : Service : TransactionStatusChangeService.getCounts");

		CountResponse res = null;

		try {
		    
			long count = transactionHistoryRepository.getTotalTransfers();
			res = new CountResponse();
			res.setCount((int) count);

		} catch (Exception e) {
			throw new ServiceException(ErrorCode.TR_022.toString(), "Error occurred getting unprocesss transfers", e,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		logger.info("END : Service : TransactionStatusChangeService.getCounts");
		return res;
	}

}
