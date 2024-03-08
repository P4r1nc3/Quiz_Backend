package pl.flashdash.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.flashdash.model.card.Card;
import pl.flashdash.model.User;
import pl.flashdash.service.CardService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/decks/{deckId}/cards")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<Card> addCardToDeck(@PathVariable Long deckId, @RequestBody Card card) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Card newCard = cardService.addCardToDeck(deckId, card, user);
        return ResponseEntity.ok(newCard);
    }

    @GetMapping
    public ResponseEntity<List<Card>> getAllCardsInDeck(@PathVariable Long deckId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Card> cards = cardService.getAllCardsInDeck(deckId, user);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<Card> getDeck(@PathVariable Long deckId, @PathVariable Long cardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Card card = cardService.getCardById(deckId, cardId, user);
        return ResponseEntity.ok(card);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<Card> updateDeck(@PathVariable Long deckId, @PathVariable Long cardId, @RequestBody Card card) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Card newCard = cardService.updateCard(deckId, cardId, card, user);
        return ResponseEntity.ok(newCard);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteDeck(@PathVariable Long deckId, @PathVariable Long cardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        cardService.deleteCard(deckId, cardId, user);
        return ResponseEntity.noContent().build();
    }
}
