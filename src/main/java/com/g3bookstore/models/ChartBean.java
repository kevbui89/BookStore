package com.g3bookstore.models;

import com.g3bookstore.customcontrollers.CustomSurveyJpaController;
import com.g3bookstore.entities.Response;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 * Responsible for getting survey data and filling the bar chart.
 *
 * @author Alessandro Ciotola
 * @version 27/02/2018
 * @since 1.8
 */
@Named(value = "chartBean")
@ManagedBean
@SessionScoped
public class ChartBean implements Serializable 
{
    @Inject
    private CustomSurveyJpaController surveyController;
    
    private HorizontalBarChartModel horizontalBarModel;
 
    /**
     * Method which will be executed after dependency injection is done to 
     * perform any initialization such as the bar chart.
     * 
     */
    @PostConstruct
    public void init() {
        createHorizontalBarModel();
    }
     
    /**
     * Method which will return the latest bar chart.
     * 
     * @return the bar chart.
     */
    public HorizontalBarChartModel getHorizontalBarModel() 
    {
        createHorizontalBarModel();
        return horizontalBarModel;
    }
     
    
    /**
     * Method which will create the bar chart
     * 
     */
    private void createHorizontalBarModel()
    {
        horizontalBarModel = new HorizontalBarChartModel();        
        List<Response> responseList = surveyController.getCurrentQuestion().getResponseList();
        
        //Get total amount of times all answers were chosen.
        double totalResponses = 0;
        for(int i = 0; i < responseList.size(); i++)
            totalResponses = totalResponses + responseList.get(i).getTotalAnswer();        
        
        //Do calculations for percentage and fill chart
        for(int i = 0; i < responseList.size(); i++)
        {
            double percentage = (responseList.get(i).getTotalAnswer() / totalResponses) * 100;
            ChartSeries chart = new ChartSeries();
            chart.setLabel(responseList.get(i).getAnswer());
            chart.set(" ", percentage);
            horizontalBarModel.addSeries(chart);
        }     
         
        //horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("ne");
        
        //Set Axis information
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setTickFormat("%d%");
        xAxis.setLabel("Total Responses");
        xAxis.setMax(0);
        xAxis.setMax(100);
         
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Response");        
    }
}
