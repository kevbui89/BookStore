package com.g3bookstore.controllers;

import com.g3bookstore.entities.Client;
import java.util.List;
import java.util.ArrayList;
import com.g3bookstore.entities.Province;
import com.g3bookstore.entities.Tax;
import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import javax.annotation.PostConstruct;
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
 * @author Alessandro, Denis, Kevin, Werner
 */
@Named("Province")
@RequestScoped
public class ProvinceJpaController {

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    @Resource
    private UserTransaction utx = null;

    /**
     * Method which search for a province based on id.
     *
     * @param id
     * @return The Province object
     */
    public Province findProvince(Integer id) {
        return em.find(Province.class, id);
    }

    public void create(Province province) throws RollbackFailureException, Exception {
        if (province.getTaxList() == null) {
            province.setTaxList(new ArrayList<Tax>());
        }
        if (province.getClientList() == null) {
            province.setClientList(new ArrayList<Client>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            List<Tax> attachedTaxList = new ArrayList<Tax>();
            for (Tax taxListTaxToAttach : province.getTaxList()) {
                taxListTaxToAttach = em.getReference(taxListTaxToAttach.getClass(), taxListTaxToAttach.getTaxId());
                attachedTaxList.add(taxListTaxToAttach);
            }
            province.setTaxList(attachedTaxList);
            List<Client> attachedClientList = new ArrayList<Client>();
            for (Client clientListClientToAttach : province.getClientList()) {
                clientListClientToAttach = em.getReference(clientListClientToAttach.getClass(), clientListClientToAttach.getClientId());
                attachedClientList.add(clientListClientToAttach);
            }
            province.setClientList(attachedClientList);
            em.persist(province);
            for (Tax taxListTax : province.getTaxList()) {
                Province oldProvinceIdOfTaxListTax = taxListTax.getProvinceId();
                taxListTax.setProvinceId(province);
                taxListTax = em.merge(taxListTax);
                if (oldProvinceIdOfTaxListTax != null) {
                    oldProvinceIdOfTaxListTax.getTaxList().remove(taxListTax);
                    oldProvinceIdOfTaxListTax = em.merge(oldProvinceIdOfTaxListTax);
                }
            }
            for (Client clientListClient : province.getClientList()) {
                Province oldProvinceIdOfClientListClient = clientListClient.getProvinceId();
                clientListClient.setProvinceId(province);
                clientListClient = em.merge(clientListClient);
                if (oldProvinceIdOfClientListClient != null) {
                    oldProvinceIdOfClientListClient.getClientList().remove(clientListClient);
                    oldProvinceIdOfClientListClient = em.merge(oldProvinceIdOfClientListClient);
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

    public void edit(Province province) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Province persistentProvince = em.find(Province.class, province.getProvinceId());
            List<Tax> taxListOld = persistentProvince.getTaxList();
            List<Tax> taxListNew = province.getTaxList();
            List<Client> clientListOld = persistentProvince.getClientList();
            List<Client> clientListNew = province.getClientList();
            List<String> illegalOrphanMessages = null;
            for (Tax taxListOldTax : taxListOld) {
                if (!taxListNew.contains(taxListOldTax)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tax " + taxListOldTax + " since its provinceId field is not nullable.");
                }
            }
            for (Client clientListOldClient : clientListOld) {
                if (!clientListNew.contains(clientListOldClient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Client " + clientListOldClient + " since its provinceId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Tax> attachedTaxListNew = new ArrayList<Tax>();
            for (Tax taxListNewTaxToAttach : taxListNew) {
                taxListNewTaxToAttach = em.getReference(taxListNewTaxToAttach.getClass(), taxListNewTaxToAttach.getTaxId());
                attachedTaxListNew.add(taxListNewTaxToAttach);
            }
            taxListNew = attachedTaxListNew;
            province.setTaxList(taxListNew);
            List<Client> attachedClientListNew = new ArrayList<Client>();
            for (Client clientListNewClientToAttach : clientListNew) {
                clientListNewClientToAttach = em.getReference(clientListNewClientToAttach.getClass(), clientListNewClientToAttach.getClientId());
                attachedClientListNew.add(clientListNewClientToAttach);
            }
            clientListNew = attachedClientListNew;
            province.setClientList(clientListNew);
            province = em.merge(province);
            for (Tax taxListNewTax : taxListNew) {
                if (!taxListOld.contains(taxListNewTax)) {
                    Province oldProvinceIdOfTaxListNewTax = taxListNewTax.getProvinceId();
                    taxListNewTax.setProvinceId(province);
                    taxListNewTax = em.merge(taxListNewTax);
                    if (oldProvinceIdOfTaxListNewTax != null && !oldProvinceIdOfTaxListNewTax.equals(province)) {
                        oldProvinceIdOfTaxListNewTax.getTaxList().remove(taxListNewTax);
                        oldProvinceIdOfTaxListNewTax = em.merge(oldProvinceIdOfTaxListNewTax);
                    }
                }
            }
            for (Client clientListNewClient : clientListNew) {
                if (!clientListOld.contains(clientListNewClient)) {
                    Province oldProvinceIdOfClientListNewClient = clientListNewClient.getProvinceId();
                    clientListNewClient.setProvinceId(province);
                    clientListNewClient = em.merge(clientListNewClient);
                    if (oldProvinceIdOfClientListNewClient != null && !oldProvinceIdOfClientListNewClient.equals(province)) {
                        oldProvinceIdOfClientListNewClient.getClientList().remove(clientListNewClient);
                        oldProvinceIdOfClientListNewClient = em.merge(oldProvinceIdOfClientListNewClient);
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
                Integer id = province.getProvinceId();
                if (findProvince(id) == null) {
                    throw new NonexistentEntityException("The province with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Province province;
            try {
                province = em.getReference(Province.class, id);
                province.getProvinceId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The province with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tax> taxListOrphanCheck = province.getTaxList();
            for (Tax taxListOrphanCheckTax : taxListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Province (" + province + ") cannot be destroyed since the Tax " + taxListOrphanCheckTax + " in its taxList field has a non-nullable provinceId field.");
            }
            List<Client> clientListOrphanCheck = province.getClientList();
            for (Client clientListOrphanCheckClient : clientListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Province (" + province + ") cannot be destroyed since the Client " + clientListOrphanCheckClient + " in its clientList field has a non-nullable provinceId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(province);
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

    public List<Province> findProvinceEntities() {
        return findProvinceEntities(true, -1, -1);
    }

    public List<Province> findProvinceEntities(int maxResults, int firstResult) {
        return findProvinceEntities(false, maxResults, firstResult);
    }

    private List<Province> findProvinceEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Province.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public int getProvinceCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Province> rt = cq.from(Province.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
