package org.example.auctify.dto.tossPayments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TossPaymentsResponseDTO {

    private String country;
    private Map<String, Object> metadata;
    private String orderId;
    private Object cashReceipts;
    private boolean isPartialCancelable;
    private String lastTransactionKey;
    private Object discount;
    private int taxExemptionAmount;
    private int suppliedAmount;
    private String secret;
    private String type;
    private boolean cultureExpense;
    private int taxFreeAmount;

    private String currency;
    private String paymentKey;

    private Checkout checkout;
    private String orderName;
    private String method;
    private boolean useEscrow;
    private int vat;
    private String mId;

    private int balanceAmount;
    private String version;

    private EasyPay easyPay;
    private int totalAmount;
    private Object cancels;
    private Object transfer;
    private Object mobilePhone;
    private Object failure;

    private Receipt receipt;
    private Object giftCertificate;
    private Object cashReceipt;

    private Card card;
    private String status;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Checkout {
        private String url;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EasyPay {
        private int amount;
        private String provider;
        private int discountAmount;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Receipt {
        private String url;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Card {
        private String ownerType;
        private String number;
        private int amount;
        private String acquireStatus;
        private boolean isInterestFree;
        private String cardType;
        private String approveNo;
        private int installmentPlanMonths;
        private Object interestPayer;
        private String issuerCode;
        private String acquirerCode;
        private boolean useCardPoint;
    }
}
