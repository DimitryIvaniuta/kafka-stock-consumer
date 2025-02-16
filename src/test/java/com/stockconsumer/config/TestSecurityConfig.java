package com.stockconsumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

/**
 * Security configuration for unit and integration tests.
 * <p>
 * This configuration provides a mock {@link AuthenticationManager}
 * to simulate authenticated users in test environments without
 * requiring real authentication flows.
 */
@Configuration
public class TestSecurityConfig {

    /**
     * Provides a mock {@link AuthenticationManager} for testing purposes.
     * <p>
     * This manager accepts all authentication requests and returns a
     * pre-authenticated token with the role "ROLE_USER".
     *
     * @return a mock {@link AuthenticationManager} for test contexts.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(new TestAuthenticationProvider()));
    }

    /**
     * Custom AuthenticationProvider for tests.
     * <p>
     * This provider accepts all authentications and returns a
     * {@link TestingAuthenticationToken} with a user role.
     */
    static class TestAuthenticationProvider implements AuthenticationProvider {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return new TestingAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return TestingAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }
}
