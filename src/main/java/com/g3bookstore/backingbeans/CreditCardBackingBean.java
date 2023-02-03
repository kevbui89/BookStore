package com.g3bookstore.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 * Class responsible for holding credit card data. 
 *
 * @author Alessandro Ciotola
 * @version 09/03/2018
 * @since 1.8
 */
@Named("cardBean")
@SessionScoped
public class CreditCardBackingBean implements Serializable
{
    private String cardType = "";
    private String cardHolderName = "";
    private String cardNumber = "";
    private String cardMonth = "";
    private String cardYear = "";
    private String cardSecurity = "";
    private List<SelectItem> selectIndexMonths = new ArrayList<>();
    private List<SelectItem> selectIndexYears = new ArrayList<>();
    
    /**
     * Method responsible for filling up the options before the page is rendered.
     * 
     */
    @PostConstruct
    public void init()
    {   
        fillCardMonths();
        fillCardYears();
    }
    
    /**
     * Method responsible for filling the select options when selecting a credit card
     * expiry month.
     * 
     */
    private void fillCardMonths()
    {
        for(int i = 1; i < 13; i++)
        {
            if(i < 10)
                selectIndexMonths.add(new SelectItem(i, "0"+i));
            else
                selectIndexMonths.add(new SelectItem(i, ""+i));
        }
    }
    
    /**
     * Method responsible for filling the select options when selecting a credit card
     * expiry year.
     * 
     */
    private void fillCardYears()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        
        for(int i = year; i < year + 21; i++)
        {
            selectIndexYears.add(new SelectItem(i, ""+i));
        }
    }
    
    /**
     *  Method responsible for returning the 12 numbers representing the month
     * 
     * @return 
     */
    public List<SelectItem> getCardMonths()
    {
        return selectIndexMonths;
    }
    
    /**
     *  Method responsible for returning the 20 numbers representing the years
     * from the current year to 20 years ahead.
     * 
     * @return 
     */
    public List<SelectItem> getCardYears()
    {
        return selectIndexYears;
    }
    
    /**
     * Method responsible for getting the card holders name.
     * 
     * @return The card holders name.
     */
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * Method responsible for setting the card holders name.
     * 
     * @param cardHolderName 
     */
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
    
    /**
     * Method responsible for getting the card type.
     * 
     * @return The card type.
     */
    public String getCardType()
    {
        return cardType;
    }
    
    /**
     * Method responsible for setting the card type.
     * 
     * @param cardType 
     */
    public void setCardType(String cardType)
    {
        this.cardType = cardType;
    }

    /**
     * Method responsible for getting the card number.
     * 
     * @return The card number.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Method responsible for setting the card number.
     * 
     * @param cardNumber 
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Method responsible for getting the card month.
     * 
     * @return The card month.
     */
    public String getCardMonth() {
        return cardMonth;
    }

    /**
     * Method responsible for setting the card month.
     * 
     * @param cardMonth 
     */
    public void setCardMonth(String cardMonth) {
        this.cardMonth = cardMonth;
    }

    /**
     * Method responsible for getting the card year.
     * 
     * @return The card year.
     */
    public String getCardYear() {
        return cardYear;
    }

    /**
     * Method responsible for setting the card year.
     * 
     * @param cardYear 
     */
    public void setCardYear(String cardYear) {
        this.cardYear = cardYear;
    }

    /**
     * Method responsible for getting the card security number.
     * 
     * @return The security number.
     */
    public String getCardSecurity() {
        return cardSecurity;
    }

    /**
     * Method responsible for getting the card security number.
     * 
     * @param cardSecurity 
     */
    public void setCardSecurity(String cardSecurity) {
        this.cardSecurity = cardSecurity;
    }   
}