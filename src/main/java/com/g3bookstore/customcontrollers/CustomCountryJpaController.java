package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.CountryJpaController;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Country;
import com.g3bookstore.entities.Country_;
import com.g3bookstore.entities.Province;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Custom controller for Countries.
 *
 * @author Alessandro Ciotola, Kevin Bui
 */
@Named("theCountry")
@RequestScoped
public class CustomCountryJpaController implements Serializable {

    private List<Country> countries = new ArrayList<>();
    private List<String> countryString = new ArrayList<>();

    @Inject
    private CountryJpaController countryJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    @PostConstruct
    public void init() {
        countries = (List<Country>) em.createNamedQuery("Country.findAll").getResultList();
    }

    /**
     * Returns an array of countries This website will only be deployed in
     * Canada for the present moment.
     *
     * @return The list of countries
     */
    public List<Country> getCountry() {
        return countries;
    }

    public List<String> countriesString() {
        countries.forEach((c) -> {
            countryString.add(c.getCountry());
        });

        return countryString;
    }

    /**
     * Allow to create a Country entity.
     *
     * @author Denis Lebedev
     * @param country
     * @throws Exception
     */
    public void create(Country country) throws Exception {
        countryJpaController.create(country);
    }

    /**
     * Allow to update a Country entity.
     *
     * @author Denis Lebedev
     * @param country
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(Country country) throws NonexistentEntityException, RollbackFailureException, Exception {
        countryJpaController.edit(country);
    }

    /**
     * Allow to delete a Country entity.
     *
     * @author Denis Lebedev
     * @param id
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        countryJpaController.destroy(id);
    }

    /**
     * Method which will find a specific province by ID
     *
     * @param id
     * @return The Country object
     */
    public Country getCountryByID(int id) {
        return countryJpaController.findCountry(id);
    }

    /**
     * Method which will search through the Country table for a country by name.
     *
     * @param countryName
     * @return The Country object
     */
    public Country getCountryByName(String countryName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Country> cq = cb.createQuery(Country.class);
        Root<Country> country = cq.from(Country.class);
        cq.select(country);
        cq.where(cb.equal(country.get(Country_.country), countryName));

        Query q = em.createQuery(cq);
        return ((Country) q.getSingleResult());
    }
}
