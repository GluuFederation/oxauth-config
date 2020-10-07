package org.gluu.configapi.auth;

import org.gluu.oxauth.client.service.IntrospectionService;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;

public class AuthClientFactory {

    @Inject
    public static IntrospectionService getIntrospectionService(String p_url, boolean followRedirects) {
        return createIntrospectionService(p_url, 200, 20, CookieSpecs.STANDARD, followRedirects);
    }

    public static IntrospectionService createIntrospectionService(String p_url, int maxTotal, int defaultMaxPerRoute,
            String cookieSpec, boolean followRedirects) {
        ApacheHttpClient43Engine engine = createEngine(maxTotal, defaultMaxPerRoute, cookieSpec, followRedirects);
        RestClientBuilder restClient = RestClientBuilder.newBuilder().baseUri(UriBuilder.fromPath(p_url).build())
                .register(engine);
        ResteasyWebTarget target = (ResteasyWebTarget) ResteasyClientBuilder.newClient(restClient.getConfiguration())
                .target(p_url);
        IntrospectionService proxy = target.proxy(IntrospectionService.class);
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
