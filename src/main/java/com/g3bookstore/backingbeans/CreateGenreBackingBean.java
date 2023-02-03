package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomGenreJpaController;
import com.g3bookstore.entities.Genre;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author lebed
 */
@Named("createGenre")
@RequestScoped
public class CreateGenreBackingBean {
    
    @Inject
    private CustomGenreJpaController genreController;
    
    private Genre genre;
    
    public Genre getGenre() {
        if(genre == null)
            genre = new Genre();
        return genre;
    }
    
    public String createGenre() throws Exception {
        genreController.create(genre);
        return null;
    }
    
}
