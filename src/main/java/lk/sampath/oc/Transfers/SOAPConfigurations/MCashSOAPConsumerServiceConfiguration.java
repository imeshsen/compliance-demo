package lk.sampath.oc.Transfers.SOAPConfigurations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import lk.sampath.oc.Transfers.Service.MCashSOAPConsumerService;


public class MCashSOAPConsumerServiceConfiguration {

private static Logger logger = LogManager.getLogger(MCashSOAPConsumerServiceConfiguration.class);
@Value("${service.url.mcash.endpoint}")
private String mCashEndpoint;
@Bean
public Jaxb2Marshaller marshaller1() {
	logger.info("jaxb marshaaler");
	Jaxb2Marshaller marshaller1 = new Jaxb2Marshaller();
	logger.info("creating JAXB marshaller in the bean: mcash");
	marshaller1.setContextPath("MCashServices.wsdl");
	return marshaller1;
}

	@Primary
	@Bean
	public MCashSOAPConsumerService mCashSoapConsumerService(Jaxb2Marshaller marshaller1) {
		logger.info("setting marshaller and unmarshaller : mcash");
		MCashSOAPConsumerService client = new MCashSOAPConsumerService();
//		client.setDefaultUri("http://wasnd-server:8088/mockMCashServicesPortBinding");
		//client.setDefaultUri("http://localhost:8088/mockMCashServicesPortBinding");
		client.setDefaultUri(mCashEndpoint);
		
		//client.setDefaultUri("http://192.125.125.96:7003/MobileCashServices/MCashServices");
		client.setMarshaller(marshaller1);
		client.setUnmarshaller(marshaller1);
		return client;
	}
}
