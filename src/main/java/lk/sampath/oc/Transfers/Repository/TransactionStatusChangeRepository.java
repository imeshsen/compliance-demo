package lk.sampath.oc.Transfers.Repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lk.sampath.oc.Transfers.Entity.CreditCardPaymentTransaction;
import lk.sampath.oc.Transfers.Entity.TransactionRequestHistory;
import lk.sampath.oc.Transfers.Entity.TransactionStatusChange;

@Repository
public interface TransactionStatusChangeRepository extends JpaRepository<TransactionStatusChange, String> {
	Optional<TransactionStatusChange>findByApprovalId(String approvalId);
	
	Optional<Collection<TransactionStatusChange>> findByTransactionRequestHistory(TransactionRequestHistory transactionRequestHistory);
	
	Optional<Collection<TransactionStatusChange>> findByUserGroup(String userGroup);

	@Query("SELECT trns FROM TransactionStatusChange trns WHERE trns.transactionRequestHistory = ?1")
	TransactionStatusChange findbyTransactionRequestID(TransactionRequestHistory transferRequestId);
	
//	@Query("SELECT trns FROM TransactionStatusChange trns WHERE trns.creditCardPaymentTransaction = ?1")
//	TransactionStatusChange findbyCcPaymentId(CreditCardPaymentTransaction ccPaymentId);
	
	
}
