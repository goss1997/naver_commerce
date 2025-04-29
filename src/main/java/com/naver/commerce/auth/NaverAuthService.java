package com.naver.commerce.auth;

import com.naver.commerce.util.SignatureGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverAuthService {

    private final WebClient.Builder webClientBuilder;

    @Value("${naver.api.base-url}")
    private String baseUrl;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    @Value("${naver.api.account-id}")
    private String accountId;

    private String accessToken;

    @PostConstruct
    public void init() {
        fetchAccessToken();
    }



    public String getAccessToken() {
        if (accessToken == null) {
            fetchAccessToken();
        }
        return accessToken;
    }

    private void fetchAccessToken() {

        TokenResponse tokenResponse = getAccessTokenTypeSeller(accountId);

        if (tokenResponse != null) {
            this.accessToken = tokenResponse.getAccessToken();
            log.info("네이버 인증 토큰 갱신 완료 : {}", accessToken);
        } else {
            log.error("네이버 인증 토큰 발급 실패");
            throw new RuntimeException("토큰 발급 실패");
        }
    }

    /**
     * Seller Access Token 발급 서비스 로직
     */
    public TokenResponse getAccessTokenTypeSeller(String accountId) {
        // 없으면 self == seller
        if( accountId == null || accountId.isEmpty() ) {
            accountId = this.accountId;
        }

        long timestamp = System.currentTimeMillis();
        String signature = SignatureGenerator.generateSignature(clientId, clientSecret, timestamp);
        log.info("Signature: {}", signature);

        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        return webClient.post()
                .uri("/external/v1/oauth2/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("accept", "application/json")
                .bodyValue("client_id=" + clientId +
                        "&timestamp=" + timestamp +
                        "&client_secret_sign=" + signature +
                        "&grant_type=client_credentials" +
                        "&type=SELLER" +
                        "&account_id=" + accountId)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    /**
     * Self Access Token 발급 서비스 로직
     */
    public TokenResponse getAccessTokenTypeSelf() {
        long timestamp = System.currentTimeMillis();
        String signature = SignatureGenerator.generateSignature(clientId, clientSecret, timestamp);
        log.info("Signature: {}", signature);

        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        return webClient.post()
                .uri("/external/v1/oauth2/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("accept", "application/json")
                .bodyValue("client_id=" + clientId +
                        "&timestamp=" + timestamp +
                        "&client_secret_sign=" + signature +
                        "&grant_type=client_credentials" +
                        "&type=SELF")
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }
}
