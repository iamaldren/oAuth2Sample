package com.aldren.config;

import com.aldren.ldap.LdapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
public class TestConfig {

    @Autowired
    private LdapService svc;

    @Bean
    public String loadDirContext() throws NamingException {
        DirContext ctx = svc.ldapContext("cn=read-only-admin,dc=example,dc=com","password");

        log.info("Context::" + ctx);

        Attributes attrs = ctx.getAttributes("");
        NamingEnumeration attrsEnum = ctx.search("ou=mathematicians,dc=example,dc=com", attrs);
        while (attrsEnum.hasMore()) {
            Attribute attr = (Attribute) attrsEnum.next();
            log.info("Attribute: " + attr.getID());
            NamingEnumeration valuesEnum = attr.getAll();
            while (valuesEnum.hasMore()) {
                log.info("Value: " + valuesEnum.next());
            }
        }

        return "";
    }

}
