package com.aldren.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ldap")
public class LdapProperties {

    private String url;

    private String dn;

    private String password;

    private String base;

}
