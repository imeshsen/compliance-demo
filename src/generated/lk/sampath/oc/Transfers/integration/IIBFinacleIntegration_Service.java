package lk.sampath.oc.Transfers.integration;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.2
 * 2019-05-06T11:41:02.147+05:30
 * Generated source version: 3.0.2
 * 
 */
@WebServiceClient(name = "IIBFinacleIntegration", 
                  wsdlLocation = "file:/D:/workspace/java/transfer/Transfers/src/main/resources/wsdl/IIBFinacleIntegration.wsdl",
                  targetNamespace = "http://www.sampath.lk/SD/IIBFinacleIntegration/") 
public class IIBFinacleIntegration_Service extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.sampath.lk/SD/IIBFinacleIntegration/", "IIBFinacleIntegration");
    public final static QName IIBFinacleIntegrationSOAP = new QName("http://www.sampath.lk/SD/IIBFinacleIntegration/", "IIBFinacleIntegrationSOAP");
    static {
        URL url = null;
        try {
            url = new URL("file:/D:/workspace/java/transfer/Transfers/src/main/resources/wsdl/IIBFinacleIntegration.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(IIBFinacleIntegration_Service.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/D:/workspace/java/transfer/Transfers/src/main/resources/wsdl/IIBFinacleIntegration.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public IIBFinacleIntegration_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public IIBFinacleIntegration_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IIBFinacleIntegration_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IIBFinacleIntegration_Service(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IIBFinacleIntegration_Service(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IIBFinacleIntegration_Service(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns IIBFinacleIntegration
     */
    @WebEndpoint(name = "IIBFinacleIntegrationSOAP")
    public IIBFinacleIntegration getIIBFinacleIntegrationSOAP() {
        return super.getPort(IIBFinacleIntegrationSOAP, IIBFinacleIntegration.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IIBFinacleIntegration
     */
    @WebEndpoint(name = "IIBFinacleIntegrationSOAP")
    public IIBFinacleIntegration getIIBFinacleIntegrationSOAP(WebServiceFeature... features) {
        return super.getPort(IIBFinacleIntegrationSOAP, IIBFinacleIntegration.class, features);
    }

}
