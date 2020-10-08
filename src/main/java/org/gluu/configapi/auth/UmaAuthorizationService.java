package org.gluu.configapi.auth;

import org.gluu.oxauth.client.uma.wrapper.UmaClient;
import org.gluu.oxauth.client.uma.UmaRptIntrospectionService;
import org.gluu.oxauth.model.uma.RptIntrospectionResponse;
import org.gluu.oxauth.model.uma.UmaMetadata;
import org.gluu.oxauth.model.uma.wrapper.Token;
import org.gluu.oxauth.model.uma.persistence.UmaResource;
import org.gluu.oxauth.service.common.EncryptionService;
import org.gluu.configapi.configuration.ConfigurationFactory;
import org.gluu.configapi.service.UmaService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.gluu.util.Pair;
import org.gluu.util.StringHelper;
import org.gluu.util.security.StringEncrypter.EncryptionException;

@ApplicationScoped
@Named("umaAuthorizationService")
public class UmaAuthorizationService extends AuthorizationService implements Serializable {

    private static final long serialVersionUID = 1L;
    private Token umaPat;
    private long umaPatAccessTokenExpiration = 0l; // When the "accessToken" will expire;
    private final ReentrantLock lock = new ReentrantLock();

    @Inject
    private Logger logger;

    @Inject
    private EncryptionService encryptionService;

    @Inject
    ConfigurationFactory configurationFactory;

    @Inject
    UmaResource configApiResource;

    @Inject
    UmaService umaService;

    @Inject
    private UmaMetadata umaMetadata;

    public void validateAuthorization(String token, ResourceInfo resourceInfo) throws Exception {
        System.out.println(" UmaAuthorizationService::validateAuthorization() - token = "+token+" , resourceInfo.getClass().getName() = "+resourceInfo.getClass().getName()+"\n");
        if (StringUtils.isBlank(token)) {
            logger.info("Token is blank");
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        List<String> resourceScopes = getRequestedScopes(resourceInfo);
        System.out.println(" UmaAuthorizationService::validateAuthorization() - resourceScopes = "+resourceScopes+"\n");
        Token patToken = null;
        try {
            patToken = getPatToken();
        } catch (Exception ex) {
            logger.error("Failed to obtain PAT token",ex);
            throw new WebApplicationException("Failed to obtain PAT token",ex);
        }

        Pair<Boolean, Response> rptTokenValidationResult;

        logger.debug("this.umaService.getUmaMetadata() = " + this.umaService.getUmaMetadata());
        System.out.println(" UmaAuthorizationService::validateAuthorization() - this.umaService.getUmaMetadata() = "+this.umaService.getUmaMetadata()+" , this.umaService.getUmaMetadata().getIntrospectionEndpoint() = "+this.umaService.getUmaMetadata().getIntrospectionEndpoint()+"\n");
        // RptIntrospectionResponse rptIntrospectionResponse = this.umaService.ge
        /*if (!resourceScopes.isEmpty()) {
            rptTokenValidationResult = this.umaService.validateRptToken(patToken, token,
                    getUmaResourceId(), resourceScopes);
        } else {
            rptTokenValidationResult = this.umaService.validateRptToken(patToken, token,
                    getUmaResourceId(), getUmaScope());
        }

        if (rptTokenValidationResult.getFirst()) {
            if (rptTokenValidationResult.getSecond() != null) {
                return ;
            }
        } else {
            logger.error("Invalid GAT/RPT token");
            throw new WebApplicationException("Invalid GAT/RPT token");
        }
*/

    }

    public String getUmaResourceId() {
        return configApiResource.getId();
    }
    
    public String getUmaScope() {
        String scopes = new String();
        List<String> scopeList = configApiResource.getScopes();
        if(scopeList!=null && !scopeList.isEmpty()) {
            scopes = scopeList.stream()
            .map(s -> s.concat(" "))
            .collect(Collectors.joining());
            System.out.println(scopes); 
        }
    
        return scopes.trim();
    }

    private String getClientId() {
        return configurationFactory.getApiClientId(); // TBD
    }

    private String getClientKeyId() {
        return null; // TBD
    }

    public Token getPatToken() throws Exception {
        if (isValidPatToken(this.umaPat, this.umaPatAccessTokenExpiration)) {
            return this.umaPat;
        }

        lock.lock();
        try {
            if (isValidPatToken(this.umaPat, this.umaPatAccessTokenExpiration)) {
                return this.umaPat;
            }

            retrievePatToken();
        } finally {
            lock.unlock();
        }

        return this.umaPat;
    }

    private boolean isEnabledUmaAuthentication() {
        return (this.umaMetadata != null) && isExistPatToken();
    }

    private boolean isExistPatToken() {
        try {
            return getPatToken() != null;
        } catch (Exception ex) {
            logger.error("Failed to check UMA PAT token status", ex);
        }

        return false;
    }

    private String getIssuer() {
        if (umaMetadata == null) {
            return "";
        }
        return umaMetadata.getIssuer();
    }

    private void retrievePatToken() throws Exception {
        this.umaPat = null;
        if (umaMetadata == null) {
            return;
        }
        System.out.println("\n\n getClientKeyStoreFile() = " + getClientKeyStoreFile()
                + " , getClientKeyStorePassword() = " + getClientKeyStorePassword() + " , getClientId() ="
                + getClientId() + " , getClientKeyId() = " + getClientKeyId() + "\n\n");
        String umaClientKeyStoreFile = getClientKeyStoreFile();
        String umaClientKeyStorePassword = getClientKeyStorePassword();
        if (StringHelper.isEmpty(umaClientKeyStoreFile) || StringHelper.isEmpty(umaClientKeyStorePassword)) {
            throw new Exception("UMA JKS keystore path or password is empty");
        }

     /*   if (umaClientKeyStorePassword != null) {
            try {
                umaClientKeyStorePassword = encryptionService.decrypt(umaClientKeyStorePassword);
            } catch (EncryptionException ex) {
                logger.error("Failed to decrypt UmaClientKeyStorePassword password", ex);
            }
        }*/

        try {
            this.umaPat = UmaClient.requestPat(umaMetadata.getTokenEndpoint(), umaClientKeyStoreFile,
                    umaClientKeyStorePassword, getClientId(), getClientKeyId());
            if (this.umaPat == null) {
                this.umaPatAccessTokenExpiration = 0l;
            } else {
                this.umaPatAccessTokenExpiration = computeAccessTokenExpirationTime(this.umaPat.getExpiresIn());
            }
        } catch (Exception ex) {
            throw new Exception("Failed to obtain valid UMA PAT token", ex);
        }

        if ((this.umaPat == null) || (this.umaPat.getAccessToken() == null)) {
            throw new Exception("Failed to obtain valid UMA PAT token");
        }
    }

    private boolean isValidPatToken(Token validatePatToken, long validatePatTokenExpiration) {
        final long now = System.currentTimeMillis();

        // Get new access token only if is the previous one is missing or expired
        return !((validatePatToken == null) || (validatePatToken.getAccessToken() == null)
                || (validatePatTokenExpiration <= now));
    }

}