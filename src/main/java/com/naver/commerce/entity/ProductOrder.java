package com.naver.commerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Order order;

    @Column(unique = true)
    private String productOrderId;
    private int quantity;
    private int initialQuantity;
    private int remainQuantity;
    private int totalProductAmount;
    private int initialProductAmount;
    private int remainProductAmount;
    private int totalPaymentAmount;
    private int initialPaymentAmount;
    private int remainPaymentAmount;
    private String productOrderStatus;
    private String productId;
    private String productName;
    private int unitPrice;
    private String productClass;
    private String originalProductId;
    private String merchantChannelId;
    private String expectedDeliveryCompany;
    private String deliveryAttributeType;
    private OffsetDateTime placeOrderDate;
    private String placeOrderStatus;
    private OffsetDateTime shippingDueDate;
    private String expectedDeliveryMethod;
    private String packageNumber;
    private String itemNo;
    private String optionCode;
    private int optionPrice;
    private String mallId;
    private String inflowPath;
    private int productDiscountAmount;
    private int initialProductDiscountAmount;
    private int remainProductDiscountAmount;
    private int sellerBurdenDiscountAmount;
    private int deliveryFeeAmount;
    private String deliveryPolicyType;
    private int sectionDeliveryFee;
    private String shippingFeeType;
    private int deliveryDiscountAmount;

    // 연관관계 매핑 - 배송지 주소 (OneToOne)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private ShippingAddress shippingAddress;

    // 연관관계 매핑 - 수취인 주소 (OneToOne)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taking_address_id")
    private TakingAddress takingAddress;

    private String claimId;
    private String claimStatus;
    private String claimType;
    private String commissionRatingType;
    private String commissionPrePayStatus;
    private Integer paymentCommission;
    private Integer saleCommission;
    private Integer knowledgeShoppingSellingInterlockCommission;
    private Integer channelCommission;
    private Integer expectedSettlementAmount;

    @OneToOne(cascade = CascadeType.ALL)
    private Return returnClaim;

    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL)
    private List<CompletedClaim> completedClaims = new ArrayList<>();


}
