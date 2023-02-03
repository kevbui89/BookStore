package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomDetailInvoiceJpaController;
import com.g3bookstore.customcontrollers.CustomMasterInvoiceJpaController;
import com.g3bookstore.customcontrollers.CustomTaxJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.MasterInvoice;
import com.g3bookstore.util.EmailSender;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;

/**
 * Responsible for backing checkout info and results.
 *
 * @author Alessandro Ciotola
 * @version 08/03/2018
 * @since 1.8
 */
@Named("checkoutBean")
@ManagedBean
@SessionScoped
public class CheckoutBackingBean implements Serializable {

    @Inject
    private CustomClientJpaController clientController;

    @Inject
    private CustomTaxJpaController taxController;

    @Inject
    private CustomMasterInvoiceJpaController masterInvoiceController;

    @Inject
    private CustomDetailInvoiceJpaController detailInvoiceController;

    private Logger log = Logger.getLogger(CheckoutBackingBean.class.getName());
    private List<Book> bookList = new ArrayList<>();
    private Client client;
    private double totalWithTax = 0;   

    public List<Book> getBookList() {
        return bookList;
    }
    
    /**
     * Method responsible for setting the book list.
     *
     * @param bookList
     */
    public void setBookList(final List<Book> bookList) {
        this.bookList = bookList;
    }

    /**
     * Method responsible for returning the tax of the purchase.
     *
     * @return
     */
    public double getTax() {
        String province = getProvince();
        double tax = getTaxRate(province);
        double total = getPriceNoTax();
        return Math.round(getTaxTotal(total, tax) * 100.0) / 100.0;
    }

    /**
     * Method responsible for returning the current client in order to get their
     * province.
     *
     * @return the client object.
     */
    public String getProvince() {
        Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("G3Cookie");
        client = clientController.getClientByEmail(cookie.getValue()).get(0);

        String province = client.getProvinceId().getProvince();
        return province;
    }

    /**
     * Method responsible for returning the tax rate of a province.
     *
     * @param province
     * @return the tax rate.
     *
     */
    private double getTaxRate(String province) {
        double tax;
        if (taxController.getTaxByProvince(province).getGst().doubleValue() != 0) {
            tax = taxController.getTaxByProvince(province).getGst().doubleValue();
        } else if (taxController.getTaxByProvince(province).getHst().doubleValue() != 0) {
            tax = taxController.getTaxByProvince(province).getHst().doubleValue();
        } else {
            tax = taxController.getTaxByProvince(province).getPst().doubleValue();
        }
        return tax;
    }

    /**
     * Method responsible for getting the total price of books before the tax.
     *
     * @return
     */
    public double getPriceNoTax() {
        double total = 0;
        
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getSalePrice().doubleValue() > 0) {
                total += bookList.get(i).getSalePrice().doubleValue();
            } else {
                total += bookList.get(i).getListPrice().doubleValue();
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Method responsible for getting the tax of the purchase.
     *
     * @param total
     * @param taxRate
     * @return
     */
    private double getTaxTotal(double total, double taxRate) {
        return Math.round((total * (taxRate / 100)) * 100.00) / 100.00;
    }

    /**
     * Method responsible for returning the total price of all books + tax.
     *
     * @return total price.
     */
    public double getTotal() {
        String province = getProvince();
        double tax = getTaxRate(province);
        double total = getPriceNoTax();
        totalWithTax = getTaxTotal(total, tax);

        return Math.round((totalWithTax + total) * 100.00) / 100.00;
    }

    /**
     * Formats the double in the correct format
     *
     * @param d
     * @return
     */
    public String formatDouble(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    /**
     * Method responsible for confirming the purchase and saving the invoice to
     * the database.
     *
     * @throws java.lang.Exception
     */
    public void checkout() throws Exception {
        MasterInvoice ma = new MasterInvoice();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        ma.setClientId(client);
        ma.setGrossValue(BigDecimal.valueOf(getTotal()));
        ma.setNetValue(BigDecimal.valueOf(getPriceNoTax()));
        ma.setSaleDate(Date.valueOf(LocalDate.of(year, month + 1, day)));

        masterInvoiceController.create(ma);

        for (int i = 0; i < bookList.size(); i++) {
            DetailInvoice di = new DetailInvoice();
            di.setBookId(bookList.get(i));
            if (bookList.get(i).getSalePrice().doubleValue() != 0) {
                di.setBookPrice(bookList.get(i).getSalePrice());
            } else {
                di.setBookPrice(bookList.get(i).getListPrice());
            }
            di.setGst(taxController.getTaxByProvince(client.getProvinceId().getProvince()).getGst());
            di.setHst(taxController.getTaxByProvince(client.getProvinceId().getProvince()).getHst());
            di.setPst(taxController.getTaxByProvince(client.getProvinceId().getProvince()).getPst());
            di.setInvoiceId(ma);

            detailInvoiceController.create(di);
        }

        EmailSender send = new EmailSender(client);
        ma = masterInvoiceController.getMasterInvoiceByID(masterInvoiceController.getMasterInvoiceId());
        send.sendEmail(ma);

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/client/confirmed_purchase.xhtml");
    }    
}
