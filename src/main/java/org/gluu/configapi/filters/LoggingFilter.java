package org.gluu.configapi.filters;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ContainerRequestFilter {

	@Context
	UriInfo info;

    @Context
    HttpServletRequest request;

	@Inject
	Logger logger;

	public void filter(ContainerRequestContext context) {
		logger.info("***********************************************************************");
		logger.info("****Request " + context.getMethod() + " " + info.getPath() + " from IP " + request.getRemoteAddr());

	}

}
