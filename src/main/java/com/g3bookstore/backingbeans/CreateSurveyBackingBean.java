package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomResponseJpaController;
import com.g3bookstore.customcontrollers.CustomSurveyJpaController;
import com.g3bookstore.entities.Response;
import com.g3bookstore.entities.Survey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class allow a manager to create a survey by filling the given form and
 * respecting the conditions
 *
 * @author Denis Lebedev
 */
@Named("createSurvey")
@SessionScoped
public class CreateSurveyBackingBean implements Serializable {

    private final static Logger log = Logger.getLogger(CreateSurveyBackingBean.class.getName());

    @Inject
    private CustomSurveyJpaController surveyC;

    @Inject
    private CustomResponseJpaController respC;

    private Survey survey;
    private List<Response> respList;
    private int totalQ;
    private final int maxResponse = 15;

    @PostConstruct
    public void init() {
        log.log(Level.INFO, "Init default variable");
        totalQ = 2;
        //Allow to contain all the repsonse possbile writen by the manager
        respList = new ArrayList();
        for (int i = 0; i < maxResponse; i++) {
            respList.add(new Response());
        }
    }
    
    public int getMaxResponse() {
        return maxResponse;
    }
    
    public int getTotalQ() {
        return totalQ;
    }

    public void setTotalQ(int totalQ) {
        this.totalQ = totalQ;
    }

    public Survey getSurvey() {
        if (survey == null) {
            survey = new Survey();
        }
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public List<Response> getResponses() {
        return respList;
    }

    public Response getResponseAt(int index) {
        log.log(Level.INFO, "Accessing at index:{0}", index);
        return respList.get(index);
    }

    public void setResponses(List<Response> respList) {
        this.respList = respList;
    }

    /**
     * Create a survey object by creating the response objects before and assign
     * it to the survey.
     *
     * @return
     * @throws Exception
     */
    public String createSurvey() throws Exception {
        log.log(Level.INFO, "Creating survey");

        respList = respList.subList(0, totalQ);

        log.log(Level.INFO, "Survey Q:{0}", survey.getQuestion());

        for (Response r : respList) {
            log.log(Level.INFO, "Given answer subListed: {0}", r.getAnswer());
        }

        for (Response r : respList) {
            respC.create(r);
        }

        survey.setResponseList(respList);
        survey.setChosen(false);

        surveyC.create(survey);

        return null;
    }
}
