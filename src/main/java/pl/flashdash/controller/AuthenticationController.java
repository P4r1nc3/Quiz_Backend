package pl.flashdash.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.flashdash.dao.request.SignUpRequest;
import pl.flashdash.dao.request.SignInRequest;
import pl.flashdash.dao.response.JwtAuthenticationResponse;
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
}