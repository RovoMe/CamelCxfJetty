package at.rovo.cxf.test.routes;

import org.apache.camel.builder.RouteBuilder;

public class Endpoint2Route extends RouteBuilder
{

	@Override
	public void configure() throws Exception
	{
		from("cxf:bean:endpoint2Service")
			.to("log:endpoint2Service");
		
		from("cxf:bean:endpoint2ServiceSSL")
			.to("log:endpoint2ServiceSSL");
	}

}
