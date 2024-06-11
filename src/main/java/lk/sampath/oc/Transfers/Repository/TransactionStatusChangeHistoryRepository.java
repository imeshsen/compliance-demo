package lk.sampath.oc.Transfers.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lk.sampath.oc.Transfers.Entity.TransactionStatusChangeHistory;

@Repository
public interface TransactionStatusChangeHistoryRepository extends JpaRepository<TransactionStatusChangeHistory, String> {
	Optional<TransactionStatusChangeHistory>findByApprovalId(String approvalId);
}
