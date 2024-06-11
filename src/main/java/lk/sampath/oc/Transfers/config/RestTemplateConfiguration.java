package lk.sampath.oc.Transfers.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfiguration {

	private static Logger logger = LogManager.getLogger(RestTemplateConfiguration.class);

	@Value("${max.scm.total.connections}")
    private Integer maxTotalConnections_SCM;

    @Value("${max.scm.route.per.host}")
    private Integer maxRoutePerHost_SCM;

    @Value("${max.trd.total.connections}")
    private Integer maxTotalConnections_TRD;

    @Value("${max.trd.route.per.host}")
    private Integer maxRoutePerHost_TRD;
    
    @Value("${connection.timeout.SCM.omnichannel.api}")
    private Integer connectionTimeOutOf_SCM_API;

    @Value("${read.timeout.SCM.omnichannel.api}")
    private Integer readTimeOutOf_SCM_API;
    
    @Value("${connection.timeout.TRD.omnichannel.api}")
    private Integer connectionTimeOutOf_TRD_API;

    @Value("${read.timeout.TRD.omnichannel.api}")
    private Integer readTimeOutOf_TRD_API;
    
    @Bean(name="poolSCM")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManagerForSCM(Registry<ConnectionSocketFactory> socketFactoryRegistry) {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        return result;
    }
    
    @Bean(name="poolTRD")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManagerForTRD(Registry<ConnectionSocketFactory> socketFactoryRegistry) {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager(socketFactoryRegistry);      
        return result;
    }
    
    @Bean(name="SCMOMNIChannelAPI")
    public RestTemplate createRestTemplateSslWithPooling() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder()
                .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("https", socketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        
        PoolingHttpClientConnectionManager poolingConnManager = this.poolingHttpClientConnectionManagerForSCM(socketFactoryRegistry);      
        poolingConnManager.setDefaultMaxPerRoute(maxRoutePerHost_SCM);
        poolingConnManager.setMaxTotal(maxTotalConnections_SCM);
        
        logger.info("Max route per host size of PoolingConnectionManager {} " , poolingConnManager.getTotalStats());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .setConnectionManager(poolingConnManager)
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(connectionTimeOutOf_SCM_API);
        factory.setReadTimeout(readTimeOutOf_SCM_API);
        logger.info("Max route per host size of PoolingConnectionManager {} " , poolingConnManager.getTotalStats());
        return new RestTemplate(factory);
    }
    
    @Bean(name="TRDOMNIChannelAPI")
    public RestTemplate createRestTemplateForTRDSslWithPooling() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder()
                .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("https", socketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager poolingConnManager = this.poolingHttpClientConnectionManagerForTRD(socketFactoryRegistry);      
        poolingConnManager.setDefaultMaxPerRoute(maxRoutePerHost_TRD);
        poolingConnManager.setMaxTotal(maxTotalConnections_TRD);
        
        logger.info("Max route per host size of PoolingConnectionManager {} " , poolingConnManager.getTotalStats());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .setConnectionManager(poolingConnManager)
                .build();
        
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(connectionTimeOutOf_TRD_API);
        factory.setReadTimeout(readTimeOutOf_TRD_API);
        logger.info("Max route per host size of PoolingConnectionManager {} " , poolingConnManager.getTotalStats());
        return new RestTemplate(factory);
    }
	
  
}
