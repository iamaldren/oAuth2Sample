package com.aldren.config;

import com.aldren.properties.LdapProperties;
import com.aldren.properties.OAuth2Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
public class OauthConfig {

    @Autowired
    private OAuth2Properties props;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        JwtAccessTokenConverter converter = new CustomTokenEnhancer();

        InputStream truststoreInput = Thread.currentThread().getContextClassLoader().getResourceAsStream(props.getFile());

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(truststoreInput, props.getSecret().toCharArray());

        KeyPair keyPair = null;
        Key key = trustStore.getKey(props.getAlias(), props.getSecret().toCharArray());
        if(key instanceof PrivateKey) {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) trustStore.getCertificate(props.getAlias());
            PublicKey publicKey = cert.getPublicKey();
            keyPair = new KeyPair(publicKey, (PrivateKey) key);
        }

        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
