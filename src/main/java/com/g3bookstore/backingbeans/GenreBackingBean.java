package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomGenreJpaController;
import com.g3bookstore.entities.Genre;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Kevin Bui
 */
@Named("theGenres")
@SessionScoped
public class GenreBackingBean implements Serializable {

    @Inject
    private CustomGenreJpaController genreController;

    /**
     * @return a list of all the existing genres
     */
    public List<Genre> getGenres() {
        return genreController.getGenres();
    }
    
    /**
     * @return true if there are no genres in the database, else false
     */
    public boolean isEmpty() {
        return genreController.getGenres().isEmpty();
    }
    
    
    
    

}
