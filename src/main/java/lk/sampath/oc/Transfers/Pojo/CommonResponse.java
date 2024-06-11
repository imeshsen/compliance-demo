package lk.sampath.oc.Transfers.Pojo;

import io.swagger.annotations.ApiModelProperty;

public class CommonResponse {
	@ApiModelProperty(notes = "Return code",example = "SUCCESS")
	protected String returnCode;
	@ApiModelProperty(notes = "Return message",example = "REQUET_SUCCESS")
	protected String returnMessage;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	@Override
	public String toString() {
		return "CommonResponse [returnCode=" + returnCode + ", returnMessage=" + returnMessage + "]";
	}
}
