package com.naver.commerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class NaverProductOrderDetailRequestDto {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @NotBlank(message = "from은 필수입니다")
    private OffsetDateTime from;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime  to;

    @NotBlank(message = "rangeType은 필수입니다")
    private String rangeType;

    private List<String> productOrderStatuses;

    private List<String> claimStatuses;

    private String placeOrderStatusType;

    private Boolean fulfillment;

    private Integer pageSize = 300;   // 기본값 300

    private Integer page = 1;         // 기본값 1

    private Boolean quantityClaimCompatibility;
}