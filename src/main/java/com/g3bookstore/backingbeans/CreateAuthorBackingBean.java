package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomAuthorJpaController;
import com.g3bookstore.entities.Author;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author lebed
 */
@Named("createAuthor")
@RequestScoped
public class CreateAuthorBackingBean implements Serializable {
    
    @Inject
    private CustomAuthorJpaController authorController;
    
    private Author author;
    
    
    public Author getAuthor() {
        if(author == null)
            author = new Author();
        return author;
    }
    
    public String createAuthor() throws Exception {
        authorController.create(author);
        return null;
    }
}
