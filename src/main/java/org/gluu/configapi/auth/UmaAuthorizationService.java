package org.gluu.configapi.auth;


import org.gluu.oxauth.client.uma.UmaRptIntrospectionService;
import org.gluu.oxauth.model.uma.RptIntrospectionResponse;
import org.gluu.configapi.service.UmaService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.apache.commons.lang.StringUtils;


@ApplicationScoped
@Named("umaAuthorizationService")
public class UmaAuthorizationService extends AuthorizationService implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String WELL_KNOWN_UMA_PATH = "/.well-known/uma2-configuration";

    @Inject
    private Logger logger;

    @Inject
    UmaService umaService;
    
    
    public void validateAuthorization(String token, ResourceInfo resourceInfo) throws Exception {
        if (StringUtils.isBlank(token)) {
            logger.info("Token is blank");
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        List<String> resourceScopes = getRequestedScopes(resourceInfo);

        logger.debug("this.umaService.getUmaMetadata() = "+this.umaService.getUmaMetadata());
       //RptIntrospectionResponse rptIntrospectionResponse = this.umaService.ge

    }

}