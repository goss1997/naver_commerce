package com.naver.commerce.dto;

import com.naver.commerce.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductOrderResponse {
    private Order order;
    private ProductOrder productOrder;
    @JsonProperty("return")
    private Return returnClaim;
    private List<CompletedClaim> completedClaims;
}
