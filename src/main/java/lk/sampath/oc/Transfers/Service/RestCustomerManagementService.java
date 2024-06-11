package lk.sampath.oc.Transfers.Service;

import java.util.HashMap;
import java.util.Map;

import lk.sampath.oc.Transfers.utils.JacksonConfig;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lk.sampath.oc.Transfers.Pojo.profileInfo.ProfileResponse;

@Service
public class RestCustomerManagementService {

	private static Logger logger = LogManager.getLogger(RestCustomerManagementService.class);

	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	@Qualifier("SCMOMNIChannelAPI")
	private RestTemplate sCMOMNIChannelAPI;
	
	@Value("${service.url.customer.endpoint}")
	private String customerBaseUrl;
	
	@Autowired
	@Qualifier("poolSCM")
	private PoolingHttpClientConnectionManager poolConnectorForSCM;
	
	@Autowired
	private JacksonConfig jacksonConfig;

	public ProfileResponse getCustomerProfileInfo(String userId, String uuid) {
		logger.info("calling update daily limits rest api");
		String url;
		try {

			url = customerBaseUrl + "/service/getVishwaUserinfo/{vishwaUsername}";

			Map<String, String> params = new HashMap<>();
			params.put("vishwaUsername", userId);

			HttpEntity<ProfileResponse> response;
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("userId", userId);
			headers.set("X-Request-ID", uuid);
			HttpEntity request = new HttpEntity(headers);
			logger.info("getVishwaUserinfo api pool {}",poolConnectorForSCM.getTotalStats());
			
			response = sCMOMNIChannelAPI.exchange(url, HttpMethod.GET, request, ProfileResponse.class, params);
			
			logger.info("getCustomerProfileInfo - Response :: " + jacksonConfig.convertToJson(response));
			return response.getBody();
		} catch (HttpStatusCodeException e) {
			logger.error("Response Status - {} , {} , {} ",e.getStatusCode() , e.getStatusText() , e.getResponseBodyAsString(), e);
			throw e;
		}catch (RestClientException e) {
			logger.error("invoke failed. cannot access url", e);
			throw e;
		}catch (Exception e) {
			logger.error("calling getVishwaUserDetails failed due to", e);
			throw e;
		}
	}

}
