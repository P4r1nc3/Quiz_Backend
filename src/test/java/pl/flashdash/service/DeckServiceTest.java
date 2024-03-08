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
import pl.flashdash.model.card.CardDeck;
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
    private Deck testDeck;

    @BeforeEach
    void setup() {
        testUser = TestUtils.createUser();
        testDeck = TestUtils.createCardDeck();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @Test
    void createDeck_WhenDeckIsValid_ShouldReturnCreatedDeck() {
        // Given
        when(deckRepository.save(any(Deck.class))).thenReturn(testDeck);

        // Act
        Deck savedDeck = deckService.createDeck(new CardDeck(), testUser);

        // Assert
        assertNotNull(savedDeck);
        assertEquals(testDeck.getName(), savedDeck.getName());
        verify(deckRepository).save(any(Deck.class));
    }

    @Test
    void getAllDecks_WhenCalled_ShouldReturnAllDecksForUser() {
        // Given
        List<Deck> expectedDecks = List.of(TestUtils.createCardDeck(), TestUtils.createCardDeck());
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
        when(deckRepository.findByIdAndUser(testDeck.getId(), testUser)).thenReturn(Optional.of(testDeck));

        // Act
        Deck foundDeck = deckService.getDeckById(testDeck.getId(), testUser);

        // Assert
        assertEquals(testDeck, foundDeck);
    }

    @Test
    void updateDeck_WhenDeckExists_ShouldReturnUpdatedDeck() {
        // Given
        when(deckRepository.findByIdAndUser(testDeck.getId(), testUser)).thenReturn(Optional.of(testDeck));
        when(deckRepository.save(any(Deck.class))).thenReturn(testDeck);

        // Act
        Deck updatedDeck = deckService.updateDeck(testDeck.getId(), testDeck, testUser);

        // Assert
        assertEquals(testDeck.getName(), updatedDeck.getName());
        verify(deckRepository).save(testDeck);
    }

    @Test
    void deleteDeck_WhenDeckIsDeleted_ShouldNotThrowException() {
        // Given
        when(deckRepository.findByIdAndUser(testDeck.getId(), testUser)).thenReturn(Optional.of(testDeck));
        doNothing().when(cardRepository).deleteAllByDeck(any(Deck.class));
        doNothing().when(deckRepository).delete(any(Deck.class));

        // Act
        assertDoesNotThrow(() -> deckService.deleteDeck(testDeck.getId(), testUser));

        // Assert
        verify(cardRepository).deleteAllByDeck(testDeck);
        verify(deckRepository).delete(testDeck);
    }

    @Test
    void getDeckById_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Long nonExistentDeckId = 999L;
        when(deckRepository.findByIdAndUser(nonExistentDeckId, testUser)).thenThrow(new DeckNotFoundException("Deck not found"));

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> deckService.getDeckById(nonExistentDeckId, testUser));
    }

    @Test
    void updateDeck_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Long nonExistentDeckId = 999L;
        when(deckRepository.findByIdAndUser(nonExistentDeckId, testUser)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> deckService.updateDeck(nonExistentDeckId, new CardDeck(), testUser));
    }

    @Test
    void deleteDeck_WhenDeckDoesNotExist_ShouldThrowDeckNotFoundException() {
        // Given
        Long nonExistentDeckId = 999L;
        when(deckRepository.findByIdAndUser(nonExistentDeckId, testUser)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DeckNotFoundException.class, () -> deckService.deleteDeck(nonExistentDeckId, testUser));
    }
}
