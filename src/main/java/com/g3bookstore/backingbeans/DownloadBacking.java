package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomMasterInvoiceJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.MasterInvoice;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * This backing bean will take care of all the download functionality of the website
 * 
 * @author Kevin
 */
@Named("DownloadBacking")
@SessionScoped
public class DownloadBacking implements Serializable {
    
    @Inject
    private CustomMasterInvoiceJpaController mInvoiceController;

    /**
     * Returns all the e-books purchased by the current client.
     *
     * @param client The current client
     * @return The list of purchased e-books
     */
    public List<Book> getPurchasedBooks(Client client) {
        List<MasterInvoice> masterInvoices = mInvoiceController.getMasterInvoiceForClient(client);
        List<DetailInvoice> detailInvoices = new ArrayList<DetailInvoice>();
        List<Book> books = new ArrayList<Book>();

        for (MasterInvoice mi : masterInvoices) {
            List<DetailInvoice> detailInvoiceForMasterInvoice = mi.getDetailInvoiceList();
            for (int i = 0; i < detailInvoiceForMasterInvoice.size(); i++) {
                detailInvoices.add(detailInvoiceForMasterInvoice.get(i));
            }
        }

        for (DetailInvoice di : detailInvoices) {
            books.add(di.getBookId());
        }

        return books;
    }

    /**
     * Returns the file as a streamed content for downloading. Always the same 
     * file since we do not have copyrights for all the current books we have on 
     * our website.
     *
     * @return file to download.
     */
    public StreamedContent getDownloadEbook() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/ebooks/pg11-images.epub");
        StreamedContent file = new DefaultStreamedContent(stream, ec.getMimeType("pg11-images.epub"), "pg11-images.epub");
        return file;
    }
}
