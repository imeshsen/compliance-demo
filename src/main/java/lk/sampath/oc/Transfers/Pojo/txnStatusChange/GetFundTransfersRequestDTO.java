package lk.sampath.oc.Transfers.Pojo.txnStatusChange;

import java.io.Serializable;
import java.math.BigDecimal;

import lk.sampath.oc.Transfers.Enums.TransactionCategory;

public class GetFundTransfersRequestDTO implements Serializable {

	private static final long serialVersionUID = 9082083470393106299L;
	private String adminUserId;
	private String transferType;
	private String transferRequestedDateFrom;
	private String transferRequestedDateto;
	private BigDecimal amountFrom;
	private BigDecimal amountTo;
	private String txnRequestedId;
	private String userId;
	private String status;
	private  String scheduleId;
	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}


	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTransferRequestedDateFrom() {
		return transferRequestedDateFrom;
	}

	public void setTransferRequestedDateFrom(String transferRequestedDateFrom) {
		this.transferRequestedDateFrom = transferRequestedDateFrom;
	}

	public String getTransferRequestedDateto() {
		return transferRequestedDateto;
	}

	public void setTransferRequestedDateto(String transferRequestedDateto) {
		this.transferRequestedDateto = transferRequestedDateto;
	}

	public BigDecimal getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(BigDecimal amountFrom) {
		this.amountFrom = amountFrom;
	}

	public BigDecimal getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(BigDecimal amountTo) {
		this.amountTo = amountTo;
	}

	public String getTxnRequestedId() {
		return txnRequestedId;
	}

	public void setTxnRequestedId(String txnRequestedId) {
		this.txnRequestedId = txnRequestedId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GetTransfers [adminUserId=" + adminUserId + ", transferType=" + transferType
				+ ", transferRequestedDateFrom=" + transferRequestedDateFrom + ", transferRequestedDateto="
				+ transferRequestedDateto + ", amountFrom=" + amountFrom + ", amountTo=" + amountTo
				+ ", txnRequestedId=" + txnRequestedId + ", userId=" + userId + ", status=" + status + "]";
	}

	public GetFundTransfersRequestDTO(String adminUserId, String transferType,
			String transferRequestedDateFrom, String transferRequestedDateto, BigDecimal amountFrom,
			BigDecimal amountTo, String txnRequestedId, String userId, String status) {
		super();
		this.adminUserId = adminUserId;
		this.transferType = transferType;
		this.transferRequestedDateFrom = transferRequestedDateFrom;
		this.transferRequestedDateto = transferRequestedDateto;
		this.amountFrom = amountFrom;
		this.amountTo = amountTo;
		this.txnRequestedId = txnRequestedId;
		this.userId = userId;
		this.status = status;

	}

	public GetFundTransfersRequestDTO() {
	}

}
