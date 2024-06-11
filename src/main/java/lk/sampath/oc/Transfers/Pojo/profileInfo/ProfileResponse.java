package lk.sampath.oc.Transfers.Pojo.profileInfo;

public class ProfileResponse {
	
	private int returnCode;

	private String returnMessage;

	private ResponseUserDetails payLoad;

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public ResponseUserDetails getPayLoad() {
		return payLoad;
	}

	public void setPayLoad(ResponseUserDetails payLoad) {
		this.payLoad = payLoad;
	}
}
