package com.naver.commerce.dto;

import java.time.OffsetDateTime;

public class LastChangeStatus {

        private String orderId;
        private String productOrderId;
        private String lastChangedType;
        private OffsetDateTime paymentDate;
        private OffsetDateTime lastChangedDate;
        private String productOrderStatus;
        private String claimType;
        private String claimStatus;
        private boolean receiverAddressChanged;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getProductOrderId() {
            return productOrderId;
        }

        public void setProductOrderId(String productOrderId) {
            this.productOrderId = productOrderId;
        }

        public String getLastChangedType() {
            return lastChangedType;
        }

        public void setLastChangedType(String lastChangedType) {
            this.lastChangedType = lastChangedType;
        }

        public OffsetDateTime getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(OffsetDateTime paymentDate) {
            this.paymentDate = paymentDate;
        }

        public OffsetDateTime getLastChangedDate() {
            return lastChangedDate;
        }

        public void setLastChangedDate(OffsetDateTime lastChangedDate) {
            this.lastChangedDate = lastChangedDate;
        }

        public String getProductOrderStatus() {
            return productOrderStatus;
        }

        public void setProductOrderStatus(String productOrderStatus) {
            this.productOrderStatus = productOrderStatus;
        }

        public String getClaimType() {
            return claimType;
        }

        public void setClaimType(String claimType) {
            this.claimType = claimType;
        }

        public String getClaimStatus() {
            return claimStatus;
        }

        public void setClaimStatus(String claimStatus) {
            this.claimStatus = claimStatus;
        }

        public boolean isReceiverAddressChanged() {
            return receiverAddressChanged;
        }

        public void setReceiverAddressChanged(boolean receiverAddressChanged) {
            this.receiverAddressChanged = receiverAddressChanged;
        }
    }