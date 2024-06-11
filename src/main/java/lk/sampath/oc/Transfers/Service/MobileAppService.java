package lk.sampath.oc.Transfers.Service;

import lk.sampath.oc.Transfers.Entity.TransactionRequestHistory;
import lk.sampath.oc.Transfers.Enums.ErrorCode;
import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Enums.InvokeStatus;
import lk.sampath.oc.Transfers.Enums.TransactionCategory;
import lk.sampath.oc.Transfers.Pojo.ResponseHeader;
import lk.sampath.oc.Transfers.Pojo.mobile.*;
import lk.sampath.oc.Transfers.Repository.TransactionHistoryRepository;
import lk.sampath.oc.Transfers.Specifications.TransactionRequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MobileAppService {

    private static Logger logger = LogManager.getLogger(MobileAppService.class);

    private final  TransactionHistoryRepository trHistoryRepo;
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    public MobileAppService(TransactionHistoryRepository trHistoryRepo) {
        this.trHistoryRepo = trHistoryRepo;
    }


    public GetTransfersAppResponse getTransfers(GetTransfersAppRequest request,String userName) {

        AppResponseHeader resHeader ;
        List<AppTransferData> appTransferDataList=new ArrayList<>();
        GetTransfersAppResponse transRes = new GetTransfersAppResponse();
        transRes.setTranList(appTransferDataList);
        transRes.setPageLimit(request.getPageLimit());
        transRes.setPageNo(request.getPageNo());

        String startDateStr = request.getPaymentRequestDateFrom() == null || request.getPaymentRequestDateFrom().isEmpty() ? null : request.getPaymentRequestDateFrom();
        String endDateStr = request.getPaymentRequestDateTo() == null || request.getPaymentRequestDateTo().isEmpty() ? null : request.getPaymentRequestDateTo();

        logger.info("start date >> " + startDateStr);
        logger.info("end date >> " + endDateStr);

        Date sDate = null;
        Date eDate = null;
        //Validate Date
        try {
            if (startDateStr != null)
                sDate = sdf.parse(startDateStr);
            if (endDateStr != null)
                eDate = sdf.parse(endDateStr);

        } catch (ParseException e) {
            logger.info("date parsing excepiton");
            logger.error(e.getStackTrace());
            resHeader = new AppResponseHeader(false,"", ErrorCode.TR_003.toString(),
                    InvokeMessage.DATE_PARSE_EXCEPTION.toString());
            transRes.setResponseHeader(resHeader);
            return transRes;
        }

        logger.info("start date >> " + sDate);
        logger.info("end date >> " + eDate);
        //validate Status can be SUCCESS,FAIL || If empty return ALL
        if(!(request.getStatus()==null || request.getStatus().isEmpty())){
           if(request.getStatus().equalsIgnoreCase("SUCCESS")||
                   request.getStatus().equalsIgnoreCase("FAIL")){
               logger.info("Request has valid Status");
           }else{
               logger.info("Status in not SUCCESS OR FAIL");
               resHeader = new AppResponseHeader(false,"", ErrorCode.TR_023.toString(),
                       InvokeMessage.STATUS_SHOULD_BE_SUCCESS_OR_FAIL.toString());
               transRes.setResponseHeader(resHeader);
               return transRes;
           }

        }
        //validate username
        if(userName==null || userName.isEmpty()){
                resHeader = new AppResponseHeader(false,"", ErrorCode.TR_024.toString(),
                        InvokeMessage.USERNAME_SHOULD_NOT_BE_EMPTY.toString());
                transRes.setResponseHeader(resHeader);
                return transRes;
        }
        //validate Pagination
        if(!(request.getPageNo()>0 && request.getPageLimit()>0)){
            resHeader = new AppResponseHeader(false,"", ErrorCode.TR_024.toString(),
                    InvokeMessage.PAGENO_AND_PAGELIMIT_SHOULD_BE_ABOVE_0.toString());
            transRes.setResponseHeader(resHeader);
            return transRes;
        }

        logger.info("inside get transfers service APP");

        Pageable pageable = PageRequest.of(request.getPageNo() - 1, request.getPageLimit(), Sort.by("requestedDate").descending());


        Page<TransactionRequestHistory> trHPage = trHistoryRepo.findAll(generateTransferQuery(sDate, eDate, request,userName,false),
                pageable);
        transRes.setTotalCount(trHPage.getTotalElements());

        if(!trHPage.getContent().isEmpty()) {
            transRes.setTranList(setAppTranData(trHPage.getContent()));
            resHeader = new AppResponseHeader(true,"Success", "","");

        }else{

            resHeader = new AppResponseHeader(false,"",ErrorCode.TR_025.toString(), InvokeMessage.NO_RECORDS_FOUND.toString());

        }
        transRes.setResponseHeader(resHeader);
        return transRes;

    }

    public GetMcashAppResponse getmobilecash(GetTransfersAppRequest request, String userName) {
        AppResponseHeader resHeader ;
        List<AppMcashData> appTransferDataList=new ArrayList<>();
        GetMcashAppResponse transRes = new GetMcashAppResponse();
        transRes.setTranList(appTransferDataList);
        transRes.setPageLimit(request.getPageLimit());
        transRes.setPageNo(request.getPageNo());

        String startDateStr = request.getPaymentRequestDateFrom() == null || request.getPaymentRequestDateFrom().isEmpty() ? null : request.getPaymentRequestDateFrom();
        String endDateStr = request.getPaymentRequestDateTo() == null || request.getPaymentRequestDateTo().isEmpty() ? null : request.getPaymentRequestDateTo();

        logger.info("start date >> " + startDateStr);
        logger.info("end date >> " + endDateStr);

        Date sDate = null;
        Date eDate = null;
        //Validate Date
        try {
            if (startDateStr != null)
                sDate = sdf.parse(startDateStr);
            if (endDateStr != null)
                eDate = sdf.parse(endDateStr);

        } catch (ParseException e) {
            logger.info("date parsing excepiton");
            logger.error(e.getStackTrace());
            resHeader = new AppResponseHeader(false,"", ErrorCode.TR_003.toString(),
                    InvokeMessage.DATE_PARSE_EXCEPTION.toString());
            transRes.setResponseHeader(resHeader);
            return transRes;
        }

        logger.info("start date >> " + sDate);
        logger.info("end date >> " + eDate);
        //validate Status can be SUCCESS,FAIL || If empty return ALL
        if(!(request.getStatus()==null || request.getStatus().isEmpty())){
            if(request.getStatus().equalsIgnoreCase("SUCCESS")||
                    request.getStatus().equalsIgnoreCase("FAIL")){
                logger.info("Request has valid Status");
            }else{
                logger.info("Status in not SUCCESS OR FAIL");
                resHeader = new AppResponseHeader(false,"", ErrorCode.TR_023.toString(),
                        InvokeMessage.STATUS_SHOULD_BE_SUCCESS_OR_FAIL.toString());
                transRes.setResponseHeader(resHeader);
                return transRes;
            }

        }
        //validate username
        if(userName==null || userName.isEmpty()){
            resHeader = new AppResponseHeader(false,"", ErrorCode.TR_024.toString(),
                    InvokeMessage.USERNAME_SHOULD_NOT_BE_EMPTY.toString());
            transRes.setResponseHeader(resHeader);
            return transRes;
        }
        //validate Pagination
        if(!(request.getPageNo()>0 && request.getPageLimit()>0)){
            resHeader = new AppResponseHeader(false,"", ErrorCode.TR_024.toString(),
                    InvokeMessage.PAGENO_AND_PAGELIMIT_SHOULD_BE_ABOVE_0.toString());
            transRes.setResponseHeader(resHeader);
            return transRes;
        }

        logger.info("inside get mobilecash service APP");

        Pageable pageable = PageRequest.of(request.getPageNo() - 1, request.getPageLimit(), Sort.by("requestedDate").descending());


        Page<TransactionRequestHistory> trHPage = trHistoryRepo.findAll(generateTransferQuery(sDate, eDate, request,userName,true),
                pageable);
        transRes.setTotalCount(trHPage.getTotalElements());

        if(!trHPage.getContent().isEmpty()) {
            transRes.setTranList(setAppMcashData(trHPage.getContent()));
            resHeader = new AppResponseHeader(true, "Success","","");

        }else{

            resHeader = new AppResponseHeader(false,"",ErrorCode.TR_025.toString(), InvokeMessage.NO_RECORDS_FOUND.toString());

        }
        transRes.setResponseHeader(resHeader);
        return transRes;
    }

    public GetTransfersDataResponse getTranData(String userName,long tranId) {
        AppResponseHeader resHeader;
        AppTransferDetail appTransferDetail=new AppTransferDetail();
        GetTransfersDataResponse transRes = new GetTransfersDataResponse();
        transRes.setTransferData(appTransferDetail);

        //validate username
        if(userName==null || userName.isEmpty()){
            resHeader = new AppResponseHeader(false,"", ErrorCode.TR_024.toString(),
                    InvokeMessage.USERNAME_SHOULD_NOT_BE_EMPTY.toString());
            transRes.setResponseHeader(resHeader);
            return transRes;
        }

        Optional<TransactionRequestHistory> transactionRequestHistory=trHistoryRepo.findByTransactionIdAndUserName( tranId,userName);
            if(transactionRequestHistory.isPresent()){
                transRes.setTransferData(setTranDetail(transactionRequestHistory.get()));
                resHeader = new AppResponseHeader(true, "Success","","");
            }else{
                resHeader = new AppResponseHeader(false,"",ErrorCode.TR_025.toString(), InvokeMessage.NO_RECORDS_FOUND.toString());
            }

        transRes.setResponseHeader(resHeader);
        return transRes;
    }

    private AppTransferDetail setTranDetail(TransactionRequestHistory transactionRequestHistory) {
        AppTransferDetail appTransferDetail=new AppTransferDetail();
        appTransferDetail.setTranId(transactionRequestHistory.getTransactionId());
        appTransferDetail.setStatus(transactionRequestHistory.getStatus());
        appTransferDetail.setTransactionCategory(transactionRequestHistory.getTransactionCategory());
        appTransferDetail.setTransactionType(transactionRequestHistory.getTransactionType());
        appTransferDetail.setFromAccountNumber(transactionRequestHistory.getFromAccountNumber());
        appTransferDetail.setToAccountNumber(transactionRequestHistory.getToAccountNumber());
        appTransferDetail.setTransactionAmount(transactionRequestHistory.getTransactionAmount());
        appTransferDetail.setCurrency(transactionRequestHistory.getCurrency());
        appTransferDetail.setTransactionDate(transactionRequestHistory.getRequestedDate());
        appTransferDetail.setCommissionAmount(transactionRequestHistory.getCommissionAmount());
        appTransferDetail.setCommissionFromAccountNumber(transactionRequestHistory.getCommissionFromAccountNumber());
        if(transactionRequestHistory.getTransactionCategory().equals(TransactionCategory.MC)){
            appTransferDetail.setMobileRecipientMobile(transactionRequestHistory.getMobileRecipientMobile());
            appTransferDetail.setMobileCashSenderNIC(transactionRequestHistory.getMobileCashSenderNIC());
            appTransferDetail.setMobileCashSenderMobile(transactionRequestHistory.getMobileCashSenderMobile());
            appTransferDetail.setMobileRecipientName(transactionRequestHistory.getMobileRecipientName());
            appTransferDetail.setMobileRecipientNIC(transactionRequestHistory.getMobileRecipientNIC());
        }else {
            appTransferDetail.setBankCode(transactionRequestHistory.getBankCode());
            appTransferDetail.setBankName(transactionRequestHistory.getBankName());
            appTransferDetail.setBranchCode(transactionRequestHistory.getBranchCode());
            appTransferDetail.setBeneficiaryRemark(transactionRequestHistory.getBenificiaryRemarks());
            appTransferDetail.setSenderRemarks(transactionRequestHistory.getSenderRemarks());
            appTransferDetail.setCardName(transactionRequestHistory.getCardName());
            appTransferDetail.setPurpose(transactionRequestHistory.getPurpose());
        }

        return appTransferDetail;

    }


    private List<AppTransferData> setAppTranData (List<TransactionRequestHistory> content) {

        List<AppTransferData> appTransferDataList=new ArrayList<>();
        content.forEach(transactionRequestHistory -> {
            AppTransferData appTransferData=new AppTransferData();
            appTransferData.setTransactionId(String.valueOf(transactionRequestHistory.getTransactionId()));
            appTransferData.setExternalId(transactionRequestHistory.getExternalId());
            appTransferData.setUserName(transactionRequestHistory.getUserName());
            appTransferData.setFromAccountNumber(transactionRequestHistory.getFromAccountNumber());
            appTransferData.setToAccountNumber(transactionRequestHistory.getToAccountNumber());
            appTransferData.setCardName(transactionRequestHistory.getCardName());
            appTransferData.setBankName(transactionRequestHistory.getBankName());
            appTransferData.setBankCode(transactionRequestHistory.getBankCode());
            appTransferData.setBranchCode(transactionRequestHistory.getBranchCode());
            appTransferData.setTransactionType(String.valueOf(transactionRequestHistory.getTransactionType()));
            appTransferData.setTransactionAmount(String.valueOf(transactionRequestHistory.getTransactionAmount()));
            appTransferData.setRequestedDate(formatStringDate(transactionRequestHistory.getRequestedDate().toString()));
            appTransferData.setStatus(transactionRequestHistory.getStatus());
            appTransferData.setChannel(transactionRequestHistory.getChannel());
            appTransferData.setTransactionCategory(String.valueOf(transactionRequestHistory.getTransactionCategory()));
            appTransferData.setBenificiaryRemarks(transactionRequestHistory.getBenificiaryRemarks());
            appTransferData.setAccountNickName(transactionRequestHistory.getAccountNickName());
            appTransferData.setSenderRemarks(transactionRequestHistory.getSenderRemarks());
            appTransferData.setCurrency(transactionRequestHistory.getCurrency());
            appTransferData.setBackendRefNumber(transactionRequestHistory.getBackendRefNumber());
            appTransferData.setCdciStatus(transactionRequestHistory.getCdciStatus());
            appTransferData.setBackendResponse(transactionRequestHistory.getBackendResponse());
            appTransferData.setMobileCashSenderNIC(transactionRequestHistory.getMobileCashSenderNIC());
            appTransferData.setMobileCashSenderMobile(transactionRequestHistory.getMobileCashSenderMobile());
            appTransferData.setMobileRecipientMobile(transactionRequestHistory.getMobileRecipientMobile());
            appTransferData.setMobileRecipientName(transactionRequestHistory.getMobileRecipientName());
            appTransferData.setMobileRecipientNIC(transactionRequestHistory.getMobileRecipientNIC());
            appTransferData.setTransferFrequency(transactionRequestHistory.getTransferFrequency());
            appTransferData.setNextSchedule(transactionRequestHistory.getNextSchedule());
            appTransferData.setNumOfTransfers(transactionRequestHistory.getNumOfTransfers());
            appTransferData.setScheduleId(transactionRequestHistory.getScheduleId());
            appTransferData.setToAccountName(transactionRequestHistory.getToAccountName());
            appTransferData.setFromAccountName(transactionRequestHistory.getFromAccountName());
            appTransferData.setCommissionFromAccountNumber(transactionRequestHistory.getCommissionFromAccountNumber());
            appTransferData.setPushNotificationSaved(transactionRequestHistory.getPushNotificationSaved());
            appTransferData.setCommissionAmount(transactionRequestHistory.getCommissionAmount());
            appTransferData.setCommissionStatus(transactionRequestHistory.getCommissionStatus());
            appTransferData.setMessage(transactionRequestHistory.getMessage());
            appTransferData.setPurpose(transactionRequestHistory.getPurpose());
            appTransferData.setScheduleOccr(transactionRequestHistory.getScheduleOccr());
            appTransferDataList.add(appTransferData);
        });

        return appTransferDataList;

    }
    private List<AppMcashData> setAppMcashData(List<TransactionRequestHistory> content) {

        List<AppMcashData> appTransferDataList=new ArrayList<>();
        content.forEach(transactionRequestHistory -> {
            AppMcashData appTransferData=new AppMcashData();
            appTransferData.setTranId(transactionRequestHistory.getTransactionId());
            appTransferData.setStatus(transactionRequestHistory.getStatus());
            appTransferData.setTransactionCategory(transactionRequestHistory.getTransactionCategory());
            appTransferData.setTransactionType(transactionRequestHistory.getTransactionType());
            appTransferData.setFromAccountNumber(transactionRequestHistory.getFromAccountNumber());
            appTransferData.setRecipientPhone(transactionRequestHistory.getMobileRecipientMobile());
            appTransferData.setTransactionAmount(transactionRequestHistory.getTransactionAmount());
            appTransferData.setCurrency(transactionRequestHistory.getCurrency());
            appTransferData.setTransactionDate(transactionRequestHistory.getRequestedDate());
            appTransferDataList.add(appTransferData);
        });

        return appTransferDataList;

    }


    private Specification<TransactionRequestHistory> generateTransferQuery(Date sDate, Date eDate, GetTransfersAppRequest request,
                                                                           String userName,boolean mCash) {
        Specification<TransactionRequestHistory> specification;
        specification = TransactionRequestSpecification
                .filterFromFromAccountNumber(request.getDebitAccount());



        if (specification == null)
            specification = TransactionRequestSpecification.filterFromMaxAmount(request.getAmountTo());
        else
            specification = specification
                    .and(TransactionRequestSpecification.filterFromMaxAmount(request.getAmountTo()));

        if (specification == null)
            specification = TransactionRequestSpecification.filterFromMinAmount(request.getAmountFrom());
        else
            specification = specification
                    .and(TransactionRequestSpecification.filterFromMinAmount(request.getAmountFrom()));

        if (specification == null)
            specification = TransactionRequestSpecification.filterFromUserNameLowerCaseMatch(userName);
        else
            specification = specification
                    .and(TransactionRequestSpecification.filterFromUserNameLowerCaseMatch(userName));

        if (sDate != null && eDate == null) {
            if (specification == null)
                specification = TransactionRequestSpecification.filterFromTxnStartDate(sDate);
            else
                specification = specification.and(TransactionRequestSpecification.filterFromTxnStartDate(sDate));
        } else if (sDate == null && eDate != null) {
            if (specification == null)
                specification = TransactionRequestSpecification.filterFromTxnEndDate(eDate);
            else
                specification = specification.and(TransactionRequestSpecification.filterFromTxnEndDate(eDate));
        } else if (sDate != null && eDate != null) {
            if (specification == null)
                specification = TransactionRequestSpecification.filterFromTxnStartDateAndEndDate(sDate, eDate);
            else
                specification = specification
                        .and(TransactionRequestSpecification.filterFromTxnStartDateAndEndDate(sDate, eDate));
        }

        if(mCash) {
            if (specification == null)
                specification = TransactionRequestSpecification.filterFromTransactionCategory(TransactionCategory.MC);
            else
                specification = specification
                        .and(TransactionRequestSpecification.filterFromTransactionCategory(TransactionCategory.MC));
        }
        else {
            if (specification == null)
                specification = TransactionRequestSpecification.filterFromNotTransactionCategory(TransactionCategory.MC);
            else
                specification = specification
                        .and(TransactionRequestSpecification.filterFromNotTransactionCategory(TransactionCategory.MC));
        }
        return specification;

    }

    public static String formatStringDate(String dateString) {
        logger.info("START | formatStringDate");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, inputFormatter);
        String formattedDateTime = dateTime.format(outputFormatter);
        logger.info("END | formatStringDate ");
        return formattedDateTime;
    }


}
