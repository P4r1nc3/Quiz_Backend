package pl.flashdash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.flashdash.model.Card;
import pl.flashdash.model.Deck;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByDeck(Deck deck);
}