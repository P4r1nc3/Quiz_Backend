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
import pl.flashdash.exception.DeckNotFoundException;
import pl.flashdash.model.Deck;
import pl.flashdash.model.User;
import pl.flashdash.service.DeckService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeckControllerTest {

    @Mock
    private DeckService deckService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DeckController sut;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = TestUtils.createUser();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
    }

    @Test
    public void createDeck_WhenDeckIsValid_ShouldReturnCreatedDeck() {
        // Given
        Deck inputDeck = TestUtils.createDeck();
        inputDeck.setDeckId(null); // Simulating a new deck creation scenario
        Deck savedDeck = TestUtils.createDeck();
        when(deckService.createDeck(any(Deck.class), eq(testUser))).thenReturn(savedDeck);

        // Act
        ResponseEntity<Deck> response = sut.createDeck(inputDeck);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedDeck, response.getBody());
    }

    @Test
    public void getAllDecks_WhenCalled_ShouldReturnAllDecksForUser() {
        // Given
        List<Deck> expectedDecks = List.of(TestUtils.createDeck(), TestUtils.createDeck());
        when(deckService.getAllDecks(eq(testUser))).thenReturn(expectedDecks);

        // Act
        ResponseEntity<List<Deck>> response = sut.getAllDecks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDecks.size(), response.getBody().size());
        assertEquals(expectedDecks, response.getBody());
    }

    @Test
    public void getDeck_WhenDeckExists_ShouldReturnDeck() {
        // Given
        Deck expectedDeck = TestUtils.createDeck();
        when(deckService.getDeckById(eq(expectedDeck.getDeckId()), eq(testUser))).thenReturn(expectedDeck);

        // Act
        ResponseEntity<Deck> response = sut.getDeck(expectedDeck.getDeckId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDeck, response.getBody());
    }

    @Test
    public void updateDeck_WhenDeckExists_ShouldReturnUpdatedDeck() {
        // Given
        Deck existingDeck = TestUtils.createDeck();
        when(deckService.updateDeck(eq(existingDeck.getDeckId()), any(Deck.class), eq(testUser))).thenReturn(existingDeck);

        // Act
        ResponseEntity<Deck> response = sut.updateDeck(existingDeck.getDeckId(), existingDeck);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingDeck, response.getBody());
    }

    @Test
    public void deleteDeck_WhenDeckIsDeleted_ShouldReturnNoContent() {
        // Given
        Long deckIdToDelete = 1L;
        doNothing().when(deckService).deleteDeck(deckIdToDelete, testUser);

        // Act
        ResponseEntity<Void> response = sut.deleteDeck(deckIdToDelete);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getDeck_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Long nonExistentDeckId = 999L;
        when(deckService.getDeckById(eq(nonExistentDeckId), eq(testUser)))
                .thenThrow(new DeckNotFoundException("Deck not found"));

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> sut.getDeck(nonExistentDeckId));
    }

    @Test
    public void updateDeck_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Deck nonExistentDeck = TestUtils.createDeck();
        nonExistentDeck.setDeckId(999L);
        when(deckService.updateDeck(eq(nonExistentDeck.getDeckId()), any(Deck.class), eq(testUser)))
                .thenThrow(new DeckNotFoundException("Deck not found"));

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> sut.updateDeck(nonExistentDeck.getDeckId(), nonExistentDeck));
    }

    @Test
    public void deleteDeck_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Long nonExistentDeckId = 999L;
        doThrow(new DeckNotFoundException("Deck not found")).when(deckService).deleteDeck(eq(nonExistentDeckId), eq(testUser));

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> sut.deleteDeck(nonExistentDeckId));
    }
}
