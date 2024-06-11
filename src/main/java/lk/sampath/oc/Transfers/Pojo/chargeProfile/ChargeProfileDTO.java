package lk.sampath.oc.Transfers.Pojo.chargeProfile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * @Author		:tharindak@n-able.biz
 * @Date		:29/04/2019
 * @Description	:This dto class is to transfer data from and to front-end.
 */
public class ChargeProfileDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String profileID;
	private String profileCode;
	private String profileName;
	private String profileDuration;
	private Date expireDate;
	private List<ChargeDTO> listOfCharges = new ArrayList<>();
	private Boolean superProfile;
	private String status;
	private Date addedOn;
	private String addedBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Date verifiedOn;
	private String verifiedBy;
	private String approvalID;
	private String approvalStatus;
	private Boolean isModified;

	public String getProfileID() {
		return profileID;
	}

	public void setProfileID(String profileID) {
		this.profileID = profileID;
	}

	public String getProfileCode() {
		return profileCode;
	}

	public void setProfileCode(String profileCode) {
		this.profileCode = profileCode;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileDuration() {
		return profileDuration;
	}

	public void setProfileDuration(String profileDuration) {
		this.profileDuration = profileDuration;
	}

	public List<ChargeDTO> getListOfCharges() {
		return listOfCharges;
	}

	public void setListOfCharges(List<ChargeDTO> listOfCharges) {
		this.listOfCharges = listOfCharges;
	}

	public Boolean getSuperProfile() {
		return superProfile;
	}

	public void setSuperProfile(Boolean superProfile) {
		this.superProfile = superProfile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getVerifiedOn() {
		return verifiedOn;
	}

	public void setVerifiedOn(Date verifiedOn) {
		this.verifiedOn = verifiedOn;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public String getApprovalID() {
		return approvalID;
	}

	public void setApprovalID(String approvalID) {
		this.approvalID = approvalID;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Boolean getIsModified() {
		return isModified;
	}

	public void setIsModified(Boolean isModified) {
		this.isModified = isModified;
	}

}
