package at.rovo.cxf.test.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.namespace.QName;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.CxfSpringEndpoint;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.configuration.jsse.TLSServerParameters;
import org.apache.cxf.configuration.security.ClientAuthentication;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import at.rovo.cxf.test.EnhancedEndpoint1Endpoint;
import at.rovo.cxf.test.EnhancedEndpoint2Endpoint;

@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })//, "classpath:META-INF/jettySSLConfig.xml" })
public class CxfEndpointConfig extends SoapSSLConfig
{
	@Resource
	protected Environment env;
	@Resource(name = "cxf")
    protected SpringBus bus;
	
	private static final String NAMESPACE = "http://serviceoperations.namespace";
	private static final String PREFIX = "test";
	
	@Bean(name="endpoint1Service")
	public CxfSpringEndpoint endpoint1Service() throws Exception
	{
		final CxfSpringEndpoint factoryBean = new CxfSpringEndpoint();
		factoryBean.setServiceClass(EnhancedEndpoint1Endpoint.class);
		factoryBean.setWsdlURL("classpath:/wsdl/test.wsdl");
		factoryBean.setEndpointName(new QName(NAMESPACE, "Endpoint1ServicePort", PREFIX));
		factoryBean.setServiceName(new QName(NAMESPACE, "Endpoint1_Service", PREFIX));
		factoryBean.setAddress(env.getProperty("services.address")+"/endpoint1");
		factoryBean.setDataFormat(DataFormat.POJO);
		final Map<String, Object> properties = new HashMap<>();
		properties.put("schema-validation-enabled", "true");
		properties.put("allowStreaming", true);
		factoryBean.setProperties(properties);
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		return factoryBean;
	}

	@Bean(name="endpoint1ServiceSSL")
	public CxfSpringEndpoint endpoint1ServiceSSL() throws Exception
	{
		final CxfSpringEndpoint factoryBean = new CxfSpringEndpoint();
		factoryBean.setServiceClass(EnhancedEndpoint1Endpoint.class);
		factoryBean.setWsdlURL("classpath:/wsdl/test.wsdl");
		factoryBean.setEndpointName(new QName(NAMESPACE, "Endpoint1ServicePort", PREFIX));
		factoryBean.setServiceName(new QName(NAMESPACE, "Endpoint1_Service", PREFIX));
		factoryBean.setAddress(env.getProperty("services.address.ssl")+"/endpoint1");
		factoryBean.setDataFormat(DataFormat.POJO);
		final Map<String, Object> properties = new HashMap<>();
		properties.put("schema-validation-enabled", "true");
		properties.put("allowStreaming", true);
		factoryBean.setProperties(properties);
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		return factoryBean;
	}
	
	@Bean(name="endpoint2Service")
	public CxfEndpoint endpoint2Service() throws Exception
	{
		final CxfSpringEndpoint factoryBean = new CxfSpringEndpoint();
		factoryBean.setServiceClass(EnhancedEndpoint2Endpoint.class);
		factoryBean.setWsdlURL("classpath:/wsdl/test.wsdl");
		factoryBean.setEndpointName(new QName(NAMESPACE, "Endpoint2ServicePort", PREFIX));
		factoryBean.setServiceName(new QName(NAMESPACE, "Endpoint2_Service", PREFIX));
		factoryBean.setAddress(env.getProperty("services.address")+"/endpoint2");
		factoryBean.setDataFormat(DataFormat.POJO);
		final Map<String, Object> properties = new HashMap<>();
		properties.put("schema-validation-enabled", "true");
		properties.put("allowStreaming", true);
		factoryBean.setProperties(properties);
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		return factoryBean;
	}
	
	@Bean(name="endpoint2ServiceSSL")
	public CxfEndpoint endpoint2ServiceSSL() throws Exception
	{
		final CxfSpringEndpoint factoryBean = new CxfSpringEndpoint();
		factoryBean.setServiceClass(EnhancedEndpoint2Endpoint.class);
		factoryBean.setWsdlURL("classpath:/wsdl/test.wsdl");
		factoryBean.setEndpointName(new QName(NAMESPACE, "Endpoint2ServicePort", PREFIX));
		factoryBean.setServiceName(new QName(NAMESPACE, "Endpoint2_Service", PREFIX));
		factoryBean.setAddress(env.getProperty("services.address.ssl")+"/endpoint2");
		factoryBean.setDataFormat(DataFormat.POJO);
		final Map<String, Object> properties = new HashMap<>();
		properties.put("schema-validation-enabled", "true");
		properties.put("allowStreaming", true);
		factoryBean.setProperties(properties);
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		return factoryBean;
	}
	
	@Bean(name = "jettySSLEngineFactory")
    public JettyHTTPServerEngineFactory jettyEngine() throws Exception {
        JettyHTTPServerEngineFactory factory = new JettyHTTPServerEngineFactory();
        factory.setBus(bus);
        
        int port = 8081;
        TLSServerParameters serverParameters = tlsParameters();
        try {
            String path = env.getProperty("services.address.ssl");
            if (path.contains(":") && path.lastIndexOf(":")+1 <= path.length() ) {
                String sPort = path.substring(path.lastIndexOf(":")+1);
                if (sPort.contains("/")) {
                    sPort = sPort.substring(0, sPort.indexOf("/"));
                }
                port = Integer.parseInt(sPort);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            factory.setTLSServerParametersForPort(port, serverParameters);
        }
                
        return factory;
    }

	//@Bean(name="tlsParameters")
	public TLSServerParameters tlsParameters() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException {
		
		TLSServerParameters tlsParameters = new TLSServerParameters();
        ClientAuthentication clientAuth = new ClientAuthentication();
        clientAuth.setRequired(false);
        clientAuth.setWant(false);
		tlsParameters.setClientAuthentication(clientAuth);
        tlsParameters.setSecureSocketProtocol("SSL");
		
		TrustManagerFactory trustmanagerfactory = 
			     TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustmanagerfactory.init(sslKeystore());
		TrustManager[] mgrs = trustmanagerfactory.getTrustManagers();
		tlsParameters.setTrustManagers(mgrs);
		
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(sslKeystore(), env.getProperty("ssl.key.password").toCharArray());
		tlsParameters.setKeyManagers(keyManagerFactory.getKeyManagers());
		
		// set all the needed include & exclude cipher filters
        FiltersType filters = new FiltersType();
        filters.getInclude().add(".*_EXPORT_.*");
        filters.getInclude().add(".*_EXPORT1024_.*");
        filters.getInclude().add(".*_WITH_3DES_.*");
        filters.getInclude().add(".*_WITH_DES_.*");
        filters.getInclude().add(".*_WITH_NULL_.*");
        filters.getExclude().add(".*_DH_anon_.*");
		tlsParameters.setCipherSuitesFilter(filters);
		
		return tlsParameters;
	}

	// @Bean(name="sslKeystore")
	public KeyStore sslKeystore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		InputStream in = this.getClass().getResourceAsStream(env.getProperty("ssl.keyStore.resource"));
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(in, env.getProperty("ssl.keyStore.password").toCharArray());
		return ks;
	}
}
