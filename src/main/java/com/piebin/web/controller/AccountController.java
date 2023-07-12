package com.piebin.web.controller;

import com.piebin.web.domain.Account;
import com.piebin.web.dto.AccountRequestDto;
import com.piebin.web.repository.AccountRepository;
import com.piebin.web.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    private final AccountRepository repository;

    @PostMapping("/api/account/validate")
    public ResponseEntity<Boolean> validate(@RequestBody AccountRequestDto dto) {
        return ResponseEntity.ok(
                repository.findById(dto.getId()).isPresent()
        );
    }

    @PostMapping("/api/account/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody AccountRequestDto dto) {
        Optional<Account> account = repository.findById(dto.getId());
        if (account.isPresent())
            return ResponseEntity.ok(false);
        service.register(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/api/account/signin")
    public ResponseEntity<Boolean> signIn(@RequestBody AccountRequestDto dto) {
        Optional<Account> account = repository.findById(dto.getId());
        if (!account.isPresent())
            return ResponseEntity.ok(false);
        if (!account.get().getPassword().equals(dto.getPw()))
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    //
    // Test API
    //

    @GetMapping("/api/test/account/validate")
    public ResponseEntity<Boolean> testValidate(@RequestParam String id) {
        return ResponseEntity.ok(
                repository.findById(id).isPresent()
        );
    }

    @GetMapping("/api/test/account/signup")
    public ResponseEntity<Boolean> testSignUp(@RequestParam String id, String pw) {
        Optional<Account> account = repository.findById(id);
        if (account.isPresent())
            // throw new IllegalArgumentException("사용할 수 없는 아이디입니다.");
            return ResponseEntity.ok(false);
        AccountRequestDto dto = new AccountRequestDto();
        dto.setId(id);
        dto.setPw(pw);
        service.register(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/api/test/account/signin")
    public ResponseEntity<Boolean> testSignIn(@RequestParam String id, String pw) {
        Optional<Account> account = repository.findById(id);
        if (!account.isPresent())
            return ResponseEntity.ok(false);
        if (!account.get().getPassword().equals(pw))
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }
}
