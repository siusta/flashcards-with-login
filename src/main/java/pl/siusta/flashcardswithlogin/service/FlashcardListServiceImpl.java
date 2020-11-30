package pl.siusta.flashcardswithlogin.service;

import org.springframework.stereotype.Service;
import pl.siusta.flashcardswithlogin.model.Flashcard;
import pl.siusta.flashcardswithlogin.model.FlashcardList;
import pl.siusta.flashcardswithlogin.repo.FlashcardListRepo;

import java.util.List;
import java.util.Optional;

@Service
public class FlashcardListServiceImpl implements FlashcardListService {
    private FlashcardListRepo fListRepo;

    public FlashcardListServiceImpl(FlashcardListRepo fListRepo) {
        this.fListRepo = fListRepo;
    }

    @Override
    public List<FlashcardList> getAllFLists() {
        return fListRepo.findAll();
    }

    @Override
    public FlashcardList getFListById(Long id) {
        Optional<FlashcardList> tempFList = fListRepo.findById(id);
        FlashcardList fList = tempFList.get();
        return fList;
    }

    @Override
    public List<FlashcardList> getFListByName(String name) {
        return fListRepo.findAllByName(name);
    }

    @Override
    public List<FlashcardList> getFListByAuthor(String author) {
        List<FlashcardList> tempFList = fListRepo.findAllByAuthor(author);
        return tempFList;
    }


    @Override
    public Boolean addFList(FlashcardList flashcardList) {
        fListRepo.save(flashcardList);
        return true;
    }

    @Override
    public void editFList(Long id, FlashcardList fEdit) {
        FlashcardList f = getFListById(id);
        f.setAuthor(fEdit.getAuthor());
        f.setName(fEdit.getName());
        f.setFlashcardsJSON(fEdit.getFlashcardsJSON());
        fListRepo.save(f);
    }

    @Override
    public void deleteFList(Long id) {
        fListRepo.deleteById(id);
    }

}
