package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Ad;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 * Generated controller updated to use CDI
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Named("Ad")
@RequestScoped
public class AdJpaController implements Serializable 
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(Ad ad) throws RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            em.persist(ad);
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

    public void edit(Ad ad) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            ad = em.merge(ad);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ad.getAdId();
                if (findAd(id) == null) {
                    throw new NonexistentEntityException("The ad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Ad ad;
            try {
                ad = em.getReference(Ad.class, id);
                ad.getAdId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ad with id " + id + " no longer exists.", enfe);
            }
            em.remove(ad);
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

    public List<Ad> findAdEntities() {
        return findAdEntities(true, -1, -1);
    }

    public List<Ad> findAdEntities(int maxResults, int firstResult) {
        return findAdEntities(false, maxResults, firstResult);
    }

    private List<Ad> findAdEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Ad.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();        
    }

    public Ad findAd(Integer id) 
    {
        return em.find(Ad.class, id);
    }

    public int getAdCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Ad> rt = cq.from(Ad.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}