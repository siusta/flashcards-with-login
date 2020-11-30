package pl.siusta.flashcardswithlogin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FlashcardList {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String author;
    private String flashcardsJSON;

    public FlashcardList() {
    }

    public FlashcardList(String name, String author,String flashcardsJSON) {
        this.name = name;
        this.author = author;
        this.flashcardsJSON= flashcardsJSON;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFlashcardsJSON() {
        return flashcardsJSON;
    }

    public void setFlashcardsJSON(String flashcardsJSON) {
        this.flashcardsJSON = flashcardsJSON;
    }

    @Override
    public String toString() {
        return "FlashcardList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", flashcardsJSON=" + flashcardsJSON +
                '}';
    }

}
