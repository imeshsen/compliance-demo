package lk.sampath.oc.Transfers.Pojo.chargeProfile;

import java.io.Serializable;
import java.util.List;

/*
 * @Author		:tharindak@n-able.biz
 * @Date		:02/05/2019
 * @Description	:This response class is to provide profile response.
 */
public class ChargeProfileResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int returnCode;

	private String returnMessage;
	private Object payLoad;
	
	private List<ChargeProfileDTO> listOfChargeProfiles;


	public List<ChargeProfileDTO> getListOfChargeProfiles() {
		return listOfChargeProfiles;
	}

	public void setListOfChargeProfiles(List<ChargeProfileDTO> listOfChargeProfiles) {
		this.listOfChargeProfiles = listOfChargeProfiles;
	}

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

	public Object getPayLoad() {
		return payLoad;
	}

	public void setPayLoad(Object payLoad) {
		this.payLoad = payLoad;
	}


}
