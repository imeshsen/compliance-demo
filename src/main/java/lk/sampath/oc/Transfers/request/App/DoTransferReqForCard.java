package lk.sampath.oc.Transfers.request.App;

import lombok.Data;

import java.math.BigDecimal;

@Data public class DoTransferReqForCard {

    private static final long serialVersionUID = 1L;

    private BigDecimal transactionAmount;
    private String encryptionType;
    private Double initiatedSerno;
    private String initiatedKey;
    private Double chainSerno;
    private String chainAuth;
    private String deviceId;
    private String IdentityType;
    private String NIC;
}
