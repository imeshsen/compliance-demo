package lk.sampath.oc.Transfers.Controllers;

import lk.sampath.oc.Transfers.Entity.ReverseMCashRequest;
import lk.sampath.oc.Transfers.Entity.WithdrawMCashRequest;
import lk.sampath.oc.Transfers.Enums.ErrorCode;
import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Pojo.*;
import lk.sampath.oc.Transfers.Pojo.mobile.*;
import lk.sampath.oc.Transfers.Service.MobileAppService;
import lk.sampath.oc.Transfers.Service.TransactionService;
import lk.sampath.oc.Transfers.request.App.DoTransferRequestForAppNet;
import lk.sampath.oc.Transfers.request.App.MLTranCategorySingleReq;
import lk.sampath.oc.Transfers.request.App.MLTransferReq;
import lk.sampath.oc.Transfers.request.App.PayCreditCardRequestForApp;
import lk.sampath.oc.Transfers.response.CustomMcashResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class MobileAppController {

	private static Logger logger = LogManager.getLogger(MobileAppController.class);

	private final MobileAppService mobileAppService;

	private final TransactionService transService;
	@Autowired
	public MobileAppController(MobileAppService mobileAppService, TransactionService transService) {
		this.mobileAppService = mobileAppService;
		this.transService = transService;
	}
	@Value("${multiple.transfers.limit}")
	private int maxTransfersLimit;



	@RequestMapping(path = "app/transactions/getTransfers", method = RequestMethod.POST)
	public ResponseEntity<GetTransfersAppResponse> getTransfersApp(@RequestHeader(name="X-Request-ID") String reqID,
																   @RequestHeader(name="username") String userName,
																   @RequestBody GetTransfersAppRequest getTransfersAppRequest) {
		ThreadContext.put("id", reqID);
		ThreadContext.put("apiName", "app/getTransfers");
		ThreadContext.put("user", userName);

		logger.info("getTransfersApp request : {}",getTransfersAppRequest.toString());

		GetTransfersAppResponse trRes = new GetTransfersAppResponse();

		try {
			trRes = mobileAppService.getTransfers(getTransfersAppRequest,userName);
			logger.info("transaction list retrieved, responseHeader : {} | historyCount : {}",
					trRes.getResponseHeader(), trRes.getTranList().size());

		} catch (Exception e) {
			logger.error("Transaction list retrieving failed", e);
			AppResponseHeader header = new AppResponseHeader(false,"", ErrorCode.TR_004.toString(),
					InvokeMessage.GET_TRANSFERS_FAILED.toString());
			trRes.setResponseHeader(header);

		}
		return ResponseEntity.ok(trRes);
	}

	@RequestMapping(path = "app/transactions/mobilecash", method = RequestMethod.POST)
	public ResponseEntity<GetMcashAppResponse> getMobileCashApp(@RequestHeader(name="X-Request-ID") String reqID,
																@RequestHeader(name="username") String userName,
																@RequestBody GetTransfersAppRequest getTransfersAppRequest) {
		ThreadContext.put("id", reqID);
		ThreadContext.put("apiName", "app/getMobileCashApp");
		ThreadContext.put("user", userName);

		logger.info("getMobileCashApp request : {}",getTransfersAppRequest.toString());

		GetMcashAppResponse trRes = new GetMcashAppResponse();

		try {
			trRes = mobileAppService.getmobilecash(getTransfersAppRequest,userName);
			logger.info("transaction list retrieved, responseHeader : {} | historyCount : {}",
					trRes.getResponseHeader(), trRes.getTranList().size());

		} catch (Exception e) {
			logger.error("Transaction list retrieving failed", e);
			AppResponseHeader header = new AppResponseHeader(false,"", ErrorCode.TR_004.toString(),
					InvokeMessage.GET_TRANSFERS_FAILED.toString());
			trRes.setResponseHeader(header);

		}
		return ResponseEntity.ok(trRes);
	}

	@RequestMapping(path = "app/transactions/details", method = RequestMethod.GET)
	public ResponseEntity<GetTransfersDataResponse> getTranData(@RequestHeader(name="X-Request-ID") String reqID,
																@RequestHeader(name="username") String userName,
																@RequestParam(name = "tranId") long tranId) {
		ThreadContext.put("id", reqID);
		ThreadContext.put("apiName", "app/getTranData");
		ThreadContext.put("user", userName);

		logger.info("getTranData request : {}",tranId);

		GetTransfersDataResponse trRes = new GetTransfersDataResponse();

		try {
			trRes = mobileAppService.getTranData(userName,tranId);

		} catch (Exception e) {
			logger.error("getTranData retrieving failed", e);
			AppResponseHeader header = new AppResponseHeader(false,"", ErrorCode.TR_004.toString(),
					InvokeMessage.GET_TRANSFERS_FAILED.toString());
			trRes.setResponseHeader(header);

		}
		return ResponseEntity.ok(trRes);
	}

	@RequestMapping(value = "app/reverseMobileCash", method = RequestMethod.POST)
	public ResponseEntity<ReverseMCashResponse> reverseMobileCash(@RequestHeader(name="X-Request-ID") String reqID,
																  @RequestBody ReverseMCashRequest reverseMCashReq) {
		ThreadContext.put("id", reqID);
		ThreadContext.put("apiName", "app/reverseMCash");
		ThreadContext.put("user", reverseMCashReq.getUserName());

		String senderPin = reverseMCashReq.getSenderPin();
		reverseMCashReq.setSenderPin("reducted");
		logger.info("reverse mobile cash request : {}", reverseMCashReq.toString());
		reverseMCashReq.setSenderPin(senderPin);

		ReverseMCashResponse mcashRes = new ReverseMCashResponse();
		ResponseHeader resHeader;

		try {
			CustomMcashResponse res = transService.reverseMCash(reverseMCashReq);
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

	@RequestMapping(value = "app/withdrawMobileCash", method = RequestMethod.POST)
	public ResponseEntity<WithdrawMCashResponse> withdrawMobileCash(@RequestHeader(name="X-Request-ID") String reqID,
																	@RequestBody WithdrawMCashRequest withdrawMCashReq) {
		ThreadContext.put("id", reqID);
		ThreadContext.put("apiName", "app/withdrawMCash");
		ThreadContext.put("user", withdrawMCashReq.getUserName());

		String beneficiaryPin = withdrawMCashReq.getBeneficiaryPin();
		withdrawMCashReq.setBeneficiaryPin("reducted");
		logger.info("withdraw mobile cash request : {}" + withdrawMCashReq.toString());
		withdrawMCashReq.setBeneficiaryPin(beneficiaryPin);

		WithdrawMCashResponse mcashRes = new WithdrawMCashResponse();
		ResponseHeader resHeader;

		try {
			CustomMcashResponse res = transService.withdrawMCash(withdrawMCashReq);
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

	@RequestMapping(value = "app/transactions/ccTransfer", method = RequestMethod.POST)
	public ResponseEntity<?> doCcPaymentTransferApp(@RequestHeader(name = "X-Request-ID") String reqID,
													@RequestHeader(name = "userName") String userName,
													@RequestBody PayCreditCardRequestForApp request) {
		ThreadContext.put("id:{}", reqID);
		ThreadContext.put("apiName:", "app/transactions/ccTransfer");
		ThreadContext.put("userID:{}", userName);

		CommonApplicationResponse response = transService.doCcPaymentTransferForApp(userName, request);
		if (response.getStatus() == 0 && ((PaymentCCResponseForAppAPIs) response.getPayload()).getCode() == 0
				&& ((PaymentCCResponseForAppAPIs) response.getPayload()).getSubCode() == 0) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}

	@RequestMapping(value = "app/transactions/doTransfer", method = RequestMethod.POST)
	public ResponseEntity<TransferResponse> doTransfer(@RequestHeader(name = "X-Request-ID") String reqID,
													   @RequestHeader(name = "userName") String userName,
													   @RequestHeader(name = "IdentityType") String IdentityType,
													   @RequestHeader(name = "IdentityValue") String IdentityValue,
													   @RequestBody DoTransferRequestForAppNet dtr) {
		ThreadContext.put("id", reqID);
		ThreadContext.put("apiName", "app/transactions/doTransfer");
		ThreadContext.put("user", userName);

		String formAccount = dtr.getFromAccountNumber();
		dtr.setFromAccountNumber("reducted");
		String toAccount = dtr.getToAccountNumber();
		dtr.setToAccountNumber("reducted");
		logger.info("userName:{}, IdentityType:{}, IdentityValue:{}, DoTransfer Request:{}", userName,
				IdentityType, IdentityValue, dtr.toString());
		dtr.setFromAccountNumber(formAccount);
		dtr.setToAccountNumber(toAccount);
		dtr.setRequestedDate(new Date());
		dtr.setNextSchedule(new Date());
		// Transaction id will be generated by omini backend
		dtr.setTransactionId(0);

		ResponseHeader responseHeader = this.transService.validateTransferForAppNet(dtr, reqID, userName, IdentityType, IdentityValue).getResponseHeader();
		logger.info("ValidateTransferResponse :{}", responseHeader);
		if (responseHeader.getReturnStatus().equals(InvokeStatus.SUCCESS)) {
			logger.info("transfer validate success");
			TransferResponse resFromApp = this.transService.doTransferForAppNet(dtr, reqID, userName,IdentityType, IdentityValue);
			logger.info("DoTransferResponse :{}", resFromApp);
			if (resFromApp.getResponseHeader().getReturnStatus().equals(InvokeStatus.SUCCESS))
				return new ResponseEntity<TransferResponse>(resFromApp, HttpStatus.OK);
			else {
				return new ResponseEntity<TransferResponse>(resFromApp, HttpStatus.BAD_REQUEST);
			}
		} else {
			logger.info("transfer validate failed");
			TransferResponse trRes = new TransferResponse();
			trRes.setResponseHeader(responseHeader);
			return new ResponseEntity<TransferResponse>(trRes, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "app/transactions/doMultipleTransfer", method = RequestMethod.POST)
	public ResponseEntity doMultipleTransfer(@RequestHeader(name = "X-Request-ID") String reqID,
											 @RequestHeader(name = "userName") String userName,
											 @RequestHeader(name = "IdentityType") String IdentityType,
											 @RequestHeader(name = "IdentityValue") String IdentityValue,
											 @RequestBody MLTransferReq mlTransferReq) {

		ThreadContext.put("id", reqID);
		ThreadContext.put("apiName", "app/transactions/doMultipleTransfer");
		ThreadContext.put("user", userName);

		logger.info("START | doMultipleTransfer - controller");

		if (maxTransfersLimit >= mlTransferReq.getTranList().size()) {

			String formAccount = mlTransferReq.getDebitAccount();
			mlTransferReq.setDebitAccount("reducted");
			List<MLTranCategorySingleReq> tranList = mlTransferReq.getTranList();
			List<String> toAccounts = new ArrayList<>();
			for (MLTranCategorySingleReq req : tranList) {
				String accountNumber = req.getToAccountNumber();
				toAccounts.add(accountNumber);
				req.setToAccountNumber("reducted");
			}

			logger.info("userName:{}, IdentityType:{}, IdentityValue:{}, MultipleTransfer Request:{}", userName,
					IdentityType, IdentityValue, mlTransferReq.toString());

			int index = 0;
			for (MLTranCategorySingleReq req : tranList) {
				String toAccount = toAccounts.get(index);
				req.setToAccountNumber(toAccount);
				req.setRequestedDate(new Date());
				req.setNextSchedule(new Date());
				// Transaction id will be generated by omini backend
				req.setTransactionId(0);
				index++;
				if (index >= toAccounts.size()) {
					break;
				}
			}
			mlTransferReq.setDebitAccount(formAccount);

			List<ValidateTransferResponse> validateTransferResponseList = this.transService.validateTransferMLForAppNet(mlTransferReq, reqID, userName, IdentityType, IdentityValue);
			boolean allHeadersTrue = true;
			for (ValidateTransferResponse vtResponse : validateTransferResponseList) {

				ResponseHeader responseHeader = vtResponse.getResponseHeader();
				boolean resHeader = responseHeader.getReturnStatus().equals(InvokeStatus.SUCCESS);

				if (!resHeader) {
					allHeadersTrue = false;
					break;
				}
			}
			if (allHeadersTrue) {
				logger.info("transfer validate success");
				MLTranList mlTranList = new MLTranList();
				List<MLTransferRes> mlTransferResList = transService.doMultipleTransferForAppNet(mlTransferReq, reqID, userName, IdentityType, IdentityValue);
				logger.info("MultipleTransferResponse :{}", mlTransferResList);
				mlTranList.setTransferResList(mlTransferResList);
				return ResponseEntity.ok().body(mlTranList);
			} else {
				logger.info("transfer validate failed");
				return ResponseEntity.badRequest().body(validateTransferResponseList);
			}
		}
		else {
			logger.info("Maximum can 5 transactions");
			ResponseHeader responseHeader = new ResponseHeader(InvokeStatus.FAIL,"500","Maximum Transaction count is 5");
			ValidateTransferResponse validateTransferResponse = new ValidateTransferResponse();
			validateTransferResponse.setResponseHeader(responseHeader);
			return ResponseEntity.badRequest().body(validateTransferResponse);
		}

	}

	@RequestMapping(value = "app/validate/multipleTransfer", method = RequestMethod.POST)
	public ResponseEntity validateMultipleTransfer(@RequestHeader(name = "X-Request-ID") String reqID,
												   @RequestHeader(name = "userName") String userName,
												   @RequestHeader(name = "IdentityType") String IdentityType,
												   @RequestHeader(name = "IdentityValue") String IdentityValue,
												   @RequestBody MLTransferReq mlTransferReq) {

		ThreadContext.put("id", reqID);
		ThreadContext.put("apiName", "app/validate/multipleTransfer");
		ThreadContext.put("user", userName);

		logger.info("START | validateMultipleTransfer - controller");

		String formAccount = mlTransferReq.getDebitAccount();
		mlTransferReq.setDebitAccount("reducted");
		List<MLTranCategorySingleReq> tranList = mlTransferReq.getTranList();
		List<String> toAccounts = new ArrayList<>();
		for (MLTranCategorySingleReq req : tranList) {
			String accountNumber = req.getToAccountNumber();
			toAccounts.add(accountNumber);
			req.setToAccountNumber("reducted");
		}

		logger.info("userName:{}, IdentityType:{}, IdentityValue:{}, MultipleTransfer Request:{}", userName,
				IdentityValue, mlTransferReq.toString());

		int index = 0;
		for (MLTranCategorySingleReq req : tranList) {
			String toAccount = toAccounts.get(index);
			req.setToAccountNumber(toAccount);
			req.setRequestedDate(new Date());
			req.setNextSchedule(new Date());
			// Transaction id will be generated by omini backend
			req.setTransactionId(0);
			index++;
			if (index >= toAccounts.size()) {
				break;
			}
		}
		mlTransferReq.setDebitAccount(formAccount);

		List<ValidateTransferResponse> validateTransferResponse = this.transService.validateTransferMLForAppNet(mlTransferReq, reqID, userName, IdentityType, IdentityValue);
		boolean allHeadersTrue = true;
		for (ValidateTransferResponse vtResponse : validateTransferResponse) {

			ResponseHeader responseHeader = vtResponse.getResponseHeader();
			boolean resHeader = responseHeader.getReturnStatus().equals(InvokeStatus.SUCCESS);

			if (!resHeader) {
				allHeadersTrue = false;
				break;
			}
		}
		if (allHeadersTrue) {
			logger.info("transfer validate success");
			return ResponseEntity.ok().body(validateTransferResponse);

		} else {
			logger.info("transfer validate failed");
			return ResponseEntity.ok().body(validateTransferResponse);
		}
	}
}
