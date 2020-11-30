package pl.siusta.flashcardswithlogin.gui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.siusta.flashcardswithlogin.config.WebSecurityConfig;
import pl.siusta.flashcardswithlogin.model.User;
import pl.siusta.flashcardswithlogin.repo.UserRepo;
import pl.siusta.flashcardswithlogin.service.UserService;

import java.util.NoSuchElementException;

@Route("register")
public class RegisterGui extends VerticalLayout {

    private WebSecurityConfig websec;
    private UserService userService;
    NavBar navBar = new NavBar();
    UserRepo userRepo;

    @Autowired
    public RegisterGui(UserService userService,UserRepo userRepo, WebSecurityConfig websec) {
        this.userService = userService;
        this.userRepo=userRepo;
        this.websec = websec;
        Label title = new Label("Register");
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            add(new Label("You already have an account"));
            return;
        }catch (Exception e){}
        UsernameField usernameField = new UsernameField("Username");
        PasswordFields passwordFields = new PasswordFields();


        Button submitButton = new Button("Submit",buttonClickEvent -> {

                if (usernameField.isValid()&& passwordFields.areValid() && usernameInDataBaseValidator(usernameField.getValue())) {
                    System.out.println(";_;");

                    saveUser(usernameField.getValue(), passwordFields.getPassword());
                    UI.getCurrent().navigate("login");

                }

        });

        setAlignItems(Alignment.CENTER);
        add(navBar,title, usernameField, passwordFields, submitButton);
    }

    private void saveUser(String username, String password) {
        User user = new User(username,websec.getPasswordEncoder().encode(password),"ROLE_USER");
        System.out.println(user.toString());
        userService.saveUser(user);
    }

    private boolean usernameInDataBaseValidator(String username) {
        try {
            userService.getUserByUsername(username);
        } catch (NoSuchElementException e) {
            System.out.println("reeeeeeeeeeeee");
            return true;
        }
        Notification.show("There is already user with that username").setDuration(3000);
        return false;
    }

}
