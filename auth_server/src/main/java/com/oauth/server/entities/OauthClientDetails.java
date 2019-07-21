package com.oauth.server.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class OauthClientDetails {

    @Id
    @NonNull
    private String clientId;
    private String resourceIds;
    @NotNull
    @NonNull
    private String clientSecret;
    @NonNull
    private String scope;
    @NonNull
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    @NonNull
    private String authorities;
    @NonNull
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoapprove;
}
