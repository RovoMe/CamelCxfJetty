package at.rovo.cxf.test.auth;

import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * CXF Interceptor that provides HTTP Basic Authentication validation.
 *
 * Based on the concepts outline here: http://chrisdail.com/2008/03/31/apache-cxf-with-http-basic-authentication
 *
 * @author CDail
 */
public class BasicAuthAuthorizationInterceptor extends SoapHeaderInterceptor {

  protected static final Logger LOG = LoggerFactory
      .getLogger(BasicAuthAuthorizationInterceptor.class);

  @Override
  public void handleMessage(Message message) throws Fault {
    // This is set by CXF
    AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);

    // If the policy is not set, the user did not specify credentials
    // A 401 is sent to the client to indicate that authentication is
    // required
    if (policy == null) {
      LOG.warn("User attempted to log in with no credentials");
      sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
      return;
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Logging in use: " + policy.getUserName());
    }

    String userId = policy.getUserName();
    String userKey = policy.getPassword();

		/* checks whether the userId is known */
    if (null == userId || "".equals(userId) || null == userKey || "".equals(userKey)) {
      LOG.error(
          "An authentication error happenend. Either the UserId ({}) or the UserKey ({}) is null or empty.",
          userId, userKey);
      sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
      return;
    }
    // check whether the userkey matches the one in the user entity found
    // for the user id
    if (userKey.equals("secret")) {
      // the user could be successfully authenticated
      LOG.debug("UserKey {} of User {} was successfully authenticated",
                userKey, userId);
    } else {
      // the userkey set in the request and the one stored in the user
      // entity do not match
      LOG.error(
          "User {} could not be authenticated. The UserKey in the user entity is {} but the UserKey in the request was {}",
          userId, "secret", userKey);
      sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
    }
  }

  private void sendErrorResponse(Message message, int responseCode) {
    Message outMessage = getOutMessage(message);
    outMessage.put(Message.RESPONSE_CODE, responseCode);

    // Set the response headers
    @SuppressWarnings("unchecked")
    Map<String, List<String>> responseHeaders = (Map<String, List<String>>) message
        .get(Message.PROTOCOL_HEADERS);
    if (responseHeaders != null) {
      responseHeaders.put("WWW-Authenticate",
                          Arrays.asList("Basic realm=realm"));
      responseHeaders.put("Content-Length",
                          Arrays.asList("0"));
    }
    message.getInterceptorChain().abort();
    try {
      getConduit(message).prepare(outMessage);
      close(outMessage);
    } catch (IOException e) {
      LOG.warn(e.getMessage(), e);
    }
  }

  private Message getOutMessage(Message inMessage) {
    Exchange exchange = inMessage.getExchange();
    Message outMessage = exchange.getOutMessage();
    if (outMessage == null) {
      Endpoint endpoint = exchange.get(Endpoint.class);
      outMessage = endpoint.getBinding().createMessage();
      exchange.setOutMessage(outMessage);
    }
    outMessage.putAll(inMessage);
    return outMessage;
  }

  private Conduit getConduit(Message inMessage) throws IOException {
    Exchange exchange = inMessage.getExchange();
    Conduit conduit = exchange.getDestination().getBackChannel(inMessage);
    exchange.setConduit(conduit);
    return conduit;
  }

  private void close(Message outMessage) throws IOException {
    OutputStream os = outMessage.getContent(OutputStream.class);
    os.flush();
    os.close();
  }
}
