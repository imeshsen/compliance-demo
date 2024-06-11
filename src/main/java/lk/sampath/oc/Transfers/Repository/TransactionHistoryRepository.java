package lk.sampath.oc.Transfers.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import lk.sampath.oc.Transfers.Entity.TransactionRequestHistory;

public interface TransactionHistoryRepository
		extends JpaRepository<TransactionRequestHistory, String>, JpaSpecificationExecutor<TransactionRequestHistory> {

	List<TransactionRequestHistory> findByuserName(String username);

	@Query("SELECT transactionCategory, currency,COUNT(transactionCategory) as count, SUM(transactionAmount) FROM TransactionRequestHistory WHERE requestedDate Between :fromDate AND :toDate GROUP BY transactionCategory,currency ")
	List<Object[]> getFundTransferReports(Date fromDate, Date toDate);

	@Query("SELECT transactionCategory,currency,COUNT(transactionCategory), SUM(transactionAmount) FROM TransactionRequestHistory WHERE requestedDate  Between :fromDate AND :toDate AND userName =:userName GROUP BY transactionCategory,currency ")
	List<Object[]> getFundTransferReports(Date fromDate, Date toDate, String userName);

	@Query("SELECT transactionCategory,currency,COUNT(transactionCategory), SUM(transactionAmount) FROM TransactionRequestHistory WHERE userName =:userName GROUP BY transactionCategory,currency ")
	List<Object[]> getFundTransferReports(String userName);

	@Query("SELECT transactionCategory,currency,COUNT(transactionCategory), SUM(transactionAmount) FROM TransactionRequestHistory GROUP BY transactionCategory,currency ")
	List<Object[]> getFundTransferReports();
	
	TransactionRequestHistory findByTransactionId(long transactionId);

	@Query("SELECT COUNT(r) FROM TransactionRequestHistory r WHERE r.status='FAILED_IN_PROCESS'")
	long getTotalTransfers();

	Optional<TransactionRequestHistory> findByTransactionIdAndUserName(long transactionId,String userName);
	
}