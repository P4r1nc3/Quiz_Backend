package pl.flashdash;

import pl.flashdash.model.card.Card;
import pl.flashdash.model.card.CardDeck;
import pl.flashdash.model.question.Question;
import pl.flashdash.model.question.QuestionDeck;
import pl.flashdash.model.Role;
import pl.flashdash.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;

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

    public static CardDeck createCardDeck() {
        CardDeck deck = new CardDeck();
        deck.setId(1L);
        deck.setUser(createUser());
        deck.setName("TestCardDeck");
        deck.setDescription("Test card deck description");
        deck.setCreatedAt(LocalDateTime.now());
        deck.setUpdatedAt(LocalDateTime.now());
        return deck;
    }

    public static QuestionDeck createQuestionDeck() {
        QuestionDeck deck = new QuestionDeck();
        deck.setId(1L);
        deck.setUser(createUser());
        deck.setName("TestQuestionDeck");
        deck.setDescription("Test question deck description");
        deck.setCreatedAt(LocalDateTime.now());
        deck.setUpdatedAt(LocalDateTime.now());
        return deck;
    }

    public static Card createCard() {
        Card card = new Card();
        card.setCardId(1L);
        card.setDeck(createCardDeck());
        card.setQuestion("What is the capital of France?");
        card.setAnswer("Paris");
        card.setDifficulty("Medium");
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        return card;
    }

    public static Question createQuestion() {
        Question question = new Question();
        question.setQuestionId(1L);
        question.setDeck(createQuestionDeck());
        question.setQuestion("What is the largest planet in our solar system?");
        question.setCorrectAnswers(Arrays.asList("Jupiter"));
        question.setIncorrectAnswers(Arrays.asList("Earth", "Mars", "Venus"));
        question.setDifficulty("Medium");
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        return question;
    }
}
