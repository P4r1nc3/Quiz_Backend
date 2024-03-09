package pl.flashdash.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.flashdash.utility.ControllerUtils;
import pl.flashdash.exception.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeckNotFoundException.class)
    public ResponseEntity<Object> handleDeckNotFoundException(DeckNotFoundException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.NOT_FOUND,
                ControllerUtils.NOT_FOUND_CAUSE,
                ControllerUtils.NOT_FOUND_ACTION,
                request
        );
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<Object> handleCardNotFoundException(CardNotFoundException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.NOT_FOUND,
                ControllerUtils.NOT_FOUND_CAUSE,
                ControllerUtils.NOT_FOUND_ACTION,
                request
        );
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<Object> handleQuestionNotFoundException(QuestionNotFoundException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.NOT_FOUND,
                ControllerUtils.NOT_FOUND_CAUSE,
                ControllerUtils.NOT_FOUND_ACTION,
                request
        );
    }

    @ExceptionHandler(DeckTypeChangeException.class)
    public ResponseEntity<Object> handleDeckTypeChangeException(DeckTypeChangeException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ControllerUtils.BAD_REQUEST_DECK_TYPE_CHANGE_ACTION,
                request
        );
    }

    @ExceptionHandler(WrongDeckInstanceException.class)
    public ResponseEntity<Object> handleWrongDeckInstanceException(WrongDeckInstanceException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ControllerUtils.BAD_REQUEST_INVALID_DECK_TYPE_ACTION,
                request
        );
    }
}
