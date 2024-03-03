package pl.flashdash.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.flashdash.model.Card;
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
}
