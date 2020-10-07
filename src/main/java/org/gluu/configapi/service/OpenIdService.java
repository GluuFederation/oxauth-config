package org.gluu.configapi.service;

import org.gluu.oxauth.client.service.IntrospectionService;
import org.gluu.configapi.auth.*;
import org.gluu.util.exception.ConfigurationException;
import org.gluu.util.init.Initializable;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Inject;
import org.slf4j.Logger;

@ApplicationScoped
public class OpenIdService extends Initializable implements Serializable {

    private static final long serialVersionUID = 4564959567069741194L;

    @Inject
    Logger logger;

    @Inject
    ConfigurationService configurationService;

    private IntrospectionService introspectionService;

    public IntrospectionService getIntrospectionService() {
        init();
        return introspectionService;
    }

    @Override
    protected void initInternal() {
        try {
            loadOpenIdConfiguration();
        } catch (IOException ex) {
            logger.error("Failed to load oxAuth OpenId configuration", ex);
            throw new ConfigurationException("Failed to load oxAuth OpenId configuration", ex);
        }
    }

    private void loadOpenIdConfiguration() throws IOException {
        logger.debug(
                "OpenIdService::loadOpenIdConfiguration() - configurationService.find().getIntrospectionEndpoint() = "
                        + configurationService.find().getIntrospectionEndpoint());
        String introspectionEndpoint = configurationService.find().getIntrospectionEndpoint();
        this.introspectionService = AuthClientFactory.getIntrospectionService(introspectionEndpoint, false);

        logger.debug("\n\n OpenIdService::loadOpenIdConfiguration() - introspectionService =" + introspectionService);
        logger.info("Successfully loaded oxAuth configuration");
    }

}
