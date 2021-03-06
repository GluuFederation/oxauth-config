/**
 *
 */
package org.gluu.configapi.service;

import org.gluu.oxauth.model.common.SubjectType;
import org.gluu.oxauth.model.crypto.signature.SignatureAlgorithm;
import org.gluu.oxauth.model.register.ApplicationType;
import org.gluu.oxauth.model.registration.Client;
import org.gluu.oxauth.service.OrganizationService;
import org.gluu.oxauth.service.common.InumService;
import org.gluu.oxauth.util.OxConstants;
import org.gluu.persist.PersistenceEntryManager;
import org.gluu.search.filter.Filter;
import org.gluu.util.StringHelper;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mougang T.Gasmyr
 *
 */
@ApplicationScoped
public class ClientService implements Serializable {

    private static final long serialVersionUID = 7912416439116338984L;

    @Inject
    private PersistenceEntryManager persistenceEntryManager;

    @Inject
    private Logger logger;

    @Inject
    private OrganizationService organizationService;

    @Inject
    private InumService inumService;

    public boolean contains(String clientDn) {
        return persistenceEntryManager.contains(clientDn, Client.class);
    }

    public void addClient(Client client) {
        persistenceEntryManager.persist(client);
    }

    public void removeClient(Client client) {
        persistenceEntryManager.removeRecursively(client.getDn());
    }

    public void updateClient(Client client) {
        persistenceEntryManager.merge(client);
    }

    public Client getClientByInum(String inum) {
        Client result = null;
        try {
            result = persistenceEntryManager.find(Client.class, getDnForClient(inum));
        } catch (Exception ex) {
            logger.error("Failed to load client entry", ex);
        }
        return result;
    }

    public List<Client> searchClients(String pattern, int sizeLimit) {
        String[] targetArray = new String[]{pattern};
        Filter displayNameFilter = Filter.createSubstringFilter(OxConstants.displayName, null, targetArray, null);
        Filter descriptionFilter = Filter.createSubstringFilter(OxConstants.description, null, targetArray, null);
        Filter inumFilter = Filter.createSubstringFilter(OxConstants.inum, null, targetArray, null);
        Filter searchFilter = Filter.createORFilter(displayNameFilter, descriptionFilter, inumFilter);
        return persistenceEntryManager.findEntries(getDnForClient(null), Client.class, searchFilter, sizeLimit);
    }

    public List<Client> getAllClients(int sizeLimit) {
        return persistenceEntryManager.findEntries(getDnForClient(null), Client.class, null, sizeLimit);
    }

    public List<Client> getAllClients() {
        return persistenceEntryManager.findEntries(getDnForClient(null), Client.class, null);
    }

    public Client getClientByDn(String Dn) {
        try {
            return persistenceEntryManager.find(Client.class, Dn);
        } catch (Exception e) {
            logger.warn("", e);
            return null;
        }
    }

    public ApplicationType[] getApplicationType() {
        return ApplicationType.values();
    }

    public SubjectType[] getSubjectTypes() {
        return SubjectType.values();
    }

    public SignatureAlgorithm[] getSignatureAlgorithms() {
        return SignatureAlgorithm.values();
    }

    public String getDnForClient(String inum) {
        String orgDn = organizationService.getDnForOrganization();
        if (StringHelper.isEmpty(inum)) {
            return String.format("ou=clients,%s", orgDn);
        }
        return String.format("inum=%s,ou=clients,%s", inum, orgDn);
    }

    public String generateInumForNewClient() {
        String newInum = null;
        String newDn = null;
        int trycount = 0;
        do {
            if (trycount < InumService.MAX_IDGEN_TRY_COUNT) {
                newInum = inumService.generateId("client");
                trycount++;
            } else {
                newInum = inumService.generateDefaultId();
            }
            newDn = getDnForClient(newInum);
        } while (persistenceEntryManager.contains(newDn, Client.class));
        return newInum;
    }
}
