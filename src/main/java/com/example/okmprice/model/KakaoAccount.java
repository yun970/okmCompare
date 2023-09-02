package com.example.okmprice.model;

public class KakaoAccount {
    Boolean profile_nickname_needs_agreement;
    Profile profile;
    Boolean has_email;
    Boolean email_needs_agreement;
    Boolean is_email_valid;
    Boolean is_email_verified;
    String email;

    public Boolean getProfile_nickname_needs_agreement() {
        return profile_nickname_needs_agreement;
    }

    public void setProfile_nickname_needs_agreement(Boolean profile_nickname_needs_agreement) {
        this.profile_nickname_needs_agreement = profile_nickname_needs_agreement;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Boolean getHas_email() {
        return has_email;
    }

    public void setHas_email(Boolean has_email) {
        this.has_email = has_email;
    }

    public Boolean getEmail_needs_agreement() {
        return email_needs_agreement;
    }

    public void setEmail_needs_agreement(Boolean email_needs_agreement) {
        this.email_needs_agreement = email_needs_agreement;
    }

    public Boolean getIs_email_valid() {
        return is_email_valid;
    }

    public void setIs_email_valid(Boolean is_email_valid) {
        this.is_email_valid = is_email_valid;
    }

    public Boolean getIs_email_verified() {
        return is_email_verified;
    }

    public void setIs_email_verified(Boolean is_email_verified) {
        this.is_email_verified = is_email_verified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
