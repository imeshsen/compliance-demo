package lk.sampath.oc.Transfers.request.App;

import lk.sampath.oc.Transfers.Enums.TransactionType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PayCreditCardRequestForApp implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cardSerialNumber;
    private String debitAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Double initiatedSerno;
    private String initiatedKey;
    private Double chainSerno;
    private String chainAuth;
    private String deviceId;
    private String paymentCurrency;
    private String NIC;
}
