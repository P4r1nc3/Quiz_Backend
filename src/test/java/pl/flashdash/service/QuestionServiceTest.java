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
import pl.flashdash.exception.QuestionNotFoundException;
import pl.flashdash.model.User;
import pl.flashdash.model.question.Question;
import pl.flashdash.model.question.QuestionDeck;
import pl.flashdash.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private DeckService deckService;

    @InjectMocks
    private QuestionService questionService;

    private User testUser;
    private QuestionDeck testDeck;
    private Question testQuestion;

    @BeforeEach
    void setup() {
        testUser = TestUtils.createUser();
        testDeck = TestUtils.createQuestionDeck();
        testQuestion = TestUtils.createQuestion();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(deckService.getDeckById(anyLong(), any(User.class))).thenReturn(testDeck);
    }

    @Test
    void addCardToDeck_WhenCardIsValid_ShouldReturnSavedCard() {
        // Given
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);

        // Act
        Question savedQuestion = questionService.addQuestionToDeck(testDeck.getId(), testQuestion, testUser);

        // Assert
        assertNotNull(savedQuestion);
        assertEquals(testQuestion.getQuestion(), savedQuestion.getQuestion());
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    void getAllCardsInDeck_WhenCalled_ShouldReturnAllCards() {
        // Given
        List<Question> expectedQuestions = List.of(testQuestion, TestUtils.createQuestion());
        when(questionRepository.findAllByDeck(testDeck)).thenReturn(expectedQuestions);

        // Act
        List<Question> questions = questionService.getAllQuestionsInDeck(testDeck.getId(), testUser);

        // Assert
        assertFalse(questions.isEmpty());
        assertEquals(expectedQuestions.size(), questions.size());
        assertEquals(expectedQuestions, questions);
    }

    @Test
    void getQuestionById_WhenCardExists_ShouldReturnCard() {
        // Given
        when(questionRepository.findByDeckAndQuestionId(testDeck, testQuestion.getQuestionId())).thenReturn(Optional.of(testQuestion));

        // Act
        Question foundQuestion = questionService.getQuestionById(testDeck.getId(), testQuestion.getQuestionId(), testUser);

        // Assert
        assertEquals(testQuestion, foundQuestion);
    }

    @Test
    void updateQuestion_WhenCardExists_ShouldReturnUpdatedCard() {
        // Given
        when(questionRepository.findByDeckAndQuestionId(testDeck, testQuestion.getQuestionId())).thenReturn(Optional.of(testQuestion));
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);

        // Act
        Question updatedQuestion = questionService.updateQuestion(testDeck.getId(), testQuestion.getQuestionId(), testQuestion, testUser);

        // Assert
        assertEquals(testQuestion.getQuestion(), updatedQuestion.getQuestion());
        verify(questionRepository).save(testQuestion);
    }

    @Test
    void deleteQuestion_WhenQuestionExists_ShouldNotThrowException() {
        // Given
        when(questionRepository.findByDeckAndQuestionId(testDeck, testQuestion.getQuestionId())).thenReturn(Optional.of(testQuestion));
        doNothing().when(questionRepository).delete(any(Question.class));

        // Act & Assert
        assertDoesNotThrow(() -> questionService.deleteQuestion(testDeck.getId(), testQuestion.getQuestionId(), testUser));

        // Assert
        verify(questionRepository).delete(testQuestion);
    }

    @Test
    void getQuestionById_WhenQuestionDoesNotExist_ShouldThrowQuestionNotFoundException() {
        // Given
        Long nonExistentQuestionId = 999L;
        when(questionRepository.findByDeckAndQuestionId(testDeck, nonExistentQuestionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuestionNotFoundException.class, () -> questionService.getQuestionById(testDeck.getId(), nonExistentQuestionId, testUser));
    }

    @Test
    void updateQuestion_WhenQuestionDoesNotExist_ShouldThrowQuestionNotFoundException() {
        // Given
        Long nonExistentQuestionId = 999L;
        when(questionRepository.findByDeckAndQuestionId(testDeck, nonExistentQuestionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuestionNotFoundException.class, () -> questionService.updateQuestion(testDeck.getId(), nonExistentQuestionId, new Question(), testUser));
    }

    @Test
    void deleteQuestion_WhenQuestionDoesNotExist_ShouldThrowQuestionNotFoundException() {
        // Given
        Long nonExistentQuestionId = 999L;
        when(questionRepository.findByDeckAndQuestionId(testDeck, nonExistentQuestionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuestionNotFoundException.class, () -> questionService.deleteQuestion(testDeck.getId(), nonExistentQuestionId, testUser));
    }
}
