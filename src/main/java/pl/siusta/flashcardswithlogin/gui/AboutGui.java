package pl.siusta.flashcardswithlogin.gui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("about")
public class AboutGui extends VerticalLayout {

    NavBar navBar = new NavBar();

    public AboutGui() {
        setWidth("80%");
        setAlignItems(Alignment.CENTER);
        String description = "<div>Welcome to the word learning app!<br><br><br>" +
                "Make a list of word pairs that you want to remember or choose one made by other users.<br><br>" +
                "Your input is case sensitive, but you don't have to worry about extra space blanks.<br><br>" +
                "This app is intended for word learning, but you can use it anyway you want,<br>" +
                "as long as you can fit it in a pair.<br><br>"+
                "Have fun learning! :)"+
                "</div>";

        Html html = new Html(description);
        add(navBar,html);
    }
}
