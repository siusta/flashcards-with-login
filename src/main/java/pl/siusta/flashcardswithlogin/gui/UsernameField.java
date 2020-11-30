package pl.siusta.flashcardswithlogin.gui;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import pl.siusta.flashcardswithlogin.model.User;

public class UsernameField extends TextField {

    private Binder<User> binder;

    public UsernameField(String label) {
        super(label);
        binder = new Binder<>();
        binder.forField(this)
                .withValidator(
                        new RegexpValidator("Username should be at least 3 characters long", "^(?=\\S+$).{3,32}"))
                .bind(User::getPassword, User::setPassword);
    }

    public boolean isValid() {
        if (this.getValue().matches("^(?=\\S+$).{3,32}")) {

            return true;
        }
        return false;
    }
}
