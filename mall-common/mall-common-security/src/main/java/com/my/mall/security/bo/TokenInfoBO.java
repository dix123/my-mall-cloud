package com.my.mall.security.bo;

/**
 * @Author: haole
 * @Date: 2025/4/29
 **/
public class TokenInfoBO {
    private UserInfoInTokenBO userInfoInToken;
    private String accessToken;
    private String refreshToken;

    private Integer expireTime;

    public UserInfoInTokenBO getUserInfoInToken() {
        return userInfoInToken;
    }

    public void setUserInfoInToken(UserInfoInTokenBO userInfoInToken) {
        this.userInfoInToken = userInfoInToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "TokenInfoBO{" +
                "userInfoInToken=" + userInfoInToken +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}
