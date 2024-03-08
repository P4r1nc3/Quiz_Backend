package pl.flashdash.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.flashdash.TestUtils;
import pl.flashdash.exception.CardNotFoundException;
import pl.flashdash.model.card.Card;
import pl.flashdash.model.User;
import pl.flashdash.model.card.CardDeck;
import pl.flashdash.repository.CardRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private DeckService deckService;

    @InjectMocks
    private CardService cardService;

    private User testUser;
    private CardDeck testDeck;
    private Card testCard;

    @BeforeEach
    void setup() {
        testUser = TestUtils.createUser();
        testDeck = TestUtils.createCardDeck();
        testCard = TestUtils.createCard();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(deckService.getDeckById(anyLong(), any(User.class))).thenReturn(testDeck);
    }

    @Test
    void addCardToDeck_WhenCardIsValid_ShouldReturnSavedCard() {
        // Given
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        // Act
        Card savedCard = cardService.addCardToDeck(testDeck.getId(), testCard, testUser);

        // Assert
        assertNotNull(savedCard);
        assertEquals(testCard.getQuestion(), savedCard.getQuestion());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void getAllCardsInDeck_WhenCalled_ShouldReturnAllCards() {
        // Given
        List<Card> expectedCards = List.of(testCard, TestUtils.createCard());
        when(cardRepository.findAllByDeck(testDeck)).thenReturn(expectedCards);

        // Act
        List<Card> cards = cardService.getAllCardsInDeck(testDeck.getId(), testUser);

        // Assert
        assertFalse(cards.isEmpty());
        assertEquals(expectedCards.size(), cards.size());
        assertEquals(expectedCards, cards);
    }

    @Test
    void getCardById_WhenCardExists_ShouldReturnCard() {
        // Given
        when(cardRepository.findByDeckAndCardId(testDeck, testCard.getCardId())).thenReturn(Optional.of(testCard));

        // Act
        Card foundCard = cardService.getCardById(testDeck.getId(), testCard.getCardId(), testUser);

        // Assert
        assertEquals(testCard, foundCard);
    }

    @Test
    void updateCard_WhenCardExists_ShouldReturnUpdatedCard() {
        // Given
        when(cardRepository.findByDeckAndCardId(testDeck, testCard.getCardId())).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        // Act
        Card updatedCard = cardService.updateCard(testDeck.getId(), testCard.getCardId(), testCard, testUser);

        // Assert
        assertEquals(testCard.getQuestion(), updatedCard.getQuestion());
        verify(cardRepository).save(testCard);
    }

    @Test
    void deleteCard_WhenCardExists_ShouldNotThrowException() {
        // Given
        when(cardRepository.findByDeckAndCardId(testDeck, testCard.getCardId())).thenReturn(Optional.of(testCard));
        doNothing().when(cardRepository).delete(any(Card.class));

        // Act & Assert
        assertDoesNotThrow(() -> cardService.deleteCard(testDeck.getId(), testCard.getCardId(), testUser));

        // Assert
        verify(cardRepository).delete(testCard);
    }

    @Test
    void getCardById_WhenCardDoesNotExist_ShouldThrowCardNotFoundException() {
        // Given
        Long nonExistentCardId = 999L;
        when(cardRepository.findByDeckAndCardId(testDeck, nonExistentCardId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(testDeck.getId(), nonExistentCardId, testUser));
    }

    @Test
    void updateCard_WhenCardDoesNotExist_ShouldThrowCardNotFoundException() {
        // Given
        Long nonExistentCardId = 999L;
        when(cardRepository.findByDeckAndCardId(testDeck, nonExistentCardId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CardNotFoundException.class, () -> cardService.updateCard(testDeck.getId(), nonExistentCardId, new Card(), testUser));
    }

    @Test
    void deleteCard_WhenCardDoesNotExist_ShouldThrowCardNotFoundException() {
        // Given
        Long nonExistentCardId = 999L;
        when(cardRepository.findByDeckAndCardId(testDeck, nonExistentCardId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CardNotFoundException.class, () -> cardService.deleteCard(testDeck.getId(), nonExistentCardId, testUser));
    }
}
