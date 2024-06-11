package lk.sampath.oc.Transfers.Pojo.mobile;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GetTransfersAppRequest {
    private String paymentRequestDateFrom;//mandatory
    private String paymentRequestDateTo;//mandatory
    private BigDecimal amountFrom;
    private BigDecimal amountTo;
    private String debitAccount;
    private String status;//mandatory


    private int pageNo;//mandatory
    private int pageLimit;//mandatory
}
