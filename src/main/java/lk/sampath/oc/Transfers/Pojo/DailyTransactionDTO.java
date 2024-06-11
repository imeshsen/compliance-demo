package lk.sampath.oc.Transfers.Pojo;

import java.math.BigDecimal;
import java.util.Date;

import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Enums.TransactionType;

//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.Id;


public class DailyTransactionDTO {

	private Date transactionDate;
	private String userName;
	private String channel;
	private TransactionCategory transactionType;
	private BigDecimal transactionAmount;
	private String currency;


	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public TransactionCategory getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionCategory transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}


	@Override
	public String toString() {
		return "DailyTransaction [transactionDate=" + transactionDate + ", userName=" + userName + ", channel="
				+ channel + ", transactionType=" + transactionType + ", transactionAmount="
				+ transactionAmount + ", transactionAmount="
						+ transactionAmount +", currency="+currency + ",]";
	}

	public DailyTransactionDTO(Date transactionDate, String userName, String channel, TransactionCategory transactionType,
			BigDecimal transactionAmount, String currency) {
		super();
		this.transactionDate = transactionDate;
		this.userName = userName;
		this.channel = channel;
		this.transactionType = transactionType;
		this.transactionAmount = transactionAmount;
		this.currency = currency;
	}
	
	public DailyTransactionDTO() {}
}
