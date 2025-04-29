package com.naver.commerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TakingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String tel1;
    private String tel2;
    private String zipCode;
    private String baseAddress;
    private String detailedAddress;
    private boolean isRoadNameAddress;
    private String addressType;
}
