package com.naver.commerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
public class CompletedClaim {

    @Id
    private String claimId; // 클레임 ID가 Primary Key

    private String claimType;
    private String claimStatus;
    private OffsetDateTime claimRequestDate;
    private String requestChannel;
    private String claimRequestReason;
    private String claimRequestDetailContent;
    private int requestQuantity;
    private OffsetDateTime claimRequestAdmissionDate;
    private OffsetDateTime claimCompleteOperationDate;
    private String refundStandbyStatus;
    private OffsetDateTime collectCompletedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order_id") // FK 연결
    private ProductOrder productOrder;
}
