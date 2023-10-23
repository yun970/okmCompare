package com.example.okmprice.OAuth;

import java.util.Map;

public class OAuthAttributes {
    private Map<String, Object> attr;
    private String nameAttributeKey;
    private String name;
    private String email;

    public static OAuthAttributes of(String registrationId, Map<String ,Object> attr){
        if("kakao".equals(registrationId)){
            return ofKakao("id", attr);
        }

        return null;
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attr){
        Map<String ,Object> kakaoAccount = (Map<String, Object>) attr.get("kakao_account");
        Map<String ,Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder().withUsername(String.valueOf(kakaoProfile.get("nickname")))
                .withEmail(String.valueOf(kakaoAccount.get("email")))
                .withAttributes(attr)
                .withNameAttributeKey(userNameAttributeName)
                .build();
    }

    private static class OAuthAttributesBuilder{
        private OAuthAttributes oAuthAttributes;

        public OAuthAttributesBuilder(){
            oAuthAttributes = new OAuthAttributes();
        }

        public OAuthAttributesBuilder withUsername(String username){
            oAuthAttributes.name = username;
            return this;
        }

        public OAuthAttributesBuilder withEmail(String email){
            oAuthAttributes.email = email;
            return this;
        }

        public OAuthAttributesBuilder withAttributes(Map<String, Object> attr){
            oAuthAttributes.attr = attr;
            return this;
        }

        public OAuthAttributesBuilder withNameAttributeKey(String nameAttributeKey){
            oAuthAttributes.nameAttributeKey = nameAttributeKey;
            return this;
        }

        public OAuthAttributes build(){
            return oAuthAttributes;
        }


    }

    private static OAuthAttributesBuilder builder(){
        return new OAuthAttributesBuilder();
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public String getNameAttributeKey() {
        return nameAttributeKey;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
