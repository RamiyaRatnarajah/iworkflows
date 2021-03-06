package com.kajan.iworkflows.service;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;

import java.security.Principal;

public interface OauthTokenService {
    void setToken(Principal principal, TokenDTO tokenDTO);

    TokenDTO getToken(Principal principal, TokenProvider tokenProvider);

    Boolean revokeToken(Principal principal, Constants.TokenProvider tokenProvider);

    Boolean isAlreadyAuthorized(Principal principal, TokenProvider provider);
}
