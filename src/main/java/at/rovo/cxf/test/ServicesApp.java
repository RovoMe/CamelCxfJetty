package at.rovo.cxf.test;

import org.apache.camel.spring.javaconfig.Main;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import at.rovo.cxf.test.config.ServicesSpringConfig;

public class ServicesApp
{
	static
	{
		java.security.Security.setProperty("networkaddress.cache.ttl", "20");
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		ServicesApp hub = new ServicesApp();
		hub.boot();
	}

	public void boot() throws Exception
	{
		Main app = new Main();
		// enable the shutdown hook
		app.enableHangupSupport();
		AnnotationConfigApplicationContext ctx = 
				new AnnotationConfigApplicationContext(ServicesSpringConfig.class);
		app.setApplicationContext(ctx);
		// do .run() instead of .start()
		app.run();
	}
}
