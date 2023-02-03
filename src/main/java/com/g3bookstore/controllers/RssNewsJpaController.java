package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.RssNews;

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
@Named("Rss")
@RequestScoped
public class RssNewsJpaController implements Serializable
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(RssNews rssNews) throws RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            em.persist(rssNews);
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

    public void edit(RssNews rssNews) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            rssNews = em.merge(rssNews);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rssNews.getRssId();
                if (findRssNews(id) == null) {
                    throw new NonexistentEntityException("The rssNews with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            RssNews rssNews;
            try {
                rssNews = em.getReference(RssNews.class, id);
                rssNews.getRssId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rssNews with id " + id + " no longer exists.", enfe);
            }
            em.remove(rssNews);
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

    public List<RssNews> findRssNewsEntities() {
        return findRssNewsEntities(true, -1, -1);
    }

    public List<RssNews> findRssNewsEntities(int maxResults, int firstResult) {
        return findRssNewsEntities(false, maxResults, firstResult);
    }

    private List<RssNews> findRssNewsEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(RssNews.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public RssNews findRssNews(Integer id) 
    {
        return em.find(RssNews.class, id);
    }

    public int getRssNewsCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<RssNews> rt = cq.from(RssNews.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}