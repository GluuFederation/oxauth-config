/**
 * 
 */
package org.gluu.configapi.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetadataBuilder;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.Timer;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.slf4j.Logger;

/**
 * @author Mougang T.Gasmyr
 *
 */
@Provider
@WebFilter(urlPatterns = "/*")
public class ApiHttpFiler implements Filter {
    private static final String METER = "meterr_";
    private static final String COUNTER = "counterr_";
    private static final String TIMER = "timerr_";

    @Inject
    Logger logger;

    @Context
    UriInfo info;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry registry;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        logger.info("##################################################");
        registry.counter(getMetaData(MetricType.COUNTER, COUNTER)).inc();
        Timer timer = registry.timer(getMetaData(MetricType.TIMER, TIMER));
        Meter meter = registry.meter(getMetaData(MetricType.METERED, METER));

        chain.doFilter(req, res);

        timer.getCount();
        meter.mark();
    }

    private Metadata getMetaData(MetricType type, String prefix) {
        return new MetadataBuilder().withName(prefix + info.getPath()).withDisplayName(prefix + info.getPath())
                .withType(type).withUnit(MetricUnits.SECONDS).build();
    }

}
