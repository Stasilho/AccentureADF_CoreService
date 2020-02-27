package com.accenture.com.snoskov.adf.universityx.security.util;

import com.accenture.com.snoskov.adf.universityx.users.model.Role;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class JwtTokenUtilTest {

    private static JwtTokenUtil jwtTokenUtil;
    private static final String secret = "d23c4c4b65b648a486f7cb03a1d72895";
    private static final String jwtTokenPart = "eyJhbGciOiJIUzI1NiJ9";
    private static final String jwtTokenFull = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbl91c2VyIiwicm9sZSI6IkFETUlOIiwiZXhwIjo1OTk3MTA0MzczLCJpYXQiOjE1ODIwNjQzNzN9.qDEdYizlPs4fp9yi-NYa-Yska_cyqXkSTnrxNafgyg0";

    @BeforeClass
    public static void initialize() {
        Environment env = Mockito.mock(Environment.class);
        when(env.getProperty(eq("universityx.api.auth.secret"))).thenReturn(secret);
        when(env.getProperty(eq("universityx.api.auth.expiration.days"))).thenReturn(Long.valueOf(7 * 20 * 365).toString());
        jwtTokenUtil = new JwtTokenUtil(env);
    }

    @Test
    public void generateToken() {
        String token = jwtTokenUtil.generateToken("admin_user", Role.ADMIN.getName());
        assertTrue(token.startsWith(jwtTokenPart));
    }

    @Test
    public void extractUsername() {
        String username = jwtTokenUtil.extractUsername(jwtTokenFull);
        assertEquals("admin_user", username);
    }

    @Test
    public void getAuthentication() {
        Authentication authentication = jwtTokenUtil.getAuthentication(jwtTokenFull);

        assertEquals("admin_user", authentication.getPrincipal().toString());
        assertEquals(Role.ADMIN.getName(), authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse(""));
    }
}