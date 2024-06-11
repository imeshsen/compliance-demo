package lk.sampath.oc.Transfers.Pojo.chargeProfile;

import java.io.Serializable;

/*
 * @Author		:tharindak@n-able.biz
 * @Date		:29/04/2019
 * @Description	:This dto class is to transfer data from and to front-end.
 */
public class ChargeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String chargeName;
	private String chargeValue;

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getChargeValue() {
		return chargeValue;
	}

	public void setChargeValue(String chargeValue) {
		this.chargeValue = chargeValue;
	}

}
