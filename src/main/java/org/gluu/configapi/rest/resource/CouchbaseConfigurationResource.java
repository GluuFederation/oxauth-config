package org.gluu.configapi.rest.resource;

import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.google.common.base.Joiner;
import org.gluu.configapi.filters.ProtectedApi;
import org.gluu.configapi.service.CouchbaseConfService;
import org.gluu.configapi.util.ApiConstants;
import org.gluu.configapi.util.Jackson;
import org.gluu.persist.couchbase.model.CouchbaseConnectionConfiguration;
import org.gluu.persist.couchbase.operation.impl.CouchbaseConnectionProvider;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Properties;

@Path(ApiConstants.BASE_API_URL + ApiConstants.CONFIG + ApiConstants.DATABASE + ApiConstants.COUCHBASE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CouchbaseConfigurationResource extends BaseResource {

    @Inject
    Logger logger;

    @Inject
    CouchbaseConfService couchbaseConfService;

    @GET
    @ProtectedApi(scopes = {READ_ACCESS})
    public Response get() {
        return Response.ok(this.couchbaseConfService.findAll()).build();
    }

    @GET
    @Path(ApiConstants.NAME_PARAM_PATH)
    @ProtectedApi(scopes = {READ_ACCESS})
    public Response getWithName(@PathParam(ApiConstants.NAME) String name) {
        return Response.ok(findByName(name)).build();
    }

    @POST
    @ProtectedApi(scopes = {WRITE_ACCESS})
    public Response add(@Valid @NotNull CouchbaseConnectionConfiguration conf) {
        couchbaseConfService.save(conf);
        conf = findByName(conf.getConfigId());
        return Response.status(Response.Status.CREATED).entity(conf).build();
    }

    @PUT
    @ProtectedApi(scopes = {WRITE_ACCESS})
    public Response update(@Valid @NotNull CouchbaseConnectionConfiguration conf) {
        findByName(conf.getConfigId());
        couchbaseConfService.save(conf);
        return Response.ok(conf).build();
    }

    @DELETE
    @Path(ApiConstants.NAME_PARAM_PATH)
    @ProtectedApi(scopes = {WRITE_ACCESS})
    public Response delete(@PathParam(ApiConstants.NAME) String name) {
        findByName(name);
        logger.trace("Delete configuration by name " + name);
        this.couchbaseConfService.remove(name);
        return Response.noContent().build();
    }

    @PATCH
    @Path(ApiConstants.NAME_PARAM_PATH)
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    @ProtectedApi(scopes = {WRITE_ACCESS})
    public Response patch(@PathParam(ApiConstants.NAME) String name, @NotNull String requestString) throws Exception {
        CouchbaseConnectionConfiguration conf = findByName(name);
        logger.info("Patch configuration by name " + name);
        conf = Jackson.applyPatch(requestString, conf);
        couchbaseConfService.save(conf);
        return Response.ok(conf).build();
    }

    @POST
    @Path(ApiConstants.TEST)
    @ProtectedApi(scopes = {READ_ACCESS})
    public Response test(@Valid @NotNull CouchbaseConnectionConfiguration conf) {
        Properties properties = new Properties();

        properties.put("couchbase.servers", Joiner.on(",").join(conf.getServers()));
        properties.put("couchbase.auth.userName", conf.getUserName());
        properties.put("couchbase.auth.userPassword", conf.getUserPassword());
        properties.put("couchbase.auth.buckets", Joiner.on(",").join(conf.getBuckets()));
        properties.put("couchbase.bucket.default", conf.getDefaultBucket());
        properties.put("couchbase.password.encryption.method", conf.getPasswordEncryptionMethod());

        CouchbaseConnectionProvider connectionProvider = new CouchbaseConnectionProvider(properties, DefaultCouchbaseEnvironment.create());
        return Response.ok(connectionProvider.isConnected()).build();
    }

    private CouchbaseConnectionConfiguration findByName(String name) {
        final Optional<CouchbaseConnectionConfiguration> optional = this.couchbaseConfService.findByName(name);
        if (optional.isEmpty()) {
            logger.trace("Could not find configuration by name '" + name + "'");
            throw new NotFoundException(getNotFoundError("Configuration - '" + name + "'"));
        }
        return optional.get();
    }
}
