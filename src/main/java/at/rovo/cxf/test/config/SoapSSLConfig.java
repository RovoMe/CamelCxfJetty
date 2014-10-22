package at.rovo.cxf.test.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.Resource;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.configuration.jsse.TLSServerParameters;
import org.apache.cxf.configuration.security.ClientAuthentication;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

@Configuration
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
public class SoapSSLConfig
{
	public final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	@Resource
	protected Environment env;
	@Resource(name = "cxf")
	protected SpringBus bus;
	
	/**
	 * SSL configuration for the SOAP based services.
	 * 
	 * @return The factory which creates a Jetty server configured with HTTPS
	 * @throws Exception
	 */
	@Bean(name = "jettySSLEngineFactory")
    public JettyHTTPServerEngineFactory jettyEngine() throws Exception {
		// JettyDestinationFactory gets injected, 
		// JettyHTTPServerEngineFactory however not :(
		LOG.info("Configuring Jetty for SSL secured connections");
        JettyHTTPServerEngineFactory factory = new JettyHTTPServerEngineFactory();
        factory.setBus(bus);
        
        int port = 8081;
        TLSServerParameters serverParameters = tlsParameters();
        try {
            String path = env.getProperty("services.address.ssl");
            LOG.debug("Jetty-SSL path: {}", path);
            if (StringUtils.isNotBlank(path) 
            		&& path.contains(":") 
            		&& path.lastIndexOf(":")+1 <= path.length() ) {
            	LOG.info("Trying to initialize SSL secured Jetty server: {}",
            			path);
                String sPort = path.substring(path.lastIndexOf(":")+1);
                if (sPort.contains("/")) {
                    sPort = sPort.substring(0, sPort.indexOf("/"));
                }
                port = Integer.parseInt(sPort);
            }
        }
        catch (Exception e) {
        	LOG.error(e.getLocalizedMessage(), e);
        }
        finally {
            factory.setTLSServerParametersForPort(port, serverParameters);
        }
                
        return factory;
    }

	/**
	 * Configures a TLS configuration for a Jetty server.
	 * 
	 * @return The TLS parameters for the Jetty server
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 */
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
		
		/**
		 * http://cxf.547215.n5.nabble.com/CXF-using-SSL-Remote-host-closed-connection-during-handshake-td5706905.html
		 * answer of coheiga (Colm) at 3/4 of the page:
		 * 
		 * ... you need to add the following ciphersuite filter to both the webservice and webserivce-consumer:
		 * 
		 * filter.getInclude().add(".*_WITH_AES_.*");
		 * 
		 * JDK 1.7 does not include DES cipher suites and so you need to add AES.
		 */
		
		// set all the needed include & exclude cipher filters
        FiltersType filters = new FiltersType();
        filters.getInclude().add(".*_EXPORT_.*");
        filters.getInclude().add(".*_EXPORT1024_.*");
        filters.getInclude().add(".*_WITH_3DES_.*");
        filters.getInclude().add(".*_WITH_DES_.*");
        filters.getInclude().add(".*_WITH_NULL_.*");
		filters.getInclude().add(".*_WITH_AES_.*");
        filters.getExclude().add(".*_DH_anon_.*");
		tlsParameters.setCipherSuitesFilter(filters);
		
		return tlsParameters;
	}

	/**
	 * Configures the keystore to use for SSL secured connections.
	 * 
	 * @return The keystore containing our certificates
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public KeyStore sslKeystore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		InputStream in = this.getClass().getResourceAsStream(env.getProperty("ssl.keyStore.resource"));
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(in, env.getProperty("ssl.keyStore.password").toCharArray());
		return ks;
	}
}
