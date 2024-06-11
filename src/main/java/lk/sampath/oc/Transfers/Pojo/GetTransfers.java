package lk.sampath.oc.Transfers.Pojo;

import java.math.BigDecimal;
import java.sql.Date;

import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Enums.TransactionType;

public class GetTransfers {

	String userName;
	Integer numberOfRecords;
	Integer startRecord;
	String fromAccountNumber;
	TransactionType txnType;
	String txnRequestedStartDate;
	String txnRequestedEndDate;
	BigDecimal amountMin;
	BigDecimal amountMax;
	TransactionCategory transferCategory;
	Integer totalTransaction;
	Boolean transactionRemaining;
	private String transferFrequency; 
	private Date nextSchedule;
	private String senderRemarks;
	private long numOfTransfers;
	private String transactionId;

	public String getSenderRemarks() {
		return senderRemarks;
	}

	public void setSenderRemarks(String senderRemarks) {
		this.senderRemarks = senderRemarks;
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(Integer numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public Integer getStartRecord() {
		return startRecord;
	}

	public void setStartRecord(Integer startRecord) {
		this.startRecord = startRecord;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public TransactionType getTxnType() {
		return txnType;
	}

	public void setTxnType(TransactionType txnType) {
		this.txnType = txnType;
	}

	public String getTxnRequestedStartDate() {
		return txnRequestedStartDate;
	}

	public void setTxnRequestedStartDate(String txnRequestedStartDate) {
		this.txnRequestedStartDate = txnRequestedStartDate;
	}

	public String getTxnRequestedEndDate() {
		return txnRequestedEndDate;
	}

	public void setTxnRequestedEndDate(String txnRequestedEndDate) {
		this.txnRequestedEndDate = txnRequestedEndDate;
	}

	public BigDecimal getAmountMin() {
		return amountMin;
	}

	public void setAmountMin(BigDecimal amountMin) {
		this.amountMin = amountMin;
	}

	public BigDecimal getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(BigDecimal amountMax) {
		this.amountMax = amountMax;
	}

	public TransactionCategory getTransferCategory() {
		return transferCategory;
	}

	public void setTransferCategory(TransactionCategory transferCategory) {
		this.transferCategory = transferCategory;
	}

	public Integer getTotalTransaction() {
		return totalTransaction;
	}

	public void setTotalTransaction(Integer totalTransaction) {
		this.totalTransaction = totalTransaction;
	}

	public Boolean getTransactionRemaining() {
		return transactionRemaining;
	}

	public void setTransactionRemaining(Boolean transactionRemaining) {
		this.transactionRemaining = transactionRemaining;
	}

	public String getTransferFrequency() {
		return transferFrequency;
	}

	public void setTransferFrequency(String transferFrequency) {
		this.transferFrequency = transferFrequency;
	}

	public Date getNextSchedule() {
		return nextSchedule;
	}

	public void setNextSchedule(Date nextSchedule) {
		this.nextSchedule = nextSchedule;
	}

	public long getNumOfTransfers() {
		return numOfTransfers;
	}

	public void setNumOfTransfers(long numOfTransfers) {
		this.numOfTransfers = numOfTransfers;
	}

	@Override
	public String toString() {
		return "GetTransfers [userName=" + userName + ", numberOfRecords=" + numberOfRecords + ", startRecord="
				+ startRecord + ", fromAccountNumber=" + fromAccountNumber + ", txnType=" + txnType
				+ ", txnRequestedStartDate=" + txnRequestedStartDate + ", txnRequestedEndDate=" + txnRequestedEndDate
				+ ", amountMin=" + amountMin + ", amountMax=" + amountMax + ", transferCategory=" + transferCategory
				+ ", totalTransaction=" + totalTransaction + ", transactionRemaining=" + transactionRemaining
				+ ", transferFrequency=" + transferFrequency + ", nextSchedule=" + nextSchedule + ", numOfTransfers="
				+ numOfTransfers + "]";
	}

	public GetTransfers(String userName, Integer numberOfRecords, Integer startRecord, String fromAccountNumber,
			TransactionType txnType, String txnRequestedStartDate, String txnRequestedEndDate, BigDecimal amountMin,
			BigDecimal amountMax, TransactionCategory transferCategory, Integer totalTransaction,
			Boolean transactionRemaining, String transferFrequency,
	 Date nextSchedule,
	 long numOfTransfers) {
		super();
		this.userName = userName;
		this.numberOfRecords = numberOfRecords;
		this.startRecord = startRecord;
		this.fromAccountNumber = fromAccountNumber;
		this.txnType = txnType;
		this.txnRequestedStartDate = txnRequestedStartDate;
		this.txnRequestedEndDate = txnRequestedEndDate;
		this.amountMin = amountMin;
		this.amountMax = amountMax;
		this.transferCategory = transferCategory;
		this.totalTransaction = totalTransaction;
		this.transactionRemaining = transactionRemaining;
		this.transferFrequency=transferFrequency;
		this.nextSchedule=nextSchedule;
		this.numOfTransfers=numOfTransfers;
		
	}

	public GetTransfers() {
	}

}
