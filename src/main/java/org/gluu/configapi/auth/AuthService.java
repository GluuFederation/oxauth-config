package org.gluu.configapi.auth;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.gluu.oxauth.model.common.IntrospectionResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

//@RegisterRestClient
public interface AuthService {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    IntrospectionResponse introspectToken(@HeaderParam("Authorization") String p_authorization,
            @FormParam("token") String p_token);

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    String introspectTokenWithResponseAsJwt(@HeaderParam("Authorization") String p_authorization,
            @FormParam("token") String p_token, @FormParam("response_as_jwt") boolean responseAsJwt);

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    JsonNode introspect(@HeaderParam("Authorization") String p_authorization, @FormParam("token") String p_token);

}
