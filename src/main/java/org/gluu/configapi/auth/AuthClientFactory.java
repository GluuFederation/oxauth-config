package org.gluu.configapi.auth;

/*import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;*/

import org.jboss.resteasy.client.jaxrs.internal.proxy.ClientInvoker;
import org.jboss.resteasy.microprofile.client.RestClientBuilderImpl;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.gluu.oxauth.client.service.IntrospectionService;
import org.slf4j.Logger;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.*;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.client.jaxrs.engines.ClientHttpEngineBuilder43;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

public class AuthClientFactory {

    private final static AuthClientFactory INSTANCE = new AuthClientFactory();

    private ApacheHttpClient43Engine engine;

    private AuthClientFactory instance() {
        return INSTANCE;
    }

    public static AuthService clientBuilder(String p_url, boolean followRedirects) {
        System.out.println("\n\n AuthClientFactory::clientBuilder() - p_url =" + p_url + " , followRedirects = "
                + followRedirects);
        return clientBuilder(p_url, 200, 20, CookieSpecs.STANDARD, followRedirects);
    }

    public static AuthService clientBuilder(String p_url, int maxTotal, int defaultMaxPerRoute, String cookieSpec,
            boolean followRedirects) {
        AuthService authService = RestClientBuilder.newBuilder().baseUri(UriBuilder.fromPath(p_url).build())
                .property("connectionPoolSize", maxTotal).property("maxPooledPerRoute", defaultMaxPerRoute)
                .build(AuthService.class);
        System.out.println("\n\n AuthClientFactory::clientBuilder() - authService =" + authService);
        return authService;
    }
    
    public static IntrospectionService getIntrospectionService(String p_url, boolean followRedirects) {
        return createIntrospectionService(p_url, 200, 20, CookieSpecs.STANDARD, followRedirects);
    }
    
    public static IntrospectionService createIntrospectionService(String p_url, int maxTotal, int defaultMaxPerRoute,
            String cookieSpec, boolean followRedirects) {
        ApacheHttpClient43Engine engine = createEngine(maxTotal, defaultMaxPerRoute, cookieSpec, followRedirects);
        System.out.println("\n\n AuthClientFactory::createIntrospectionService() - engine =" + engine);
        RestClientBuilder restClient = RestClientBuilder.newBuilder().baseUri(UriBuilder.fromPath(p_url).build())
                .register(engine);
        System.out.println("\n\n AuthClientFactory::createIntrospectionService() - restClient =" + restClient);
        ResteasyWebTarget target = (ResteasyWebTarget) ResteasyClientBuilder.newClient(restClient.getConfiguration()).target(p_url);
        IntrospectionService proxy = target.proxy(IntrospectionService.class);
        System.out.println("\n\n AuthClientFactory::createIntrospectionService() - proxy =" + proxy);
        return proxy;
   
    }

    public static IntrospectionService createIntrospectionService_1(String p_url, int maxTotal, int defaultMaxPerRoute,
            String cookieSpec, boolean followRedirects) {
        /*
         * ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();
         *  ResteasyWebTarget target = client.target(UriBuilder.fromPath(p_url)); IntrospectionService proxy =
         * target.proxy(IntrospectionService.class);
         * 
         */
        //ApacheHttpClient43Engine engine = createEngine(maxTotal, defaultMaxPerRoute, cookieSpec, followRedirects);
        ClientHttpEngineBuilder43 engineBuilder = new ClientHttpEngineBuilder43();
        RestClientBuilder restClient = RestClientBuilder.newBuilder().baseUri(UriBuilder.fromPath(p_url).build())
                .property("connectionPoolSize", maxTotal).property("maxPooledPerRoute", defaultMaxPerRoute);

        //WebTarget target = ResteasyClientBuilder.newClient(restClient.getConfiguration()).target(p_url);
        System.out.println("\n\n AuthClientFactory::createIntrospectionService() - restClient =" + restClient);
        ResteasyWebTarget target = (ResteasyWebTarget) ResteasyClientBuilder.newClient(restClient.getConfiguration()).target(p_url);
        IntrospectionService proxy = target.proxy(IntrospectionService.class);
        System.out.println("\n\n AuthClientFactory::createIntrospectionService() - proxy =" + proxy);
        return proxy;
    }

    public static ApacheHttpClient43Engine createEngine(boolean followRedirects) {
        return createEngine(200, 20, CookieSpecs.STANDARD, followRedirects);
    }

    public static ApacheHttpClient43Engine createEngine(int maxTotal, int defaultMaxPerRoute, String cookieSpec,
            boolean followRedirects) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(cookieSpec).build())
                .setConnectionManager(cm).build();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        final ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(httpClient);
        engine.setFollowRedirects(followRedirects);
        return engine;
    }
}
