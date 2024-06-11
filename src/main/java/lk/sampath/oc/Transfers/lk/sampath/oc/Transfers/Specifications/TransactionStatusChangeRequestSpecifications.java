package lk.sampath.oc.Transfers.lk.sampath.oc.Transfers.Specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import lk.sampath.oc.Transfers.Entity.TransactionStatusChange;

public class TransactionStatusChangeRequestSpecifications {
	private static Logger logger = LogManager.getLogger(TransactionStatusChangeRequestSpecifications.class);

	public static Specification<TransactionStatusChange> filterFromAdminUserId(String adminUserId){
		if(adminUserId!=null&&!adminUserId.equals("")) {
			logger.info("user name not null");
			return (root, query, cb) ->cb.equal(root.get("adminUserId"), adminUserId);
		}
		return null;
	}
	
}
