package at.rovo.cxf.test.config;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.management.InstrumentationManager;
import org.apache.cxf.management.jmx.InstrumentationManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import at.rovo.cxf.test.routes.Endpoint1Route;
import at.rovo.cxf.test.routes.Endpoint2Route;

@Configuration
@Profile("default")
@PropertySource("classpath:services.properties")
@Import({CxfEndpointConfig.class})
public class ServicesSpringConfig extends CamelConfiguration
{

	@Resource
	protected Environment env;
	
	@Resource(name="cxf")
	protected SpringBus bus;
	
	@Bean(name="appType")
	public String appType()
	{
		return "Services";
	}
	
	// In order to inject properties in fields directly
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
	{
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean(name="org.apache.cxf.management.InstrumentationManager")
	public InstrumentationManager instrumentationManager()
	{
		final InstrumentationManagerImpl im = new InstrumentationManagerImpl();
		im.setEnabled(true);
		im.setUsePlatformMBeanServer(true);
		im.setCreateMBServerConnectorFactory(true);
		im.setPersistentBusId("hub-services");
		im.setBus(bus);
		
		return im;
	}

	@Override
	protected void setupCamelContext(CamelContext camelContext) throws Exception {
		super.setupCamelContext(camelContext);

		final PropertiesComponent pc = new PropertiesComponent("classpath:"
				+ env.getProperty("propertyfile"));
		camelContext.addComponent("properties", pc);
	}
	
	@Override
	public List<RouteBuilder> routes()
	{
		List<RouteBuilder> routes = new ArrayList<>();
		routes.add(new Endpoint1Route());
		routes.add(new Endpoint2Route());
		return routes;
	}
}
