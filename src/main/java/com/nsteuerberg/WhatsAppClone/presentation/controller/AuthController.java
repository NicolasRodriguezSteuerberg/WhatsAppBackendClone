package com.nsteuerberg.WhatsAppClone.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @PostMapping("login")
    public ResponseEntity<?> signIn() {
        return null;
    }

    @PostMapping("register")
    public ResponseEntity<?> signUp() {
        return null;
    }
}
