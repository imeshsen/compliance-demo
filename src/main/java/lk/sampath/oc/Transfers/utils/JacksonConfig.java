package lk.sampath.oc.Transfers.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JacksonConfig {
	private StringUtils stringUtils;
	private static final Logger logger = LoggerFactory.getLogger(JacksonConfig.class.getName());
	
	public String convertToJson(Object object) {
		String json = "";
		try {
			if(stringUtils.isEmpty(object)) {
				return "Object is Empty";
			}
			ObjectMapper objectMapper = new ObjectMapper();
			json = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error("error occur when executing convertToJson ::", e);
		}
		return json;
	}
}
