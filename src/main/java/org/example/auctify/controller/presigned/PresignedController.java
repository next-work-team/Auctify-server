package org.example.auctify.controller.presigned;

import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.s3.ImageRequestDTO;
import org.example.auctify.dto.s3.PresignedUrlResponseDTO;
import org.example.auctify.service.s3.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PresignedController {

    private final FileUploadService fileUploadService;

    /**
     * S3에게 pre-signed URL (권한) 요청하는 엔드포인트
     * 프론트에서 이 URL을 받아서 AWS S3에 직접 업로드함.
     *
     * @param imageDTO 파일 이름 정보를 담은 DTO
     * @return AWS S3에 업로드할 수 있는 Presigned URL
     */
    @PostMapping("/presigned")
    public ResponseEntity<PresignedUrlResponseDTO> createPresignedUrl(@RequestBody ImageRequestDTO imageDTO) {
        String path = "contact";  // S3 내 저장될 폴더 경로
        String s3Key = path + "/" + imageDTO.getImageName();  // 업로드될 S3 Key 생성

        PresignedUrlResponseDTO presignedUrl = fileUploadService.getPreSignedUrl(path,imageDTO.getImageName());

        return ResponseEntity.ok(presignedUrl);  // ✅ Presigned URL과 S3 Key 반환
    }



}