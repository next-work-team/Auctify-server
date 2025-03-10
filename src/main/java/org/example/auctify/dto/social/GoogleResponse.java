package org.example.auctify.dto.social;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }


    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        Object emailObj = attribute.get("email");
        return emailObj != null ? emailObj.toString() : null;
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

}
