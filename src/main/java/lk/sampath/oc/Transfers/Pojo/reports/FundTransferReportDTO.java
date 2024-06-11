package lk.sampath.oc.Transfers.Pojo.reports;

import java.io.Serializable;

public class FundTransferReportDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String transactionType;
	private String currency;
	private double amount;
	private long transactionCount;

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(long transactionCount) {
		this.transactionCount = transactionCount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundTransferReportDTO [transactionType=");
		builder.append(transactionType);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", transactionCount=");
		builder.append(transactionCount);
		builder.append("]");
		return builder.toString();
	}
	
}
