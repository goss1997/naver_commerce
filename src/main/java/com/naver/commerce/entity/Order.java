package com.naver.commerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "orders") // order는 예약어라 테이블명은 orders로 설정!
@Getter
@Setter
public class Order {

    @Id
    private String orderId;

    private OffsetDateTime orderDate;
    private String ordererId;
    private String ordererNo;
    private String ordererName;
    private String ordererTel;
    private String isDeliveryMemoParticularInput;
    private OffsetDateTime paymentDate;
    private String paymentMeans;
    private String payLocationType;
    private int orderDiscountAmount;
    private int generalPaymentAmount;
    private int naverMileagePaymentAmount;
    private int chargeAmountPaymentAmount;
    private int payLaterPaymentAmount;
}
