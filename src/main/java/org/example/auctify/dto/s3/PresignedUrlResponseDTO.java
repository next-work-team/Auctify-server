package org.example.auctify.dto.s3;

public class PresignedUrlResponseDTO {
    private final String presignedUrl;
    private final String s3Key;

    public PresignedUrlResponseDTO(String presignedUrl, String s3Key) {
        this.presignedUrl = presignedUrl;
        this.s3Key = s3Key;
    }

    public String getPresignedUrl() {
        return presignedUrl;
    }

    public String getS3Key() {
        return s3Key;
    }
}