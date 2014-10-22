package at.rovo.cxf.test.routes;

import org.apache.camel.builder.RouteBuilder;

public class Endpoint1Route extends RouteBuilder
{

	@Override
	public void configure() throws Exception
	{
		from("cxf:bean:endpoint1Service")
			.to("log:endpoint1Service");
		
		from("cxf:bean:endpoint1ServiceSSL")
			.to("log:endpoint1ServiceSSL");
	}

}
