package org.example.auctify.controller.presigned;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.auctify.dto.s3.DeleteImageRequestDTO;
import org.example.auctify.dto.s3.ImageRequestDTO;
import org.example.auctify.dto.s3.PresignedUrlResponseDTO;
import org.springframework.http.ResponseEntity;

/**
 * PresignedController API 문서 인터페이스 (Swagger/OpenAPI 어노테이션 포함)
 */
@Tag(name = "Presigned API", description = "S3 Presigned URL 생성 및 파일 삭제 기능")
public interface PresignedControllerDocs {

    /**
     * S3 Presigned URL 생성
     *
     * @param imageDTO 업로드할 파일 이름 정보를 담은 DTO
     * @return Presigned URL과 S3 Key 반환
     */
    @Operation(
            summary = "Presigned URL 생성",
            description = "클라이언트가 S3에 직접 업로드할 수 있도록 Presigned URL을 발급합니다. presignedUrl와 s3Key를 전달 " +
                    "presignedUrl 로 put요청으로 이미지 등록 요청  버킷 주소 + s3Key = 이미지 주소 "

    )
    @ApiResponse(responseCode = "200", description = "Presigned URL 발급 성공")
    ResponseEntity<PresignedUrlResponseDTO> createPresignedUrl(ImageRequestDTO imageDTO);

    /**
     * S3에 업로드된 파일 삭제
     *
     * @param dto 삭제할 S3 Key 정보를 담은 DTO
     */
    @Operation(
            summary = "파일 삭제",
            description = "S3에 저장된 파일을 주어진 S3 Key로 삭제합니다."
    )
    @ApiResponse(responseCode = "204", description = "파일 삭제 성공")
    ResponseEntity<Void> deletePresigned(DeleteImageRequestDTO dto);
}
