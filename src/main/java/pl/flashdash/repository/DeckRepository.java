package pl.flashdash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.flashdash.model.Deck;
import pl.flashdash.model.User;

import java.util.List;
import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findAllByUser(User user);
    Optional<Deck> findByDeckIdAndUser(Long deckId, User user);
}

