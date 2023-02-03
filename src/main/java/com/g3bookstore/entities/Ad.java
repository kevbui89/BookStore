package com.g3bookstore.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity class for Ads.
 *
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "ad", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "Ad.findAll", query = "SELECT a FROM Ad a")
    , @NamedQuery(name = "Ad.findByAdId", query = "SELECT a FROM Ad a WHERE a.adId = :adId")
    , @NamedQuery(name = "Ad.findByTitle", query = "SELECT a FROM Ad a WHERE a.title = :title")})
public class Ad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ad_id")
    private Integer adId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "image")
    private String image;
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "ad_link")
    private String adLink;
    @Basic(optional = false)
    @NotNull
    private boolean chosen;

    public Ad() {
    }

    public Ad(Integer adId) {
        this.adId = adId;
    }

    public Ad(Integer adId, String title) {
        this.adId = adId;
        this.title = title;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public boolean getChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (adId != null ? adId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ad)) {
            return false;
        }
        Ad other = (Ad) object;
        if ((this.adId == null && other.adId != null) || (this.adId != null && !this.adId.equals(other.adId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.Ad[ adId=" + adId + " ]";
    }
}
