package com.naver.commerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Getter
@Setter
public class NaverLastChangedProductOrdersRequestDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotBlank(message = "lastChangedFrom은 필수입니다")
    private OffsetDateTime lastChangedFrom; // 필수

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime lastChangedTo;   // 선택

    private String lastChangedType;         // 선택

    private String moreSequence;            // 선택

    private Integer limitCount;             // 선택
}