package com.naver.commerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity(name = "returns")
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 생성되는 기본 키
    private Long id;

    private String claimId;
    private String claimStatus;
    private OffsetDateTime returnCompletedDate;
    private String returnDetailedReason;
    private String returnReason;
    private OffsetDateTime claimRequestDate;
    private String requestChannel;
    private int requestQuantity;
    private OffsetDateTime collectCompletedDate;
    private String refundStandbyStatus;

}
