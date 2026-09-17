package com.example.hrms.controller.api;

import com.example.hrms.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private final UserAccountService userAccountService;

    public AuthApiController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        return userAccountService.authenticate(request.email(), request.password())
                .map(account -> {
                    HttpSession session = servletRequest.getSession(true);
                    session.setAttribute("AUTH_USER_EMAIL", account.getEmail());
                    return Map.<String, Object>of("authenticated", true, "email", account.getEmail());
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    }


    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody SignupRequest request) {
        userAccountService.register(request.fullName(), request.email(), request.password());
        return Map.of("created", true);
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest request, Principal principal) {
        HttpSession session = request.getSession(false);
        String sessionEmail = session == null ? null : (String) session.getAttribute("AUTH_USER_EMAIL");
        String email = sessionEmail != null ? sessionEmail : principal == null ? "" : principal.getName();
        return Map.of("authenticated", !email.isBlank(), "email", email);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Map.of("loggedOut", true);
    }

    public record LoginRequest(String email, String password) {
    }

    public record SignupRequest(String fullName, String email, String password) {
    }
}
