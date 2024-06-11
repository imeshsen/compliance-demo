package lk.sampath.oc.Transfers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lk.sampath.oc.Transfers.Entity.ReverseMCashRequest;



@Repository
public interface ReverseMCashRepository extends JpaRepository<ReverseMCashRequest, String>{

}
