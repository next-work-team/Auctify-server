package org.example.auctify.controller.presigned;

import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.s3.DeleteImageRequestDTO;
import org.example.auctify.dto.s3.ImageRequestDTO;
import org.example.auctify.dto.s3.PresignedUrlResponseDTO;
import org.example.auctify.service.s3.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PresignedController implements  PresignedControllerDocs{

    private final FileUploadService fileUploadService;

    /**
     * S3에게 pre-signed URL (권한) 요청하는 엔드포인트
     * 프론트에서 이 URL을 받아서 AWS S3에 직접 업로드함.
     *
     * @param imageDTO 파일 이름 정보를 담은 DTO
     * @return AWS S3에 업로드할 수 있는 Presigned URL
     */
    /**
     *
     * {
     *     "presignedUrl": "https://버킷명.s3.ap-northeast-2.amazonaws.com
     *     /contact/1bd2fca5-ee98-48a1-93f9-df66b9635e93matest.png?
     *     Content-Length=20971520&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250418T065100Z&X-Amz-SignedHeaders=host
     *     &X-Amz-Expires=119&X-Amz-Credential=AKIAYQYUAUFVNFA6XN6A%2F20250418%2Fap-
     *     northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=2bc86bee7ed8fd78bb234a3c69b1f050ca15b022d7b5778b0f0f08a0470dafcc",
     *
     *     "s3Key": "contact/1bd2fca5-ee98-48a1-93f9-df66b9635e93matest.png"  //이걸로 나중에 삭제도 한다. 여기다 주소 붙여서 이미지 가져옴
     *     // 실제 이미지 요청시 https://버킷명.s3.ap-northeast-2.amazonaws.com/contact/1bd2fca5-ee98-48a1-93f9-df66b9635e93matest.png 이미지(S3key)
     * }
     */
    @PostMapping("/presigned")
    public ResponseEntity<PresignedUrlResponseDTO> createPresignedUrl(@RequestBody ImageRequestDTO imageDTO) {
        String path = "contact";  // S3 내 저장될 폴더 경로
        String s3Key = path + "/" + imageDTO.getImageName();  // 업로드될 S3 Key 생성

        PresignedUrlResponseDTO presignedUrl = fileUploadService.getPreSignedUrl(path,imageDTO.getImageName());

        return ResponseEntity.ok(presignedUrl);  // ✅ Presigned URL과 S3 Key 반환
    }


    /**
     * S3에 업로드된 파일 삭제
     * @param dto { "s3Key": "contact/uuid파일명.jpg" }
     */
    // https://버킷명.s3.ap-northeast-2.amazonaws.com/ 주소
    // contact/1bd2fca5-ee98-48a1-93f9-df66b9635e93matest.png 이미지(S3key)
    //
    @DeleteMapping("/presigned")
    public ResponseEntity<Void> deletePresigned(@RequestBody DeleteImageRequestDTO dto) {
        fileUploadService.deleteFile(dto.getS3Key());
        return ResponseEntity.noContent().build();
    }

}