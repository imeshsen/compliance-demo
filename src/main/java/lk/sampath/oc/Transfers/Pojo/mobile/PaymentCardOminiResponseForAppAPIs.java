/**
 * 
 */
package lk.sampath.oc.Transfers.Pojo.mobile;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hrsupun
 *
 */
@Data
public class PaymentCardOminiResponseForAppAPIs implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long code;
	private Long subCode;
	private String message;
	private String messageCustomer;
	private String requestId;
	private String requestReference;
	private Double chainSerno;
	private String chainAuth;

	public PaymentCardOminiResponseForAppAPIs(PaymentCardResponseForAppAPIs response) {
		this.code = response.getCode();
		this.subCode = response.getSubCode();
		this.message = response.getMessage();
		this.messageCustomer = response.getMessageCustomer();
		this.requestId = response.getRequestId();
		this.chainAuth = response.getChainAuth();
		this.requestReference = response.getRequestReference();
		this.chainSerno = response.getChainSerno();

	}


	public PaymentCardOminiResponseForAppAPIs() {
		
	}
}
