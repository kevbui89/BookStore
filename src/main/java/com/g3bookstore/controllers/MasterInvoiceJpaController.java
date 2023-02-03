package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.MasterInvoice;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.annotation.Resource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 * Generated controller updated to use CDI
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Named("MasterInvoice")
@RequestScoped
public class MasterInvoiceJpaController implements Serializable 
{    
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(MasterInvoice masterInvoice) throws RollbackFailureException, Exception {
        if (masterInvoice.getDetailInvoiceList() == null) {
            masterInvoice.setDetailInvoiceList(new ArrayList<DetailInvoice>());
        }
        try {
            utx.begin();
            Client clientId = masterInvoice.getClientId();
            if (clientId != null) {
                clientId = em.getReference(clientId.getClass(), clientId.getClientId());
                masterInvoice.setClientId(clientId);
            }
            List<DetailInvoice> attachedDetailInvoiceList = new ArrayList<DetailInvoice>();
            for (DetailInvoice detailInvoiceListDetailInvoiceToAttach : masterInvoice.getDetailInvoiceList()) {
                detailInvoiceListDetailInvoiceToAttach = em.getReference(detailInvoiceListDetailInvoiceToAttach.getClass(), detailInvoiceListDetailInvoiceToAttach.getDetailId());
                attachedDetailInvoiceList.add(detailInvoiceListDetailInvoiceToAttach);
            }
            masterInvoice.setDetailInvoiceList(attachedDetailInvoiceList);
            em.persist(masterInvoice);
            if (clientId != null) {
                clientId.getMasterInvoiceList().add(masterInvoice);
                clientId = em.merge(clientId);
            }
            for (DetailInvoice detailInvoiceListDetailInvoice : masterInvoice.getDetailInvoiceList()) {
                MasterInvoice oldInvoiceIdOfDetailInvoiceListDetailInvoice = detailInvoiceListDetailInvoice.getInvoiceId();
                detailInvoiceListDetailInvoice.setInvoiceId(masterInvoice);
                detailInvoiceListDetailInvoice = em.merge(detailInvoiceListDetailInvoice);
                if (oldInvoiceIdOfDetailInvoiceListDetailInvoice != null) {
                    oldInvoiceIdOfDetailInvoiceListDetailInvoice.getDetailInvoiceList().remove(detailInvoiceListDetailInvoice);
                    oldInvoiceIdOfDetailInvoiceListDetailInvoice = em.merge(oldInvoiceIdOfDetailInvoiceListDetailInvoice);
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

    public void edit(MasterInvoice masterInvoice) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            MasterInvoice persistentMasterInvoice = em.find(MasterInvoice.class, masterInvoice.getInvoiceId());
            Client clientIdOld = persistentMasterInvoice.getClientId();
            Client clientIdNew = masterInvoice.getClientId();
            List<DetailInvoice> detailInvoiceListOld = persistentMasterInvoice.getDetailInvoiceList();
            List<DetailInvoice> detailInvoiceListNew = masterInvoice.getDetailInvoiceList();
            List<String> illegalOrphanMessages = null;
            for (DetailInvoice detailInvoiceListOldDetailInvoice : detailInvoiceListOld) {
                if (!detailInvoiceListNew.contains(detailInvoiceListOldDetailInvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetailInvoice " + detailInvoiceListOldDetailInvoice + " since its invoiceId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clientIdNew != null) {
                clientIdNew = em.getReference(clientIdNew.getClass(), clientIdNew.getClientId());
                masterInvoice.setClientId(clientIdNew);
            }
            List<DetailInvoice> attachedDetailInvoiceListNew = new ArrayList<DetailInvoice>();
            for (DetailInvoice detailInvoiceListNewDetailInvoiceToAttach : detailInvoiceListNew) {
                detailInvoiceListNewDetailInvoiceToAttach = em.getReference(detailInvoiceListNewDetailInvoiceToAttach.getClass(), detailInvoiceListNewDetailInvoiceToAttach.getDetailId());
                attachedDetailInvoiceListNew.add(detailInvoiceListNewDetailInvoiceToAttach);
            }
            detailInvoiceListNew = attachedDetailInvoiceListNew;
            masterInvoice.setDetailInvoiceList(detailInvoiceListNew);
            masterInvoice = em.merge(masterInvoice);
            if (clientIdOld != null && !clientIdOld.equals(clientIdNew)) {
                clientIdOld.getMasterInvoiceList().remove(masterInvoice);
                clientIdOld = em.merge(clientIdOld);
            }
            if (clientIdNew != null && !clientIdNew.equals(clientIdOld)) {
                clientIdNew.getMasterInvoiceList().add(masterInvoice);
                clientIdNew = em.merge(clientIdNew);
            }
            for (DetailInvoice detailInvoiceListNewDetailInvoice : detailInvoiceListNew) {
                if (!detailInvoiceListOld.contains(detailInvoiceListNewDetailInvoice)) {
                    MasterInvoice oldInvoiceIdOfDetailInvoiceListNewDetailInvoice = detailInvoiceListNewDetailInvoice.getInvoiceId();
                    detailInvoiceListNewDetailInvoice.setInvoiceId(masterInvoice);
                    detailInvoiceListNewDetailInvoice = em.merge(detailInvoiceListNewDetailInvoice);
                    if (oldInvoiceIdOfDetailInvoiceListNewDetailInvoice != null && !oldInvoiceIdOfDetailInvoiceListNewDetailInvoice.equals(masterInvoice)) {
                        oldInvoiceIdOfDetailInvoiceListNewDetailInvoice.getDetailInvoiceList().remove(detailInvoiceListNewDetailInvoice);
                        oldInvoiceIdOfDetailInvoiceListNewDetailInvoice = em.merge(oldInvoiceIdOfDetailInvoiceListNewDetailInvoice);
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
                Integer id = masterInvoice.getInvoiceId();
                if (findMasterInvoice(id) == null) {
                    throw new NonexistentEntityException("The masterInvoice with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            MasterInvoice masterInvoice;
            try {
                masterInvoice = em.getReference(MasterInvoice.class, id);
                masterInvoice.getInvoiceId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The masterInvoice with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetailInvoice> detailInvoiceListOrphanCheck = masterInvoice.getDetailInvoiceList();
            for (DetailInvoice detailInvoiceListOrphanCheckDetailInvoice : detailInvoiceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MasterInvoice (" + masterInvoice + ") cannot be destroyed since the DetailInvoice " + detailInvoiceListOrphanCheckDetailInvoice + " in its detailInvoiceList field has a non-nullable invoiceId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Client clientId = masterInvoice.getClientId();
            if (clientId != null) {
                clientId.getMasterInvoiceList().remove(masterInvoice);
                clientId = em.merge(clientId);
            }
            em.remove(masterInvoice);
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

    public List<MasterInvoice> findMasterInvoiceEntities() {
        return findMasterInvoiceEntities(true, -1, -1);
    }

    public List<MasterInvoice> findMasterInvoiceEntities(int maxResults, int firstResult) {
        return findMasterInvoiceEntities(false, maxResults, firstResult);
    }

    private List<MasterInvoice> findMasterInvoiceEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(MasterInvoice.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public MasterInvoice findMasterInvoice(Integer id) 
    {
        return em.find(MasterInvoice.class, id);
    }

    public int getMasterInvoiceCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<MasterInvoice> rt = cq.from(MasterInvoice.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}