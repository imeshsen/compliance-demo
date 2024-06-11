package lk.sampath.oc.Transfers.Specifications;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import lk.sampath.oc.Transfers.Entity.TransactionRequestHistory;
import lk.sampath.oc.Transfers.Enums.TransactionCategory;

public class TransactionRequestSpecification {

	private static Logger logger = LogManager.getLogger(TransactionRequestSpecification.class);

	public static Specification<TransactionRequestHistory> filterFromUserName(String userName) {
		if (userName != null && !userName.equals("")) {
			logger.info("user name not null");
			return (root, query, cb) -> cb.equal(root.get("userName"), userName);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromSenderRemarks(String remark) {
		if (remark != null && !remark.equals("")) {
			logger.info("remark not null");
			return (root, query, cb) -> cb.like(cb.lower(root.get("senderRemarks")), "%"+remark.toLowerCase()+"%");
		}	
		return null;
	}
	
	public static Specification<TransactionRequestHistory> filterFromExternelId(String externalId) {
		if (externalId != null && !externalId.equals("")) {
			logger.info("external id not null");
			return (root, query, cb) -> cb.equal(root.get("transactionId"), externalId);
		}
		return null;
	}
	
	public static Specification<TransactionRequestHistory> filterFromTxnRequestedId(String txnRequestedId) {
		if (txnRequestedId != null && !txnRequestedId.equals("")) {
			logger.info("txnRequestedId id not null");
			return (root, query, cb) -> cb.equal(root.get("transactionId"), txnRequestedId);
		}
		return null;
	}
	public static Specification<TransactionRequestHistory> filterFromScheduleId(String scheduleId) {
		if (scheduleId != null && !scheduleId.equals("")) {
			logger.info("scheduleId id not null");
			return (root, query, cb) -> cb.equal(root.get("scheduleId"), scheduleId);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromStatus(String status) {
		if (status != null && !status.equals("")) {
			logger.info("status not null");
			return (root, query, cb) -> cb.equal(root.get("status"), status);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromFromAccountNumber(String accountNumber) {
		if (accountNumber != null && !accountNumber.equals("")) {
			logger.info("acc num not null");
			return (root, query, cb) -> cb.equal(root.get("fromAccountNumber"), accountNumber);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterTransactionCategory(TransactionCategory txnType) {
		if (txnType != null) {
			logger.info("txn type not null");
			return (root, query, cb) -> cb.equal(root.get("transactionCategory"), txnType);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromTxnEndDate(Date txnEndDate) {
		logger.info("txn end date not null");
		logger.info(txnEndDate);
		if (txnEndDate != null) {
			LocalDate endLocalDate = txnEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDateTime endLocalDateTime = endLocalDate.atTime(LocalTime.MAX);
			Date endDate = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
			return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("requestedDate").as(Date.class), endDate);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromTxnStartDate(Date txnStartDate) {
		logger.info("txn start date not null");
		logger.info(txnStartDate);
		if (txnStartDate != null) {
			return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("requestedDate").as(Date.class), txnStartDate);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromTxnStartDateAndEndDate(Date txnStartDate,
			Date txnEndDate) {
		logger.info("txn start date not null");
		logger.info(txnStartDate);
		logger.info("txn start date not null");
		logger.info(txnEndDate);
		if (txnStartDate != null && txnEndDate != null) {
			LocalDate endLocalDate = txnEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDateTime endLocalDateTime = endLocalDate.atTime(LocalTime.MAX);
			Date endDate = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
			return (root, query, cb) -> cb.between(root.get("requestedDate").as(Date.class), txnStartDate, endDate);
		} else if (txnStartDate != null) {
			return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("requestedDate").as(Date.class), txnStartDate);
		} else if (txnEndDate != null) {
			LocalDate endLocalDate = txnEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDateTime endLocalDateTime = endLocalDate.atTime(LocalTime.MAX);
			Date endDate = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
			return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("requestedDate").as(Date.class), endDate);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromMinAmount(BigDecimal minAmount) {
		if (minAmount != null) {
			logger.info("min amount not null");
			return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("transactionAmount"), minAmount);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromMinAndMaxAmount(BigDecimal minAmount,
			BigDecimal maxAmount) {
		if (minAmount != null && maxAmount != null) {
			logger.info("min and max amount not null");
			return (root, query, cb) -> cb.between(root.get("transactionAmount"), minAmount, maxAmount);
		} else if (minAmount != null) {
			logger.info("min amount not null");
			return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("transactionAmount"), minAmount);
		} else if (maxAmount != null) {
			logger.info("max amount not null");
			return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("transactionAmount"), maxAmount);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromMaxAmount(BigDecimal maxAmount) {
		if (maxAmount != null) {
			logger.info("max amount not null");
			return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("transactionAmount"), maxAmount);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromTransactionCategory(
			TransactionCategory transCategory) {
		if (transCategory != null && !transCategory.equals("")) {
			logger.info("txn category not null");
			return (root, query, cb) -> cb.equal(root.get("transactionCategory"), transCategory);
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromUserNameLowerCaseMatch(String userName) {
		if (userName != null && !userName.isEmpty()) {
			logger.info("user name not null");
			return (root, query, cb) -> cb.equal(cb.lower(root.get("userName")), userName.toLowerCase());
		}
		return null;
	}

	public static Specification<TransactionRequestHistory> filterFromNotTransactionCategory(
			TransactionCategory transCategory) {
		if (transCategory != null && !transCategory.equals("")) {
			logger.info("txn category not null");
			return (root, query, cb) -> cb.notEqual(root.get("transactionCategory"), transCategory);
		}
		return null;
	}

}
