package apptive.london.bridge.global.auth.local.controller;

import apptive.london.bridge.domain.user.entity.Role;
import apptive.london.bridge.global.auth.local.data.RegisterRequest;
import apptive.london.bridge.global.auth.local.data.AuthenticationRequest;
import apptive.london.bridge.global.auth.local.data.AuthenticationResponse;
import apptive.london.bridge.global.auth.local.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register/v1")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request, Role.USER));
    }

    @PostMapping("/register/admin/v1")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request, Role.ADMIN));
    }

    @PostMapping("/register/creator/v1")
    public ResponseEntity<AuthenticationResponse> registerManager(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request, Role.CREATOR));
    }

    @PostMapping("/authenticate/v1")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token/v1")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
