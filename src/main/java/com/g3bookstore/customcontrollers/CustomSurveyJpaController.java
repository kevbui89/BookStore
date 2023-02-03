package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.SurveyJpaController;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Response;
import com.g3bookstore.entities.Survey;
import com.g3bookstore.entities.Survey_;

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
 * Custom controller for Surveys.
 *
 * @author Alessandro Ciotola, Denis Lebedev
 */
@Named("theSurvey")
@RequestScoped
public class CustomSurveyJpaController implements Serializable {

    @Inject
    private SurveyJpaController surveyJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a Survey entity.
     *
     * @param surv
     * @throws Exception
     */
    public void create(Survey surv) throws Exception {
        surveyJpaController.create(surv);
    }

    /**
     * Allow to update a Survey entity.
     *
     * @param surv
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(Survey surv) throws NonexistentEntityException, RollbackFailureException, Exception {
        surveyJpaController.edit(surv);
    }

    /**
     * Allow to delete a Survey entity.
     *
     * @param id
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws RollbackFailureException, Exception {
        surveyJpaController.destroy(id);
    }

    /**
     * Method which will return all surveys in the database
     *
     * @return
     */
    public List<Survey> getSurveys() {
        return surveyJpaController.findSurveyEntities();
    }

    /**
     * Method which will find a specific survey by ID
     *
     * @param id
     * @return
     */
    public Survey getSurveyByID(int id) {
        return surveyJpaController.findSurvey(id);
    }

    /**
     * Method which will search through the Survey table for a specific survey
     * by question.
     *
     * @param question
     * @return The Survey object
     */
    public Survey getSurveyByQuestion(String question) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> survey = cq.from(Survey.class);
        cq.select(survey);
        cq.where(cb.equal(survey.get(Survey_.question), question));

        Query q = em.createQuery(cq);
        return (Survey) q.getSingleResult();
    }

    /**
     * Method which will return a list of all responses for a given survey
     * question.
     *
     * @param id
     * @return The Survey object
     */
    public List<Response> getResponsesBySurvey(int id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> survey = cq.from(Survey.class);
        cq.select(survey);
        cq.where(cb.equal(survey.get(Survey_.surveyId), id));

        Query q = em.createQuery(cq);
        Survey newSurvey = (Survey) q.getSingleResult();
        List<Response> responses = newSurvey.getResponseList();
        return responses;
    }
    
    /**
     * Method which will return the current question.
     *
     * @return The Survey object
     */
    public Survey getCurrentQuestion() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> survey = cq.from(Survey.class);
        cq.select(survey);
        cq.where(cb.equal(survey.get(Survey_.chosen), true));

        Query q = em.createQuery(cq);
        Survey newSurvey = (Survey) q.getSingleResult();
        return newSurvey;
    }
}
