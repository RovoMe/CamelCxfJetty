package at.rovo.cxf.test;

import javax.jws.WebService;
import namespace.acknowledgement.Acknowledgement;
import namespace.serviceoperations.Endpoint1Endpoint;
import namespace.serviceoperations.Endpoint1Operation1Request;
import namespace.serviceoperations.Endpoint1Operation2Request;
import namespace.serviceoperations.Endpoint1Operation2Response;
import namespace.serviceoperations.OperationFault;
import org.apache.cxf.annotations.FastInfoset;
import org.apache.cxf.annotations.GZIP;
import org.apache.cxf.annotations.SchemaValidation;


@WebService(targetNamespace = "http://serviceoperations.namespace", name = "Endpoint1_Endpoint")
@FastInfoset
@SchemaValidation
@GZIP(threshold = 500)
public class EnhancedEndpoint1Endpoint implements Endpoint1Endpoint
{
	@Override
	public Endpoint1Operation2Response endpoint1Operation2(
			Endpoint1Operation2Request in) throws OperationFault
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Acknowledgement endpoint1Operation1(Endpoint1Operation1Request in)
			throws OperationFault
	{
		// TODO Auto-generated method stub
		return null;
	}
}
