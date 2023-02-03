package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.MasterInvoice;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.DetailInvoice;

import java.util.List;
import java.io.Serializable;

import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 * Generated controller updated to use CDI
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Named("DetailInvoice")
@RequestScoped
public class DetailInvoiceJpaController implements Serializable
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(DetailInvoice detailInvoice) throws RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            MasterInvoice invoiceId = detailInvoice.getInvoiceId();
            if (invoiceId != null) {
                invoiceId = em.getReference(invoiceId.getClass(), invoiceId.getInvoiceId());
                detailInvoice.setInvoiceId(invoiceId);
            }
            Book bookId = detailInvoice.getBookId();
            if (bookId != null) {
                bookId = em.getReference(bookId.getClass(), bookId.getBookId());
                detailInvoice.setBookId(bookId);
            }
            em.persist(detailInvoice);
            if (invoiceId != null) {
                invoiceId.getDetailInvoiceList().add(detailInvoice);
                invoiceId = em.merge(invoiceId);
            }
            if (bookId != null) {
                bookId.getDetailInvoiceList().add(detailInvoice);
                bookId = em.merge(bookId);
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

    public void edit(DetailInvoice detailInvoice) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            DetailInvoice persistentDetailInvoice = em.find(DetailInvoice.class, detailInvoice.getDetailId());
            MasterInvoice invoiceIdOld = persistentDetailInvoice.getInvoiceId();
            MasterInvoice invoiceIdNew = detailInvoice.getInvoiceId();
            Book bookIdOld = persistentDetailInvoice.getBookId();
            Book bookIdNew = detailInvoice.getBookId();
            if (invoiceIdNew != null) {
                invoiceIdNew = em.getReference(invoiceIdNew.getClass(), invoiceIdNew.getInvoiceId());
                detailInvoice.setInvoiceId(invoiceIdNew);
            }
            if (bookIdNew != null) {
                bookIdNew = em.getReference(bookIdNew.getClass(), bookIdNew.getBookId());
                detailInvoice.setBookId(bookIdNew);
            }
            detailInvoice = em.merge(detailInvoice);
            if (invoiceIdOld != null && !invoiceIdOld.equals(invoiceIdNew)) {
                invoiceIdOld.getDetailInvoiceList().remove(detailInvoice);
                invoiceIdOld = em.merge(invoiceIdOld);
            }
            if (invoiceIdNew != null && !invoiceIdNew.equals(invoiceIdOld)) {
                invoiceIdNew.getDetailInvoiceList().add(detailInvoice);
                invoiceIdNew = em.merge(invoiceIdNew);
            }
            if (bookIdOld != null && !bookIdOld.equals(bookIdNew)) {
                bookIdOld.getDetailInvoiceList().remove(detailInvoice);
                bookIdOld = em.merge(bookIdOld);
            }
            if (bookIdNew != null && !bookIdNew.equals(bookIdOld)) {
                bookIdNew.getDetailInvoiceList().add(detailInvoice);
                bookIdNew = em.merge(bookIdNew);
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
                Integer id = detailInvoice.getDetailId();
                if (findDetailInvoice(id) == null) {
                    throw new NonexistentEntityException("The detailInvoice with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            DetailInvoice detailInvoice;
            try {
                detailInvoice = em.getReference(DetailInvoice.class, id);
                detailInvoice.getDetailId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detailInvoice with id " + id + " no longer exists.", enfe);
            }
            MasterInvoice invoiceId = detailInvoice.getInvoiceId();
            if (invoiceId != null) {
                invoiceId.getDetailInvoiceList().remove(detailInvoice);
                invoiceId = em.merge(invoiceId);
            }
            Book bookId = detailInvoice.getBookId();
            if (bookId != null) {
                bookId.getDetailInvoiceList().remove(detailInvoice);
                bookId = em.merge(bookId);
            }
            em.remove(detailInvoice);
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

    public List<DetailInvoice> findDetailInvoiceEntities() {
        return findDetailInvoiceEntities(true, -1, -1);
    }

    public List<DetailInvoice> findDetailInvoiceEntities(int maxResults, int firstResult) {
        return findDetailInvoiceEntities(false, maxResults, firstResult);
    }

    private List<DetailInvoice> findDetailInvoiceEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(DetailInvoice.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public DetailInvoice findDetailInvoice(Integer id) 
    {
        return em.find(DetailInvoice.class, id);
    }

    public int getDetailInvoiceCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<DetailInvoice> rt = cq.from(DetailInvoice.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}