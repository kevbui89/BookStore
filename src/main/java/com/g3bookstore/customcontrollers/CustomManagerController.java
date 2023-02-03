package com.g3bookstore.customcontrollers;

import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Publisher;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Custom controller for Manager. This section of code uses JPQL because
 * Criteria Builder did not work. For example, the 'between' method. We had
 * consulted Java EE 7 documentation and our teacher without successfully found
 * the issue. This class contain all the queries required for a manager page.
 *
 * @author Werner Castanaza, Denis Lebedev
 */
@Named("manager")
@RequestScoped
public class CustomManagerController implements Serializable {

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to find what a publisher sold over the given two dates and order
     * the result by the total sale of that book and the sale date.
     *
     * @param pub
     * @param startDate
     * @param endDate
     * @return List of Books
     */
    public List<Book> totalSaleByPublisherAndDate(Publisher pub, Date startDate, Date endDate) {
        TypedQuery<Book> query = em.createQuery(
                "Select b from Book b"
                + " join b.publisherId p"
                + " join b.detailInvoiceList d"
                + " join d.invoiceId m"
                + " WHERE p.publisher = ?1 AND"
                + " m.saleDate BETWEEN ?2 AND ?3"
                + " ORDER BY m.saleDate DESC, b.totalSale DESC", Book.class);

        query.setParameter(1, pub.getPublisher());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);

        return query.getResultList();
    }

    /**
     * Retrieve all the books between a given range and order them by the gross
     * value.
     *
     * @param startDate
     * @param endDate
     * @return List of Books
     */
    public List<Book> bookTopSellerOrderBySales(Date startDate, Date endDate) {
        TypedQuery<Book> query = em.createQuery(
                "Select b from Book b"
                + " join b.detailInvoiceList d"
                + " join d.invoiceId m"
                + " WHERE m.saleDate BETWEEN ?1 AND ?2"
                + " GROUP BY d.bookId"
                + " ORDER BY SUM(m.grossValue) DESC", Book.class);

        query.setParameter(1, startDate);
        query.setParameter(2, endDate);

        return query.getResultList();
    }

    /**
     * Retrieve how much that book was sold.
     *
     * @param book
     * @return List of Books
     */
    public List<Integer> totalSoldByBook(Book book) {
        TypedQuery<Integer> query = em.createQuery(
                "Select count(b) from Book b"
                + " join b.detailInvoiceList di"
                + " WHERE b.bookId = ?1", Integer.class);

        query.setParameter(1, book.getBookId());

        return query.getResultList();
    }

    /**
     * Retrieve the clients that bought items and order them by the biggest
     * amount of money used.
     *
     * @param startDate
     * @param endDate
     * @return List of Books
     */
    public List<Client> clientTopSellerOrderByPurchases(Date startDate, Date endDate) {
        TypedQuery<Client> query = em.createQuery(
                "Select DISTINCT c from Client c"
                + " join c.masterInvoiceList m"
                + " join m.detailInvoiceList d"
                + " WHERE m.saleDate BETWEEN ?1 AND ?2"
                + " GROUP BY d.bookId"
                + " ORDER BY SUM(m.grossValue) DESC", Client.class);

        query.setParameter(1, startDate);
        query.setParameter(2, endDate);

        return query.getResultList();
    }

    /**
     * Retrieve the books that have never been sold in a given range and order
     * by the title.
     *
     * @param startDate
     * @param endDate
     * @return List of Books
     */
    public List<Book> allBookNeverSold(Date startDate, Date endDate) {
        TypedQuery<Book> query = em.createQuery(
                "Select b from Book b"
                + " WHERE b.bookId NOT IN"
                + " ( Select d.bookId.bookId from DetailInvoice d"
                + " join d.invoiceId m"
                + " WHERE m.saleDate BETWEEN ?1 AND ?2 )"
                + " ORDER BY b.title ASC", Book.class);

        query.setParameter(1, startDate);
        query.setParameter(2, endDate);

        return query.getResultList();
    }

    /**
     * Retrieve all the books sold by a specific author and a given range of
     * dates. Order by the sale date and the gross.
     *
     * @param auth
     * @param startDate
     * @param endDate
     * @return List of Books
     */
    public List<Book> allBookSoldByAuthorOrderedByDate(Author auth, Date startDate, Date endDate) {
        TypedQuery<Book> query = em.createQuery(
                "Select b from Book b"
                + " join b.authorList a"
                + " join b.detailInvoiceList d "
                + " join d.invoiceId m"
                + " WHERE a.name = ?1 AND m.saleDate BETWEEN ?2 AND ?3"
                + " ORDER BY m.saleDate DESC", Book.class);

        query.setParameter(1, auth.getName());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);

        return query.getResultList();
    }

    /**
     * Retrieve all the books bought by a specific client and order by the sale
     * date.
     *
     * @param client
     * @param startDate
     * @param endDate
     * @return List of Books
     */
    public List<Book> allBookBoughtByClientOrderedByDate(Client client, Date startDate, Date endDate) {
        TypedQuery<Book> query = em.createQuery(
                "Select b from Book b"
                + " join b.detailInvoiceList d "
                + " join d.invoiceId m"
                + " join m.clientId c"
                + " WHERE c.email = ?1 AND m.saleDate BETWEEN ?2 AND ?3"
                + " ORDER BY m.saleDate DESC", Book.class);

        query.setParameter(1, client.getEmail());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);

        return query.getResultList();
    }

    /**
     * Retrieve all the books sold by a specific publisher and the given date
     * order by the sale date.
     *
     * @param pub
     * @param startDate
     * @param endDate
     * @return List of Books
     */
    public List<Book> allBookSoldByPublisherOrderedByDate(Publisher pub, Date startDate, Date endDate) {
        TypedQuery<Book> query = em.createQuery(
                "Select b from Book b"
                + " join b.publisherId p"
                + " join b.detailInvoiceList d"
                + " join d.invoiceId m"
                + " WHERE p.publisher = ?1 AND m.saleDate BETWEEN ?2 AND ?3"
                + " ORDER BY m.saleDate DESC", Book.class);

        query.setParameter(1, pub.getPublisher());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);

        return query.getResultList();
    }

    /**
     * Retrieve how much money were made between the two given dates.
     *
     * @param startDate
     * @param endDate
     * @return Total sales
     */
    public BigDecimal totalMoneyMade(Date startDate, Date endDate) {
        TypedQuery<BigDecimal> query = em.createQuery(
                "Select SUM(m.grossValue) from Book b"
                + " join b.detailInvoiceList d"
                + " join d.invoiceId m"
                + " WHERE m.saleDate BETWEEN ?1 AND ?2"
                + " GROUP BY d.bookId", BigDecimal.class);

        query.setParameter(1, startDate);
        query.setParameter(2, endDate);

        return query.getSingleResult();
    }

    /**
     * Retrieve how much money the book store earned with the given author and
     * two dates.
     *
     * @param auth
     * @param startDate
     * @param endDate
     * @return Total sales
     */
    public BigDecimal totalMoneyByAuthor(Author auth, Date startDate, Date endDate) {
        TypedQuery<BigDecimal> query = em.createQuery(
                "Select SUM(m.grossValue) from Book b"
                + " join b.authorList a"
                + " join b.detailInvoiceList d "
                + " join d.invoiceId m"
                + " WHERE a.name = ?1 AND m.saleDate BETWEEN ?2 AND ?3", BigDecimal.class);

        query.setParameter(1, auth.getName());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);

        return query.getSingleResult();
    }

    /**
     * Retrieve how much money the book store made with the given client and two
     * dates.
     *
     * @param client
     * @param startDate
     * @param endDate
     * @return Total sales
     */
    public BigDecimal totalMoneyByClient(Client client, Date startDate, Date endDate) {
        TypedQuery<BigDecimal> query = em.createQuery(
                "Select SUM(m.grossValue) from Book b"
                + " join b.detailInvoiceList d "
                + " join d.invoiceId m"
                + " join m.clientId c"
                + " WHERE c.email = ?1 AND m.saleDate BETWEEN ?2 AND ?3", BigDecimal.class);

        query.setParameter(1, client.getEmail());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);

        return query.getSingleResult();
    }

    /**
     * Retrieve how much money the book store made with the 
     * given publisher and two dates.
     *
     * @param pub
     * @param startDate
     * @param endDate
     * @return List of Books
     */
    public BigDecimal totalMoneyByPublisher(Publisher pub, Date startDate, Date endDate) {
        TypedQuery<BigDecimal> query = em.createQuery(
                "Select SUM(m.grossValue) from Book b"
                + " join b.publisherId p"
                + " join b.detailInvoiceList d"
                + " join d.invoiceId m"
                + " WHERE p.publisher = ?1 AND m.saleDate BETWEEN ?2 AND ?3", BigDecimal.class);

        query.setParameter(1, pub.getPublisher());
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);

        return query.getSingleResult();
    }
}
