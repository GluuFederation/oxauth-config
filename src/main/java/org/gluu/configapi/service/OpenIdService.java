package org.gluu.configapi.service;

import org.gluu.oxauth.client.service.IntrospectionService;
import org.gluu.configapi.auth.AuthService;
import org.gluu.oxauth.client.OpenIdConfigurationClient;
import org.gluu.oxauth.client.OpenIdConfigurationResponse;
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
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.gluu.configapi.auth.*;

@ApplicationScoped
public class OpenIdService extends Initializable implements Serializable {

    private static final long serialVersionUID = 4564959567069741194L;

    public static final String WELL_KNOWN_OPENID_PATH = "/.well-known/openid-configuration";

    @Inject
    Logger logger;

    @Inject
    ConfigurationService configurationService;

    private ClientHttpEngine clientHttpEngine;
    
    //@Inject
    private IntrospectionService introspectionService;
    //private AuthService introspectionService;

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
        System.out.println("OpenIdService::loadOpenIdConfiguration() - Entry");
        String openIdProvider = configurationService.find().getIssuer();
        System.out.println("OpenIdService::loadOpenIdConfiguration() - openIdProvider = "+openIdProvider);
        if (StringHelper.isEmpty(openIdProvider)) {
            logger.error("OpenIdProvider Url is invalid");
            throw new ConfigurationException("OpenIdProvider Url is invalid");
        }
        if (!openIdProvider.endsWith(WELL_KNOWN_OPENID_PATH)) {
            openIdProvider += WELL_KNOWN_OPENID_PATH;
        }
        System.out.println("OpenIdService::loadOpenIdConfiguration() - openIdProvider_2 = "+openIdProvider);
        //this.clientHttpEngine = createClientHttpEngine();
        // introspectionService =
        // ClientFactory.instance().createIntrospectionService(openIdProvider,clientHttpEngine);
        // //Error - org.jboss.resteasy.client.ClientExecutor
        //introspectionService = ClientFactory.instance().createIntrospectionService(openIdProvider);
        //System.out.println("\n\n OpenIdService::loadOpenIdConfiguration() - introspectionService ="+introspectionService);
        
        //final OpenIdConfigurationClient openIdConfigurationClient = new OpenIdConfigurationClient(openIdProvider);
        //System.out.println("\n\n OpenIdService::loadOpenIdConfiguration() - openIdConfigurationClient ="+openIdConfigurationClient);
        //final OpenIdConfigurationResponse response = openIdConfigurationClient.execOpenIdConfiguration();
       // this.introspectionService = AuthClientFactory.clientBuilder("https://pujavs4.2.gluu.server/oxauth/restv1/introspection",false);
        this.introspectionService = AuthClientFactory.getIntrospectionService("https://pujavs4.2.gluu.server/oxauth/restv1/introspection",false);
        System.out.println("\n\n OpenIdService::loadOpenIdConfiguration() - introspectionService ="+introspectionService);
        logger.info("Successfully loaded oxAuth configuration");
       

    }

    public ClientHttpEngine createClientHttpEngine() {
        ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(createClient());
        return engine;
    }

    public HttpClient createClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        HttpClientBuilder httClientBuilder = HttpClients.custom();

        HttpClient httpClient = httClientBuilder
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                .setConnectionManager(cm).build();
        cm.setMaxTotal(200); // Increase max total connection to 200
        cm.setDefaultMaxPerRoute(20); // Increase default max connection per route to 20

        return httpClient;
        // rptConnectionPoolMaxTotal = 200
        // rptConnectionPoolDefaultMaxPerRoute = 20
        // rptConnectionPoolValidateAfterInactivity = 10
        // rptConnectionPoolCustomKeepAliveTimeout = 5
    }

}
