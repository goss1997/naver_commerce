package com.naver.commerce.controller;

import com.naver.commerce.dto.NaverLastChangedProductOrdersRequestDto;
import com.naver.commerce.dto.NaverProductOrderDetailRequestDto;
import com.naver.commerce.dto.NaverProductOrderDto;
import com.naver.commerce.service.NaverCommerceService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/naver")
@RequiredArgsConstructor
@Slf4j
public class NaverCommerceController {

    private final NaverCommerceService naverCommerceService;

    @PostMapping("/product-orders")
    public Mono<ResponseEntity<JsonNode>> getProductOrderList(@RequestBody NaverProductOrderDetailRequestDto requestDto) {
        return naverCommerceService.getProductOrderList(requestDto)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Failed to fetch product order details", e);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping("/product-orders")
    public Mono<ResponseEntity<JsonNode>> getProductOrder(@RequestBody NaverProductOrderDto naverProductOrderDto) {
        return naverCommerceService.getProductOrder(naverProductOrderDto)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Failed to fetch product order", e);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping("/product-orders/last-changed-statuses")
    public Mono<ResponseEntity<JsonNode>> getLastChangedProductOrders(@RequestBody NaverLastChangedProductOrdersRequestDto requestDto) {
        return naverCommerceService.getLastChangedProductOrders(
                        requestDto.getLastChangedFrom(),
                        requestDto.getLastChangedTo(),
                        requestDto.getLastChangedType(),
                        requestDto.getMoreSequence(),
                        requestDto.getLimitCount()
                )
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Failed to fetch last changed product orders", e);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }


}