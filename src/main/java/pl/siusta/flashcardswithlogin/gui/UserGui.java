package pl.siusta.flashcardswithlogin.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.siusta.flashcardswithlogin.model.Flashcard;
import pl.siusta.flashcardswithlogin.model.FlashcardList;
import pl.siusta.flashcardswithlogin.model.User;
import pl.siusta.flashcardswithlogin.service.FlashcardListService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Route("user")
public class UserGui extends VerticalLayout{
    FlashcardListService fService;

    NavBar navBar = new NavBar();
    HomeGui homeGui = new HomeGui();
    TextField searchbar = new TextField();
    VerticalLayout listLayout = new VerticalLayout();

    public UserGui(FlashcardListService fService) {
        this.fService = fService;
        add(navBar, searchbar, listLayout);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateList(allLists(fService.getFListByAuthor(user.getUsername()), user));
        search(user);
    }

    private void search(User user) {
        searchbar.setPlaceholder("Search");
        searchbar.getStyle().set("width", "800px");
        setAlignItems(Alignment.CENTER);
        searchbar.setValueChangeMode(ValueChangeMode.EAGER);
        searchbar.addValueChangeListener(e -> {
            updateList(allLists(homeGui.byName(fService.getFListByAuthor(user.getUsername()),searchbar.getValue()),user));
        });
    }

    public void updateList(List<HorizontalLayout> allLists) {
        listLayout.removeAll();
        for (HorizontalLayout h : allLists
        ) {
            listLayout.add(h);
        }
    }

    public List<HorizontalLayout> allLists(List<FlashcardList> flashcardLists, User user) {
        List<HorizontalLayout> all = new ArrayList<>();
        flashcardLists.sort(Comparator.comparing(FlashcardList::getName));
        for (FlashcardList f : flashcardLists
        ) {
            all.add(oneList(f, user));
        }
        return all;
    }

    public List<Flashcard> dataSetUp(Long id) throws JsonProcessingException {
        FlashcardList flashcardList = fService.getFListById(id);
        List<Flashcard> fList = homeGui.getFlashcardsFromJson(flashcardList.getFlashcardsJSON());
        return fList;
    }

    public HorizontalLayout oneList(FlashcardList f, User user) {
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
        Button edit = new Button("Edit", buttonClickEvent -> {
            try {
                editFList(f);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        Button delete = new Button("Delete", buttonClickEvent -> {
            delete(f.getId(),user);
        });
        hlayout.setFlexGrow(1, name);
        hlayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        hlayout.add(name, view, learn, exercise, edit, delete);
        hlayout.setPadding(true);
        hlayout.setMargin(true);
        return hlayout;
    }

    public void delete(Long id, User user){
        Dialog dialog = new Dialog();
        dialog.open();
        Label sure = new Label("Are you sure?");
        Button yes = new Button("Yes", buttonClickEvent -> {
            fService.deleteFList(id);
            updateList((allLists(fService.getFListByAuthor(user.getUsername()), user)));
            dialog.close();
        });
        Button no = new Button("No", buttonClickEvent -> dialog.close());
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(yes, no);
        buttons.setAlignItems(Alignment.CENTER);
        dialog.add(sure, buttons);


    }


    public void editFList(FlashcardList flashcardList) throws JsonProcessingException {
        Dialog dialog = new Dialog();
        dialog.open();
        VerticalLayout words = new VerticalLayout();
        words.setAlignItems(Alignment.CENTER);
        List<Flashcard> flashcards = homeGui.getFlashcardsFromJson(flashcardList.getFlashcardsJSON());
        for (Flashcard f: flashcards
             ) {
            HorizontalLayout row = new HorizontalLayout();
            TextField word = new TextField();
            TextField meaning = new TextField();
            word.setValue(f.getWord());
            meaning.setValue(f.getMeaning());
            Button ok = new Button("ok",buttonClickEvent -> {
                f.setWord(word.getValue());
                f.setMeaning(meaning.getValue());
            });
            row.add(word, meaning, ok);
            words.add(row);
        }
        Button save = new Button("save",buttonClickEvent -> {
            try {
                fService.editFList(flashcardList.getId(), new FlashcardList(flashcardList.getName(),
                        flashcardList.getAuthor(),
                        convertFlashcardsToString(flashcards)));
                dialog.close();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            };
        });
        words.add(save);
        dialog.add(words);
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


    public String convertFlashcardsToString(List<Flashcard> flashcards) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String temp="[";
        for (Flashcard f:flashcards
        ) {
            temp = temp + objectMapper.writeValueAsString(f) + ",";
        }
        String json = temp.substring(0,temp.length()-1) + "]";
        return json;
    }
}
