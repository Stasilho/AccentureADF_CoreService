package com.stanislavnoskov.adf.universityx.security;

class SecurityConstants {

    private SecurityConstants() {}

    static final String HEADER_AUTHORIZATION = "Authorization";
    static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    static final String SIGN_UP_URL = "/apis/users/sign-up";
    static final String AUTH_URL = "/apis/users/authenticate";
}
