package pl.siusta.flashcardswithlogin.gui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import pl.siusta.flashcardswithlogin.model.User;

public class PasswordFields extends Div {
    private PasswordField passwordField;
    private PasswordField rePasswordField;
    private Binder<User> binder;

    public PasswordFields() {
        VerticalLayout passwords = new VerticalLayout();
        binder = new Binder<>();
        passwordField = new PasswordField();
        passwordField.setLabel("Password");
        passwordField.setPlaceholder("Enter password");
        passwordField.setRequired(true);

        binder.forField(passwordField)
                .withValidator(new RegexpValidator("Password should contain lowercase and uppercase letters and digits and be at least 8 characters long", "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,32}$"))
                .bind(User::getPassword, User::setPassword);


        rePasswordField = new PasswordField();
        rePasswordField.setLabel("Repeat password");
        rePasswordField.setPlaceholder("repeat password");
        rePasswordField.setRequired(true);
        binder.forField(rePasswordField)
                .withValidator(this::rePasswordValidator, "Passwords should match")
                .bind(User::getPassword, User::setPassword);


        passwords.add(passwordField, rePasswordField);
        add(passwords);

    }

    public String getPassword() {
        return passwordField.getValue();
    }

    private boolean passwordValidator(String password) {

        if (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,32}$")) {

            return true;
        }
        return false;
    }

    private Boolean rePasswordValidator(String password) {

        if (password.equals(passwordField.getValue())) {
            return true;
        }
        return false;
    }

    public boolean areValid() {
        return passwordValidator(passwordField.getValue()) && rePasswordValidator(rePasswordField.getValue());
    }
}
