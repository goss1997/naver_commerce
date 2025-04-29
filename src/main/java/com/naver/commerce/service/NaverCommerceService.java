package com.naver.commerce.service;

import com.naver.commerce.auth.NaverAuthService;
import com.naver.commerce.dto.NaverProductOrderDetailRequestDto;
import com.naver.commerce.dto.NaverProductOrderDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import com.naver.commerce.util.DateTimeUtil;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverCommerceService {

    private final WebClient.Builder webClientBuilder;
    private final NaverAuthService naverAuthService;

    @Value("${naver.api.base-url}")
    private String baseUrl;


    public  Mono<JsonNode>getSellerInfoByToken(String jwe) {
        String token = naverAuthService.getAccessToken();
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/external/v1/commerce-solutions/seller-info-by-token")
                        .queryParam("token", jwe)
                        .build())
                .header("Authorization", "Bearer " + "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJtZXJjIiwic3ViIjoiU0VMTEVSX0lORk8iLCJleHAiOjE3NDU0NzIzMjIsImlhdCI6MTc0NTQ3MjI2Miwic29sdXRpb25JZCI6IlNPTF8xUk95UXNJRGpCcHprU2hvRzRCcVVyIiwiYWNjb3VudFVpZCI6Im5jcF8yc1NGMVM0TnZwbDYycUhIcEhWS0giLCJyb2xlR3JvdXBUeXBlIjoiUkVQUkVTRU5UIiwiY2hhbm5lbE5hbWUiOiLrsJjtkojqtazsobDrjIAiLCJkZWZhdWx0Q2hhbm5lbE5vIjoxMDE1ODUxNDAsInR5cGUiOiJTVE9SRUZBUk0iLCJ1cmwiOiJodHRwczovL3NtYXJ0c3RvcmUubmF2ZXIuY29tL3Zvb21lcmFuZ3N0b3JlIiwicmVwcmVzZW50SW1hZ2VVcmwiOiJodHRwOi8vc2hvcDEucGhpbmYubmF2ZXIubmV0LzIwMjQwODI2XzI2Mi8xNzI0NjMyNjQ4MDc3RUhrSkFfSlBFRy8zNzkwNzgyMTg4MTIzMzU0N18xNDU3NTUxNjk5LmpwZyIsImNhdGVnb3J5SWQiOiI1MDAwMDAwMCIsInJlcHJlc2VudFR5cGUiOiJET01FU1RJQ19CVVNJTkVTUyIsImJ1c2luZXNzVHlwZSI6IkNPUlBPUkFUSU9OIiwiYnVzaW5lc3NSZWdpc3RyYXRpb25OdW1iZXIiOiIyOTg4NjAyMjkyIiwiYWN0aW9uR3JhZGUiOiJGSUZUSCIsInNlcnZpY2VTYXRpc2ZhY3Rpb25HcmFkZSI6ZmFsc2UsInBsYW5JZCI6IlBMTl8ybmhyNlBnSDBjRHVCSHNDRDgzVWlTIiwic3Vic2NyaXB0aW9uSWQiOiIxUEhhcTJ2QXQ1MWN1YklRaUUwNW1DIiwicm91bmQiOjI4LCJyb3VuZEVuZERhdGUiOjE3NDU3NjU5OTk5OTksInN0YXR1cyI6IlNVQlNDUklCSU5HIn0.KkFxSK7YfRxHUHvWPYjNbWdC4Y7MAnzIFrewramR5M1tXtgMpE6BEEy4URYOJ_O_u9_Srl6q1MW0MpIhIwjzYuZzizlLdbl7LQB4ZMuV9vopu9JNWtHDfIEDbEb-gMJGIYw2Yqw_ziA8cZV06L8OIFZCdZtocp0kMTY-9Z83wBrKh9X3SupVoJSan57wFg5b4bgZr0co4MJjl_d_aGG9gp1mAJga-LZuqPgb3SCyZLAi0fVCjL9aJbyelqlA7KCG7EVvX0qVp8_m3Bz_QiRjWlT4otSWYjbzFr9BTwC_aEpjV9hPDbIaermhY8xOY7y8VN3Zrs4NmFXEKISa7TGepw")
                .header("Accept", "application/json;charset=UTF-8")
                .retrieve()
                .bodyToMono(JsonNode.class);
    }


    public Mono<JsonNode> getProductOrderList(NaverProductOrderDetailRequestDto requestDto) {
        String token = naverAuthService.getAccessToken();
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        MultiValueMap<String, String> queryParams = buildQueryParams(requestDto);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/external/v1/pay-order/seller/product-orders")
                        .queryParams(queryParams)
                        .build())
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json;charset=UTF-8")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSubscribe(subscription -> log.info("Requesting product order details with params: {}", queryParams))
                .doOnSuccess(response -> log.info("Successfully fetched product order details"))
                .doOnError(WebClientResponseException.class, e -> {
                    WebClientResponseException ex = (WebClientResponseException) e;
                    log.error("API Error: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
                })
                .doOnError(Exception.class, e -> log.error("Unexpected Error occurred while fetching product order details", e));
    }

    private MultiValueMap<String, String> buildQueryParams(NaverProductOrderDetailRequestDto requestDto) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        // 필수 파라미터
        queryParams.add("from", DateTimeUtil.formatToIso8601(requestDto.getFrom()));
        queryParams.add("rangeType", requestDto.getRangeType());

        // 선택 파라미터
        if (requestDto.getTo() != null) queryParams.add("to", DateTimeUtil.formatToIso8601(requestDto.getTo()));

        if (requestDto.getProductOrderStatuses() != null) {
            requestDto.getProductOrderStatuses().forEach(status ->
                    queryParams.add("productOrderStatuses", status));
        }

        if (requestDto.getClaimStatuses() != null) {
            requestDto.getClaimStatuses().forEach(status ->
                    queryParams.add("claimStatuses", status));
        }

        if (requestDto.getPlaceOrderStatusType() != null) {
            queryParams.add("placeOrderStatusType", requestDto.getPlaceOrderStatusType());
        }

        if (requestDto.getFulfillment() != null) {
            queryParams.add("fulfillment", String.valueOf(requestDto.getFulfillment()));
        }

        if (requestDto.getPageSize() != null) {
            queryParams.add("pageSize", String.valueOf(requestDto.getPageSize()));
        } else {
            queryParams.add("pageSize", "300"); // 기본값 300
        }

        if (requestDto.getPage() != null) {
            queryParams.add("page", String.valueOf(requestDto.getPage()));
        } else {
            queryParams.add("page", "1"); // 기본값 1
        }

        if (requestDto.getQuantityClaimCompatibility() != null) {
            queryParams.add("quantityClaimCompatibility", String.valueOf(requestDto.getQuantityClaimCompatibility()));
        }

        return queryParams;
    }


    public Mono<JsonNode> getProductOrder(NaverProductOrderDto naverProductOrderDto) {
        String token = naverAuthService.getAccessToken();
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        return webClient.post()
                .uri("/external/v1/pay-order/seller/product-orders/query")
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .bodyValue(naverProductOrderDto) // <-- JSON body로 보냄
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSubscribe(subscription -> log.info("Requesting product order with body: {}", naverProductOrderDto))
                .doOnSuccess(response -> log.info("Successfully fetched product order details"))
                .doOnError(WebClientResponseException.class, e -> {
                    WebClientResponseException ex = (WebClientResponseException) e;
                    log.error("API Error: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
                })
                .doOnError(Exception.class, e -> log.error("Unexpected Error occurred while fetching product order details", e));
    }


    public Mono<JsonNode> getLastChangedProductOrders(OffsetDateTime lastChangedFrom,
                                                      OffsetDateTime lastChangedTo,
                                                      String lastChangedType,
                                                      String moreSequence,
                                                      Integer limitCount) {
        String token = naverAuthService.getAccessToken();
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("lastChangedFrom", DateTimeUtil.formatToIso8601(lastChangedFrom));

        if (lastChangedTo != null) {
            queryParams.add("lastChangedTo", DateTimeUtil.formatToIso8601(lastChangedTo));
        }
        if (lastChangedType != null) {
            queryParams.add("lastChangedType", lastChangedType);
        }
        if (moreSequence != null) {
            queryParams.add("moreSequence", moreSequence);
        }
        if (limitCount != null) {
            queryParams.add("limitCount", String.valueOf(limitCount));
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/external/v1/pay-order/seller/product-orders/last-changed-statuses")
                        .queryParams(queryParams)
                        .build())
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json;charset=UTF-8")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSubscribe(subscription -> log.info("Requesting last changed product orders with params: {}", queryParams))
                .doOnSuccess(response -> log.info("Successfully fetched last changed product orders"))
                .doOnError(WebClientResponseException.class, e -> {
                    WebClientResponseException ex = (WebClientResponseException) e;
                    log.error("API Error: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
                })
                .doOnError(Exception.class, e -> log.error("Unexpected Error occurred while fetching last changed product orders", e));
    }
}
