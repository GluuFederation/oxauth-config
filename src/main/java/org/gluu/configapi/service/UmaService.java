package org.gluu.configapi.service;

import org.gluu.oxauth.client.uma.UmaMetadataService;
import org.gluu.oxauth.client.uma.UmaPermissionService;
import org.gluu.oxauth.client.uma.UmaRptIntrospectionService;
import org.gluu.oxauth.model.uma.UmaMetadata;
import org.gluu.configapi.auth.*;
import org.gluu.exception.OxIntializationException;
import org.gluu.util.exception.ConfigurationException;
import org.gluu.util.init.Initializable;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

@ApplicationScoped
public class UmaService extends Initializable implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String WELL_KNOWN_UMA_PATH = "/.well-known/uma2-configuration";

    @Inject
    Logger logger;

    @Inject
    ConfigurationService configurationService;

    private UmaMetadata umaMetadata;
    private UmaPermissionService umaPermissionService;
    private UmaRptIntrospectionService umaRptIntrospectionService;

    @Override
    protected void initInternal() {
        try {
            loadUmaConfigurationService();
        } catch (Exception ex) {
            throw new ConfigurationException("Failed to load oxAuth UMA configuration", ex);
        }
    }

    public UmaMetadata getUmaMetadata() throws Exception {
        init();
        return this.umaMetadata;
    }

    public void loadUmaConfigurationService() throws Exception {
        this.umaMetadata = getUmaMetadataConfiguration();
        this.umaPermissionService = AuthClientFactory.getUmaPermissionService(this.umaMetadata, false);
        this.umaRptIntrospectionService = AuthClientFactory.getUmaRptIntrospectionService(this.umaMetadata, false);
    }

    @Produces
    @ApplicationScoped
    @Named("umaMetadataConfiguration")
    public UmaMetadata getUmaMetadataConfiguration() throws OxIntializationException {

        logger.info("##### Getting UMA Metadata Service ...");
        logger.debug(
                "\n\n UmaService::initUmaMetadataConfiguration() - configurationService.find().getUmaConfigurationEndpoint() = "
                        + configurationService.find().getUmaConfigurationEndpoint());
        UmaMetadataService umaMetadataService = AuthClientFactory
                .getUmaMetadataService(configurationService.find().getUmaConfigurationEndpoint(), false);
        logger.debug("\n\n UmaService::initUmaMetadataConfiguration() - umaMetadataService = " + umaMetadataService);

        logger.info("##### Getting UMA Metadata ...");
        UmaMetadata umaMetadata = umaMetadataService.getMetadata();
        logger.debug("\n\n UmaService::initUmaMetadataConfiguration() - umaMetadata = " + umaMetadata);
        logger.info("##### Getting UMA metadata ... DONE");

        if (umaMetadata == null) {
            throw new OxIntializationException("UMA meta data configuration is invalid!");
        }

        return umaMetadata;
    }

}
