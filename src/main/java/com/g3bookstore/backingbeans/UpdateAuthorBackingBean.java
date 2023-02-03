package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomAuthorJpaController;
import com.g3bookstore.entities.Author;
import com.g3bookstore.util.MessageProvider;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 * This class allow to update a author name by managing the events of the table.
 *
 * @author Denis Lebedev
 */
@Named("updateAuthor")
@SessionScoped
public class UpdateAuthorBackingBean implements Serializable {

    private static final Logger log = Logger.getLogger(UpdateAuthorBackingBean.class.getName());

    @Inject
    private CustomAuthorJpaController authorController;

    private List<Author> authorList;

    @PostConstruct
    public void init() {
        authorList = authorController.getAuthors();
    }

    public List<Author> getAuthors() {
        return authorList;
    }

    public void setAuthors(List<Author> authorList) {
        this.authorList = authorList;
    }

    public void onRowEdit(RowEditEvent event) throws Exception {
        log.log(Level.INFO, "Update the author object");
        Author auth = (Author) event.getObject();
        log.log(Level.INFO, "New Name:{0}", auth.getName());
        authorController.update(auth);

    }

}
