package com.naver.commerce.controller;


import com.naver.commerce.auth.NaverAuthService;
import com.naver.commerce.auth.TokenResponse;
import com.naver.commerce.jwt.JwtTokenValidator;
import com.naver.commerce.service.NaverCommerceService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final JwtTokenValidator jwtTokenValidator;
    private final NaverCommerceService naverCommerceService;
    private final NaverAuthService naverAuthService;


    @GetMapping("/token")
    public ResponseEntity<?> validateJwtToken(@RequestParam String jwt) throws ServerException {

        String issuer = "merc";
        String subjectType = "SELLER_INFO";

        log.info(" {}",jwt);

        Map<String, Object> claimWithVerify = jwtTokenValidator.getClaimWithVerify(jwt, issuer, subjectType);

        return ResponseEntity.ok(claimWithVerify);
    }


    @GetMapping("/seller-info-by-token")
    public Mono<ResponseEntity<JsonNode>> getSellerInfo(@RequestParam String token) {
        return naverCommerceService.getSellerInfoByToken(token)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Failed to get seller info", e);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping("/token/self")
    public TokenResponse getTokenSelf() {
        return naverAuthService.getAccessTokenTypeSelf();
    }

    @GetMapping("/token/seller")
    public TokenResponse getTokenSeller(@RequestParam String accountId) {
        return naverAuthService.getAccessTokenTypeSeller(accountId);
    }


}
