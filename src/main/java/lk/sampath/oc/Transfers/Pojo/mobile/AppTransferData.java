package lk.sampath.oc.Transfers.Pojo.mobile;


import lk.sampath.oc.Transfers.Enums.CommissionStatus;
import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Enums.TransactionType;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;


import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AppTransferData implements Serializable {

    private String transactionId;

    private String externalId;

    private String userName;

    private String fromAccountNumber;

    private String toAccountNumber;

    private String cardName;

    private String bankName;

    private String bankCode;

    private String branchCode;

    private String  transactionType;

    private String transactionAmount;

    private String requestedDate;

    private String status;

    private String channel;

    private String transactionCategory;

    private String benificiaryRemarks;

    private String accountNickName;

    private String senderRemarks;

    private String currency;

    private String backendRefNumber;

    private String cdciStatus;

    private String backendResponse;

    private String mobileCashSenderNIC;

    private String mobileCashSenderMobile;

    private String mobileRecipientMobile;

    private String mobileRecipientName;

    private String mobileRecipientNIC;

    private String transferFrequency;

    private Date nextSchedule;

    private long numOfTransfers;

    private String scheduleId;

    private String toAccountName;

    private String fromAccountName;

    private String commissionFromAccountNumber;

    private String pushNotificationSaved;

    private BigDecimal commissionAmount;

    private CommissionStatus commissionStatus;

    private String message;

    private String purpose;

    private String scheduleOccr;
}



