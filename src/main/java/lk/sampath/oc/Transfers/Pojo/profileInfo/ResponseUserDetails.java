package lk.sampath.oc.Transfers.Pojo.profileInfo;

import java.util.List;

/**
 * @author Suhail N-able
 *
 */
public class ResponseUserDetails {

//	private ProfileInformationDto profileInformation;
//
//
//	public ProfileInformationDto getProfileInformation() {
//		return profileInformation;
//	}
//
//	public void setProfileInformation(ProfileInformationDto profileInformation) {
//		this.profileInformation = profileInformation;
//	}
	
	private String chargeProfile;
	private List<String> customerId;
	private String limitId;
	private String customerName;
	
	public String getChargeProfile() {
		return chargeProfile;
	}
	public void setChargeProfile(String chargeProfile) {
		this.chargeProfile = chargeProfile;
	}
	public List<String> getCustomerId() {
		return customerId;
	}
	public void setCustomerId(List<String> customerId) {
		this.customerId = customerId;
	}
	public String getLimitId() {
		return limitId;
	}
	public void setLimitId(String limitId) {
		this.limitId = limitId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	
}