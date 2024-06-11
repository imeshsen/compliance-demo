package lk.sampath.oc.Transfers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lk.sampath.oc.Transfers.Entity.TransactionRequest;


@Repository
public interface TransactionRepository extends 
									JpaRepository<TransactionRequest,String>{

	
}
