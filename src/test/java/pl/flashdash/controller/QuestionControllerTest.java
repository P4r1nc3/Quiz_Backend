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
import pl.flashdash.exception.QuestionNotFoundException;
import pl.flashdash.model.User;
import pl.flashdash.model.question.Question;
import pl.flashdash.service.QuestionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private QuestionController sut;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = TestUtils.createUser();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
    }

    @Test
    public void addQuestionToDeck_WhenQuestionIsValid_ShouldReturnCreatedQuestion() {
        // Given
        Question inputQuestion = TestUtils.createQuestion();
        Long deckId = 1L;
        when(questionService.addQuestionToDeck(eq(deckId), any(Question.class), eq(testUser))).thenReturn(inputQuestion);

        // Act
        ResponseEntity<Question> response = sut.addQuestionToDeck(deckId, inputQuestion);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inputQuestion, response.getBody());
    }

    @Test
    public void getAllQuestionsInDeck_WhenCalled_ShouldReturnAllQuestionsForDeck() {
        // Given
        Long deckId = 1L;
        List<Question> expectedQuestions = List.of(TestUtils.createQuestion(), TestUtils.createQuestion());
        when(questionService.getAllQuestionsInDeck(eq(deckId), eq(testUser))).thenReturn(expectedQuestions);

        // Act
        ResponseEntity<List<Question>> response = sut.getAllQuestionsInDeck(deckId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedQuestions.size(), response.getBody().size());
        assertEquals(expectedQuestions, response.getBody());
    }

    @Test
    public void getQuestion_WhenQuestionExists_ShouldReturnQuestion() {
        // Given
        Long deckId = 1L;
        Long questionId = 1L;
        Question expectedQuestion = TestUtils.createQuestion();
        when(questionService.getQuestionById(eq(deckId), eq(questionId), eq(testUser))).thenReturn(expectedQuestion);

        // Act
        ResponseEntity<Question> response = sut.getQuestion(deckId, questionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedQuestion, response.getBody());
    }

    @Test
    public void updateQuestion_WhenQuestionExists_ShouldReturnUpdatedQuestion() {
        // Given
        Long deckId = 1L;
        Long questionId = 1L;
        Question updatedQuestion = TestUtils.createQuestion();
        when(questionService.updateQuestion(eq(deckId), eq(questionId), any(Question.class), eq(testUser))).thenReturn(updatedQuestion);

        // Act
        ResponseEntity<Question> response = sut.updateQuestion(deckId, questionId, updatedQuestion);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedQuestion, response.getBody());
    }

    @Test
    public void deleteQuestion_WhenQuestionIsDeleted_ShouldReturnNoContent() {
        // Given
        Long deckId = 1L;
        Long questionId = 1L;
        doNothing().when(questionService).deleteQuestion(eq(deckId), eq(questionId), eq(testUser));

        // Act
        ResponseEntity<Void> response = sut.deleteQuestion(deckId, questionId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getQuestion_WhenQuestionDoesNotExist_ShouldThrowQuestionNotFoundException() {
        // Given
        Long deckId = 1L;
        Long nonExistentQuestionId = 999L;
        when(questionService.getQuestionById(eq(deckId), eq(nonExistentQuestionId), eq(testUser)))
                .thenThrow(new QuestionNotFoundException("Question not found"));

        // Act & Assert
        assertThrows(QuestionNotFoundException.class, () -> sut.getQuestion(deckId, nonExistentQuestionId));
    }

    @Test
    public void updateQuestion_WhenQuestionDoesNotExist_ShouldThrowQuestionNotFoundException() {
        // Given
        Long deckId = 1L;
        Long nonExistentQuestionId = 999L;
        Question nonExistentQuestion = TestUtils.createQuestion();
        when(questionService.updateQuestion(eq(deckId), eq(nonExistentQuestionId), any(Question.class), eq(testUser)))
                .thenThrow(new QuestionNotFoundException("Question not found"));

        // Act & Assert
        assertThrows(QuestionNotFoundException.class, () -> sut.updateQuestion(deckId, nonExistentQuestionId, nonExistentQuestion));
    }

    @Test
    public void deleteQuestion_WhenQuestionDoesNotExist_ShouldThrowQuestionNotFoundException() {
        // Given
        Long deckId = 1L;
        Long nonExistentQuestionId = 999L;
        doThrow(new QuestionNotFoundException("Question not found")).when(questionService).deleteQuestion(eq(deckId), eq(nonExistentQuestionId), eq(testUser));

        // Act & Assert
        assertThrows(QuestionNotFoundException.class, () -> sut.deleteQuestion(deckId, nonExistentQuestionId));
    }
}
