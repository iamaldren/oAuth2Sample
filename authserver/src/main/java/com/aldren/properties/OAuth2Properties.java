package com.aldren.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "oauth2.keystore")
public class OAuth2Properties {

    private String file;
    private String secret;
    private String alias;

}
