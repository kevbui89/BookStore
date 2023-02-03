package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomReviewJpaController;
import com.g3bookstore.entities.Review;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class intended to manage the review management page
 *
 * @author Werner
 */
@Named("ReviewsManagement")
@SessionScoped
public class ReviewsManagementBackingBean1 implements Serializable {

    @Inject
    private CustomReviewJpaController reviewController;
    private Review selectedReview;

    /**
     * Fetches all the reviews from the db
     *
     * @return
     */
    public List<Review> getReviews() {
        return reviewController.getReviews();
    }

    /**
     * Fetches all the reviews that have been seen (approved/rejected) by the
     * manager
     *
     * @return
     */
    public List<Review> getSeenReviews() {
        return reviewController.getAllReviewsSeen();
    }

    /**
     * Fetches all the reviews that have not been (approved/rejected) by the
     * manager
     *
     * @return
     */
    public List<Review> getPendingReviews() {
        return reviewController.getAllReviewsPending();
    }

    /**
     * Returns the review selected
     *
     * @return
     */
    public Review getSelectedReview() {
        return selectedReview;
    }

    /**
     * Sets the review from the item that was selected
     *
     * @param selectedReview
     */
    public void setSelectedReview(Review selectedReview) {
        this.selectedReview = selectedReview;
    }

    /**
     * Method responsible for updating a review to rejected status
     *
     * @param review
     * @throws Exception
     */
    public void RejectReview(Review review) throws Exception {
        review.setPending(false);
        review.setValid(false);
        reviewController.update(review);
    }

    /**
     * Method responsible for updating a review to approved status
     * @param review
     * @throws Exception 
     */
    public void AproveReview(Review review) throws Exception {
        review.setPending(false);
        review.setValid(true);
        reviewController.update(review);
    }

}
