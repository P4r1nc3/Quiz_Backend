package pl.flashdash;

import pl.flashdash.model.Card;
import pl.flashdash.model.Deck;
import pl.flashdash.model.Role;
import pl.flashdash.model.User;

import java.time.LocalDateTime;

public class TestUtils {

    public static User createUser() {
        User user = new User();
        user.setUserId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("securePassword");
        user.setRole(Role.USER);
        return user;
    }

    public static Deck createDeck() {
        Deck deck = new Deck();
        deck.setDeckId(1L);
        deck.setUser(createUser());
        deck.setName("TestDeck");
        deck.setDescription("Test deck description");
        deck.setCreatedAt(LocalDateTime.now());
        deck.setUpdatedAt(LocalDateTime.now());
        return deck;
    }

    public static Card createCard() {
        Card card = new Card();
        card.setCardId(1L);
        card.setDeck(createDeck());
        card.setQuestion("What is the capital of France?");
        card.setAnswer("Paris");
        card.setDifficulty("Medium");
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        return card;
    }
}
