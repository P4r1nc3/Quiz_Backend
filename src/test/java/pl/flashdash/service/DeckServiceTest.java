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
import pl.flashdash.exception.DeckNotFoundException;
import pl.flashdash.model.Deck;
import pl.flashdash.model.User;
import pl.flashdash.repository.CardRepository;
import pl.flashdash.repository.DeckRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeckServiceTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DeckService deckService;

    private User testUser;
    private Deck inputDeck;

    @BeforeEach
    void setup() {
        testUser = TestUtils.createUser();
        inputDeck = TestUtils.createDeck();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
    }

    @Test
    void createDeck_WhenDeckIsValid_ShouldReturnCreatedDeck() {
        // Given
        inputDeck.setDeckId(null); // Simulating a new deck creation scenario
        when(deckRepository.save(any(Deck.class))).thenReturn(inputDeck);

        // Act
        Deck savedDeck = deckService.createDeck(new Deck(), testUser);

        // Assert
        assertNotNull(savedDeck);
        assertEquals(inputDeck.getName(), savedDeck.getName());
        verify(deckRepository).save(any(Deck.class));
    }

    @Test
    void getAllDecks_WhenCalled_ShouldReturnAllDecksForUser() {
        // Given
        List<Deck> expectedDecks = List.of(TestUtils.createDeck(), TestUtils.createDeck());
        when(deckRepository.findAllByUser(testUser)).thenReturn(expectedDecks);

        // Act
        List<Deck> decks = deckService.getAllDecks(testUser);

        // Assert
        assertFalse(decks.isEmpty());
        assertEquals(expectedDecks.size(), decks.size());
        assertEquals(expectedDecks, decks);
    }

    @Test
    void getDeckById_WhenDeckExists_ShouldReturnDeck() {
        // Given
        when(deckRepository.findByDeckIdAndUser(inputDeck.getDeckId(), testUser)).thenReturn(Optional.of(inputDeck));

        // Act
        Deck foundDeck = deckService.getDeckById(inputDeck.getDeckId(), testUser);

        // Assert
        assertEquals(inputDeck, foundDeck);
    }

    @Test
    void updateDeck_WhenDeckExists_ShouldReturnUpdatedDeck() {
        // Given
        when(deckRepository.findById(inputDeck.getDeckId())).thenReturn(Optional.of(inputDeck));
        when(deckRepository.save(any(Deck.class))).thenReturn(inputDeck);

        // Act
        Deck updatedDeck = deckService.updateDeck(inputDeck.getDeckId(), inputDeck, testUser);

        // Assert
        assertEquals(inputDeck.getName(), updatedDeck.getName());
        verify(deckRepository).save(inputDeck);
    }

    @Test
    void deleteDeck_WhenDeckIsDeleted_ShouldNotThrowException() {
        // Given
        when(deckRepository.findById(inputDeck.getDeckId())).thenReturn(Optional.of(inputDeck));
        doNothing().when(cardRepository).deleteAllByDeck(any(Deck.class));
        doNothing().when(deckRepository).delete(any(Deck.class));

        // Act
        assertDoesNotThrow(() -> deckService.deleteDeck(inputDeck.getDeckId(), testUser));

        // Assert
        verify(cardRepository).deleteAllByDeck(inputDeck);
        verify(deckRepository).delete(inputDeck);
    }

    @Test
    void getDeckById_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Long nonExistentDeckId = 999L;
        when(deckRepository.findByDeckIdAndUser(nonExistentDeckId, testUser)).thenThrow(new DeckNotFoundException("Deck not found"));

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> deckService.getDeckById(nonExistentDeckId, testUser));
    }

    @Test
    void updateDeck_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Long nonExistentDeckId = 999L;
        when(deckRepository.findById(nonExistentDeckId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> deckService.updateDeck(nonExistentDeckId, new Deck(), testUser));
    }

    @Test
    void deleteDeck_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Long nonExistentDeckId = 999L;
        when(deckRepository.findById(nonExistentDeckId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> deckService.deleteDeck(nonExistentDeckId, testUser));
    }
}
