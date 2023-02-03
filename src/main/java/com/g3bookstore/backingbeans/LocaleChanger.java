package com.g3bookstore.backingbeans;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * This class changes the locale of the current server session.
 *
 * @author Ken Fogel https://gitlab.com/omniprof/JSFSample25ParameterEvents
 * @author Kevin Bui
 * @version 1.0
 * @since 2018-02-06
 */
@Named("localeChanger")
@SessionScoped
public class LocaleChanger implements Serializable {

    private static final Logger log = Logger.getLogger("LocaleChanger.class");
    private static final long serialVersionUID = 1l;
    private boolean newCookie = false;
    private String currentLocale;

    /**
     * Changes the locale of the current session. Thus provoking a language
     * change.
     *
     * @return null all the time to refresh the page.
     */
    public String changeLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        String languageCode = getLanguageCode(context);
        currentLocale = languageCode;
        writeLocaleCookie(languageCode);
        
        return setLocale(languageCode, context);
    }

    /**
     * Gets the language code from the http request.
     *
     * @param context current context of the request
     * @return returns the language code.
     */
    private String getLanguageCode(FacesContext context) {
        Map<String, String> params = context.getExternalContext()
                .getRequestParameterMap();
        return params.get("languageCode");
    }

    /**
     * Checks if the locale cookie is set and if so sets the appropriate session
     * locale.
     */
    public void checkCookieLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!newCookie) {
            newCookie = false;
        } 
        
        if (currentLocale != null) {
            setLocale(currentLocale, context);
        }
    }

    /**
     * Sets the locale with the specified value and then writes a cookie with
     * the locale
     *
     * @param languageCode locale code.
     * @param context where to change the locale.
     * @return where to go after.
     */
    private String setLocale(String languageCode, FacesContext context) {
        
        Locale aLocale;
        switch (languageCode) {
            case "en_CA":
                aLocale = Locale.CANADA;
                currentLocale = languageCode;
                break;
            case "fr_CA":
                aLocale = Locale.CANADA_FRENCH;
                currentLocale = languageCode;
                break;
            default:
                aLocale = Locale.getDefault();
        }
        context.getViewRoot().setLocale(aLocale);

        return null;
    }

    /**
     * Writes a cookie with the given locale for the session.
     *
     * @param code content of the cookie
     */
    public void writeLocaleCookie(String code) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().addResponseCookie("G3Cookie", code, null);
        newCookie = true;
    }

}
