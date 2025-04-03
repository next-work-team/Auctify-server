package org.example.auctify.controller.tossPayments;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.tossPayments.PayRequestDTO;
import org.example.auctify.dto.tossPayments.PaymentResponseDTO;
import org.example.auctify.dto.tossPayments.TossPaymentsResponseDTO;
import org.example.auctify.service.payment.PaymentService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
@Log4j2
public class PaymentController {

    private static final String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    //private static final String API_SECRET_KEY = "test_sk_DnyRpQWGrNDxm0Gogva23Kwv1M9E";

    private final PaymentService paymentService;

    @PostMapping(value = "/confirm")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> confirmPayment(@RequestBody PayRequestDTO payRequestDTO) throws Exception {
        try {
            System.out.println(">>> [confirmPayment] 수신된 JSON: " + payRequestDTO.toString());

            JSONObject obj = new JSONObject();
            obj.put("orderId", payRequestDTO.getOrderId());
            obj.put("amount", payRequestDTO.getAmount());
            obj.put("paymentKey", payRequestDTO.getPaymentKey());
            System.out.println(">>> [confirmPayment] 토스 결제 인증 요청 시작");
            JSONObject response = sendRequest(obj, WIDGET_SECRET_KEY, "https://api.tosspayments.com/v1/payments/confirm");
            System.out.println(">>> [confirmPayment] 토스 응답: " + response.toJSONString());

            ObjectMapper objectMapper = new ObjectMapper();
            TossPaymentsResponseDTO paymentResponse = objectMapper.readValue(response.toJSONString(), TossPaymentsResponseDTO.class);
            // 이제 paymentResponse 객체에서 필요한 필드들을 꺼내서 쓸 수 있음
            System.out.println(">>> [confirmPayment] 매핑된 객체: " + paymentResponse.getOrderName());

            // TODO: 여기에 PaymentService 호출해서 DB 저장 처리

            // >>> [confirmPayment] 응답:
            // {"country":"KR","metadata":null,"orderId":"MC4xODI2OTAzODk3MjI3","cashReceipts":null,"isPartialCancelable":true,"lastTransactionKey":"txrd_a01jqp8b9787118c4ndwsz5b6xh",
            // "discount":null,"taxExemptionAmount":0,"suppliedAmount":45455,"secret":"ps_ex6BJGQOVDKq25JGeY6q3W4w2zNb","type":"NORMAL","cultureExpense":false,"taxFreeAmount":0,
            // "requestedAt":"2025-03-31T23:05:19+09:00","currency":"KRW","paymentKey":"tgen_202503312305194Ic66","virtualAccount":null,
            // "checkout":{"url":"https:\/\/api.tosspayments.com\/v1\/payments\/tgen_202503312305194Ic66\/checkout"},
            // "orderName":"토스 티셔츠 외 2건","method":"간편결제","useEscrow":false,"vat":4545,"mId":"tgen_docs","approvedAt":"2025-03-31T23:05:36+09:00",
            // "balanceAmount":50000,"version":"2022-11-16","easyPay":{"amount":0,"provider":"토스페이",
            // "discountAmount":0},"totalAmount":50000,"cancels":null,"transfer":null,"mobilePhone":null,"failure":null,
            // "receipt":{"url":"https:\/\/dashboard.tosspayments.com\/receipt\/redirection?transactionId=tgen_202503312305194Ic66&ref=PX"},
            // "giftCertificate":null,"cashReceipt":null,"card":{"ownerType":"개인","number":"53275073****768*","amount":50000,"acquireStatus":"READY",
            // "isInterestFree":false,"cardType":"신용",
            // "approveNo":"00000000","installmentPlanMonths":0,"interestPayer":null,"issuerCode":"24","acquirerCode":"21","useCardPoint":false},"status":"DONE"}

            payRequestDTO.setType(paymentResponse.getMethod());
            PaymentResponseDTO paymentResponseDTO = paymentService.saveConfirmedPayment(payRequestDTO);


            return ResponseEntity.ok(ApiResponseDTO.success(paymentResponseDTO));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Create Payment"));
        }
    }






    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            System.out.println(">>> [sendRequest] 응답 읽기 실패: " + e.getMessage());
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Error reading response");
            return errorResponse;
        }
    }

    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
        System.out.println(">>> [createConnection] 요청 URL: " + urlString);
        System.out.println(">>> [createConnection] 사용된 시크릿 키: " + secretKey);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }
}

