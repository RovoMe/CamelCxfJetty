package at.rovo.cxf.test.routes;

import namespace.acknowledgement.Acknowledgement;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class Endpoint1Route extends RouteBuilder
{

	@Override
	public void configure() throws Exception
	{
		from("cxf:bean:endpoint1Service")
			.log("endpoint1Service")
			.process(new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					Acknowledgement ack = new Acknowledgement();
					ack.setDetails("Success");
					exchange.getIn().setBody(ack);
				}
			});
		
		from("cxf:bean:endpoint1ServiceSSL")
			.to("log:endpoint1ServiceSSL")
			.process(new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					Acknowledgement ack = new Acknowledgement();
					ack.setDetails("Success");
					exchange.getIn().setBody(ack);
				}
			});;
	}

}
