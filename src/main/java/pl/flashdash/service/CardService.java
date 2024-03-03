package pl.flashdash.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.flashdash.exception.CardNotFoundException;
import pl.flashdash.model.Card;
import pl.flashdash.model.Deck;
import pl.flashdash.model.User;
import pl.flashdash.repository.CardRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final DeckService deckService;

    public Card addCardToDeck(Long deckId, Card card, User user) {
        Deck deck = deckService.getDeckById(deckId, user);

        card.setDeck(deck);
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());

        return cardRepository.save(card);
    }

    public List<Card> getAllCardsInDeck(Long deckId, User user) {
        Deck deck = deckService.getDeckById(deckId, user);

        return cardRepository.findAllByDeck(deck);
    }

    public Card getCardById(Long deckId, Long cardId, User user) {
        Deck deck = deckService.getDeckById(deckId, user);

        return cardRepository.findByDeckAndCardId(deck, cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));
    }

    public Card updateCard(Long deckId, Long cardId, Card cardDetails, User user) {
        Deck deck = deckService.getDeckById(deckId, user);
        Card card = cardRepository.findByDeckAndCardId(deck, cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));

        card.setQuestion(cardDetails.getQuestion());
        card.setAnswer(cardDetails.getAnswer());
        card.setDifficulty(cardDetails.getDifficulty());
        card.setUpdatedAt(LocalDateTime.now());

        return cardRepository.save(card);
    }

    public void deleteCard(Long deckId, Long cardId, User user) {
        Deck deck = deckService.getDeckById(deckId, user);
        Card card = cardRepository.findByDeckAndCardId(deck, cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));

        cardRepository.delete(card);
    }
}
