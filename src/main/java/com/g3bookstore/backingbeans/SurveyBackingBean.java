package com.g3bookstore.backingbeans;

import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.customcontrollers.CustomResponseJpaController;
import com.g3bookstore.customcontrollers.CustomSurveyJpaController;
import com.g3bookstore.entities.Response;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Responsible for backing survey display and results.
 *
 * @author Alessandro Ciotola
 * @version 21/02/2018
 * @since 1.8
 */
@Named("surveyBackingBean")
@ManagedBean
@SessionScoped
public class SurveyBackingBean implements Serializable
{
    @Inject
    private CustomSurveyJpaController surveyController;
    
    @Inject
    private CustomResponseJpaController responseController;

    private boolean chartPanel = false;
    private boolean answerPanel = true;
    
    /**
     * Returns the current question of the survey.
     *
     * @return the current question of the survey.
     */
    public String getQuestion()
    {
        return surveyController.getCurrentQuestion().getQuestion();
    }
    
    /**
     * Returns the list of answers for the question.
     *
     * @return the list of answers for the survey.
     */
    public List<Response> getAnswers()
    {
        return surveyController.getCurrentQuestion().getResponseList();
    }
    
    /**
     * Method responsible for looking at the answer the user chose and then 
     * updating the survey/response tables.
     * 
     * @param response
     * @throws com.g3bookstore.controllers.exceptions.RollbackFailureException
     */
    public void usersChoice(int response) throws RollbackFailureException, Exception
    {
        chartPanel = true;
        answerPanel = false;
        
        Response rep = responseController.getResponseByID(response);
        rep.setTotalAnswer(rep.getTotalAnswer() + 1);
        responseController.update(rep);        
    }
    
    /**
     * Returns the boolean answer if the panel is visible or not.
     *
     * @return the boolean answer if the panel is visible or not.
     */
    public boolean getAnswerPanel()
    {
        return answerPanel;
    }
    
    /**
     * Method responsible for setting the the boolean if the panel is visible or not.
     * 
     * @param answerPanel 
     */
    public void setAnswerPanel(boolean answerPanel)
    {
        this.answerPanel = answerPanel;
    }
    
    /**
     * Returns the boolean answer if the panel is visible or not.
     *
     * @return the boolean answer if the panel is visible or not.
     */
    public boolean getChartPanel()
    {
        return chartPanel;
    }
    
    /**
     * Method responsible for setting the the boolean if the panel is visible or not.
     * 
     * @param chartPanel 
     */
    public void setChartPanel(boolean chartPanel)
    {
        this.chartPanel = chartPanel;
    }
}