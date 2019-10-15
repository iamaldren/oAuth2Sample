package com.aldren.config;

import com.aldren.ldap.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

@Configuration
public class TestConfig {

    @Autowired
    private LdapService svc;

    @Bean
    public String loadDirContext() throws NamingException {
        DirContext ctx = svc.ldapContext("riemann","password");

        return "";
    }

}
