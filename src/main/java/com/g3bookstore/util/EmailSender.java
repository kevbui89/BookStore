package com.g3bookstore.util;

import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.MasterInvoice;
import com.g3bookstore.entities.Province;
import com.g3bookstore.entities.Tax;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;
import java.text.SimpleDateFormat;

/**
 * This class will be responsible for sending email to the client when
 * requested.
 *
 * @author Kevin
 * @version 1.0
 */
public class EmailSender {

    private final String mankabooksEmail = "mankabookstore@gmail.com";
    private final String mankabooksPw = "mankabooks1";
    private final String smtpUrl = "smtp.gmail.com";
    private final int smtpPort = 465;
    private Client client;
    private ResourceBundle msgs;

    /**
     * Instantiates the object.
     *
     * @param c The client object
     */
    public EmailSender(Client c) {
        this.client = c;
        this.msgs = currentLocale();
    }

    /**
     * Sends an email with the master invoice information to a client.
     *
     * @param mi
     */
    public void sendEmail(MasterInvoice mi) {
        Email email = new Email();
        String message = createMessage(mi);
        email.from(mankabooksEmail).to(client.getEmail()).bcc(mankabooksEmail)
                .addHtml(message).subject("MankabookS - " + msgs.getString("invoice"));

        SmtpServer<SmtpSslServer> smtpServer = SmtpSslServer
                .create(smtpUrl, smtpPort)
                .authenticateWith(mankabooksEmail, mankabooksPw);

        SendMailSession session = smtpServer.createSession();
        session.open();
        session.sendMail(email);
        session.close();
    }

    /**
     * Creates a message to the user inside the email.
     *
     * @param order The newly created order for this customer.
     * @return a message to the user inside the email.
     */
    private String createMessage(MasterInvoice mi) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        // Print overall details
        String message = "<h2><i>MankabookS " + msgs.getString("invoice") + "</i></h2><h4>" + msgs.getString("thankyoushop") + "</h4>"
                + "<b>" + msgs.getString("invoiceid") + ": </b>" + mi.getInvoiceId() + "<br/>"
                + "<b>" + msgs.getString("purchasedate") + ": </b>"
                + sdf.format(mi.getSaleDate()) + "<br/>";

        message += "<h4>" + msgs.getString("items") + "</h4><hr/>";

        List<DetailInvoice> items = mi.getDetailInvoiceList();

        // Print each item of the master invoice
        for (DetailInvoice di : items) {
            message += "<b>" + msgs.getString("itemno") + ": </b>" + di.getBookId().getBookId() + "<br/>";
            if (di.getBookId() != null) {
                message += "<b>" + msgs.getString("title") + ": </b>" + di.getBookId().getTitle() + "<br/>"
                        + "<b>" + msgs.getString("book_isbn") + ": </b>" + di.getBookId().getIsbn() + "<br/>"
                        + "<b>" + msgs.getString("book_price") + ": </b>$"
                        + Math.round(di.getBookPrice().doubleValue() * 100) / 100.0 + "<br/><hr/>";
            }
        }

        message += "<b>" + msgs.getString("subtotal") + ": </b>$" + Math.round(mi.getNetValue().doubleValue() * 100) / 100.0 + "<br/>";

        Province province = client.getProvinceId();
        List<Tax> taxes = province.getTaxList();

        // Get all 3 taxes, convert them into percentages
        if (taxes.get(0).getGst().intValue() != 0) {
            message += "<b>" + msgs.getString("gst") + ": </b>" + (Math.round(taxes.get(0).getGst().doubleValue() * 100000) / 100000.0) + "%<br/>";
        }
        if (taxes.get(0).getPst().intValue() != 0) {
            message += "<b>" + msgs.getString("pst") + ": </b>" + (Math.round(taxes.get(0).getPst().doubleValue() * 100000) / 100000.0) + "%<br/>";
        }
        if (taxes.get(0).getHst().intValue() != 0) {
            message += "<b>" + msgs.getString("hst") + ": </b>" + (Math.round(taxes.get(0).getHst().doubleValue() * 100000) / 100000.0) + "%<br/>";
        }

        message += "<b>" + msgs.getString("total") + ": </b>$" + Math.round(mi.getGrossValue().doubleValue() * 100) / 100.0 + "<br/>";

        return message;
    }

    /**
     * Returns the bundle of the user. Used to send the email in french or
     * english depending on the user's cookie value.
     *
     * @return The message bundle
     */
    private ResourceBundle currentLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            return ResourceBundle.getBundle("MessageBundle");
        }

        Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
        Object cookie = cookieMap.get("G3Cookie");

        if (cookie != null) {
            String locale = ((Cookie) cookie).getValue();
            if (locale.equals("en_CA")) {
                return ResourceBundle.getBundle("MessageBundle", Locale.CANADA);
            } else if (locale.equals("fr_CA")) {
                return ResourceBundle.getBundle("MessageBundle", Locale.CANADA_FRENCH);
            }
        }

        return ResourceBundle.getBundle("MessageBundle");
    }

}
