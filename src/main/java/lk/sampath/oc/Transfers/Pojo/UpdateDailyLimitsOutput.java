package lk.sampath.oc.Transfers.Pojo;

import java.io.Serializable;

import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;

public class UpdateDailyLimitsOutput implements Serializable{

	private static final long serialVersionUID = 1L;
	InvokeStatus invokeStatus;
	InvokeMessage invokeMessage;
	
	
	public InvokeStatus getInvokeStatus() {
		return invokeStatus;
	}
	public void setInvokeStatus(InvokeStatus invoke_status) {
		this.invokeStatus = invoke_status;
	}
	public InvokeMessage getInvokeMessage() {
		return invokeMessage;
	}
	public void setInvokeMessage(InvokeMessage invoke_message) {
		this.invokeMessage = invoke_message;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateDailyLimitsOutput [invokeStatus=");
		builder.append(invokeStatus);
		builder.append(", invokeMessage=");
		builder.append(invokeMessage);
		builder.append("]");
		return builder.toString();
	}
	
}
