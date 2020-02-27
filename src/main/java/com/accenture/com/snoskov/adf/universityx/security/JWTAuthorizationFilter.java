package com.accenture.com.snoskov.adf.universityx.security;

import com.accenture.com.snoskov.adf.universityx.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.accenture.com.snoskov.adf.universityx.security.SecurityConstants.AUTHORIZATION_TOKEN_PREFIX;
import static com.accenture.com.snoskov.adf.universityx.security.SecurityConstants.HEADER_AUTHORIZATION;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    private JwtTokenUtil jwtTokenUtil;

    JWTAuthorizationFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_AUTHORIZATION);

        if (header != null && header.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

            if (authentication != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        if (token != null) {
            try {
                return jwtTokenUtil.getAuthentication(token.replace(AUTHORIZATION_TOKEN_PREFIX, ""));
            } catch (Exception e) {
                LOGGER.warn("Authorization failed.", e);
            }
        }
        return null;
    }
}
