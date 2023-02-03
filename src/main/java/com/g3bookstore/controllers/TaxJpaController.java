package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Province;
import com.g3bookstore.entities.Tax;

import java.io.Serializable;
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
 * @author Alessandro, Denis, Kevin, Werner
 */
@Named("Tax")
@RequestScoped
public class TaxJpaController implements Serializable 
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(Tax tax) throws RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Province provinceId = tax.getProvinceId();
            if (provinceId != null) {
                provinceId = em.getReference(provinceId.getClass(), provinceId.getProvinceId());
                tax.setProvinceId(provinceId);
            }
            em.persist(tax);
            if (provinceId != null) {
                provinceId.getTaxList().add(tax);
                provinceId = em.merge(provinceId);
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

    public void edit(Tax tax) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Tax persistentTax = em.find(Tax.class, tax.getTaxId());
            Province provinceIdOld = persistentTax.getProvinceId();
            Province provinceIdNew = tax.getProvinceId();
            if (provinceIdNew != null) {
                provinceIdNew = em.getReference(provinceIdNew.getClass(), provinceIdNew.getProvinceId());
                tax.setProvinceId(provinceIdNew);
            }
            tax = em.merge(tax);
            if (provinceIdOld != null && !provinceIdOld.equals(provinceIdNew)) {
                provinceIdOld.getTaxList().remove(tax);
                provinceIdOld = em.merge(provinceIdOld);
            }
            if (provinceIdNew != null && !provinceIdNew.equals(provinceIdOld)) {
                provinceIdNew.getTaxList().add(tax);
                provinceIdNew = em.merge(provinceIdNew);
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
                Integer id = tax.getTaxId();
                if (findTax(id) == null) {
                    throw new NonexistentEntityException("The tax with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try {
            utx.begin();
            Tax tax;
            try {
                tax = em.getReference(Tax.class, id);
                tax.getTaxId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tax with id " + id + " no longer exists.", enfe);
            }
            Province provinceId = tax.getProvinceId();
            if (provinceId != null) {
                provinceId.getTaxList().remove(tax);
                provinceId = em.merge(provinceId);
            }
            em.remove(tax);
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

    public List<Tax> findTaxEntities() {
        return findTaxEntities(true, -1, -1);
    }

    public List<Tax> findTaxEntities(int maxResults, int firstResult) {
        return findTaxEntities(false, maxResults, firstResult);
    }

    private List<Tax> findTaxEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Tax.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();        
    }

    public Tax findTax(Integer id) 
    {
        return em.find(Tax.class, id);
    }

    public int getTaxCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Tax> rt = cq.from(Tax.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}