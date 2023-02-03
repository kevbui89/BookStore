package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.AdJpaController;
import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Ad;
import com.g3bookstore.entities.Ad_;
import java.io.Serializable;
import java.util.List;
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
 * Custom controller for Ads.
 *
 * @author Alessandro Ciotola
 */
@Named("theAd")
@RequestScoped
public class CustomAdJpaController implements Serializable {

    @Inject
    private AdJpaController adJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a valid ad.
     *
     * @author Denis Lebedev
     * @param ad
     * @throws Exception
     */
    public void create(Ad ad) throws Exception {
        adJpaController.create(ad);
    }

    /**
     * Allow to update an ad.
     *
     * @author Denis Lebedev
     * @param ad
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(Ad ad) throws NonexistentEntityException, RollbackFailureException, Exception {
        adJpaController.edit(ad);
    }

    /**
     * Allow to delete an ad.
     *
     * @author Denis Lebedev
     * @param id
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        adJpaController.destroy(id);
    }

    /**
     * Method which will return all ads in the database
     *
     * @return
     */
    public List<Ad> getAds() {
        return adJpaController.findAdEntities();
    }

    /**
     * Method which will find a specific ad by ID
     *
     * @param id
     * @return
     */
    public Ad getAdByID(int id) {
        return adJpaController.findAd(id);
    }

    /**
     * Method which will search through the Ad table for a specific ad by title.
     *
     * @param title
     * @return The Ad object
     */
    public Ad getAdByTitle(String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ad> cq = cb.createQuery(Ad.class);
        Root<Ad> ad = cq.from(Ad.class);
        cq.select(ad);
        cq.where(cb.equal(ad.get(Ad_.title), title));

        Query q = em.createQuery(cq);
        return (Ad) q.getSingleResult();
    }

    /**
     * Gets all approved ads
     *
     * @param b
     * @return The list of ads chosen
     */
    public List<Ad> getChosenAd() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ad> query = cb.createQuery(Ad.class);
        Root<Ad> root = query.from(Ad.class);
        query.select(root);
        query.where(cb.equal(root.get(Ad_.chosen), true));

        return em.createQuery(query).getResultList();
    }
}
