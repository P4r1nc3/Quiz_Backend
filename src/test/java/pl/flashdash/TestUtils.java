package pl.flashdash;

import pl.flashdash.model.Deck;
import pl.flashdash.model.User;

import java.time.LocalDateTime;

public class TestUtils {

    public static User createUser() {
        User user = new User();
        user.setUserId(1);
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
}

