package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Response;
import com.g3bookstore.entities.Survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Generated controller updated to use CDI
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Named("Response")
@RequestScoped
public class ResponseJpaController implements Serializable 
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(Response response) throws RollbackFailureException, Exception 
    {
        if (response.getSurveyList() == null) {
            response.setSurveyList(new ArrayList<Survey>());
        }
        try {
            utx.begin();
            List<Survey> attachedSurveyList = new ArrayList<Survey>();
            for (Survey surveyListSurveyToAttach : response.getSurveyList()) {
                surveyListSurveyToAttach = em.getReference(surveyListSurveyToAttach.getClass(), surveyListSurveyToAttach.getSurveyId());
                attachedSurveyList.add(surveyListSurveyToAttach);
            }
            response.setSurveyList(attachedSurveyList);
            em.persist(response);
            for (Survey surveyListSurvey : response.getSurveyList()) {
                surveyListSurvey.getResponseList().add(response);
                surveyListSurvey = em.merge(surveyListSurvey);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } 
    }

    public void edit(Response response) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Response persistentResponse = em.find(Response.class, response.getResponseId());
            List<Survey> surveyListOld = persistentResponse.getSurveyList();
            List<Survey> surveyListNew = response.getSurveyList();
            List<Survey> attachedSurveyListNew = new ArrayList<Survey>();
            for (Survey surveyListNewSurveyToAttach : surveyListNew) {
                surveyListNewSurveyToAttach = em.getReference(surveyListNewSurveyToAttach.getClass(), surveyListNewSurveyToAttach.getSurveyId());
                attachedSurveyListNew.add(surveyListNewSurveyToAttach);
            }
            surveyListNew = attachedSurveyListNew;
            response.setSurveyList(surveyListNew);
            response = em.merge(response);
            for (Survey surveyListOldSurvey : surveyListOld) {
                if (!surveyListNew.contains(surveyListOldSurvey)) {
                    surveyListOldSurvey.getResponseList().remove(response);
                    surveyListOldSurvey = em.merge(surveyListOldSurvey);
                }
            }
            for (Survey surveyListNewSurvey : surveyListNew) {
                if (!surveyListOld.contains(surveyListNewSurvey)) {
                    surveyListNewSurvey.getResponseList().add(response);
                    surveyListNewSurvey = em.merge(surveyListNewSurvey);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = response.getResponseId();
                if (findResponse(id) == null) {
                    throw new NonexistentEntityException("The response with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Response response;
            try {
                response = em.getReference(Response.class, id);
                response.getResponseId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The response with id " + id + " no longer exists.", enfe);
            }
            List<Survey> surveyList = response.getSurveyList();
            for (Survey surveyListSurvey : surveyList) {
                surveyListSurvey.getResponseList().remove(response);
                surveyListSurvey = em.merge(surveyListSurvey);
            }
            em.remove(response);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public List<Response> findResponseEntities() {
        return findResponseEntities(true, -1, -1);
    }

    public List<Response> findResponseEntities(int maxResults, int firstResult) {
        return findResponseEntities(false, maxResults, firstResult);
    }

    private List<Response> findResponseEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Response.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();        
    }

    public Response findResponse(Integer id) 
    {
        return em.find(Response.class, id);
    }

    public int getResponseCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Response> rt = cq.from(Response.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}