package pl.flashdash.service;

import pl.flashdash.dao.request.SignUpRequest;
import pl.flashdash.dao.request.SignInRequest;
import pl.flashdash.dao.response.JwtAuthenticationResponse;


public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
