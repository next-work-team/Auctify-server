package org.example.auctify.dto.social;

public interface OAuth2Response {

    //제공자(Ex. kakao, google,...)
    String getProvider();

    // 제공가에서 발급해주는 아이디(번호)
    String getProviderId();

    // 이메일
    String getEmail();

    //사용자 실명(설정한 이름)
    String getName();

}
