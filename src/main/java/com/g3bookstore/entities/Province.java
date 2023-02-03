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
 * Entity class for Provinces.
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "province", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "Province.findAll", query = "SELECT p FROM Province p")
    , @NamedQuery(name = "Province.findByProvinceId", query = "SELECT p FROM Province p WHERE p.provinceId = :provinceId")
    , @NamedQuery(name = "Province.findByProvince", query = "SELECT p FROM Province p WHERE p.province = :province")})
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "province_id")
    private Integer provinceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "province")
    private String province;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provinceId")
    private List<Tax> taxList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provinceId")
    private List<Client> clientList;

    public Province() {
    }

    public Province(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Province(Integer provinceId, String province) {
        this.provinceId = provinceId;
        this.province = province;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<Tax> getTaxList() {
        return taxList;
    }

    public void setTaxList(List<Tax> taxList) {
        this.taxList = taxList;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (provinceId != null ? provinceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Province)) {
            return false;
        }
        Province other = (Province) object;
        if ((this.provinceId == null && other.provinceId != null) || (this.provinceId != null && !this.provinceId.equals(other.provinceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.Province[ provinceId=" + provinceId + " ]";
    }
    
}
