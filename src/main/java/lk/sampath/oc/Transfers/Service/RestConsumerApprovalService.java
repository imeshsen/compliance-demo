package lk.sampath.oc.Transfers.Service;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import lk.sampath.oc.Transfers.Enums.ExceptionCode;
import lk.sampath.oc.Transfers.Exceptions.InvalidUrlException;
import lk.sampath.oc.Transfers.Pojo.approval.ApprovalDTO;
import lk.sampath.oc.Transfers.Pojo.approval.ApprovalListResponseDTO;
import lk.sampath.oc.Transfers.Pojo.approval.ApprovalResponseDTO;

@Service
public class RestConsumerApprovalService {

	private static Logger logger = LogManager.getLogger(RestConsumerApprovalService.class);

	RestTemplate restTemplate = new RestTemplate();

	@Value("${service.url.approvals.endpoint}")
	private String approvalsBaseUrl;

	public ApprovalResponseDTO createApproval(ApprovalDTO approvalDto, String userId, String groupId) {
		logger.info("calling update daily limits rest api");
		URI url;
		try {
			url = new URI(approvalsBaseUrl + "/approvals/createApproval");
			ApprovalResponseDTO response = null;

			HttpHeaders headers = new HttpHeaders();
			headers.set("userId", userId);
			headers.set("userGroup", groupId);
			HttpEntity<ApprovalDTO> request = new HttpEntity<ApprovalDTO>(approvalDto, headers);
			response = restTemplate.postForObject(url, request, ApprovalResponseDTO.class);
			return response;
		} catch (URISyntaxException e) {
			throw new InvalidUrlException(ExceptionCode.URL_NOT_FOUND.toString());
		} catch (HttpStatusCodeException e) {
			logger.error("Response Status - " + e.getStatusCode() + " " + e.getStatusText() + " " + e.getResponseBodyAsString(),e);
			throw e;
		}
	}

	public ApprovalResponseDTO approve(ApprovalDTO approvalDto, String userId, String groupId) {
		logger.info("calling approval rest api");
		URI url;
		try {
			url = new URI(approvalsBaseUrl + "/approvals/approve");
			ApprovalResponseDTO response = null;

			HttpHeaders headers = new HttpHeaders();
			headers.set("userId", userId);
			headers.set("userGroup", groupId);
			HttpEntity<ApprovalDTO> request = new HttpEntity<ApprovalDTO>(approvalDto, headers);
			response = restTemplate.postForObject(url, request, ApprovalResponseDTO.class);
			logger.info("response from approval : {}",response);
			return response;
		} catch (URISyntaxException e) {
			throw new InvalidUrlException(ExceptionCode.URL_NOT_FOUND.toString());

		}
	}
	
	public ApprovalListResponseDTO retrieveApprovals(String status, String type,String groupId,String userName) {
		logger.info("calling retrieveApprovals limits rest api");
		URI url;
		try {
			url = new URI(approvalsBaseUrl + "/approvals/retrieveApprovals?status="+status+"&type="+type);
			
			ResponseEntity<ApprovalListResponseDTO> response = null;

			HttpHeaders headers = new HttpHeaders();
			headers.set("userGroup", groupId);
			headers.set("userId", userName);
			HttpEntity<String> request = new HttpEntity<String>( headers);
			response = restTemplate.exchange(url, HttpMethod.GET, request, ApprovalListResponseDTO.class);
			 
			return response.getBody();
		} catch (URISyntaxException e) {
			throw new InvalidUrlException(ExceptionCode.URL_NOT_FOUND.toString());

		}
	}

}
