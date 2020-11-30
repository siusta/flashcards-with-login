package pl.siusta.flashcardswithlogin.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.siusta.flashcardswithlogin.model.Flashcard;
import pl.siusta.flashcardswithlogin.model.FlashcardList;
import pl.siusta.flashcardswithlogin.service.FlashcardListService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Route("")
public class HomeGui extends VerticalLayout {
    FlashcardListService fService;

    NavBar navbar = new NavBar();
    TextField searchbar = new TextField();
    Checkbox filterByAuthor = new Checkbox();


    @Autowired
    public HomeGui(FlashcardListService fService) {
        this.fService = fService;
        List<FlashcardList> flashcardLists = fService.getAllFLists();
        VerticalLayout listLayout = new VerticalLayout();
        add(navbar, searchbar, filterByAuthor, listLayout);
        setMargin(true);
        updateList(allLists(flashcardLists), listLayout);
        search(flashcardLists,listLayout);
    }

    public HomeGui() {

    }

    private void search(List<FlashcardList> all, VerticalLayout listLayout) {
        searchbar.setPlaceholder("Search");
        filterByAuthor.setLabel("Filter by author.");
        searchbar.getStyle().set("width", "800px");
        setAlignItems(Alignment.CENTER);
        searchbar.setValueChangeMode(ValueChangeMode.EAGER);
        searchbar.addValueChangeListener(e -> {
            if (filterByAuthor.getValue()) updateList(allLists(byAuthor(all, searchbar.getValue())),listLayout);
            else updateList(allLists(byName(all, searchbar.getValue())),listLayout);
        });
    }

    public List<FlashcardList> byName(List<FlashcardList> all, String name) {
        List<FlashcardList> subList = new ArrayList<>();
        for (FlashcardList f : all
        ) {
            if (f.getName().contains(name)) subList.add(f);
        }
        return subList;
    }

    public List<FlashcardList> byAuthor(List<FlashcardList> all, String author) {
        List<FlashcardList> subList = new ArrayList<>();
        for (FlashcardList f : all
        ) {
            if (f.getAuthor().contains(author)) subList.add(f);
        }
        return subList;
    }

    public void updateList(List<HorizontalLayout> allLists, VerticalLayout listLayout) {
        listLayout.removeAll();
        for (HorizontalLayout h : allLists
        ) {
            listLayout.add(h);
        }
    }


    public HorizontalLayout oneList(FlashcardList f) {
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setWidth("97%");
        hlayout.getStyle().set("border", "1px solid #9E9E9E");
        Label name = new Label(f.getName());
        Button view = new Button("View", buttonClickEvent -> {
            try {
                viewWords("All word pairs:", dataSetUp(f.getId()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        Button learn = new Button("Learn", buttonClickEvent -> {
            try {
                learn(dataSetUp(f.getId()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        Button exercise = new Button("Exercise", buttonClickEvent -> {
            try {
                exercise(dataSetUp(f.getId()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        hlayout.setFlexGrow(1, name);
        hlayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        hlayout.add(name, view, learn, exercise);
        hlayout.setPadding(true);
        hlayout.setMargin(true);
        return hlayout;
    }

    public List<HorizontalLayout> allLists(List<FlashcardList> flashcardLists) {
        List<HorizontalLayout> all = new ArrayList<>();
        flashcardLists.sort(Comparator.comparing(FlashcardList::getName));
        for (FlashcardList f : flashcardLists
        ) {
            all.add(oneList(f));
        }
        return all;
    }

    public List<Flashcard> dataSetUp(Long id) throws JsonProcessingException {
        FlashcardList flashcardList = fService.getFListById(id);
        List<Flashcard> fList = getFlashcardsFromJson(flashcardList.getFlashcardsJSON());
        return fList;
    }

    public void viewWords(String titleStr, List<Flashcard> fList) {
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setAlignItems(Alignment.CENTER);
        Dialog dialog = new Dialog();
        dialog.open();
        Label title = new Label(titleStr);
        vlayout.add(title);
        for (int i = 0; i < fList.size(); i++) {
            Label wordPair = new Label(fList.get(i).getWord() + " -- " + fList.get(i).getMeaning());
            vlayout.add(wordPair);
        }
        Button button = new Button("ok", buttonClickEvent -> {
            dialog.close();
        });
        vlayout.add(button);
        dialog.add(vlayout);
    }

    public void learn(List<Flashcard> fList) throws JsonProcessingException {
        Dialog dialog = new Dialog();
        dialog.open();
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setAlignItems(Alignment.CENTER);
        AtomicInteger i = new AtomicInteger(0);
        Label word = new Label(fList.get(i.get()).getWord());
        Label meaning = new Label(fList.get(i.get()).getMeaning());
        TextField repeat = new TextField();
        Button next = new Button("next", buttonClickEvent -> {
            if ((i.get() < fList.size())) {
                if (fList.get(i.get()).getMeaning().equals(repeat.getValue().trim())) {
                    i.getAndIncrement();
                    repeat.clear();
                    word.setText(fList.get(i.get()).getWord());
                    meaning.setText(fList.get(i.get()).getMeaning());
                }
            } else {
                dialog.close();
            }
        });
        vlayout.add(word, meaning, repeat, next);
        dialog.add(vlayout);
        next.addClickShortcut(Key.ENTER);
        next.setAutofocus(true);
    }

    public void exercise(List<Flashcard> fList) throws JsonProcessingException {
        List<Flashcard> wrong = new ArrayList<>();
        Dialog dialog = new Dialog();
        dialog.open();
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setAlignItems(Alignment.CENTER);
        Collections.shuffle(fList);
        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger ok = new AtomicInteger();
        AtomicBoolean clicked = new AtomicBoolean(true);
        Label word = new Label(fList.get(i.get()).getWord());
        TextField meaning = new TextField();
        Label score = new Label();
        Button confirm = new Button("ok", buttonClickEvent -> {
            if (clicked.get() && (i.get() < fList.size())) {
                if (fList.get(i.get()).getMeaning().equals(meaning.getValue().trim())) {
                    ok.getAndIncrement();
                } else wrong.add(fList.get(i.get()));
                score.setText("Your score: " + ok + "/" + fList.size());
                i.getAndIncrement();
                clicked.set(false);
            } else {
                if (i.get() == fList.size()) {
                    dialog.close();
                    if (!wrong.isEmpty()) viewWords("You got these wrong:", wrong);
                }
                clicked.set(true);
                word.setText(fList.get(i.get()).getWord());
                meaning.clear();
            }
        });
        confirm.addClickShortcut(Key.ENTER);
        vlayout.add(word, meaning, confirm, score);
        dialog.add(vlayout);
        meaning.setAutofocus(true);
    }

    public List<Flashcard> getFlashcardsFromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Flashcard> flashcards = objectMapper.readValue(json, new TypeReference<List<Flashcard>>() {
        });
        return flashcards;
    }
}
