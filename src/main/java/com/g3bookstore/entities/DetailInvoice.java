package com.g3bookstore.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entity class for Detailed Invoices.
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "detail_invoice", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "DetailInvoice.findAll", query = "SELECT d FROM DetailInvoice d")
    , @NamedQuery(name = "DetailInvoice.findByDetailId", query = "SELECT d FROM DetailInvoice d WHERE d.detailId = :detailId")
    , @NamedQuery(name = "DetailInvoice.findByBookPrice", query = "SELECT d FROM DetailInvoice d WHERE d.bookPrice = :bookPrice")
    , @NamedQuery(name = "DetailInvoice.findByGst", query = "SELECT d FROM DetailInvoice d WHERE d.gst = :gst")
    , @NamedQuery(name = "DetailInvoice.findByPst", query = "SELECT d FROM DetailInvoice d WHERE d.pst = :pst")
    , @NamedQuery(name = "DetailInvoice.findByHst", query = "SELECT d FROM DetailInvoice d WHERE d.hst = :hst")})
public class DetailInvoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detail_id")
    private Integer detailId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "book_price")
    private BigDecimal bookPrice;
    @Column(name = "gst")
    private BigDecimal gst;
    @Column(name = "pst")
    private BigDecimal pst;
    @Column(name = "hst")
    private BigDecimal hst;
    @Basic(optional = false)
    @NotNull
    private boolean status;
    @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
    @ManyToOne(optional = false)
    private MasterInvoice invoiceId;
    @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    @ManyToOne(optional = false)
    private Book bookId;

    public DetailInvoice() {
    }

    public DetailInvoice(Integer detailId) {
        this.detailId = detailId;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

    public BigDecimal getGst() {
        return gst;
    }

    public void setGst(BigDecimal gst) {
        this.gst = gst;
    }

    public BigDecimal getPst() {
        return pst;
    }

    public void setPst(BigDecimal pst) {
        this.pst = pst;
    }

    public BigDecimal getHst() {
        return hst;
    }

    public void setHst(BigDecimal hst) {
        this.hst = hst;
    }

    public MasterInvoice getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(MasterInvoice invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Book getBookId() {
        return bookId;
    }

    public void setBookId(Book bookId) {
        this.bookId = bookId;
    }
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailId != null ? detailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailInvoice)) {
            return false;
        }
        DetailInvoice other = (DetailInvoice) object;
        if ((this.detailId == null && other.detailId != null) || (this.detailId != null && !this.detailId.equals(other.detailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.DetailInvoice[ detailId=" + detailId + " ]";
    }
    
}
