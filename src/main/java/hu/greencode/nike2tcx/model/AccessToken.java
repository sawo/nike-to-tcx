package hu.greencode.nike2tcx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("profile_img_url")
    private String profileImgUrl;

    @JsonProperty("profile_type")
    private String profileType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccessToken{");
        sb.append("accessToken='").append(accessToken).append('\'');
        sb.append(", expiresIn='").append(expiresIn).append('\'');
        sb.append(", profileImgUrl='").append(profileImgUrl).append('\'');
        sb.append(", profileType='").append(profileType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
