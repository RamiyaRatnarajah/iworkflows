package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.repository.Oauth2TokenRepository;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.util.Constants.OauthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class OauthTokenServiceImpl implements OauthTokenService {

    @Autowired
    Oauth2TokenRepository oauth2TokenRepository;

    public OauthTokenServiceImpl() {
    }

    @Override
    public void setOauth2Tokens(Principal principal, Oauth2TokenDTO oauth2TokenDTO) {
        oauth2TokenRepository.setOauth2Token(principal, oauth2TokenDTO);
    }

    @Override
    public Oauth2TokenDTO getOauth2Tokens(Principal principal, OauthProvider oauthProvider) {
        return oauth2TokenRepository.getOauth2Token(principal, oauthProvider);
    }

    @Override
    public Boolean revokeOauth2Token(Principal principal, OauthProvider oauthProvider) {
        return oauth2TokenRepository.revokeOauth2Token(principal, oauthProvider);
    }

    @Override
    public Boolean alreadyAuthorized(Principal principal, OauthProvider provider) {
        return oauth2TokenRepository.alreadyAuthorized(principal, provider);
    }
}
