package lk.sampath.oc.Transfers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lk.sampath.oc.Transfers.Entity.WithdrawMCashRequest;


@Repository
public interface WithdrawMCashRepository extends JpaRepository<WithdrawMCashRequest, String>{

}
