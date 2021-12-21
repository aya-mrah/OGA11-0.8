package com.oga.userservice.dto;

import com.oga.userservice.Entity.UserEntity;


public class UserTokenDto {

    private String token;
    private UserEntity userEntity;

    public UserTokenDto(String token, UserEntity userEntity) {
        this.token = token;
        this.userEntity = userEntity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
