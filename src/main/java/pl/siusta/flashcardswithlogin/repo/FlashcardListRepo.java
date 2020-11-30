package pl.siusta.flashcardswithlogin.repo;

import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.siusta.flashcardswithlogin.model.FlashcardList;

import java.util.List;

@PropertySource(value = "my.properties", encoding="UTF-8")
@Repository
public interface FlashcardListRepo extends JpaRepository<FlashcardList,Long> {
    List<FlashcardList> findAllByName(String name);
    List<FlashcardList> findAllByAuthor(String author);
}
