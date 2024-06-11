package lk.sampath.oc.Transfers.Pojo.mobile;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentCCResponseForAppAPIs implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8647813718400347152L;

	private Long code;
	private Long subCode;
	private String message;
	private String messageCustomer;
	private String requestId;
	private Long ccReferenceId;
	private String requestReference;
	private Double chainSerno;
	private String chainAuth;
}
