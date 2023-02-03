package com.g3bookstore.backingbeans;

import java.io.OutputStream;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * Bean that will take care of converting a page into PDF.
 *
 * @author Kevin
 */
@ManagedBean(name = "printBackingBean")
@SessionScoped
public class PrintBackingBean implements Serializable {

    private Logger log = Logger.getLogger(PrintBackingBean.class.getName());

    /**
     * Creates a new instance of PrintBackingBean
     */
    public PrintBackingBean() {
    }

    /**
     * Creates a PDF for the user
     */
    public void createPDF() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(true);
        String url = "http://localhost:8080/G3BookStore/client/print_invoice.xhtml;jsessionid=" + session.getId() + "?pdf=true";
        try {
           ITextRenderer renderer = new ITextRenderer();
           renderer.setDocument(new URL(url).toString());
           renderer.layout();
           HttpServletResponse response = (HttpServletResponse) ec.getResponse();
           response.reset();
           response.setContentType("application/pdf");
           response.setHeader("Content-Disposition", "inline; filename=\"print-file.pdf\"");
           OutputStream os = response.getOutputStream();
           renderer.createPDF(os);
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error making PDF: {0}", ex.getMessage());
        }
        fc.responseComplete();
    }
}
