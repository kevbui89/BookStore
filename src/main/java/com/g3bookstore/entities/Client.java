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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity class for Clients.
 *
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "client", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
    , @NamedQuery(name = "Client.findByClientId", query = "SELECT c FROM Client c WHERE c.clientId = :clientId")
    , @NamedQuery(name = "Client.findByPassword", query = "SELECT c FROM Client c WHERE c.password = :password")
    , @NamedQuery(name = "Client.findByTitle", query = "SELECT c FROM Client c WHERE c.title = :title")
    , @NamedQuery(name = "Client.findByUsername", query = "SELECT c FROM Client c WHERE c.username = :username")
    , @NamedQuery(name = "Client.findByEmail", query = "SELECT c FROM Client c WHERE c.email = :email")
    , @NamedQuery(name = "Client.findByLastName", query = "SELECT c FROM Client c WHERE c.lastName = :lastName")
    , @NamedQuery(name = "Client.findByFirstName", query = "SELECT c FROM Client c WHERE c.firstName = :firstName")
    , @NamedQuery(name = "Client.findByCompany", query = "SELECT c FROM Client c WHERE c.company = :company")
    , @NamedQuery(name = "Client.findByAddressOne", query = "SELECT c FROM Client c WHERE c.addressOne = :addressOne")
    , @NamedQuery(name = "Client.findByAddressTwo", query = "SELECT c FROM Client c WHERE c.addressTwo = :addressTwo")
    , @NamedQuery(name = "Client.findByPostalCode", query = "SELECT c FROM Client c WHERE c.postalCode = :postalCode")
    , @NamedQuery(name = "Client.findByHomeTel", query = "SELECT c FROM Client c WHERE c.homeTel = :homeTel")
    , @NamedQuery(name = "Client.findByCellTel", query = "SELECT c FROM Client c WHERE c.cellTel = :cellTel")
    , @NamedQuery(name = "Client.findByCity", query = "SELECT c FROM Client c WHERE c.city = :city")
    , @NamedQuery(name = "Client.findByManager", query = "SELECT c FROM Client c WHERE c.manager = :manager")})
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "client_id")
    private Integer clientId;
    @Size(max = 30)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "username")
    private String username;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "address_one")
    private String addressOne;
    @Size(max = 80)
    @Column(name = "address_two")
    private String addressTwo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "postal_code")
    private String postalCode;
    @Size(max = 20)
    @Column(name = "home_tel")
    private String homeTel;
    @Size(max = 20)
    @Column(name = "cell_tel")
    private String cellTel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Column(name = "manager")
    private boolean manager;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId")
    private List<MasterInvoice> masterInvoiceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId")
    private List<Review> reviewList;
    @JoinColumn(name = "province_id", referencedColumnName = "province_id")
    @ManyToOne(optional = false)
    private Province provinceId;
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    @ManyToOne(optional = false)
    private Country countryId;
    @JoinColumn(name = "last_genre_visited", referencedColumnName = "genre_id")
    @ManyToOne
    private Genre lastGenreVisited;
    @Basic(optional = false)
    @NotNull
    @Column(name = "first_login")
    private boolean firstLogin;

    public Client() {
    }

    public Client(Integer clientId) {
        this.clientId = clientId;
    }

    public Client(Integer clientId, String title, String username, String email, String lastName, String firstName, String company, String addressOne, String postalCode, String city, boolean manager) {
        this.clientId = clientId;
        this.title = title;
        this.username = username;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.company = company;
        this.addressOne = addressOne;
        this.postalCode = postalCode;
        this.city = city;
        this.manager = manager;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddressOne() {
        return addressOne;
    }

    public void setAddressOne(String addressOne) {
        this.addressOne = addressOne;
    }

    public String getAddressTwo() {
        return addressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getHomeTel() {
        return homeTel;
    }

    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
    }

    public String getCellTel() {
        return cellTel;
    }

    public void setCellTel(String cellTel) {
        this.cellTel = cellTel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean getManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public List<MasterInvoice> getMasterInvoiceList() {
        return masterInvoiceList;
    }

    public void setMasterInvoiceList(List<MasterInvoice> masterInvoiceList) {
        this.masterInvoiceList = masterInvoiceList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public Province getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Province provinceId) {
        this.provinceId = provinceId;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public Genre getLastGenreVisited() {
        return lastGenreVisited;
    }

    public void setLastGenreVisited(Genre lastGenreVisited) {
        this.lastGenreVisited = lastGenreVisited;
    }

    public boolean getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientId != null ? clientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.clientId == null && other.clientId != null) || (this.clientId != null && !this.clientId.equals(other.clientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  provinceId + " ";
    }

  
    
    
}
