package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.ReviewJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Review;
import com.g3bookstore.entities.Review_;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Custom controller for Reviews.
 *
 * @author Kevin Bui
 */
@Named("theReview")
@RequestScoped
public class CustomReviewJpaController implements Serializable {

    @Inject
    private ReviewJpaController reviewJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    private Logger log = Logger.getLogger(CustomReviewJpaController.class.getName());

    /**
     * Method which will return all reviews in the database
     *
     * @return
     */
    public List<Review> getReviews() {
        return reviewJpaController.findReviewEntities();
    }

    public void create(Review review) throws Exception {
        reviewJpaController.create(review);
    }

    public void update(Review review) throws Exception {
        reviewJpaController.edit(review);
    }

    /**
     * Method which will find a specific review by ID
     *
     * @param id
     * @return
     */
    public Review getReviewByID(int id) {
        return reviewJpaController.findReview(id);
    }

    /**
     * Method which will search through the review table for the reviews for a
     * specific book using the book id.
     *
     * @param id The book object
     * @return The Review objects
     */
    public List<Review> getReviewByBook(Book b) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> review = cq.from(Review.class);
        cq.select(review);
        cq.where(cb.equal(review.get(Review_.bookId), b));

        Query q = em.createQuery(cq);
        return ((List<Review>) q.getResultList());
    }

    /**
     * Method which will search through the review table for the reviews for a
     * specific rating.
     *
     * @param id The rating
     * @return The Review objects
     */
    public List<Review> getReviewByRating(int rating) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> review = cq.from(Review.class);
        cq.select(review);
        cq.where(cb.equal(review.get(Review_.rating), rating));

        Query q = em.createQuery(cq);
        return ((List<Review>) q.getResultList());
    }

    /**
     * Gets all the reviews for all books
     *
     * @return The list of reviews
     */
    public List<Review> getAllReviews() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> review = cq.from(Review.class);
        CriteriaQuery<Review> all = cq.select(review);
        TypedQuery<Review> allQuery = em.createQuery(all);

        return (List<Review>) allQuery.getResultList();
    }

    public List<Review> getAllReviewsPending() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> review = cq.from(Review.class);
        CriteriaQuery<Review> all = cq.select(review).where(cb.equal(review.get(Review_.pending), true));
        TypedQuery<Review> allQuery = em.createQuery(all);

        return (List<Review>) allQuery.getResultList();
    }
    public List<Review> getAllReviewsSeen() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> review = cq.from(Review.class);
        CriteriaQuery<Review> all = cq.select(review).where(cb.equal(review.get(Review_.pending), false));
        TypedQuery<Review> allQuery = em.createQuery(all);

        return (List<Review>) allQuery.getResultList();
    }

    /**
     * Gets all approved comments for a book
     *
     * @param b
     * @return The list of reviews approved
     */
    public List<Review> getApprovedReviews(Book b) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Review> query = cb.createQuery(Review.class);
        Root<Review> root = query.from(Review.class);
        query.select(root);
        query.where(cb.and(
                cb.equal(root.get(Review_.bookId), b),
                cb.equal(root.get(Review_.valid), true)
        ));

        return em.createQuery(query).getResultList();
    }

}
