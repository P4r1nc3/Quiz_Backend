package pl.flashdash.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.flashdash.exception.DeckNotFoundException;
import pl.flashdash.model.CardDeck;
import pl.flashdash.model.Deck;
import pl.flashdash.model.QuestionDeck;
import pl.flashdash.model.User;
import pl.flashdash.repository.DeckRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;

    public Deck createDeck(Deck request, User user) {
        request.setUser(user);
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        return deckRepository.save(request);
    }

    public List<Deck> getAllDecks(User user) {
        return deckRepository.findAllByUser(user);
    }

    public Deck getDeckById(Long deckId, User user) {
        return deckRepository.findByIdAndUser(deckId, user)
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

    @Transactional
    public void deleteDeck(Long deckId, User user) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new DeckNotFoundException("Deck not found with id " + deckId + ", for user " + user.getUserId() + "."));

        deckRepository.delete(deck);
    }
}
