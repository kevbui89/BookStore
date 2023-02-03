package com.g3bookstore.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity class for Ebook Formats.
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "ebook_format", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "EbookFormat.findAll", query = "SELECT e FROM EbookFormat e")
    , @NamedQuery(name = "EbookFormat.findByFormatId", query = "SELECT e FROM EbookFormat e WHERE e.formatId = :formatId")
    , @NamedQuery(name = "EbookFormat.findByBookFormat", query = "SELECT e FROM EbookFormat e WHERE e.bookFormat = :bookFormat")})
public class EbookFormat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "format_id")
    private Integer formatId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "book_format")
    private String bookFormat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formatId")
    private List<Book> bookList;

    public EbookFormat() {
    }

    public EbookFormat(Integer formatId) {
        this.formatId = formatId;
    }

    public EbookFormat(Integer formatId, String bookFormat) {
        this.formatId = formatId;
        this.bookFormat = bookFormat;
    }

    public Integer getFormatId() {
        return formatId;
    }

    public void setFormatId(Integer formatId) {
        this.formatId = formatId;
    }

    public String getBookFormat() {
        return bookFormat;
    }

    public void setBookFormat(String bookFormat) {
        this.bookFormat = bookFormat;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formatId != null ? formatId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EbookFormat)) {
            return false;
        }
        EbookFormat other = (EbookFormat) object;
        if ((this.formatId == null && other.formatId != null) || (this.formatId != null && !this.formatId.equals(other.formatId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.EbookFormat[ formatId=" + formatId + " ]";
    }
    
}
