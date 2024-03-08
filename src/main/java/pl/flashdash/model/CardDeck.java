package pl.flashdash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@DiscriminatorValue("CARD")
public class CardDeck extends Deck {

    @JsonIgnore
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards;
}
