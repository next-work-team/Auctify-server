package org.example.auctify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private int status;      // HTTP 상태 코드
    private String message;  // 메시지
    private T data;          // 실제 응답 데이터 (제네릭)

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(200, "Success", data);
    }

    public static <T> ApiResponseDTO<T> error(int status, String message) {
        return new ApiResponseDTO<>(status, message, null);
    }
}
