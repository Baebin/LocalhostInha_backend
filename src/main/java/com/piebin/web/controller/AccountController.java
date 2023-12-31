package com.piebin.web.controller;

import com.piebin.web.domain.Account;
import com.piebin.web.dto.AccountGetInfoRequestDto;
import com.piebin.web.dto.AccountRequestDto;
import com.piebin.web.dto.AccountSignInRequestDto;
import com.piebin.web.repository.AccountRepository;
import com.piebin.web.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    private final AccountRepository repository;

    @PostMapping("/api/account/validate")
    public ResponseEntity<Boolean> validate(
            @RequestBody AccountRequestDto dto) {
        Optional<Account> account = repository.findById(dto.getId());
        if (account.isPresent())
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/api/account/signup")
    public ResponseEntity<Boolean> signUp(
            @RequestBody AccountRequestDto dto) {
        Optional<Account> account = repository.findById(dto.getId());
        if (account.isPresent())
            return ResponseEntity.ok(false);
        service.register(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/api/account/signin")
    public ResponseEntity<Boolean> signIn(
            @RequestBody AccountSignInRequestDto dto) {
        Optional<Account> account = repository.findById(dto.getId());
        if (!account.isPresent())
            return ResponseEntity.ok(false);
        if (!account.get().getPassword().equals(dto.getPw()))
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/api/account/getinfo")
    public String getInfo(
            @RequestBody AccountGetInfoRequestDto dto) {
        return service.getInfo(dto);
    }

    //
    // Test API
    //

    @GetMapping("/api/test/account/validate")
    public ResponseEntity<Boolean> testValidate(
            @RequestParam("id") String id) {
        Optional<Account> account = repository.findById(id);
        if (account.isPresent())
            return ResponseEntity.ok(false);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/api/test/account/signup")
    public ResponseEntity<Boolean> testSignUp(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("pw") String pw,
            @RequestParam("email") String email) {
        Optional<Account> account = repository.findById(id);
        if (account.isPresent())
            // throw new IllegalArgumentException("사용할 수 없는 아이디입니다.");
            return ResponseEntity.ok(false);
        AccountRequestDto dto = new AccountRequestDto();
        dto.setId(id);
        dto.setName(name);
        dto.setPw(pw);
        dto.setEmail(email);
        service.register(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/api/test/account/signin")
    public ResponseEntity<Boolean> testSignIn(
            @RequestParam("id") String id,
            @RequestParam("pw") String pw) {
        Optional<Account> account = repository.findById(id);
        if (!account.isPresent())
            return ResponseEntity.ok(false);
        if (!account.get().getPassword().equals(pw))
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/api/test/account/getinfo")
    public String getInfo(
            @RequestParam("id") String id) {
        AccountGetInfoRequestDto dto = new AccountGetInfoRequestDto();
        dto.setId(id);
        return service.getInfo(dto);
    }
}
