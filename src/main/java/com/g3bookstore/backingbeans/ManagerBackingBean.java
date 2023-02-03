package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomManagerController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class responsible for managing the content in the manager page
 *
 * @author lebedev, Werner
 */
@Named("managerBacking")
@SessionScoped
public class ManagerBackingBean implements Serializable {

    @Inject
    private CustomManagerController managerController;

   

}
