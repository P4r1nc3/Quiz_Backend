package pl.flashdash.model.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.flashdash.model.Deck;

import java.util.Set;

@Getter
@Setter
@Entity
@DiscriminatorValue("QUESTION")
public class QuestionDeck extends Deck {

    @JsonIgnore
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions;
}
