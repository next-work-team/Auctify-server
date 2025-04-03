package org.example.auctify.controller.tossPayments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.tossPayments.PayRequestDTO;
import org.example.auctify.dto.tossPayments.PaymentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "결제", description = "결제 API")
public interface PaymentInterface {

    // 결제 정보를 전달받아서 결제 처리
    @Operation(summary = "결제 정보를 가지고 결제 처리", description = "경매 물품 정보 페이지 입니다.")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> confirmPayment(@RequestBody PayRequestDTO payRequestDTO);
}
