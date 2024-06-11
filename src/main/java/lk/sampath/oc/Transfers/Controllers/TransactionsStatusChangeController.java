
package lk.sampath.oc.Transfers.Controllers;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.sampath.oc.Transfers.Enums.ErrorCode;
import lk.sampath.oc.Transfers.Pojo.CommonResponse;
import lk.sampath.oc.Transfers.Pojo.CountResponse;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.ApprovedTransferStatusUpdateResponse;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.SubmitTransferStatusChangeRequestDTO;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.SubmitTransferStatusUpdateResponse;
import lk.sampath.oc.Transfers.Pojo.txnStatusChange.TransferStatusUpdateDTO;
import lk.sampath.oc.Transfers.Service.TransactionsStatusChangeService;

@RestController
public class TransactionsStatusChangeController {

	private static Logger logger = LogManager.getLogger(TransactionsStatusChangeController.class);

	@Autowired
	private TransactionsStatusChangeService transactionsStatusChangeService;

	@ApiOperation(value = "Submit status update of fund transfer for approval", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully retrieved list"),
			@ApiResponse(code = 401, message = "you are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "the resource you were trying to reach is not found") })
	@RequestMapping(path = "/submitTransferStatusUpdate", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<SubmitTransferStatusUpdateResponse> submitTransferStatusUpdate(
			@RequestBody SubmitTransferStatusChangeRequestDTO submitTransferStatusUpdate) {
		// ThreadContext.put("id", submitTransferStatusUpdate.getId());
		ThreadContext.put("apiName", "submitTransferStatusUpdate");
		ThreadContext.put("user", submitTransferStatusUpdate.getAdminUserId());
		logger.info("validate transfer details : " + submitTransferStatusUpdate.toString());
		SubmitTransferStatusUpdateResponse obj = transactionsStatusChangeService
				.submitTransferStatusUpdate(submitTransferStatusUpdate);
		if (obj.getReturnCode().equals(ErrorCode.TR_001.toString())) {
			return new ResponseEntity<SubmitTransferStatusUpdateResponse>(obj, HttpStatus.OK);
		} else {
			return new ResponseEntity<SubmitTransferStatusUpdateResponse>(obj, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "Get fund transfers status updates pending authorization", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully retrieved list"),
			@ApiResponse(code = 401, message = "you are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "the resource you were trying to reach is not found") })
	@RequestMapping(path = "/transferStatusAuthorizations/{userGroup}", method = RequestMethod.GET)
	public ResponseEntity<CommonResponse> getTransferStatusAuthorizations(
			@PathVariable("userGroup") String userGroup,@RequestParam("userId") String userId) {
		// ThreadContext.put("id", submitTransferStatusUpdate.getId());
		ThreadContext.put("apiName", "getTransferStatusAuthorizations");
		ThreadContext.put("userGroup", userGroup);
		CommonResponse obj = transactionsStatusChangeService.getTransferStatusAuthorizationsByGroupID(userGroup, userId);
		if (obj.getReturnCode().equals(ErrorCode.TR_001.toString())) {
			return new ResponseEntity<CommonResponse>(obj, HttpStatus.OK);
		} else {
			return new ResponseEntity<CommonResponse>(obj, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Approve status update of fund transfer for approval", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully retrieved list"),
			@ApiResponse(code = 401, message = "you are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "the resource you were trying to reach is not found") })
	@RequestMapping(path = "/approveTransferStatusUpdate", method = RequestMethod.POST)
	public ResponseEntity<ApprovedTransferStatusUpdateResponse> approveTransferStatusUpdate(
			@RequestBody TransferStatusUpdateDTO transferStatusUpdateDTO) {

		// ThreadContext.put("id", submitTransferStatusUpdate.getId());
		ThreadContext.put("apiName", "approveTransferStatusUpdate");
		ThreadContext.put("user", transferStatusUpdateDTO.getAdminUserId());
		ApprovedTransferStatusUpdateResponse obj = transactionsStatusChangeService
				.approveTransferStatusUpdate(transferStatusUpdateDTO);
		if (obj.getReturnCode().equals(ErrorCode.TR_001.toString())) {
			return new ResponseEntity<ApprovedTransferStatusUpdateResponse>(obj, HttpStatus.OK);
		} else {
			return new ResponseEntity<ApprovedTransferStatusUpdateResponse>(obj, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(path = "/transfer/failed_in_process/count")
	public CountResponse getTransferCount() {
		
		ThreadContext.put("id", UUID.randomUUID().toString());
		ThreadContext.put("apiName", "/transfer/failed_in_process/count");
		
		logger.info("START : Controller : Getting counts");
		
		CountResponse res = transactionsStatusChangeService.getCounts();
	
		logger.info("RESPONSE : " + res);
		ThreadContext.clearMap();
		return res;
		
	}
}
