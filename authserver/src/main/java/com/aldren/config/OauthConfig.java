package com.aldren.config;

import com.aldren.properties.LdapProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(LdapProperties.class)
public class OauthConfig {

    @Autowired
    private LdapProperties props;

    @Autowired
    private DataSource dataSource;

    @Bean
    private JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        /**
         * TODO
         *
         * To generate a self-signed SSL cert.
         * Extract the public key, and use as the signature for the jwt.
         *
         * Resource resource = new ByteArrayResource(Base64.getDecoder().decode(keystore.getBase64()));
         * KeyPair keyPair = new KeyStoreKeyFactory(resource,keystore.getSecret().toCharArray()).getKeyPair(keyStore.getAlias());
         * converter.setKeyPair(keyPair);
         */
        return converter;
    }

    @Bean
    private TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
