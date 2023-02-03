package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.ProvinceJpaController;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
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

/**
 * Custom controller for Provinces.
 *
 * @author Alessandro Ciotola, Kevin Bui
 */
@Named("theProvince")
@RequestScoped
public class CustomProvinceJpaController implements Serializable {

    private List<Province> provinces;
    private List<String> provincesString = new ArrayList<>();

    @Inject
    private ProvinceJpaController provincesJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Returns a list of all Canadian provinces
     *
     * @return A List of all Canadian provinces
     */
    public List<Province> getProvinces() {
        return provinces;
    }

    /**
     * Returns an array of string of the provinces
     *
     * @return
     */
    public List<String> getProvinceString() {
        for (Province p : provinces) {
            provincesString.add(p.getProvince());
        }

        return provincesString;
    }

    /**
     * Gets the list of all provinces from the database.
     */
    @PostConstruct
    public void init() {
        provinces = (List<Province>) em.createNamedQuery("Province.findAll").getResultList();
    }

    /**
     * Allow to create a Province entity.
     *
     * @author Denis Lebedev
     * @param province
     * @throws Exception
     */
    public void create(Province province) throws Exception {
        provincesJpaController.create(province);
    }

    /**
     * Allow to update a Province entity.
     *
     * @author Denis Lebedev
     * @param province
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(Province province) throws NonexistentEntityException, RollbackFailureException, Exception {
        provincesJpaController.edit(province);
    }

    /**
     * Allow to delete a Province entity.
     *
     * @author Denis Lebedev
     * @param id
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        provincesJpaController.destroy(id);
    }

    /**
     * Method which will find a specific province by ID
     *
     * @param id
     * @return The Province object.
     */
    public Province getProvinceByID(int id) {
        return provincesJpaController.findProvince(id);
    }
}
