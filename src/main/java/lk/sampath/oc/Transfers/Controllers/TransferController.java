package lk.sampath.oc.Transfers.Controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lk.sampath.oc.Transfers.Entity.ReverseMCashRequest;
import lk.sampath.oc.Transfers.Entity.TransactionRequest;
import lk.sampath.oc.Transfers.Entity.WithdrawMCashRequest;
import lk.sampath.oc.Transfers.Enums.ErrorCode;
import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Enums.TransactionType;
import lk.sampath.oc.Transfers.Pojo.CommonApplicationResponse;
import lk.sampath.oc.Transfers.Pojo.GetTransfers;
import lk.sampath.oc.Transfers.Pojo.PaymentCCResponse;
import lk.sampath.oc.Transfers.Pojo.PaymentCardOminiResponse;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lk.sampath.oc.Transfers.Pojo.ReverseMCashResponse;
import lk.sampath.oc.Transfers.Pojo.TransferResponse;
import lk.sampath.oc.Transfers.Pojo.ValidateTransferResponse;
import lk.sampath.oc.Transfers.Pojo.WithdrawMCashResponse;
import lk.sampath.oc.Transfers.Pojo.getTransfersResponse;
import lk.sampath.oc.Transfers.Pojo.reports.FundTransferReportRequestDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.FundTransfersReportResponseDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.GetFundTransfersRequestDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.GetFundTransfersResponseDTO;
import lk.sampath.oc.Transfers.Service.TransactionService;
import lk.sampath.oc.Transfers.request.PayCreditCardRequest;
import lk.sampath.oc.Transfers.response.CustomMcashResponse;

@RestController
public class TransferController {

	private static Logger logger = LogManager.getLogger(TransferController.class);

	@Autowired
	private TransactionService transService;
	
	@Autowired
	private HttpServletRequest header;

	@RequestMapping(value = "/transactions/validate", method = RequestMethod.POST)
	public ResponseEntity<ValidateTransferResponse> validateTransfer(@RequestBody TransactionRequest tr) {		
		ThreadContext.put("id", header.getHeader("X-Request-ID"));
		ThreadContext.put("apiName", "validateTransfer");
		ThreadContext.put("user", tr.getUserName());
		
		String accNum = tr.getToAccountNumber();
		String ccNum = tr.getCardName();
		tr.setToAccountNumber("xxxxxxxxx");
		tr.setCardName("xxxxxxxxx");
		logger.info("validate transfer request : " + tr.toString());
		tr.setToAccountNumber(accNum);
		tr.setCardName(ccNum);
		
		ValidateTransferResponse response = this.transService.validateTransfer(tr, header.getHeader("X-Request-ID"));
		logger.info("vaidate transfer response : {}", response);
		if (InvokeStatus.SUCCESS.equals(response.getResponseHeader().getReturnStatus())) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}

	@RequestMapping(value = "/transactions/schedule/add", method = RequestMethod.POST)
	public ResponseEntity<TransferResponse> doTransferForSchedule(@RequestBody TransactionRequest tr) {
		String uuid=header.getHeader("X-Request-ID");
		ThreadContext.put("id", uuid);
		ThreadContext.put("apiName", "doTransfer");
		ThreadContext.put("user", tr.getUserName());

		String formAccount = tr.getFromAccountNumber();
		tr.setFromAccountNumber("reducted");
		String toAccount = tr.getToAccountNumber();
		tr.setToAccountNumber("reducted");
		logger.info("do transfer request : " + tr.toString());
		tr.setFromAccountNumber(formAccount);
		tr.setToAccountNumber(toAccount);

		tr.setRequestedDate(tr.getRequestedDate());
		tr.setNextSchedule(tr.getNextSchedule());
		// Transaction id will be generated by omini backend
		tr.setTransactionId(0);

		ResponseHeader responseHeader = this.transService.validateTransfer(tr,uuid).getResponseHeader();
		if (responseHeader.getReturnStatus().equals(InvokeStatus.SUCCESS)) {
			logger.info("transfer validate success");
			tr.setTransactionType(TransactionType.SCHEDULE);
			TransferResponse obj = this.transService.doTransfer(tr,uuid);
			if (obj.getResponseHeader().getReturnStatus().equals(InvokeStatus.SUCCESS))
				return new ResponseEntity<TransferResponse>(obj, HttpStatus.OK);
			else {
				return new ResponseEntity<TransferResponse>(obj, HttpStatus.BAD_REQUEST);
			}
		} else {
			logger.info("transfer validate failed");
			TransferResponse trRes = new TransferResponse();
			trRes.setResponseHeader(responseHeader);
			return new ResponseEntity<TransferResponse>(trRes, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/transactions/add", method = RequestMethod.POST)
	public ResponseEntity<TransferResponse> doTransfer(@RequestBody TransactionRequest tr) {
		String uuid = header.getHeader("X-Request-ID");
		ThreadContext.put("id", uuid);
		ThreadContext.put("apiName", "doTransfer");
		ThreadContext.put("user", tr.getUserName());

		String formAccount = tr.getFromAccountNumber();
		tr.setFromAccountNumber("reducted");
		String toAccount = tr.getToAccountNumber();
		tr.setToAccountNumber("reducted");
		logger.info("do transfer request : " + tr.toString());
		tr.setFromAccountNumber(formAccount);
		tr.setToAccountNumber(toAccount);

		tr.setRequestedDate(new Date());
		tr.setNextSchedule(new Date());
		// Transaction id will be generated by omini backend
		tr.setTransactionId(0);

		ResponseHeader responseHeader = this.transService.validateTransfer(tr,uuid).getResponseHeader();
		if (responseHeader.getReturnStatus().equals(InvokeStatus.SUCCESS)) {
			logger.info("transfer validate success");
			TransferResponse obj = this.transService.doTransfer(tr,uuid);
			if (obj.getResponseHeader().getReturnStatus().equals(InvokeStatus.SUCCESS))
				return new ResponseEntity<TransferResponse>(obj, HttpStatus.OK);
			else {
				return new ResponseEntity<TransferResponse>(obj, HttpStatus.BAD_REQUEST);
			}
		} else {
			logger.info("transfer validate failed");
			TransferResponse trRes = new TransferResponse();
			trRes.setResponseHeader(responseHeader);
			return new ResponseEntity<TransferResponse>(trRes, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(path = "/transactions/getTransfers", method = RequestMethod.POST)
	public ResponseEntity<getTransfersResponse> getTransfers(@RequestBody GetTransfers getTransObj) {
		ThreadContext.put("id", getTransObj.getUserName());
		ThreadContext.put("apiName", "getTransfers");
		ThreadContext.put("user", getTransObj.getUserName());

		logger.info("get transfers request : {}",getTransObj.toString());

		getTransfersResponse trRes = new getTransfersResponse();

		try {
			trRes = this.transService.getTransfers(getTransObj);
			logger.info("transaction list retrieved, responseHeader : {} | historyCount : {}",
					trRes.getResponseHeader(), trRes.getTrList().size());
			if (trRes.getResponseHeader().getReturnStatus().equals(InvokeStatus.SUCCESS)) {
				return ResponseEntity.ok(trRes);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(trRes);
			}
		} catch (Exception e) {
			logger.error("Transaction list retrieving failed", e);
			ResponseHeader header = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_004.toString(),
					InvokeMessage.GET_TRANSFERS_FAILED.toString());
			trRes.setResponseHeader(header);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(trRes);
		}
	}

	@RequestMapping(path = "/transactions/getFundTransfers", method = RequestMethod.POST)
	public ResponseEntity<GetFundTransfersResponseDTO> getFundTransfers(@RequestBody GetFundTransfersRequestDTO getFundTransfers) {
		ThreadContext.put("id", getFundTransfers.getAdminUserId());
		ThreadContext.put("apiName", "getFundTransfers");
		ThreadContext.put("user", getFundTransfers.getAdminUserId());

		logger.info("get fund transfers request ");

		GetFundTransfersResponseDTO trRes = new GetFundTransfersResponseDTO();

		try {
			trRes = this.transService.getFundTransfers(getFundTransfers);
			
			//response log is disabled due to the credit card numbers are captured in the log
			logger.info("Successfully return the list : disableLog");
			return ResponseEntity.ok(trRes);
		} catch (Exception e) {
			logger.error("Error while getFundTransfers : {}",e.getLocalizedMessage());
			ResponseHeader header = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_004.toString(),
					InvokeMessage.GET_TRANSFERS_FAILED.toString());
			trRes.setResponseHeader(header);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(trRes);
		}
	}

	@RequestMapping(path = "/transactions/getFundTransferReport", method = RequestMethod.POST)
	public ResponseEntity<FundTransfersReportResponseDTO> getFundTransfersReport(
			@RequestBody FundTransferReportRequestDTO fundTransferReportDTO) {
		ThreadContext.put("id", fundTransferReportDTO.getAdminUserId());
		ThreadContext.put("apiName", "getFundTransferReport");
		ThreadContext.put("user", fundTransferReportDTO.getAdminUserId());

		logger.info("get fund transfer report request ");

		FundTransfersReportResponseDTO trRes = new FundTransfersReportResponseDTO();

		try {
			trRes = this.transService.getFundTransferReport(fundTransferReportDTO);
			// List<FundTransferReportDTO> trList = trRes.getListOfTransactions();
			logger.info("transaction list retrieved  : {}", trRes.toString());
			return ResponseEntity.ok(trRes);
		} catch (Exception e) {
			logger.error("getFundTransfersReport failed due to : {}", e.getLocalizedMessage());
			ResponseHeader header = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_004.toString(),
					InvokeMessage.GET_TRANSFERS_FAILED.toString());
			trRes.setResponseHeader(header);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(trRes);
		}
	}

	@RequestMapping(value = "/withdrawMobileCash", method = RequestMethod.POST)
	public ResponseEntity<WithdrawMCashResponse> withdrawMobileCash(@RequestBody WithdrawMCashRequest withdrawMCashReq) {
		ThreadContext.put("id", withdrawMCashReq.getUserName());
		ThreadContext.put("apiName", "withdrawMCash");
		ThreadContext.put("user", withdrawMCashReq.getUserName());
		
		String beneficiaryPin = withdrawMCashReq.getBeneficiaryPin();
		withdrawMCashReq.setBeneficiaryPin("reducted");
		logger.info("withdraw mobile cash request : {}" + withdrawMCashReq.toString());
		withdrawMCashReq.setBeneficiaryPin(beneficiaryPin);

		WithdrawMCashResponse mcashRes = new WithdrawMCashResponse();
		ResponseHeader resHeader;

		try {
			CustomMcashResponse res = this.transService.withdrawMCash(withdrawMCashReq);
			if (res.getStatus() == -99) {
				resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_016.toString(),
						InvokeMessage.WITHDRAW_MOBILE_CASH_FAILED.toString() + " - " + res.getDescription());
				mcashRes.setResponseHeader(resHeader);
				mcashRes.setWithdrawResponse(res);
				logger.info("withdrawMobileCash failed : {}", mcashRes.toString());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mcashRes);
			} else {
				resHeader = new ResponseHeader(InvokeStatus.SUCCESS,
						InvokeMessage.WITHDRAW_MOBILE_CASH_SUCCESS.toString());
				mcashRes.setResponseHeader(resHeader);
				mcashRes.setWithdrawResponse(res);
				logger.info("withdrawMobileCash success : {}", mcashRes.toString());
				return ResponseEntity.ok(mcashRes);
			}
		} catch (Exception e) {
			logger.error("Withdraw mobile cash failed due to : {}", e);
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_001.toString(),
					InvokeMessage.WITHDRAW_MOBILE_CASH_FAILED.toString());
			mcashRes.setResponseHeader(resHeader);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mcashRes);
		}
	}

	@RequestMapping(value = "/reverseMobileCash", method = RequestMethod.POST)
	public ResponseEntity<ReverseMCashResponse> reverseMobileCash(@RequestBody ReverseMCashRequest reverseMCashReq) {
		ThreadContext.put("id", reverseMCashReq.getUserName());
		ThreadContext.put("apiName", "reverseMCash");
		ThreadContext.put("user", reverseMCashReq.getUserName());
		
		String senderPin = reverseMCashReq.getSenderPin();
		reverseMCashReq.setSenderPin("reducted");
		logger.info("reverse mobile cash request : {}", reverseMCashReq.toString());
		reverseMCashReq.setSenderPin(senderPin);
		
		ReverseMCashResponse mcashRes = new ReverseMCashResponse();
		ResponseHeader resHeader;

		try {
			CustomMcashResponse res = this.transService.reverseMCash(reverseMCashReq);
			if (res.getStatus() == -99) {
				resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_015.toString(),
						InvokeMessage.REVERSE_MOBILE_CASH_FAILED.toString() + " - " + res.getDescription());
				mcashRes.setResponseHeader(resHeader);
				mcashRes.setReverseResponse(res);
				logger.info("reverseMobileCash failed : {}", mcashRes.toString());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mcashRes);
			} else {
				resHeader = new ResponseHeader(InvokeStatus.SUCCESS,
						InvokeMessage.REVERSE_MOBILE_CASH_SUCCESS.toString());
				mcashRes.setResponseHeader(resHeader);
				mcashRes.setReverseResponse(res);
				logger.info("reverseMobileCash success : {}", mcashRes.toString());
				return ResponseEntity.ok(mcashRes);
			}
		} catch (Exception e) {
			logger.error("Reversing mobile cash failed due to : {}", e);
			resHeader = new ResponseHeader(InvokeStatus.FAIL, ErrorCode.TR_002.toString(),
					InvokeMessage.REVERSE_MOBILE_CASH_FAILED.toString());
			mcashRes.setResponseHeader(resHeader);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mcashRes);
		}
	}

	
	@RequestMapping(value = "/transactions/payCreditCard", method = RequestMethod.POST)
	public ResponseEntity<?> doCcPaymentTransfer(@RequestBody PayCreditCardRequest request) {
		CommonApplicationResponse response = transService.doCcPaymentTransfer(request);
		if (response.getStatus() == 0 && ((PaymentCCResponse) response.getPayload()).getCode() == 0
				&& ((PaymentCCResponse) response.getPayload()).getSubCode() == 0) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}
}
