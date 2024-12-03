package org.example.cropmonitoringsystembackend.service;

import org.example.cropmonitoringsystembackend.auth.request.SignInRequest;
import org.example.cropmonitoringsystembackend.auth.request.SignUpRequest;
import org.example.cropmonitoringsystembackend.auth.response.JWTAuthResponse;

public interface AuthenticationService {
    JWTAuthResponse signIn(SignInRequest signInRequest);

    JWTAuthResponse signUp(SignUpRequest signUpRequest);
}
