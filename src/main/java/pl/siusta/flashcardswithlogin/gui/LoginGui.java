package pl.siusta.flashcardswithlogin.gui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.Collections;

@Route("login")
public class LoginGui extends VerticalLayout  {

    LoginForm login = new LoginForm();


    public LoginGui() {
        NavBar navBar = new NavBar();
        add(navBar);
        getElement().appendChild(login.getElement());
        login.setAction("login");
        setAlignItems(Alignment.CENTER);
        login.setForgotPasswordButtonVisible(false);
    }

//    @Override
//    public void beforeEnter(BeforeEnterEvent event) { //
//        if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()) {
//            login.setError(true); //
//        }
//    }
}
