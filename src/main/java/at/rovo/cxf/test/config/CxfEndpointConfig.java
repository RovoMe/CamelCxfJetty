package at.rovo.cxf.test.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.CxfSpringEndpoint;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import at.rovo.cxf.test.EnhancedEndpoint1Endpoint;
import at.rovo.cxf.test.EnhancedEndpoint2Endpoint;
import at.rovo.cxf.test.auth.BasicAuthAuthorizationInterceptor;

@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
@Import(SoapSSLConfig.class)
public class CxfEndpointConfig 
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
		factoryBean.setAddress(env.getProperty("services.address")+"/endpoint1");
		factoryBean.setServiceClass(EnhancedEndpoint1Endpoint.class);
		factoryBean.setWsdlURL("classpath:/wsdl/test.wsdl");
		factoryBean.setEndpointName(new QName(NAMESPACE, "Endpoint1ServicePort", PREFIX));
		factoryBean.setServiceName(new QName(NAMESPACE, "Endpoint1_Service", PREFIX));
		factoryBean.setDataFormat(DataFormat.POJO);
		final Map<String, Object> properties = new HashMap<>();
		properties.put("schema-validation-enabled", "true");
		properties.put("allowStreaming", true);
		factoryBean.setProperties(properties);
		factoryBean.getInInterceptors().add(httpAuthInterceptor());
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		return factoryBean;
	}

	@Bean(name="endpoint1ServiceSSL")
	public CxfSpringEndpoint endpoint1ServiceSSL() throws Exception
	{
		final CxfSpringEndpoint factoryBean = new CxfSpringEndpoint();
		factoryBean.setAddress(env.getProperty("services.address.ssl")+"/endpoint1");
		factoryBean.setServiceClass(EnhancedEndpoint1Endpoint.class);
		factoryBean.setWsdlURL("classpath:/wsdl/test.wsdl");
		factoryBean.setEndpointName(new QName(NAMESPACE, "Endpoint1ServicePort", PREFIX));
		factoryBean.setServiceName(new QName(NAMESPACE, "Endpoint1_Service", PREFIX));
		factoryBean.setDataFormat(DataFormat.POJO);
		final Map<String, Object> properties = new HashMap<>();
		properties.put("schema-validation-enabled", "true");
		properties.put("allowStreaming", true);
		factoryBean.setProperties(properties);
		factoryBean.getInInterceptors().add(httpAuthInterceptor());
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		return factoryBean;
	}
	
	@Bean(name="endpoint2Service")
	public CxfEndpoint endpoint2Service() throws Exception
	{
		final CxfSpringEndpoint factoryBean = new CxfSpringEndpoint();
		factoryBean.setAddress(env.getProperty("services.address")+"/endpoint2");
		factoryBean.setServiceClass(EnhancedEndpoint2Endpoint.class);
		factoryBean.setWsdlURL("classpath:/wsdl/test.wsdl");
		factoryBean.setEndpointName(new QName(NAMESPACE, "Endpoint2ServicePort", PREFIX));
		factoryBean.setServiceName(new QName(NAMESPACE, "Endpoint2_Service", PREFIX));
		factoryBean.setDataFormat(DataFormat.POJO);
		final Map<String, Object> properties = new HashMap<>();
		properties.put("schema-validation-enabled", "true");
		properties.put("allowStreaming", true);
		factoryBean.setProperties(properties);
		factoryBean.getInInterceptors().add(httpAuthInterceptor());
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		return factoryBean;
	}
	
	@Bean(name="endpoint2ServiceSSL")
	public CxfEndpoint endpoint2ServiceSSL() throws Exception
	{
		final CxfSpringEndpoint factoryBean = new CxfSpringEndpoint();
		factoryBean.setAddress(env.getProperty("services.address.ssl")+"/endpoint2");
		factoryBean.setServiceClass(EnhancedEndpoint2Endpoint.class);
		factoryBean.setWsdlURL("classpath:/wsdl/test.wsdl");
		factoryBean.setEndpointName(new QName(NAMESPACE, "Endpoint2ServicePort", PREFIX));
		factoryBean.setServiceName(new QName(NAMESPACE, "Endpoint2_Service", PREFIX));
		factoryBean.setDataFormat(DataFormat.POJO);
		final Map<String, Object> properties = new HashMap<>();
		properties.put("schema-validation-enabled", "true");
		properties.put("allowStreaming", true);
		factoryBean.setProperties(properties);
		factoryBean.getInInterceptors().add(httpAuthInterceptor());
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		
		return factoryBean;
	}
	
	  @Bean(name = "httpAuthInterceptor")
	  public BasicAuthAuthorizationInterceptor httpAuthInterceptor() 
	  {
	    return new BasicAuthAuthorizationInterceptor();
	  }
}
