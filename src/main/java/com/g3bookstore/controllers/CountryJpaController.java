package com.g3bookstore.controllers;

import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Country;
import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 * Generated controller updated to use CDI
 *
 * @author Alessandro Ciotola, Kevin Bui
 */
@Named("Country")
@RequestScoped
public class CountryJpaController {
    
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    /**
     * Method which will find a country by id.
     *
     * @param id
     * @return
     */
    public Country findCountry(Integer id) {
        return em.find(Country.class, id);
    }

    public void create(Country country) throws RollbackFailureException, Exception {
        if (country.getClientList() == null) {
            country.setClientList(new ArrayList<Client>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            List<Client> attachedClientList = new ArrayList<Client>();
            for (Client clientListClientToAttach : country.getClientList()) {
                clientListClientToAttach = em.getReference(clientListClientToAttach.getClass(), clientListClientToAttach.getClientId());
                attachedClientList.add(clientListClientToAttach);
            }
            country.setClientList(attachedClientList);
            em.persist(country);
            for (Client clientListClient : country.getClientList()) {
                Country oldCountryIdOfClientListClient = clientListClient.getCountryId();
                clientListClient.setCountryId(country);
                clientListClient = em.merge(clientListClient);
                if (oldCountryIdOfClientListClient != null) {
                    oldCountryIdOfClientListClient.getClientList().remove(clientListClient);
                    oldCountryIdOfClientListClient = em.merge(oldCountryIdOfClientListClient);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public void edit(Country country) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Country persistentCountry = em.find(Country.class, country.getCountryId());
            List<Client> clientListOld = persistentCountry.getClientList();
            List<Client> clientListNew = country.getClientList();
            List<String> illegalOrphanMessages = null;
            for (Client clientListOldClient : clientListOld) {
                if (!clientListNew.contains(clientListOldClient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Client " + clientListOldClient + " since its countryId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Client> attachedClientListNew = new ArrayList<Client>();
            for (Client clientListNewClientToAttach : clientListNew) {
                clientListNewClientToAttach = em.getReference(clientListNewClientToAttach.getClass(), clientListNewClientToAttach.getClientId());
                attachedClientListNew.add(clientListNewClientToAttach);
            }
            clientListNew = attachedClientListNew;
            country.setClientList(clientListNew);
            country = em.merge(country);
            for (Client clientListNewClient : clientListNew) {
                if (!clientListOld.contains(clientListNewClient)) {
                    Country oldCountryIdOfClientListNewClient = clientListNewClient.getCountryId();
                    clientListNewClient.setCountryId(country);
                    clientListNewClient = em.merge(clientListNewClient);
                    if (oldCountryIdOfClientListNewClient != null && !oldCountryIdOfClientListNewClient.equals(country)) {
                        oldCountryIdOfClientListNewClient.getClientList().remove(clientListNewClient);
                        oldCountryIdOfClientListNewClient = em.merge(oldCountryIdOfClientListNewClient);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = country.getCountryId();
                if (findCountry(id) == null) {
                    throw new NonexistentEntityException("The country with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Country country;
            try {
                country = em.getReference(Country.class, id);
                country.getCountryId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The country with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Client> clientListOrphanCheck = country.getClientList();
            for (Client clientListOrphanCheckClient : clientListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Country (" + country + ") cannot be destroyed since the Client " + clientListOrphanCheckClient + " in its clientList field has a non-nullable countryId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(country);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public List<Country> findCountryEntities() {
        return findCountryEntities(true, -1, -1);
    }

    public List<Country> findCountryEntities(int maxResults, int firstResult) {
        return findCountryEntities(false, maxResults, firstResult);
    }

    private List<Country> findCountryEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Country.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();

    }

    public int getCountryCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Country> rt = cq.from(Country.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
