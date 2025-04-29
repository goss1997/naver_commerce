package com.naver.commerce.service;

import com.naver.commerce.dto.ProductOrderResponse;
import com.naver.commerce.entity.CompletedClaim;
import com.naver.commerce.entity.Order;
import com.naver.commerce.entity.ProductOrder;
import com.naver.commerce.repository.ProductOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductOrderService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Transactional
    public void saveProductOrder(ProductOrder productOrder) {
        productOrderRepository.save(productOrder); // DB에 저장
    }

    public boolean existsProductOrder(ProductOrder productOrder) {
        return productOrderRepository.existsByProductOrderId(productOrder.getProductOrderId());
    }

    public boolean existsOrder(Order order) {
        return productOrderRepository.existsById(order.getOrderId());
    }


    public ProductOrder convertToProductOrderEntity(ProductOrderResponse productOrderResponse) {
        ProductOrder productOrder = new ProductOrder();

        mapOrderInfo(productOrder, productOrderResponse);
        mapProductOrderInfo(productOrder, productOrderResponse);
        mapAddressInfo(productOrder, productOrderResponse);
        mapCommissionInfo(productOrder, productOrderResponse);
        mapClaimInfo(productOrder, productOrderResponse);
        mapReturnInfo(productOrder, productOrderResponse);

        return productOrder;
    }

    private void mapReturnInfo(ProductOrder productOrder, ProductOrderResponse productOrderResponse) {
        productOrder.setReturnClaim(productOrderResponse.getReturnClaim());
    }

    private void mapOrderInfo(ProductOrder productOrder, ProductOrderResponse productOrderResponse) {
        productOrder.setOrder(productOrderResponse.getOrder());
    }

    private void mapProductOrderInfo(ProductOrder productOrder, ProductOrderResponse productOrderResponse) {
        productOrder.setProductOrderId(productOrderResponse.getProductOrder().getProductOrderId());
        productOrder.setQuantity(productOrderResponse.getProductOrder().getQuantity());
        productOrder.setInitialQuantity(productOrderResponse.getProductOrder().getInitialQuantity());
        productOrder.setRemainQuantity(productOrderResponse.getProductOrder().getRemainQuantity());
        productOrder.setTotalProductAmount(productOrderResponse.getProductOrder().getTotalProductAmount());
        productOrder.setInitialProductAmount(productOrderResponse.getProductOrder().getInitialProductAmount());
        productOrder.setRemainProductAmount(productOrderResponse.getProductOrder().getRemainProductAmount());
        productOrder.setTotalPaymentAmount(productOrderResponse.getProductOrder().getTotalPaymentAmount());
        productOrder.setInitialPaymentAmount(productOrderResponse.getProductOrder().getInitialPaymentAmount());
        productOrder.setRemainPaymentAmount(productOrderResponse.getProductOrder().getRemainPaymentAmount());
        productOrder.setProductOrderStatus(productOrderResponse.getProductOrder().getProductOrderStatus());
        productOrder.setProductId(productOrderResponse.getProductOrder().getProductId());
        productOrder.setProductName(productOrderResponse.getProductOrder().getProductName());
        productOrder.setUnitPrice(productOrderResponse.getProductOrder().getUnitPrice());
        productOrder.setProductClass(productOrderResponse.getProductOrder().getProductClass());
        productOrder.setOriginalProductId(productOrderResponse.getProductOrder().getOriginalProductId());
        productOrder.setMerchantChannelId(productOrderResponse.getProductOrder().getMerchantChannelId());
        productOrder.setExpectedDeliveryCompany(productOrderResponse.getProductOrder().getExpectedDeliveryCompany());
        productOrder.setDeliveryAttributeType(productOrderResponse.getProductOrder().getDeliveryAttributeType());
        productOrder.setPlaceOrderDate(productOrderResponse.getProductOrder().getPlaceOrderDate());
        productOrder.setPlaceOrderStatus(productOrderResponse.getProductOrder().getPlaceOrderStatus());
        productOrder.setShippingDueDate(productOrderResponse.getProductOrder().getShippingDueDate());
        productOrder.setExpectedDeliveryMethod(productOrderResponse.getProductOrder().getExpectedDeliveryMethod());
        productOrder.setPackageNumber(productOrderResponse.getProductOrder().getPackageNumber());
        productOrder.setItemNo(productOrderResponse.getProductOrder().getItemNo());
        productOrder.setOptionCode(productOrderResponse.getProductOrder().getOptionCode());
        productOrder.setOptionPrice(productOrderResponse.getProductOrder().getOptionPrice());
        productOrder.setMallId(productOrderResponse.getProductOrder().getMallId());
        productOrder.setInflowPath(productOrderResponse.getProductOrder().getInflowPath());
        productOrder.setProductDiscountAmount(productOrderResponse.getProductOrder().getProductDiscountAmount());
        productOrder.setInitialProductDiscountAmount(productOrderResponse.getProductOrder().getInitialProductDiscountAmount());
        productOrder.setRemainProductDiscountAmount(productOrderResponse.getProductOrder().getRemainProductDiscountAmount());
        productOrder.setSellerBurdenDiscountAmount(productOrderResponse.getProductOrder().getSellerBurdenDiscountAmount());
        productOrder.setDeliveryFeeAmount(productOrderResponse.getProductOrder().getDeliveryFeeAmount());
        productOrder.setDeliveryPolicyType(productOrderResponse.getProductOrder().getDeliveryPolicyType());
        productOrder.setSectionDeliveryFee(productOrderResponse.getProductOrder().getSectionDeliveryFee());
        productOrder.setShippingFeeType(productOrderResponse.getProductOrder().getShippingFeeType());
        productOrder.setDeliveryDiscountAmount(productOrderResponse.getProductOrder().getDeliveryDiscountAmount());
    }
    private void mapCommissionInfo(ProductOrder productOrder, ProductOrderResponse productOrderResponse) {
        productOrder.setClaimId(productOrderResponse.getProductOrder().getClaimId());
        productOrder.setClaimStatus(productOrderResponse.getProductOrder().getClaimStatus());
        productOrder.setClaimType(productOrderResponse.getProductOrder().getClaimType());
        productOrder.setCommissionRatingType(productOrderResponse.getProductOrder().getCommissionRatingType());
        productOrder.setCommissionPrePayStatus(productOrderResponse.getProductOrder().getCommissionPrePayStatus());
        productOrder.setPaymentCommission(productOrderResponse.getProductOrder().getPaymentCommission());
        productOrder.setSaleCommission(productOrderResponse.getProductOrder().getSaleCommission());
        productOrder.setKnowledgeShoppingSellingInterlockCommission(productOrderResponse.getProductOrder().getKnowledgeShoppingSellingInterlockCommission());
        productOrder.setChannelCommission(productOrderResponse.getProductOrder().getChannelCommission());
        productOrder.setExpectedSettlementAmount(productOrderResponse.getProductOrder().getExpectedSettlementAmount());
    }
    private void mapAddressInfo(ProductOrder productOrder, ProductOrderResponse productOrderResponse) {
        productOrder.setShippingAddress(productOrderResponse.getProductOrder().getShippingAddress());
        productOrder.setTakingAddress(productOrderResponse.getProductOrder().getTakingAddress());
    }

    private void mapClaimInfo(ProductOrder productOrder, ProductOrderResponse productOrderResponse) {
        productOrder.setReturnClaim(productOrderResponse.getProductOrder().getReturnClaim());

        // 기존 CompletedClaims 초기화
        List<CompletedClaim> completedClaims = new ArrayList<>();

        if (productOrderResponse.getCompletedClaims() != null) {
            for (CompletedClaim claim : productOrderResponse.getCompletedClaims()) {
                claim.setProductOrder(productOrder); // ★★ CompletedClaim 쪽에 ProductOrder 연결해줘야 해
                completedClaims.add(claim);
            }
        }

        productOrder.setCompletedClaims(completedClaims); // ★★ ProductOrder 쪽에 CompletedClaims 연결
    }

}
