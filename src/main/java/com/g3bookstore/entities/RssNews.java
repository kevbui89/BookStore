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
 * Entity class for Rss Feeds.
 *
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "rss_news", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "RssNews.findAll", query = "SELECT r FROM RssNews r")
    , @NamedQuery(name = "RssNews.findByRssId", query = "SELECT r FROM RssNews r WHERE r.rssId = :rssId")
    , @NamedQuery(name = "RssNews.findByTitle", query = "SELECT r FROM RssNews r WHERE r.title = :title")
    , @NamedQuery(name = "RssNews.findByLink", query = "SELECT r FROM RssNews r WHERE r.link = :link")})
public class RssNews implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rss_id")
    private Integer rssId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "link")
    private String link;
    @Basic(optional = false)
    @NotNull
    @Column(name = "display")
    private Boolean display;

    public RssNews() {
    }

    public RssNews(Integer rssId) {
        this.rssId = rssId;
    }

    public RssNews(Integer rssId, String title, String link) {
        this.rssId = rssId;
        this.title = title;
        this.link = link;
    }

    public Integer getRssId() {
        return rssId;
    }

    public void setRssId(Integer rssId) {
        this.rssId = rssId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rssId != null ? rssId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RssNews)) {
            return false;
        }
        RssNews other = (RssNews) object;
        if ((this.rssId == null && other.rssId != null) || (this.rssId != null && !this.rssId.equals(other.rssId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.RssNews[ rssId=" + rssId + " ]";
    }
}
