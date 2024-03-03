package pl.flashdash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.flashdash.model.Card;
import pl.flashdash.model.Deck;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByDeck(Deck deck);
    Optional<Card> findByDeckAndCardId(Deck deck, Long cardId);
    void deleteAllByDeck(Deck deck);
}