package pl.flashdash.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.flashdash.model.User;
import pl.flashdash.service.DeckService;
import pl.flashdash.model.Deck;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/decks")
public class DeckController {
    private final DeckService deckService;

    @PostMapping
    public ResponseEntity<Deck> createDeck(@RequestBody Deck deck) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Deck newDeck = deckService.createDeck(deck, user);
        return new ResponseEntity<>(newDeck, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Deck>> getAllDecks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Deck> decks = deckService.getAllDecks(user);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<Deck> getDeck(@PathVariable Long deckId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Deck deck = deckService.getDeckById(deckId, user);
        return new ResponseEntity<>(deck, HttpStatus.OK);
    }
}
