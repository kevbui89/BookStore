package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomDetailInvoiceJpaController;
import com.g3bookstore.customcontrollers.CustomMasterInvoiceJpaController;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.MasterInvoice;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class responsible for managing the order management page
 *
 * @author Werner
 */
@Named("OrderManagement")
@SessionScoped
public class OrderManagementBackingBean implements Serializable {

    @Inject
    private CustomMasterInvoiceJpaController masterController;
    @Inject
    private CustomDetailInvoiceJpaController detailController;

    private boolean status;
    private MasterInvoice selectedMasterInvoice;
    private DetailInvoice di;

    /**
     * Getter/Helper function for returning the status of the status button
     *
     * @return
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Sets the status of the button
     *
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Returns a list of all the invoices on the db
     *
     * @return
     */
    public List<MasterInvoice> getInvoices() {
        return masterController.getMasterInvoices();
    }

    /**
     * Returns a list of all the Detail invoice on the db
     *
     * @return
     */
    public List<DetailInvoice> getDetailInvoices() {
        return detailController.getDetailedInvoice();
    }

    /**
     * Removes a single book from the invoice
     *
     * @param di
     * @throws Exception
     */
    public void removeBook(DetailInvoice di) throws Exception {
        this.di = detailController.getDetailInvoiceByID(di.getDetailId());
        this.di.setStatus(!di.getStatus());
        detailController.update(this.di);
    }

    /**
     * Cancels the order
     *
     * @return
     */
    public MasterInvoice getSelectedMasterInvoice() {
        return selectedMasterInvoice;
    }

    /**
     * Removes all the items from the invoice
     *
     * @param invoice
     * @throws Exception
     */
    public void clearInvoice(MasterInvoice invoice) throws Exception {
        for (DetailInvoice temp : SelectedDetailInvoice(invoice)) {
            temp.setStatus(false);
            detailController.update(temp);
        }
    }

    /**
     * Add all the items back onto the invoice
     *
     * @param invoice
     * @throws Exception
     */
    public void addInvoice(MasterInvoice invoice) throws Exception {
        for (DetailInvoice temp : SelectedDetailInvoice(invoice)) {
            temp.setStatus(true);
            detailController.update(temp);
        }
    }

    /**
     * returns a list of the selected items
     *
     * @param di
     * @return
     */
    public List<DetailInvoice> SelectedDetailInvoice(MasterInvoice di) {
        return detailController.getDetailedInvoiceFromMasterInvoice(di);
    }

    /**
     * Returns the status of an invoice
     *
     * @param invoice
     * @return
     */
    public boolean statusOfInvoice(MasterInvoice invoice) {

        List<DetailInvoice> temp = invoice.getDetailInvoiceList();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getStatus()) {
                return true;
            }
        }
        return false;
    }


}
