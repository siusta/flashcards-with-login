package pl.siusta.flashcardswithlogin.service;

import org.springframework.stereotype.Service;
import pl.siusta.flashcardswithlogin.model.Flashcard;
import pl.siusta.flashcardswithlogin.model.FlashcardList;

import java.util.List;

@Service
public interface FlashcardListService {
    List<FlashcardList> getAllFLists();
    FlashcardList getFListById(Long id);
    List<FlashcardList> getFListByName(String name);
    List<FlashcardList> getFListByAuthor(String author);
    Boolean addFList(FlashcardList flashcardList);
    void editFList(Long id, FlashcardList flashcardList);
    void deleteFList(Long id);
}
