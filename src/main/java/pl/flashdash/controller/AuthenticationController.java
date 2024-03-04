package pl.flashdash.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.flashdash.utility.ControllerUtils;
import pl.flashdash.dao.request.SignUpRequest;
import pl.flashdash.dao.request.SignInRequest;
import pl.flashdash.dao.response.JwtAuthenticationResponse;
import pl.flashdash.exception.InvalidLoginDetailsException;
import pl.flashdash.exception.UserAlreadyExistsException;
import pl.flashdash.service.security.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @ExceptionHandler(InvalidLoginDetailsException.class)
    public ResponseEntity<Object> handleInvalidLoginDetailsException(InvalidLoginDetailsException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                "Check your login credentials and try again.",
                request
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                "Try using a different email.",
                request
        );
    }
}
