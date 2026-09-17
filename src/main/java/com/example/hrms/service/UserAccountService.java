package com.example.hrms.service;

import com.example.hrms.model.UserAccount;
import com.example.hrms.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount register(String fullName, String email, String password) {
        if (userAccountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("An account already exists for this email.");
        }

        UserAccount account = new UserAccount();
        account.setFullName(fullName);
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setRole("HR_ADMIN");
        return userAccountRepository.save(account);
    }

    public boolean hasUsers() {
        return userAccountRepository.count() > 0;
    }

    public Optional<UserAccount> authenticate(String email, String password) {
        return userAccountRepository.findByEmail(email)
                .filter(account -> passwordEncoder.matches(password, account.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(account.getEmail())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }
}
