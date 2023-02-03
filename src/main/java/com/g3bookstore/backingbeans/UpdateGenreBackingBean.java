package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomGenreJpaController;
import com.g3bookstore.entities.Genre;
import com.g3bookstore.util.MessageProvider;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *  Allow to update the actual genre if it is valid.
 * 
 * @author Denis Lebedev
 */
@Named("updateGenre")
@SessionScoped
public class UpdateGenreBackingBean implements Serializable {

    private static final Logger log = Logger.getLogger(UpdateGenreBackingBean.class.getName());

    @Inject
    private CustomGenreJpaController genreController;
    private List<Genre> genreList;

    @PostConstruct
    public void init() {
        genreList = genreController.getGenres();
    }

    public List<Genre> getGenres() {
        return genreList;
    }

    public void setGenres(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public void onRowEdit(RowEditEvent event) throws Exception {
        Genre temp = (Genre) event.getObject();
        log.log(Level.INFO, "Given new genre:{0}", temp.getGenre());
        log.log(Level.INFO, "Updating genre");
        genreController.update(temp);

    }
}
