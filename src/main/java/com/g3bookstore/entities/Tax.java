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

/**
 * Entity class for Taxes.
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "tax", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t")
    , @NamedQuery(name = "Tax.findByTaxId", query = "SELECT t FROM Tax t WHERE t.taxId = :taxId")
    , @NamedQuery(name = "Tax.findByGst", query = "SELECT t FROM Tax t WHERE t.gst = :gst")
    , @NamedQuery(name = "Tax.findByPst", query = "SELECT t FROM Tax t WHERE t.pst = :pst")
    , @NamedQuery(name = "Tax.findByHst", query = "SELECT t FROM Tax t WHERE t.hst = :hst")})
public class Tax implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tax_id")
    private Integer taxId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "gst")
    private BigDecimal gst;
    @Column(name = "pst")
    private BigDecimal pst;
    @Column(name = "hst")
    private BigDecimal hst;
    @JoinColumn(name = "province_id", referencedColumnName = "province_id")
    @ManyToOne(optional = false)
    private Province provinceId;

    public Tax() {
    }

    public Tax(Integer taxId) {
        this.taxId = taxId;
    }

    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
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

    public Province getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Province provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxId != null ? taxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tax)) {
            return false;
        }
        Tax other = (Tax) object;
        if ((this.taxId == null && other.taxId != null) || (this.taxId != null && !this.taxId.equals(other.taxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.Tax[ taxId=" + taxId + " ]";
    }
    
}
