package com.aldren.config;

import com.aldren.ldap.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class TestConfig {

    @Autowired
    private LdapService svc;

    @Bean
    public String loadDirContext() throws NamingException {
        DirContext ctx = svc.ldapContext("riemann","password");

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(objectClass=user)(sAMAccountName={0}))";

        Set<String> roles = new HashSet<>();

        NamingEnumeration<?> answer = ctx.search("dc=example,dc=com", searchFilter, new Object[]{"riemann"}, searchControls);

        while(answer.hasMoreElements()) {
            SearchResult sr = (SearchResult) answer.next();
            Attributes attr = sr.getAttributes();

            System.out.println("Attributes::" + attr.toString());
        }

        return "";
    }

}
