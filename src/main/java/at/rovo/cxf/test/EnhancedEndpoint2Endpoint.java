package at.rovo.cxf.test;

import javax.jws.WebService;
import namespace.serviceoperations.Endpoint2Endpoint;
import namespace.serviceoperations.Endpoint2OperationRequest;
import namespace.serviceoperations.Endpoint2OperationResponse;
import namespace.serviceoperations.OperationFault;
import org.apache.cxf.annotations.FastInfoset;
import org.apache.cxf.annotations.GZIP;
import org.apache.cxf.annotations.SchemaValidation;

@WebService(targetNamespace = "http://serviceoperations.namespace", name = "Endpoint2_Endpoint")
@FastInfoset
@SchemaValidation
@GZIP(threshold = 500)
public class EnhancedEndpoint2Endpoint implements Endpoint2Endpoint
{
	@Override
	public Endpoint2OperationResponse endpoint2Operation(
			Endpoint2OperationRequest in) throws OperationFault
	{
		// TODO Auto-generated method stub
		return null;
	}
}
