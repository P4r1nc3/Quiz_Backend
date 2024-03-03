package pl.flashdash.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.flashdash.exception.DeckNotFoundException;
import pl.flashdash.model.Deck;
import pl.flashdash.model.User;
import pl.flashdash.repository.DeckRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;

    public Deck createDeck(Deck deck, User user) {
        // Deck comes to us having only name + desc, we need to specify user based on the token
        deck.setUser(user);
        deck.setCreatedAt(LocalDateTime.now());
        deck.setUpdatedAt(LocalDateTime.now());

        return deckRepository.save(deck);
    }

    public List<Deck> getAllDecks(User user) {
        return deckRepository.findAllByUser(user);
    }

    public Deck getDeckById(Long deckId, User user) {
        return deckRepository.findByDeckIdAndUser(deckId, user)
                .orElseThrow(() -> new DeckNotFoundException("Deck not found with id " + deckId + ", for user " + user.getUserId() + "."));
    }

    public Deck updateDeck(Long deckId, Deck deckDetails, User user) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new DeckNotFoundException("Deck not found with id " + deckId + ", for user " + user.getUserId() + "."));

        deck.setName(deckDetails.getName());
        deck.setDescription(deckDetails.getDescription());
        deck.setUpdatedAt(LocalDateTime.now());

        return deckRepository.save(deck);
    }

    public void deleteDeck(Long id) {
        deckRepository.deleteById(id);
    }
}
