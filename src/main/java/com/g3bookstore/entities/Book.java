package com.g3bookstore.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity class for Books.
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "book", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b")
    , @NamedQuery(name = "Book.findByBookId", query = "SELECT b FROM Book b WHERE b.bookId = :bookId")
    , @NamedQuery(name = "Book.findByIsbn", query = "SELECT b FROM Book b WHERE b.isbn = :isbn")
    , @NamedQuery(name = "Book.findByTitle", query = "SELECT b FROM Book b WHERE b.title = :title")
    , @NamedQuery(name = "Book.findByPubDate", query = "SELECT b FROM Book b WHERE b.pubDate = :pubDate")
    , @NamedQuery(name = "Book.findByInventoryDate", query = "SELECT b FROM Book b WHERE b.inventoryDate = :inventoryDate")
    , @NamedQuery(name = "Book.findByPage", query = "SELECT b FROM Book b WHERE b.page = :page")
    , @NamedQuery(name = "Book.findByListPrice", query = "SELECT b FROM Book b WHERE b.listPrice = :listPrice")
    , @NamedQuery(name = "Book.findBySalePrice", query = "SELECT b FROM Book b WHERE b.salePrice = :salePrice")
    , @NamedQuery(name = "Book.findByWholesalePrice", query = "SELECT b FROM Book b WHERE b.wholesalePrice = :wholesalePrice")
    , @NamedQuery(name = "Book.findByRemoved", query = "SELECT b FROM Book b WHERE b.removed = :removed")
    , @NamedQuery(name = "Book.findByTotalSale", query = "SELECT b FROM Book b WHERE b.totalSale = :totalSale")
    , @NamedQuery(name = "Book.findByImage", query = "SELECT b FROM Book b WHERE b.image = :image")})
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "book_id")
    private Integer bookId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "isbn")
    private String isbn;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "Title")
    private String title;
    @Lob
    @Size(max = 16777215)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pub_date")
    @Temporal(TemporalType.DATE)
    private Date pubDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inventory_date")
    @Temporal(TemporalType.DATE)
    private Date inventoryDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "page")
    private int page;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "list_price")
    private BigDecimal listPrice;
    @Column(name = "sale_price")
    private BigDecimal salePrice;
    @Column(name = "wholesale_price")
    private BigDecimal wholesalePrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "removed")
    private boolean removed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_sale")
    private int totalSale;
    @Size(max = 100)
    @Column(name = "image")
    private String image;
    @JoinTable(name = "book_author", joinColumns = {
        @JoinColumn(name = "book_id", referencedColumnName = "book_id")}, inverseJoinColumns = {
        @JoinColumn(name = "author_id", referencedColumnName = "author_id")})
    @ManyToMany
    private List<Author> authorList;
    @JoinTable(name = "book_genre", joinColumns = {
        @JoinColumn(name = "book_id", referencedColumnName = "book_id")}, inverseJoinColumns = {
        @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")})
    @ManyToMany
    private List<Genre> genreList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookId")
    private List<DetailInvoice> detailInvoiceList;
    @JoinColumn(name = "publisher_id", referencedColumnName = "publisher_id")
    @ManyToOne(optional = false)
    private Publisher publisherId;
    @JoinColumn(name = "format_id", referencedColumnName = "format_id")
    @ManyToOne(optional = false)
    private EbookFormat formatId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookId")
    private List<Review> reviewList;

    public Book() {
    }

    public Book(Integer bookId) {
        this.bookId = bookId;
    }

    public Book(Integer bookId, String isbn, String title, Date pubDate, Date inventoryDate, int page, boolean removed, int totalSale) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.pubDate = pubDate;
        this.inventoryDate = inventoryDate;
        this.page = page;
        this.removed = removed;
        this.totalSale = totalSale;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(BigDecimal wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public boolean getRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public int getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(int totalSale) {
        this.totalSale = totalSale;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public List<DetailInvoice> getDetailInvoiceList() {
        return detailInvoiceList;
    }

    public void setDetailInvoiceList(List<DetailInvoice> detailInvoiceList) {
        this.detailInvoiceList = detailInvoiceList;
    }

    public Publisher getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Publisher publisherId) {
        this.publisherId = publisherId;
    }

    public EbookFormat getFormatId() {
        return formatId;
    }

    public void setFormatId(EbookFormat formatId) {
        this.formatId = formatId;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.Book[ bookId=" + bookId + " ]";
    }
    
}
