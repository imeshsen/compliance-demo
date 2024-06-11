package lk.sampath.oc.Transfers.Pojo;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;

public class ValidateTransferOutput {
	
	InvokeStatus invokeStatus;
	String invokeMessage;
	private String invokeCode;
	
	
	public InvokeStatus getInvokeStatus() {
		return invokeStatus;
	}
	public void setInvokeStatus(InvokeStatus invoke_status) {
		this.invokeStatus = invoke_status;
	}
	public String getInvokeMessage() {
		return invokeMessage;
	}
	public void setInvokeMessage(String debitAccountValidationFailed) {
		this.invokeMessage = debitAccountValidationFailed;
	}
	public String getInvokeCode() {
		return invokeCode;
	}
	public void setInvokeCode(String invokeCode) {
		this.invokeCode = invokeCode;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValidateTransferOutput [invokeStatus=");
		builder.append(invokeStatus);
		builder.append(", invokeMessage=");
		builder.append(invokeMessage);
		builder.append(", invokeCode=");
		builder.append(invokeCode);
		builder.append("]");
		return builder.toString();
	}
		
}
