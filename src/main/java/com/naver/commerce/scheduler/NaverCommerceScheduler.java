package com.naver.commerce.scheduler;

import com.naver.commerce.dto.LastChangeStatus;
import com.naver.commerce.dto.LastChangeStatusesResponse;
import com.naver.commerce.dto.NaverProductOrderDto;
import com.naver.commerce.dto.ProductOrderResponse;
import com.naver.commerce.entity.ProductOrder;
import com.naver.commerce.service.NaverCommerceService;
import com.naver.commerce.service.ProductOrderService;
import com.naver.commerce.util.DateTimeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverCommerceScheduler {

    private final NaverCommerceService naverCommerceService;
    private final ProductOrderService productOrderService;

    // 1분마다 실행 (fixedRate = 60000ms)
    @Scheduled(fixedRate = 60000)
    public void fetchLastChangedProductOrders() {
        log.info("스케줄러 시작: 변경된 상품 주문 조회");

//        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
//        OffsetDateTime oneMinuteAgo = now.minusMinutes(1);

        // Test
        OffsetDateTime now = LocalDateTime.of(2025, 4, 23, 11, 57)
                .atZone(ZoneId.of("Asia/Seoul"))
                .withZoneSameInstant(ZoneOffset.UTC)
                .toOffsetDateTime();
        OffsetDateTime oneMinuteAgo = now.minusMinutes(1);

        naverCommerceService.getLastChangedProductOrders(
                        oneMinuteAgo,
                        null,
                        "CLAIM_REQUESTED",   // 처리가 완료된 상태
                        // TODO : 추후 300개가 넘을 경우의 로직 구현할 것.
                        null,   // moreSequence - 초기엔 null
                        300     // limitCount - 원하는 만큼 설정
                )
                .doOnNext(lastChanges -> {
                    try {
                        handleLastChanges(lastChanges, oneMinuteAgo, now);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnError(error -> log.error("변경 상품 주문 조회 실패", error))
                .subscribe();
    }

    private void handleLastChanges(JsonNode lastChanges, OffsetDateTime from, OffsetDateTime to) throws JsonProcessingException {
        String formattedFrom = DateTimeUtil.formatToMinuteKST(from);
        String toMinute = String.valueOf(to.getMinute());


        JsonNode dataNode = lastChanges.path("data");
        // data 필드가 없는 경우 그냐 return
        if (dataNode.isMissingNode() || dataNode.isNull()) {
            return;
        }

        String data = dataNode.toString();


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // (선택) ISO 형식으로 읽고/쓰게 함
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // JsonNode를 LastChangeStatusesResponse 클래스로 변환
        LastChangeStatusesResponse response = mapper.readValue(data, LastChangeStatusesResponse.class);

        // claimStatus가 RETURN_DONE인 상품 아이디 리스트
        List<String> productOrderIds = response.getLastChangeStatuses().stream()
                .filter(LastChangeStatus -> "RETURN_REQUEST".equals(LastChangeStatus.getClaimStatus()))
                .map(LastChangeStatus::getProductOrderId)
                .toList();

        log.info("\n== INFO ==> {} ~ {}분 조회된 변경 상품 아이디 리스트 \n{}", formattedFrom, toMinute, productOrderIds);

        // 상세 내역 조회를 위한 dto 생성
        NaverProductOrderDto dto = new NaverProductOrderDto();
        dto.setProductOrderIds(productOrderIds);

        // 상품 주문 상세 내역 조회
        naverCommerceService.getProductOrder(dto)
                .doOnNext(productOrder -> {
                    JsonNode dataArray = productOrder.get("data");
                    dataArray.forEach(jsonNode -> {
                        ProductOrderResponse productOrderResponse = null;
                        try {
                            productOrderResponse = mapper.readValue(jsonNode.toString(), ProductOrderResponse.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        // 상품 db에 저장
                        ProductOrder productOrderEntity = productOrderService.convertToProductOrderEntity(productOrderResponse);
                        if (!productOrderService.existsProductOrder(productOrderEntity) && !productOrderService.existsOrder(productOrderEntity.getOrder())) {
                            productOrderService.saveProductOrder(productOrderEntity); // DB에 저장
                        }

                    });
                })
                .doOnError(error -> log.error("상품 주문 상세 내역 조회 실패", error))
                .subscribe();

    }

}
