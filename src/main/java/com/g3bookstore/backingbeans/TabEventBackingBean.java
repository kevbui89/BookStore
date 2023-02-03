package com.g3bookstore.backingbeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

/**
 * This class prevent tab switching on validation errors or 
 * invalid submit.
 * 
 * @author Denis Lebedev
 */
@Named("tabEvent")
@RequestScoped
public class TabEventBackingBean {
    private TabView messagesTab = new TabView();

    public TabView getMessagesTab () {
        return messagesTab;
    }

    public void setMessagesTab(TabView messagesTab ) {
        this.messagesTab = messagesTab;
    }

    public void onTabChange(TabChangeEvent event) {   
        TabView tabView = (TabView) event.getComponent();

        int activeIndex = tabView.getChildren().indexOf(event.getTab());

        this.messagesTab.setActiveIndex(activeIndex);

    }
}
