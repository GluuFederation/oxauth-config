package org.gluu.configapi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.gluu.model.GluuStatus;
import org.gluu.model.SmtpConfiguration;
import org.gluu.persist.annotation.*;
import org.gluu.persist.model.base.InumEntry;
import org.gluu.service.cache.CacheConfiguration;
import org.gluu.service.document.store.conf.DocumentStoreConfiguration;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 */
@DataEntry
@ObjectClass(value = "gluuConfiguration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GluuConfiguration extends InumEntry implements Serializable {

    @AttributeName
    private String description;

    @AttributeName
    private String displayName;

    @AttributeName(name = "gluuHostname", updateOnly = true)
    private String hostname;

    @AttributeName(name = "gluuLastUpdate", updateOnly = true)
    private Date lastUpdate;

    @AttributeName(name = "gluuConfigurationPollingInterval")
    private String pollingInterval;

    @AttributeName(name = "gluuStatus", updateOnly = true)
    private GluuStatus status;

    @AttributeName(name = "userPassword", ignoreDuringRead = true)
    private String userPassword;

    @AttributeName(name = "gluuHTTPstatus", updateOnly = true)
    private String gluuHttpStatus;

    @AttributeName(name = "gluuDSstatus", updateOnly = true)
    private String gluuDSStatus;

    @AttributeName(name = "gluuVDSstatus", updateOnly = true)
    private String gluuVDSStatus;

    @AttributeName(name = "gluuSPTR")
    private String gluuSPTR;

    @AttributeName(name = "gluuSslExpiry", updateOnly = true)
    private String sslExpiry;

    @AttributeName(name = "gluuOrgProfileMgt")
    private boolean profileManagment;

    @AttributeName(name = "gluuManageIdentityPermission")
    private boolean manageIdentityPermission;

    @AttributeName(name = "gluuVdsCacheRefreshEnabled")
    private boolean vdsCacheRefreshEnabled;

    @AttributeName(name = "oxTrustCacheRefreshServerIpAddress")
    private String cacheRefreshServerIpAddress;

    @AttributeName(name = "gluuVdsCacheRefreshPollingInterval")
    private String vdsCacheRefreshPollingInterval;

    @AttributeName(name = "gluuVdsCacheRefreshLastUpdate")
    private Date vdsCacheRefreshLastUpdate;

    @AttributeName(name = "gluuVdsCacheRefreshLastUpdateCount")
    private String vdsCacheRefreshLastUpdateCount;

    @AttributeName(name = "gluuVdsCacheRefreshProblemCount")
    private String vdsCacheRefreshProblemCount;

    @AttributeName(name = "gluuScimEnabled")
    private boolean scimEnabled;

    @AttributeName(name = "gluuPassportEnabled")
    private boolean passportEnabled;

    @AttributeName(name = "gluuRadiusEnabled")
    private boolean radiusEnabled;

    @AttributeName(name = "gluuSamlEnabled")
    private boolean samlEnabled;

    @AttributeName(name = "oxTrustEmail")
    private String contactEmail;

    @AttributeName(name = "oxSmtpConfiguration")
    @JsonObject
    private SmtpConfiguration smtpConfiguration;

    @AttributeName(name = "gluuConfigurationDnsServer")
    private String configurationDnsServer;

    @AttributeName(name = "gluuMaxLogSize")
    private int maxLogSize;

    @AttributeName(name = "oxAuthenticationMode")
    private String authenticationMode;

    @AttributeName(name = "oxTrustAuthenticationMode")
    private String oxTrustAuthenticationMode;

    @AttributeName(name = "oxLogConfigLocation")
    private String oxLogConfigLocation;

    @AttributeName(name = "passwordResetAllowed")
    private boolean passwordResetAllowed;

    @AttributeName(name = "oxCacheConfiguration")
    @JsonObject
    private CacheConfiguration cacheConfiguration;

    @AttributeName(name = "oxDocumentStoreConfiguration")
    @JsonObject
    private DocumentStoreConfiguration documentStoreConfiguration;

    @CustomObjectClass
    private String[] customObjectClasses;

    public final SmtpConfiguration getSmtpConfiguration() {
        return smtpConfiguration;
    }

    public final void setSmtpConfiguration(SmtpConfiguration smtpConfiguration) {
        this.smtpConfiguration = smtpConfiguration;
    }

    public String getConfigurationDnsServer() {
        return configurationDnsServer;
    }

    public void setConfigurationDnsServer(String configurationDnsServer) {
        this.configurationDnsServer = configurationDnsServer;
    }

    public String getAuthenticationMode() {
        return authenticationMode;
    }

    public void setAuthenticationMode(String authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public String getOxTrustAuthenticationMode() {
        return oxTrustAuthenticationMode;
    }

    public void setOxTrustAuthenticationMode(String oxTrustAuthenticationMode) {
        this.oxTrustAuthenticationMode = oxTrustAuthenticationMode;
    }

    public String getCacheRefreshServerIpAddress() {
        return cacheRefreshServerIpAddress;
    }

    public void setCacheRefreshServerIpAddress(String cacheRefreshServerIpAddress) {
        this.cacheRefreshServerIpAddress = cacheRefreshServerIpAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGluuDSStatus() {
        return gluuDSStatus;
    }

    public void setGluuDSStatus(String gluuDSStatus) {
        this.gluuDSStatus = gluuDSStatus;
    }

    public String getGluuHttpStatus() {
        return gluuHttpStatus;
    }

    public void setGluuHttpStatus(String gluuHttpStatus) {
        this.gluuHttpStatus = gluuHttpStatus;
    }

    public String getGluuVDSStatus() {
        return gluuVDSStatus;
    }

    public void setGluuVDSStatus(String gluuVDSStatus) {
        this.gluuVDSStatus = gluuVDSStatus;
    }

    public String getGluuSPTR() {
        return gluuSPTR;
    }

    public void setGluuSPTR(String gluuSPTR) {
        this.gluuSPTR = gluuSPTR;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isManageIdentityPermission() {
        return manageIdentityPermission;
    }

    public void setManageIdentityPermission(boolean manageIdentityPermission) {
        this.manageIdentityPermission = manageIdentityPermission;
    }

    public int getMaxLogSize() {
        return maxLogSize;
    }

    public void setMaxLogSize(int maxLogSize) {
        this.maxLogSize = maxLogSize;
    }

    public String getOxLogConfigLocation() {
        return oxLogConfigLocation;
    }

    public void setOxLogConfigLocation(String oxLogConfigLocation) {
        this.oxLogConfigLocation = oxLogConfigLocation;
    }

    public boolean isPasswordResetAllowed() {
        return passwordResetAllowed;
    }

    public void setPasswordResetAllowed(boolean passwordResetAllowed) {
        this.passwordResetAllowed = passwordResetAllowed;
    }

    public String getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(String pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public String getContactEmail() {
        if (this.contactEmail == null || this.contactEmail.isEmpty()) {
            return "example@orgname.com";
        }
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public boolean isProfileManagment() {
        return profileManagment;
    }

    public void setProfileManagment(boolean profileManagment) {
        this.profileManagment = profileManagment;
    }

    public boolean isScimEnabled() {
        return scimEnabled;
    }

    public void setScimEnabled(boolean scimEnabled) {
        this.scimEnabled = scimEnabled;
    }

    public String getSslExpiry() {
        return sslExpiry;
    }

    public void setSslExpiry(String sslExpiry) {
        this.sslExpiry = sslExpiry;
    }

    public GluuStatus getStatus() {
        return status;
    }

    public void setStatus(GluuStatus status) {
        this.status = status;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isVdsCacheRefreshEnabled() {
        return vdsCacheRefreshEnabled;
    }

    public void setVdsCacheRefreshEnabled(boolean vdsCacheRefreshEnabled) {
        this.vdsCacheRefreshEnabled = vdsCacheRefreshEnabled;
    }

    public Date getVdsCacheRefreshLastUpdate() {
        return vdsCacheRefreshLastUpdate;
    }

    public void setVdsCacheRefreshLastUpdate(Date vdsCacheRefreshLastUpdate) {
        this.vdsCacheRefreshLastUpdate = vdsCacheRefreshLastUpdate;
    }

    public String getVdsCacheRefreshLastUpdateCount() {
        return vdsCacheRefreshLastUpdateCount;
    }

    public void setVdsCacheRefreshLastUpdateCount(String vdsCacheRefreshLastUpdateCount) {
        this.vdsCacheRefreshLastUpdateCount = vdsCacheRefreshLastUpdateCount;
    }

    public String getVdsCacheRefreshPollingInterval() {
        return vdsCacheRefreshPollingInterval;
    }

    public void setVdsCacheRefreshPollingInterval(String vdsCacheRefreshPollingInterval) {
        this.vdsCacheRefreshPollingInterval = vdsCacheRefreshPollingInterval;
    }

    public String getVdsCacheRefreshProblemCount() {
        return vdsCacheRefreshProblemCount;
    }

    public void setVdsCacheRefreshProblemCount(String vdsCacheRefreshProblemCount) {
        this.vdsCacheRefreshProblemCount = vdsCacheRefreshProblemCount;
    }

    public boolean isPassportEnabled() {
        return passportEnabled;
    }

    public void setPassportEnabled(boolean passportEnabled) {
        this.passportEnabled = passportEnabled;
    }

    public boolean isRadiusEnabled() {
        return radiusEnabled;
    }

    public void setRadiusEnabled(boolean radiusEnabled) {
        this.radiusEnabled = radiusEnabled;
    }

    public boolean isSamlEnabled() {
        return this.samlEnabled;
    }

    public void setSamlEnabled(boolean samlEnabled) {
        this.samlEnabled = samlEnabled;
    }

    public CacheConfiguration getCacheConfiguration() {
        return cacheConfiguration;
    }

    public void setCacheConfiguration(CacheConfiguration cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }

    public DocumentStoreConfiguration getDocumentStoreConfiguration() {
        return documentStoreConfiguration;
    }

    public void setDocumentStoreConfiguration(DocumentStoreConfiguration documentStoreConfiguration) {
        this.documentStoreConfiguration = documentStoreConfiguration;
    }

    public String[] getCustomObjectClasses() {
        return customObjectClasses;
    }

    public void setCustomObjectClasses(String[] customObjectClasses) {
        this.customObjectClasses = customObjectClasses;
    }


}
