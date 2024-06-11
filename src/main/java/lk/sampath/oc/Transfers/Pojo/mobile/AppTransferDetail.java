package lk.sampath.oc.Transfers.Pojo.mobile;


import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Enums.TransactionType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AppTransferDetail implements Serializable {

    private long tranId;
    private String status;
    private TransactionCategory transactionCategory;
    private TransactionType transactionType;
    private String fromAccountNumber;
    private String toAccountName;
    private String toAccountNumber;

    private BigDecimal transactionAmount;

    private String currency;

    private Date transactionDate;

    private String commissionFromAccountNumber;

    private BigDecimal commissionAmount;

    private String bankCode;
    private String bankName;

    private String branchCode;

    private String beneficiaryRemark;
    private String senderRemarks;


    private String cardName;

    private String purpose;

    private String mobileCashSenderNIC;

    private String mobileCashSenderMobile;

    private String mobileRecipientMobile;

    private String mobileRecipientName;

    private String mobileRecipientNIC;




}
