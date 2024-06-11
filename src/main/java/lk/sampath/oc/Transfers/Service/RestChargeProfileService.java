package lk.sampath.oc.Transfers.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import lk.sampath.oc.Transfers.Enums.ExceptionCode;
import lk.sampath.oc.Transfers.Enums.InvokeMessage;
import lk.sampath.oc.Transfers.Exceptions.InvalidUrlException;
import lk.sampath.oc.Transfers.Pojo.chargeProfile.ChargeDTO;
import lk.sampath.oc.Transfers.Pojo.chargeProfile.ChargeProfileResponse;

@Service
public class RestChargeProfileService {

	private static Logger logger = LogManager.getLogger(RestChargeProfileService.class);

	RestTemplate restTemplate = new RestTemplate();

	@Value("${service.url.chargeProfile.endpoint}")
	private String chargeProfileBaseUrl;

	public ChargeProfileResponse getChargeProfile(String userId, String profileCode) {
		logger.info("calling update daily limits rest api");
		URI url;
		try {
			url = new URI(chargeProfileBaseUrl + "/chargeprofile/getChargeProfiles?profileCode=" + profileCode);
			HttpEntity<ChargeProfileResponse> response = null;

			HttpHeaders headers = new HttpHeaders();
			headers.set("adminUserId", userId);
			HttpEntity request = new HttpEntity(headers);

			response = restTemplate.exchange(url, HttpMethod.GET, request, ChargeProfileResponse.class);
			// response = restTemplate.getForObject(url, responseType, uriVariables)
			return response.getBody();
		} catch (URISyntaxException e) {
			logger.error("Error On Charge Profile", e);
			throw new InvalidUrlException(ExceptionCode.URL_NOT_FOUND.toString());
		} catch (HttpStatusCodeException e) {
			logger.error("Response Status - " + e.getStatusCode() + " " + e.getStatusText() + " "
					+ e.getResponseBodyAsString(), e);
			throw e;
		}
	}

	public double getChargeProfile(String userId, String profileId, String chargeType) throws Exception {
		ChargeProfileResponse chargeProfileResponse = getChargeProfile(userId, profileId);
		if (chargeProfileResponse.getListOfChargeProfiles().size() <= 0) {
			throw new Exception(InvokeMessage.CHARGE_PROFILE_NOT_FOUND.toString());
		}
		Iterator<ChargeDTO> chargeDTO = chargeProfileResponse.getListOfChargeProfiles().get(0).getListOfCharges()
				.iterator();
		while (chargeDTO.hasNext()) {
			ChargeDTO obj = chargeDTO.next();
			if (obj.getChargeName().equalsIgnoreCase(chargeType)) {
				try {
					return Double.parseDouble(obj.getChargeValue());
				} catch (Exception e) {
					throw new Exception(InvokeMessage.INVALID_FORMAT.toString());
				}
			}
		}
		throw new Exception(InvokeMessage.CHARGE_VALUE_NOT_FOUND.toString());

	}

}
