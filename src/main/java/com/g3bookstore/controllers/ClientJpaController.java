package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Review;
import com.g3bookstore.entities.Province;
import com.g3bookstore.entities.Country;
import com.g3bookstore.entities.MasterInvoice;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Generated controller updated to use CDI
 *
 * @author Alessandro Ciotola, Kevin Bui
 */
@Named("Client")
@RequestScoped
public class ClientJpaController implements Serializable 
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(Client client) throws RollbackFailureException, Exception {
        if (client.getMasterInvoiceList() == null) {
            client.setMasterInvoiceList(new ArrayList<MasterInvoice>());
        }
        if (client.getReviewList() == null) {
            client.setReviewList(new ArrayList<Review>());
        }
        try {
            utx.begin();
            Province provinceId = client.getProvinceId();
            if (provinceId != null) {
                provinceId = em.getReference(provinceId.getClass(), provinceId.getProvinceId());
                client.setProvinceId(provinceId);
            }
            Country countryId = client.getCountryId();
            if (countryId != null) {
                countryId = em.getReference(countryId.getClass(), countryId.getCountryId());
                client.setCountryId(countryId);
            }
            List<MasterInvoice> attachedMasterInvoiceList = new ArrayList<MasterInvoice>();
            for (MasterInvoice masterInvoiceListMasterInvoiceToAttach : client.getMasterInvoiceList()) {
                masterInvoiceListMasterInvoiceToAttach = em.getReference(masterInvoiceListMasterInvoiceToAttach.getClass(), masterInvoiceListMasterInvoiceToAttach.getInvoiceId());
                attachedMasterInvoiceList.add(masterInvoiceListMasterInvoiceToAttach);
            }
            client.setMasterInvoiceList(attachedMasterInvoiceList);
            List<Review> attachedReviewList = new ArrayList<Review>();
            for (Review reviewListReviewToAttach : client.getReviewList()) {
                reviewListReviewToAttach = em.getReference(reviewListReviewToAttach.getClass(), reviewListReviewToAttach.getReviewId());
                attachedReviewList.add(reviewListReviewToAttach);
            }
            client.setReviewList(attachedReviewList);
            em.persist(client);
            if (provinceId != null) {
                provinceId.getClientList().add(client);
                provinceId = em.merge(provinceId);
            }
            if (countryId != null) {
                countryId.getClientList().add(client);
                countryId = em.merge(countryId);
            }
            for (MasterInvoice masterInvoiceListMasterInvoice : client.getMasterInvoiceList()) {
                Client oldClientIdOfMasterInvoiceListMasterInvoice = masterInvoiceListMasterInvoice.getClientId();
                masterInvoiceListMasterInvoice.setClientId(client);
                masterInvoiceListMasterInvoice = em.merge(masterInvoiceListMasterInvoice);
                if (oldClientIdOfMasterInvoiceListMasterInvoice != null) {
                    oldClientIdOfMasterInvoiceListMasterInvoice.getMasterInvoiceList().remove(masterInvoiceListMasterInvoice);
                    oldClientIdOfMasterInvoiceListMasterInvoice = em.merge(oldClientIdOfMasterInvoiceListMasterInvoice);
                }
            }
            for (Review reviewListReview : client.getReviewList()) {
                Client oldClientIdOfReviewListReview = reviewListReview.getClientId();
                reviewListReview.setClientId(client);
                reviewListReview = em.merge(reviewListReview);
                if (oldClientIdOfReviewListReview != null) {
                    oldClientIdOfReviewListReview.getReviewList().remove(reviewListReview);
                    oldClientIdOfReviewListReview = em.merge(oldClientIdOfReviewListReview);
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

    public void edit(Client client) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Client persistentClient = em.find(Client.class, client.getClientId());
            Province provinceIdOld = persistentClient.getProvinceId();
            Province provinceIdNew = client.getProvinceId();
            Country countryIdOld = persistentClient.getCountryId();
            Country countryIdNew = client.getCountryId();
            List<MasterInvoice> masterInvoiceListOld = persistentClient.getMasterInvoiceList();
            List<MasterInvoice> masterInvoiceListNew = client.getMasterInvoiceList();
            List<Review> reviewListOld = persistentClient.getReviewList();
            List<Review> reviewListNew = client.getReviewList();
            List<String> illegalOrphanMessages = null;
            for (MasterInvoice masterInvoiceListOldMasterInvoice : masterInvoiceListOld) {
                if (!masterInvoiceListNew.contains(masterInvoiceListOldMasterInvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MasterInvoice " + masterInvoiceListOldMasterInvoice + " since its clientId field is not nullable.");
                }
            }
            for (Review reviewListOldReview : reviewListOld) {
                if (!reviewListNew.contains(reviewListOldReview)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Review " + reviewListOldReview + " since its clientId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (provinceIdNew != null) {
                provinceIdNew = em.getReference(provinceIdNew.getClass(), provinceIdNew.getProvinceId());
                client.setProvinceId(provinceIdNew);
            }
            if (countryIdNew != null) {
                countryIdNew = em.getReference(countryIdNew.getClass(), countryIdNew.getCountryId());
                client.setCountryId(countryIdNew);
            }
            List<MasterInvoice> attachedMasterInvoiceListNew = new ArrayList<MasterInvoice>();
            for (MasterInvoice masterInvoiceListNewMasterInvoiceToAttach : masterInvoiceListNew) {
                masterInvoiceListNewMasterInvoiceToAttach = em.getReference(masterInvoiceListNewMasterInvoiceToAttach.getClass(), masterInvoiceListNewMasterInvoiceToAttach.getInvoiceId());
                attachedMasterInvoiceListNew.add(masterInvoiceListNewMasterInvoiceToAttach);
            }
            masterInvoiceListNew = attachedMasterInvoiceListNew;
            client.setMasterInvoiceList(masterInvoiceListNew);
            List<Review> attachedReviewListNew = new ArrayList<Review>();
            for (Review reviewListNewReviewToAttach : reviewListNew) {
                reviewListNewReviewToAttach = em.getReference(reviewListNewReviewToAttach.getClass(), reviewListNewReviewToAttach.getReviewId());
                attachedReviewListNew.add(reviewListNewReviewToAttach);
            }
            reviewListNew = attachedReviewListNew;
            client.setReviewList(reviewListNew);
            client = em.merge(client);
            if (provinceIdOld != null && !provinceIdOld.equals(provinceIdNew)) {
                provinceIdOld.getClientList().remove(client);
                provinceIdOld = em.merge(provinceIdOld);
            }
            if (provinceIdNew != null && !provinceIdNew.equals(provinceIdOld)) {
                provinceIdNew.getClientList().add(client);
                provinceIdNew = em.merge(provinceIdNew);
            }
            if (countryIdOld != null && !countryIdOld.equals(countryIdNew)) {
                countryIdOld.getClientList().remove(client);
                countryIdOld = em.merge(countryIdOld);
            }
            if (countryIdNew != null && !countryIdNew.equals(countryIdOld)) {
                countryIdNew.getClientList().add(client);
                countryIdNew = em.merge(countryIdNew);
            }
            for (MasterInvoice masterInvoiceListNewMasterInvoice : masterInvoiceListNew) {
                if (!masterInvoiceListOld.contains(masterInvoiceListNewMasterInvoice)) {
                    Client oldClientIdOfMasterInvoiceListNewMasterInvoice = masterInvoiceListNewMasterInvoice.getClientId();
                    masterInvoiceListNewMasterInvoice.setClientId(client);
                    masterInvoiceListNewMasterInvoice = em.merge(masterInvoiceListNewMasterInvoice);
                    if (oldClientIdOfMasterInvoiceListNewMasterInvoice != null && !oldClientIdOfMasterInvoiceListNewMasterInvoice.equals(client)) {
                        oldClientIdOfMasterInvoiceListNewMasterInvoice.getMasterInvoiceList().remove(masterInvoiceListNewMasterInvoice);
                        oldClientIdOfMasterInvoiceListNewMasterInvoice = em.merge(oldClientIdOfMasterInvoiceListNewMasterInvoice);
                    }
                }
            }
            for (Review reviewListNewReview : reviewListNew) {
                if (!reviewListOld.contains(reviewListNewReview)) {
                    Client oldClientIdOfReviewListNewReview = reviewListNewReview.getClientId();
                    reviewListNewReview.setClientId(client);
                    reviewListNewReview = em.merge(reviewListNewReview);
                    if (oldClientIdOfReviewListNewReview != null && !oldClientIdOfReviewListNewReview.equals(client)) {
                        oldClientIdOfReviewListNewReview.getReviewList().remove(reviewListNewReview);
                        oldClientIdOfReviewListNewReview = em.merge(oldClientIdOfReviewListNewReview);
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
                Integer id = client.getClientId();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getClientId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MasterInvoice> masterInvoiceListOrphanCheck = client.getMasterInvoiceList();
            for (MasterInvoice masterInvoiceListOrphanCheckMasterInvoice : masterInvoiceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the MasterInvoice " + masterInvoiceListOrphanCheckMasterInvoice + " in its masterInvoiceList field has a non-nullable clientId field.");
            }
            List<Review> reviewListOrphanCheck = client.getReviewList();
            for (Review reviewListOrphanCheckReview : reviewListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the Review " + reviewListOrphanCheckReview + " in its reviewList field has a non-nullable clientId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Province provinceId = client.getProvinceId();
            if (provinceId != null) {
                provinceId.getClientList().remove(client);
                provinceId = em.merge(provinceId);
            }
            Country countryId = client.getCountryId();
            if (countryId != null) {
                countryId.getClientList().remove(client);
                countryId = em.merge(countryId);
            }
            em.remove(client);
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

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Client.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Client findClient(Integer id) {
        return em.find(Client.class, id);
    }

    public int getClientCount() {      
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Client> rt = cq.from(Client.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}
