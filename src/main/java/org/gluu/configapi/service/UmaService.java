package org.gluu.configapi.service;

import org.gluu.oxauth.client.uma.UmaClientFactory;
import org.gluu.oxauth.client.uma.UmaMetadataService;
import org.gluu.oxauth.client.uma.UmaRptIntrospectionService;
import org.gluu.oxauth.model.uma.PermissionTicket;
import org.gluu.oxauth.model.uma.RptIntrospectionResponse;
import org.gluu.oxauth.model.uma.UmaMetadata;
import org.gluu.exception.OxIntializationException;
import org.gluu.util.StringHelper;
import org.gluu.util.exception.ConfigurationException;
import org.gluu.util.init.Initializable;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;

@ApplicationScoped
public class UmaService extends Initializable implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String WELL_KNOWN_UMA_PATH = "/.well-known/uma2-configuration";

    @Inject
    Logger logger;

    @Inject
    ConfigurationService configurationService;
    
    private ClientHttpEngine clientHttpEngine;
    private UmaMetadata umaMetadata;
    private UmaRptIntrospectionService rptIntrospectionService;
        
    @Override
    protected void initInternal() {
        try {
            loadUmaConfigurationService();
        } catch (Exception ex) {
            throw new ConfigurationException("Failed to load oxAuth UMA configuration");
        }               
    }
   
    
    public UmaMetadata getUmaMetadata() throws Exception {
        init();
        return this.umaMetadata;
    }
    
    public void loadUmaConfigurationService() throws Exception {

        UmaMetadata umaMetadata = initUmaMetadataConfiguration();
    }


    public UmaMetadata initUmaMetadataConfiguration() throws OxIntializationException {
        String umaConfigurationEndpoint = getUmaConfigurationEndpoint();
        if (StringHelper.isEmpty(umaConfigurationEndpoint)) {
            return null;
        }

        logger.info("##### Getting UMA metadata ...");
        UmaMetadataService metaDataConfigurationService;
        if (this.clientHttpEngine == null) {
            metaDataConfigurationService = UmaClientFactory.instance().createMetadataService(umaConfigurationEndpoint);
        } else {
            metaDataConfigurationService = UmaClientFactory.instance().createMetadataService(umaConfigurationEndpoint,
                    this.clientHttpEngine);
        }
        UmaMetadata metadataConfiguration = metaDataConfigurationService.getMetadata();

        logger.info("##### Getting UMA metadata ... DONE");

        if (metadataConfiguration == null) {
            throw new OxIntializationException("UMA meta data configuration is invalid!");
        }

        return metadataConfiguration;
    }

    
    public String getUmaConfigurationEndpoint()  {
        String umaProvider = configurationService.find().getUmaConfigurationEndpoint();

        if (StringHelper.isEmpty(umaProvider)) {
            logger.error("UmaConfiguration Url is invalid");
            throw new ConfigurationException("UmaConfiguration Url is invalid");
        }
        if (!umaProvider.endsWith(WELL_KNOWN_UMA_PATH)) {
            umaProvider += WELL_KNOWN_UMA_PATH;
        }
        return umaProvider;
    }
    
   

}
