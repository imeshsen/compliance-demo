/**
 * 
 */
package lk.sampath.oc.Transfers.response;

import java.io.Serializable;

import lk.sampath.oc.Transfers.integration.mcash.ResultData;

/**
 * @author hrsupun
 *
 */
public class CustomMcashResponse extends ResultData implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean flag;

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomMcashResponse [flag=");
		builder.append(flag);
		builder.append(", description=");
		builder.append(this.description);
		builder.append(", smcTranId=");
		builder.append(this.smcTranId);
		builder.append(", status=");
		builder.append(this.status);
		builder.append("]");
		return builder.toString();
	}
}
