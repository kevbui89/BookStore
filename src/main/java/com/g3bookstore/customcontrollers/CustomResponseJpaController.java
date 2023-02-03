package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.ResponseJpaController;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Response;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Custom controller for Responses.
 *
 * @author Alessandro Ciotola, Denis Lebedev
 */
@Named("theResponse")
@RequestScoped
public class CustomResponseJpaController implements Serializable {

    @Inject
    private ResponseJpaController responseJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a Response entity.
     *
     * @param rep
     * @throws Exception
     */
    public void create(Response rep) throws Exception {
        responseJpaController.create(rep);
    }

    /**
     * Allow to update a Response entity.
     *
     * @param rep
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(Response rep) throws NonexistentEntityException, RollbackFailureException, Exception {
        responseJpaController.edit(rep);
    }

    /**
     * Allow to delete a Response entity.
     *
     * @param id
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws RollbackFailureException, Exception {
        responseJpaController.destroy(id);
    }

    /**
     * Method which will return all responses in the database
     *
     * @return
     */
    public List<Response> getResponses() {
        return responseJpaController.findResponseEntities();
    }

    /**
     * Method which will find a specific response by ID
     *
     * @param id
     * @return
     */
    public Response getResponseByID(int id) {
        return responseJpaController.findResponse(id);
    }
}
