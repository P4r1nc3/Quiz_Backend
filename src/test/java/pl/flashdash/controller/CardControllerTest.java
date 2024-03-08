package pl.flashdash.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.flashdash.TestUtils;
import pl.flashdash.exception.CardNotFoundException;
import pl.flashdash.model.card.Card;
import pl.flashdash.model.User;
import pl.flashdash.service.CardService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    @Mock
    private CardService cardService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CardController sut;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = TestUtils.createUser();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
    }

    @Test
    public void addCardToDeck_WhenCardIsValid_ShouldReturnCreatedCard() {
        // Given
        Card inputCard = TestUtils.createCard();
        Long deckId = 1L;
        when(cardService.addCardToDeck(eq(deckId), any(Card.class), eq(testUser))).thenReturn(inputCard);

        // Act
        ResponseEntity<Card> response = sut.addCardToDeck(deckId, inputCard);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inputCard, response.getBody());
    }

    @Test
    public void getAllCardsInDeck_WhenCalled_ShouldReturnAllCardsForDeck() {
        // Given
        Long deckId = 1L;
        List<Card> expectedCards = List.of(TestUtils.createCard(), TestUtils.createCard());
        when(cardService.getAllCardsInDeck(eq(deckId), eq(testUser))).thenReturn(expectedCards);

        // Act
        ResponseEntity<List<Card>> response = sut.getAllCardsInDeck(deckId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCards.size(), response.getBody().size());
        assertEquals(expectedCards, response.getBody());
    }

    @Test
    public void getCard_WhenCardExists_ShouldReturnCard() {
        // Given
        Long deckId = 1L;
        Long cardId = 1L;
        Card expectedCard = TestUtils.createCard();
        when(cardService.getCardById(eq(deckId), eq(cardId), eq(testUser))).thenReturn(expectedCard);

        // Act
        ResponseEntity<Card> response = sut.getDeck(deckId, cardId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCard, response.getBody());
    }

    @Test
    public void updateCard_WhenCardExists_ShouldReturnUpdatedCard() {
        // Given
        Long deckId = 1L;
        Long cardId = 1L;
        Card updatedCard = TestUtils.createCard();
        when(cardService.updateCard(eq(deckId), eq(cardId), any(Card.class), eq(testUser))).thenReturn(updatedCard);

        // Act
        ResponseEntity<Card> response = sut.updateDeck(deckId, cardId, updatedCard);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCard, response.getBody());
    }

    @Test
    public void deleteCard_WhenCardIsDeleted_ShouldReturnNoContent() {
        // Given
        Long deckId = 1L;
        Long cardId = 1L;
        doNothing().when(cardService).deleteCard(eq(deckId), eq(cardId), eq(testUser));

        // Act
        ResponseEntity<Void> response = sut.deleteDeck(deckId, cardId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getCard_WhenCardDoesNotExist_ShouldThrowCardNotFoundException() {
        // Given
        Long deckId = 1L;
        Long nonExistentCardId = 999L;
        when(cardService.getCardById(eq(deckId), eq(nonExistentCardId), eq(testUser)))
                .thenThrow(new CardNotFoundException("Card not found"));

        // Act & Assert
        assertThrows(CardNotFoundException.class, () -> sut.getDeck(deckId, nonExistentCardId));
    }

    @Test
    public void updateCard_WhenCardDoesNotExist_ShouldThrowCardNotFoundException() {
        // Given
        Long deckId = 1L;
        Long nonExistentCardId = 999L;
        Card nonExistentCard = TestUtils.createCard();
        when(cardService.updateCard(eq(deckId), eq(nonExistentCardId), any(Card.class), eq(testUser)))
                .thenThrow(new CardNotFoundException("Card not found"));

        // Act & Assert
        assertThrows(CardNotFoundException.class, () -> sut.updateDeck(deckId, nonExistentCardId, nonExistentCard));
    }

    @Test
    public void deleteCard_WhenCardDoesNotExist_ShouldThrowCardNotFoundException() {
        // Given
        Long deckId = 1L;
        Long nonExistentCardId = 999L;
        doThrow(new CardNotFoundException("Card not found")).when(cardService).deleteCard(eq(deckId), eq(nonExistentCardId), eq(testUser));

        // Act & Assert
        assertThrows(CardNotFoundException.class, () -> sut.deleteDeck(deckId, nonExistentCardId));
    }
}
