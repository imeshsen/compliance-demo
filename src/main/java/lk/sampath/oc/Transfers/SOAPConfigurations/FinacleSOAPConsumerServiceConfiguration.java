package lk.sampath.oc.Transfers.SOAPConfigurations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import lk.sampath.oc.Transfers.Service.FinacleSOAPConsumerService;



public class FinacleSOAPConsumerServiceConfiguration {

	private static Logger logger = LogManager.getLogger(FinacleSOAPConsumerServiceConfiguration.class);
	
	@Value("${service.url.iib.finacle.endpoint}")
	private String iibFinacleEndpoint;
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		logger.info("creating JAXB marshaller in the bean : finacle");
		marshaller.setContextPath("finacle.wsdl");
		return marshaller;
	}

	@Primary
	@Bean
	public FinacleSOAPConsumerService finacleSoapConsumerService(Jaxb2Marshaller marshaller) {
		logger.info("setting marshaller and unmarshaller : finacle");
		FinacleSOAPConsumerService client = new FinacleSOAPConsumerService();
//		client.setDefaultUri(iibFinacleEndpoint);
////		client.setDefaultUri("http://192.168.125.92:7800/sd/iib/IIBFinacleIntegration");
//		//client.setDefaultUri("http://localhost:8088/mockIIBFinacleIntegrationSOAP");
//		client.setMarshaller(marshaller);
//		client.setUnmarshaller(marshaller);
		return client;
	}
}
