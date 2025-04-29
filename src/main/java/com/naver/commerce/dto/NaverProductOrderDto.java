package com.naver.commerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NaverProductOrderDto {

    private List<String> productOrderIds;

    private boolean quantityClaimCompatibility = true;

}
