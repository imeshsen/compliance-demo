package lk.sampath.oc.Transfers.Pojo.mobile;


import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Enums.TransactionType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AppMcashData implements Serializable {

    private long tranId;
    private String status;
    private BigDecimal transactionAmount;
    private String currency;
    private TransactionCategory transactionCategory;
    private TransactionType transactionType;
    private String fromAccountNumber;
    private String recipientPhone;

    private Date transactionDate;
































}
