/**
 * 
 */
package lk.sampath.oc.Transfers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lk.sampath.oc.Transfers.Entity.CreditCardPaymentTransaction;

/**
 * @author hrsupun
 *
 */
public interface CCPaymentTrnRepository extends JpaRepository<CreditCardPaymentTransaction, Long> {

//	CreditCardPaymentTransaction findByCcPaymentId(long ccPaymentId);
}
