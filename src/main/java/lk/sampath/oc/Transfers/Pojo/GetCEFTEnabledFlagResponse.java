package lk.sampath.oc.Transfers.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCEFTEnabledFlagResponse {

	@JsonProperty("Status")
	private String status;

	@JsonProperty("CEFT_ENABLE_FLAG")
	private String ceftEnableFlag;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCeftEnableFlag() {
		return ceftEnableFlag;
	}

	public void setCeftEnableFlag(String ceftEnableFlag) {
		this.ceftEnableFlag = ceftEnableFlag;
	}
	
}
