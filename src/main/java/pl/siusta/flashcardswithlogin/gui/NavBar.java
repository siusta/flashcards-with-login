package pl.siusta.flashcardswithlogin.gui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

public class NavBar extends AppLayout implements BeforeEnterObserver {
    private Map<Class<? extends Component>, Tab> navigationTargetToTab = new HashMap<>();
    private Tabs tabs = new Tabs();

    public NavBar() {
        addNavTab("Home", HomeGui.class);
        addNavTab("Add new word list", AddGui.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            addNavTab("User panel",UserGui.class);
            Tab logout = new Tab("Logout");
            tabs.add(logout);
            tabs.addSelectedChangeListener(e->{
                if(logout.isSelected())UI.getCurrent().getPage().setLocation("/logout");
            });
        }
        else {
            addNavTab("Login",LoginGui.class);
            addNavTab("Register",RegisterGui.class);
        }

        addNavTab("About", AboutGui.class);
        addToNavbar(tabs);
    }

    private void addNavTab(String label, Class<? extends Component> target) {
        Tab tab = new Tab(new RouterLink(label, target));
        navigationTargetToTab.put(target, tab);
        tabs.add(tab);
    }




    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        tabs.setSelectedTab(navigationTargetToTab.get(event.getNavigationTarget()));
    }
}
