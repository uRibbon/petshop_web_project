package com.dog.shop.dto.social;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponse {
    private long id;
    private String connected_at;
    private Properties properties;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    // getters, setters ...
    @Data
    public static class Properties {
        private String nickname;
        // getters, setters ...
    }

    @Data
    public static class KakaoAccount {
        private boolean profile_nickname_needs_agreement;
        private Profile profile;
        private boolean has_email;
        private boolean email_needs_agreement;
        private boolean is_email_valid;
        private boolean is_email_verified;
        private String email;
        private boolean has_birthday;
        private boolean birthday_needs_agreement;

        // getters, setters ...
        @Data
        public static class Profile {
            private String nickname;
            // getters, setters ...
        }
    }
}