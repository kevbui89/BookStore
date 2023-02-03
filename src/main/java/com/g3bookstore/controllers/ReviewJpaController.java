package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Review;

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
@Named("Review")
@RequestScoped
public class ReviewJpaController implements Serializable 
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(Review review) throws RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Client clientId = review.getClientId();
            if (clientId != null) {
                clientId = em.getReference(clientId.getClass(), clientId.getClientId());
                review.setClientId(clientId);
            }
            Book bookId = review.getBookId();
            if (bookId != null) {
                bookId = em.getReference(bookId.getClass(), bookId.getBookId());
                review.setBookId(bookId);
            }
            em.persist(review);
            if (clientId != null) {
                clientId.getReviewList().add(review);
                clientId = em.merge(clientId);
            }
            if (bookId != null) {
                bookId.getReviewList().add(review);
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

    public void edit(Review review) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Review persistentReview = em.find(Review.class, review.getReviewId());
            Client clientIdOld = persistentReview.getClientId();
            Client clientIdNew = review.getClientId();
            Book bookIdOld = persistentReview.getBookId();
            Book bookIdNew = review.getBookId();
            if (clientIdNew != null) {
                clientIdNew = em.getReference(clientIdNew.getClass(), clientIdNew.getClientId());
                review.setClientId(clientIdNew);
            }
            if (bookIdNew != null) {
                bookIdNew = em.getReference(bookIdNew.getClass(), bookIdNew.getBookId());
                review.setBookId(bookIdNew);
            }
            review = em.merge(review);
            if (clientIdOld != null && !clientIdOld.equals(clientIdNew)) {
                clientIdOld.getReviewList().remove(review);
                clientIdOld = em.merge(clientIdOld);
            }
            if (clientIdNew != null && !clientIdNew.equals(clientIdOld)) {
                clientIdNew.getReviewList().add(review);
                clientIdNew = em.merge(clientIdNew);
            }
            if (bookIdOld != null && !bookIdOld.equals(bookIdNew)) {
                bookIdOld.getReviewList().remove(review);
                bookIdOld = em.merge(bookIdOld);
            }
            if (bookIdNew != null && !bookIdNew.equals(bookIdOld)) {
                bookIdNew.getReviewList().add(review);
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
                Integer id = review.getReviewId();
                if (findReview(id) == null) {
                    throw new NonexistentEntityException("The review with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Review review;
            try {
                review = em.getReference(Review.class, id);
                review.getReviewId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The review with id " + id + " no longer exists.", enfe);
            }
            Client clientId = review.getClientId();
            if (clientId != null) {
                clientId.getReviewList().remove(review);
                clientId = em.merge(clientId);
            }
            Book bookId = review.getBookId();
            if (bookId != null) {
                bookId.getReviewList().remove(review);
                bookId = em.merge(bookId);
            }
            em.remove(review);
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

    public List<Review> findReviewEntities() {
        return findReviewEntities(true, -1, -1);
    }

    public List<Review> findReviewEntities(int maxResults, int firstResult) {
        return findReviewEntities(false, maxResults, firstResult);
    }

    private List<Review> findReviewEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Review.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Review findReview(Integer id) 
    {
        return em.find(Review.class, id);
    }

    public int getReviewCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Review> rt = cq.from(Review.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}