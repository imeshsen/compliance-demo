/**
 * 
 */
package lk.sampath.oc.Transfers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import lk.sampath.oc.Transfers.Service.FinacleSOAPConsumerService;

/**
 * @author hrsupun
 *
 */
@Configuration
public class TransferConfiguration {

	@Value("${sampath.cc.service.auth.username}")
	private String basicAuthUserName;

	@Value("${sampath.cc.service.cardpayment.auth.password}")
	private String basicAuthCardPayPassword;

	@Value("${sampath.cc.service.getcardnumber.authid}")
	private String getcardNumberAuthid;

	@Value("${sampath.cc.service.getcardserial.authid}")
	private String getcardSerialAuthid;

	@Value("${service.url.iib.finacle.wsdl}")
	private String iibFinacleWsdl;

	@Value("${sampath.cc.service.auth.app.username}")
	private String basicAuthAppUserName;

	@Value("${sampath.cc.service.cardpayment.auth.app.password}")
	private String basicAuthCardPayAppPassword;

	@Value("${sampath.cc.service.getcardnumber.auth.app.password}")
	private String getcardNumberAuthAppPassword;

	@Value("${sampath.cc.service.getcardserial.auth.app.password}")
	private String getcardSerialAuthAppPassword;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean(name = "payCardRestTemplate")
	public RestTemplate payCardRestTemplate(RestTemplateBuilder builder) {
		return builder.basicAuthentication(basicAuthUserName, basicAuthCardPayPassword).build();
	}

	@Bean(name = "getCardNumberRestTemplate")
	public RestTemplate getCardNumberFromSerial(RestTemplateBuilder builder) {
		return builder.basicAuthentication(basicAuthUserName, getcardNumberAuthid).build();
	}

	@Bean(name = "getCardSerailRestTemplate")
	public RestTemplate getSerialFromCardNumber(RestTemplateBuilder builder) {
		return builder.basicAuthentication(basicAuthUserName, getcardSerialAuthid).build();
	}

	@Bean(name = "payCardAppRestTemplate")
	public RestTemplate payCardAppRestTemplate(RestTemplateBuilder builder) {
		return builder.basicAuthentication(basicAuthAppUserName, basicAuthCardPayAppPassword).build();
	}

	@Bean(name = "getCardNumberAppRestTemplate")
	public RestTemplate getCardNumberAppRestTemplate(RestTemplateBuilder builder) {
		return builder.basicAuthentication(basicAuthAppUserName, getcardNumberAuthAppPassword).build();
	}

	@Bean(name = "getCardSerialAppRestTemplate")
	public RestTemplate getCardSerialAppRestTemplate(RestTemplateBuilder builder) {
		return builder.basicAuthentication(basicAuthAppUserName, getcardSerialAuthAppPassword).build();
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("lk.sampath.oc.Transfers.integration.finacleIntegration");
		return marshaller;
	}

	@Bean
	@Primary
	public FinacleSOAPConsumerService soapConnector(Jaxb2Marshaller marshaller) {
		FinacleSOAPConsumerService client = new FinacleSOAPConsumerService();
		client.setDefaultUri(iibFinacleWsdl);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
