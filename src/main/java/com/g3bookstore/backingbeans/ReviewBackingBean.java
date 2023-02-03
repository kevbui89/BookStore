package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomReviewJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Review;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class that manages the reviews
 *
 * @author Kevin
 */
@Named("ReviewsBean")
@SessionScoped
public class ReviewBackingBean implements Serializable {

    @Inject
    private CustomReviewJpaController reviewController;

    @Inject
    private CustomBookJpaController bookController;

    @Inject
    private CustomClientJpaController clientController;

    private List<Review> reviews = new ArrayList<Review>();
    private int bookId;

    private Logger log = Logger.getLogger(ReviewBackingBean.class.getName());

    /**
     * Returns all the reviews
     *
     * @return
     */
    public List<Review> getAllReviews() {
        reviews = reviewController.getAllReviews();
        int size = reviews.size();

        return reviews;
    }

    /**
     * Returns all the reviews for a specific book
     *
     * @return The list of reviews for a book
     */
    public List<Review> getReviewsForBook() {
        Map<String, String> parameters = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();

        log.log(Level.INFO, null, parameters.get("bookid"));

        // Check the query string
        try {
            // Return default if invalid
            String bookId = parameters.get("bookid");
            if (bookId == null || !bookId.matches("^[0-9]*$")) {
                return null;
            }

            int id = Integer.parseInt(bookId);

            Book b = bookController.getBookByID(id);

            return reviewController.getReviewByBook(b);
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Returns all the reviews for a specific book
     *
     * @return The list of reviews for a book
     */
    public List<Review> getValidReviewsForBook() {
        Map<String, String> parameters = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();

        log.log(Level.INFO, null, parameters.get("bookid"));

        // Check the query string
        try {
            // Return default if invalid
            String bookId = parameters.get("bookid");
            if (bookId == null || !bookId.matches("^[0-9]*$")) {
                return null;
            }

            int id = Integer.parseInt(bookId);

            Book b = bookController.getBookByID(id);

            return reviewController.getApprovedReviews(b);
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Returns a list of reviews given a book object
     *
     * @param book The book object
     * @return The list of reviews for this book
     */
    public List<Review> getValidReviewsForBook(Book book) {
        return reviewController.getApprovedReviews(book);
    }

    /**
     * Returns the ratings of a book given a book id
     *
     * @param id The id of the book
     * @return The list of ratinsg
     */
    public List<Integer> getRatings(int id) {
        Book b = getBookById(id);
        List<Review> reviews = getValidReviewsForBook(b);
        List<Integer> ratings = new ArrayList<Integer>();

        for (Review r : reviews) {
            ratings.add(r.getRating());
        }

        return ratings;
    }

    /**
     * Calculates the average rating of a book given an id
     *
     * @param id The id of the book
     * @return The average rating
     */
    public int calculateAverage(int id) {
        List<Integer> ratings = getRatings(id);
        int sum = 0;
        for (int i = 0; i < ratings.size(); i++) {
            sum = sum + ratings.get(i);
        }

        if (ratings.size() == 0) {
            return 0;
        }

        return sum / ratings.size();
    }

    /**
     * Sets the bookId
     *
     * @return The book object
     */
    public void getBookIdForReview(int id) {
        bookId = id;
    }

    /**
     * Returns the book by id
     *
     * @param id The id of the book
     * @return
     */
    public Book getBookById(int id) {
        return bookController.getBookByID(id);
    }

    /**
     * Returns book information
     *
     * @return
     */
    public Book getBookInformationForReviews() {
        return bookController.getBookByID(bookId);
    }

    /**
     * Adds a client review for a book.
     *
     * @param clientId The client id
     * @param reviewText The review
     * @param rating The rating
     * @return The string of the navigation depending on the conditions
     * @throws Exception
     */
    public String addReview(int clientId, String titleText, String reviewText, int rating) throws Exception {
        Client client = clientController.getClientByID(clientId);
        Book book = bookController.getBookByID(bookId);

        if (client == null) {
            return "/client/login.xhtml";
        }
        if (book == null) {
            return "/client/index.xhtml";
        }

        Review review = new Review();
        review.setBookId(book);
        review.setClientId(client);
        review.setCommentTitle(titleText);
        review.setComment(reviewText);
        review.setRating(rating);
        review.setReviewDate(new Date());
        review.setValid(Boolean.FALSE);
        reviewController.create(review);

        return "/client/review_success.xhtml";
    }
}
